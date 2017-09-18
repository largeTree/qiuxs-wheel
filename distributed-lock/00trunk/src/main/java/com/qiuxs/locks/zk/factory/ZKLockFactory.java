package com.qiuxs.locks.zk.factory;

import java.util.concurrent.locks.ReadWriteLock;

import com.qiuxs.locks.lock.DistributedLock;

public interface ZKLockFactory {

	boolean isInited();

	void setRetryCount(int retryCount);

	DistributedLock getLock(String lockKey);

	ReadWriteLock getReadWriteLock(String lockKey);

	void destory();

}
