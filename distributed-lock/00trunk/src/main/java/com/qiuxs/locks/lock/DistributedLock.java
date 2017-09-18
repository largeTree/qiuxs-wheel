package com.qiuxs.locks.lock;

import java.util.concurrent.TimeUnit;

/**
 * 分布式锁接口
 * @author qiuxs
 *
 */
public interface DistributedLock {

	void lock();

	boolean tryLock();

	boolean tryLock(long time, TimeUnit unit);

	void unlock();

}
