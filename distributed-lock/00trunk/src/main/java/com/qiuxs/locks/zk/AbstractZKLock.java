package com.qiuxs.locks.zk;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkNoNodeException;

/**
 * 抽象zk锁
 * 
 * @author qiuxs
 *
 */
public abstract class AbstractZKLock {

	/** Zk连接 */
	private ZkClient zkclient;

	/** 锁根节点 */
	private String basePath;

	/** 锁节点 */
	private String path;

	private static final String LOCK_PREFIX = "lock-";

	/** 最大重试次数 */
	private int maxRetryCount;

	protected AbstractZKLock(ZkClient zkclient, String key, int maxRetryCount) {
		this.zkclient = zkclient;
		this.basePath = "/" + key;
		this.path = this.basePath.concat("/").concat(LOCK_PREFIX);
		this.maxRetryCount = maxRetryCount;
		this.zkclient.createPersistent(basePath, true);
	}

	/**
	 * 创建锁节点
	 * 
	 * @return
	 */
	private String createLockNode() {
		return this.zkclient.createEphemeralSequential(path, null);
	}

	/**
	 * 删除当前锁节点
	 * 
	 * @param ourPath
	 */
	protected void deleteOurPath(String ourPath) {
		this.zkclient.delete(ourPath);
	}

	/**
	 * 等待获取锁
	 * 
	 * @param ourPath
	 * @param startMillis
	 * @param millisToWait
	 * @return
	 */
	private boolean waitToLock(String ourPath, long startMillis,
	        long millisToWait) {
		boolean haveTheLock = false;
		boolean doDelete = false;
		try {
			while (!haveTheLock) {
				// 该方法实现获取locker节点下的所有顺序节点，并且从小到大排序
				List<String> children = getSortedChildren();
				String sequenceNodeName = ourPath
				        .substring(basePath.length() + 1);
				// 计算刚才客户端创建的顺序节点在locker的所有子节点中排序位置，如果是排序为0，则表示获取到了锁
				int ourIdx = children.indexOf(sequenceNodeName);
				/*
				 * 如果在getSortedChildren中没有找到之前创建的[临时]顺序节点，这表示可能由于网络闪断而导致
				 * Zookeeper认为连接断开而删除了我们创建的节点，此时需要抛出异常，让上一级去处理
				 * 上一级的做法是捕获该异常，并且执行重试指定的次数 见后面的 attemptLock方法
				 */
				if (ourIdx < 0) {
					throw new ZkNoNodeException("节点没有找到: " + sequenceNodeName);
				}

				// 如果当前客户端创建的节点在locker子节点列表中位置大于0，表示其它客户端已经获取了锁
				// 此时当前客户端需要等待其它客户端释放锁，
				boolean isGetTheLock = ourIdx == 0;

				// 如何判断其它客户端是否已经释放了锁？从子节点列表中获取到比自己次小的哪个节点，并对其建立监听
				String pathToWatch = isGetTheLock ? null : children
				        .get(ourIdx - 1);
				if (isGetTheLock) {
					haveTheLock = true;
				} else {
					// 如果次小的节点被删除了，则表示当前客户端的节点应该是最小的了，所以使用CountDownLatch来实现等待
					String previousSequencePath = basePath.concat("/").concat(
					        pathToWatch);
					final CountDownLatch latch = new CountDownLatch(1);
					final IZkDataListener previousListener = new IZkDataListener() {

						// 次小节点删除事件发生时，让countDownLatch结束等待
						// 此时还需要重新让程序回到while，重新判断一次！
						public void handleDataDeleted(String dataPath)
						        throws Exception {
							latch.countDown();
						}

						public void handleDataChange(String dataPath,
						        Object data) throws Exception {
							// ignore
						}
					};

					try {
						// 如果节点不存在会出现异常
						this.zkclient.subscribeDataChanges(
						        previousSequencePath, previousListener);

						if (millisToWait > 0) {
							millisToWait -= (System.currentTimeMillis() - startMillis);
							startMillis = System.currentTimeMillis();
							if (millisToWait <= 0) {
								doDelete = true; // timed out - delete our node
								break;
							}

							latch.await(millisToWait, TimeUnit.MICROSECONDS);
						} else {
							latch.await();
						}

					} catch (ZkNoNodeException e) {
						// ignore
					} finally {
						this.zkclient.unsubscribeDataChanges(
						        previousSequencePath, previousListener);
					}
				}

			}
		} catch (Exception e) {
			// 发生异常需要删除节点
			doDelete = true;
			throw new RuntimeException(e);
		} finally {
			if (doDelete) {
				this.deleteOurPath(ourPath);
			}
		}
		return haveTheLock;
	}

	/**
	 * 执行锁定操作
	 * 
	 * @param time
	 * @param unit
	 * @return
	 */
	protected String lock(long time, TimeUnit unit) {
		final long start = System.currentTimeMillis();
		final long milisToWait = unit != null ? unit.toMillis(time) : 0;
		String ourPath = null;
		boolean hasTehLock = false;
		boolean isDone = false;
		int retryCount = 0;
		while (!isDone) {
			isDone = true;
			try {
				ourPath = createLockNode();
				hasTehLock = waitToLock(ourPath, start, milisToWait);
			} catch (ZkNoNodeException e) {
				if (retryCount++ < maxRetryCount) {
					isDone = false;
				} else {
					throw e;
				}
			}
		}
		return hasTehLock ? ourPath : null;
	}

	/**
	 * 获取节点序号
	 * 
	 * @param str
	 * @param lockName
	 * @return
	 */
	private String getLockNodeNumber(String str, String lockName) {
		int index = str.lastIndexOf(lockName);
		if (index >= 0) {
			index += lockName.length();
			return index <= str.length() ? str.substring(index) : "";
		}
		return str;
	}

	/**
	 * 获取当前父节点下所有子节点
	 * 
	 * @return
	 */
	private List<String> getSortedChildren() {
		List<String> children = this.zkclient.getChildren(basePath);

		Collections.sort(children, new Comparator<String>() {
			@Override
			public int compare(String str1, String str2) {
				return getLockNodeNumber(str1, LOCK_PREFIX).compareTo(getLockNodeNumber(str2, LOCK_PREFIX));
			}
		});

		//	JDK 1.8 实现	
		//		children.sort((str1, str2) -> {
		//			return getLockNodeNumber(str1, LOCK_PREFIX).compareTo(getLockNodeNumber(str2, LOCK_PREFIX));
		//		});
		return children;
	}

	/**
	 * 释放锁
	 * 
	 * @param ourLockPath
	 */
	protected void releaseLock(String ourLockPath) {
		this.deleteOurPath(ourLockPath);
	}
}
