package com.qiuxs.locks.zk;

import java.util.concurrent.TimeUnit;

import org.I0Itec.zkclient.ZkClient;

import com.qiuxs.locks.lock.DistributedLock;

/**
 * 基于zookeeper的分布式锁
 * @author qiuxs
 *
 */
public class ZKLock extends AbstractZKLock implements DistributedLock {

	private String ourLockPath;

	ZKLock(ZkClient zkclient, String key, int retryCount) {
		super(zkclient, key, retryCount);
	}

	/**
	 * 执行锁定操作
	 */
	@Override
	public void lock() {
		if ((ourLockPath = super.lock(-1, null)) == null) {
			throw new RuntimeException("can't get lock");
		}
	}

	@Override
	public boolean tryLock() {
		throw new UnsupportedOperationException("暂未实现");
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) {
		throw new UnsupportedOperationException("暂未实现");
	}

	@Override
	public void unlock() {
		if (this.ourLockPath == null) {
			throw new RuntimeException("not locked!");
		}
		super.releaseLock(this.ourLockPath);
	}

}
