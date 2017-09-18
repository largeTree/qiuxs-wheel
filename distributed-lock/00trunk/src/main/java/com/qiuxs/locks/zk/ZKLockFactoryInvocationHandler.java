package com.qiuxs.locks.zk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import com.qiuxs.locks.zk.factory.ZKLockFactory;

public class ZKLockFactoryInvocationHandler implements InvocationHandler {

	private ZKLockFactory target;

	ZKLockFactoryInvocationHandler(ZKLockFactory factory) {
		this.target = factory;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (this.target.isInited()) {
			return method.invoke(target, args);
		}
		throw new RuntimeException("factory is not inited!");
	}

}
