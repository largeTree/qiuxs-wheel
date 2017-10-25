package com.qiuxs.lock;

import com.qiuxs.locks.lock.DistributedLock;
import com.qiuxs.locks.zk.ZKLockFactoryImpl;
import com.qiuxs.locks.zk.factory.ZKLockFactory;

/**
 * Unit test for simple App.
 */
public class AppTest {

	public static void main(String[] args) {
		ZKLockFactory factory = ZKLockFactoryImpl.builderFactory("127.0.0.1:2181", 5000);
		final DistributedLock lock1 = factory.getLock("lock_1");
		lock1.lock();
		System.out.println("lock1 be get lock");
		//		new Thread(() -> {
		//			try {
		//				Thread.sleep(10000);
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//			lock1.unlock();
		//			System.out.println("lock1 unlocked");
		//		}).start();
		//		new Thread(() -> {
		//			try {
		//				for (int i = 0; i < 10; i++) {
		//					Thread.sleep(1000);
		//					System.out.println("i=" + i);
		//				}
		//			} catch (Exception e) {
		//				e.printStackTrace();
		//			}
		//		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(10000);
				} catch (Exception e) {
					e.printStackTrace();
				}
				lock1.unlock();
				System.out.println("lock1 unlocked");
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < 10; i++) {
						Thread.sleep(1000);
						System.out.println("i=" + i);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
		DistributedLock lock2 = factory.getLock("lock_1");
		lock2.lock();
		System.out.println("lock2 be get lock");
		lock2.unlock();
		System.out.println("lock2 unlocked");
		factory.destory();
	}
}