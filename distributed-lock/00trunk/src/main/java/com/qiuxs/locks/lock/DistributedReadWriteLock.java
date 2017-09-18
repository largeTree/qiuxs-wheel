package com.qiuxs.locks.lock;

/**
 * 分布式读写锁接口
 * @author qiuxs
 *
 */
public interface DistributedReadWriteLock {

	DistributedLock readLock();

	DistributedLock writeLock();

}
