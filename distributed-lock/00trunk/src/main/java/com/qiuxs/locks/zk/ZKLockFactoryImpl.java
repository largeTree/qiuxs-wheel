package com.qiuxs.locks.zk;

import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

import org.I0Itec.zkclient.ZkClient;
import org.apache.log4j.Logger;
import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.qiuxs.locks.lock.DistributedLock;
import com.qiuxs.locks.zk.factory.ZKLockFactory;

/**
 * zk锁工厂类
 * @author qiuxs
 *
 */
public class ZKLockFactoryImpl implements ZKLockFactory {

	private static Logger log = Logger.getLogger(ZKLockFactoryImpl.class);

	private static ZKLockFactory factory;
	private static final byte[] factory_lock = new byte[0];

	private boolean inited;

	private int retryCount;

	private ZkClient zkclient;

	private Map<String, DistributedLock> locks_cache = new ConcurrentHashMap<String, DistributedLock>();

	private ZKLockFactoryImpl(String host, int sessionTimeout) {
		this.zkclient = new ZkClient(host, sessionTimeout);
		this.inited = true;
		log.info("inited ZKLockFactory...");
	}

	public static ZKLockFactory builderFactory(String host, int sessionTimeout) {
		if (factory == null) {
			synchronized (factory_lock) {
				if (factory == null)
					factory = (ZKLockFactory) Proxy.newProxyInstance(ZKLockFactory.class.getClassLoader(), new Class<?>[] { ZKLockFactory.class },
							new ZKLockFactoryInvocationHandler(new ZKLockFactoryImpl(host, sessionTimeout)));
			}
		}
		return factory;
	}

	@Override
	public DistributedLock getLock(String lockKey) {
		DistributedLock lock = locks_cache.get(lockKey);
		if (lock == null) {
			lock = new ZKLock(zkclient, lockKey, retryCount);
		}
		return lock;
	}

	@Override
	public ReadWriteLock getReadWriteLock(String lockKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isInited() {
		return inited;
	}

	@Override
	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	@Override
	public void destory() {
		this.zkclient.close();
		this.inited = false;
	}

}
