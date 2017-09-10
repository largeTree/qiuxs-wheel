package com.qiuxs.btctrade.context;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.util.CallApiUtils;
import com.qiuxs.fdn.exception.utils.Exceptions;
import com.qiuxs.fdn.uconfig.UConfigUtils;

/**
 * Btc上下文
 * @author qiuxs
 *
 */
public class BtcContextManager {

	/** 公钥 */
	private static String publicKey;
	/** 私钥 */
	private static String privateKey;

	/** 当前账户状态 */
	private static AccountState accountState;
	/** 买入价因子 */
	private static Map<BtcContants.CoinTypes, BigDecimal> buy_factor = new ConcurrentHashMap<BtcContants.CoinTypes, BigDecimal>();
	static {
		// 初始化各币种买入价因子
		buy_factor.put(BtcContants.CoinTypes.doge, BigDecimal.valueOf(0.1D));
	}

	/** 买入因子调整比例 */
	private static Map<BtcContants.CoinTypes, BigDecimal> buy_factor_adjust_rate = new ConcurrentHashMap<BtcContants.CoinTypes, BigDecimal>();
	static {
		// 初始化各币种买入价因子调整比例
		buy_factor_adjust_rate.put(BtcContants.CoinTypes.doge, BigDecimal.valueOf(0.1));
	}

	/** 买入因子调整读写锁 */
	private static Map<BtcContants.CoinTypes, ReadWriteLock> buy_factor_adjust_locks = new HashMap<BtcContants.CoinTypes, ReadWriteLock>();
	static {
		for (BtcContants.CoinTypes coinType : BtcContants.CoinTypes.values()) {
			buy_factor_adjust_locks.put(coinType, new ReentrantReadWriteLock());
		}
	}

	/** 买单最大数量 */
	private static Map<BtcContants.CoinTypes, Integer> max_buy_order = new ConcurrentHashMap<BtcContants.CoinTypes, Integer>();
	static {
		// 初始化各币种 最大买单数量
		max_buy_order.put(BtcContants.CoinTypes.doge, 1);
	}

	/**
	 * 初始化Btc上下文
	 */
	public static void init() {
		publicKey = UConfigUtils.getConfig("key").getString("public");
		privateKey = UConfigUtils.getConfig("key").getString("private");
		// 加载账户状态
		updateCurrentAccountInfo();
		// 加载数据库中 处于打开状态的单据
		OrderCacheHolder.init();
	}

	/**
	 * 更新当前账户状态
	 */
	private static void updateCurrentAccountInfo() {
		AccountState accountState = CallApiUtils.getAccountState();
		BtcContextManager.accountState = accountState;
	}

	/**
	 * 获取当前账户状态
	 * @return
	 */
	public static AccountState getAccountState() {
		updateCurrentAccountInfo();
		return accountState;
	}

	/**
	 * 获取公钥
	 * @return
	 */
	public static String getPublicKey() {
		return publicKey;
	}

	/**
	 * 获取私钥
	 * @return
	 */
	public static String getPrivateKey() {
		return privateKey;
	}

	/**
	 * 获取购买因子
	 * 当价格跌至  最低价 * (1 + 买入因子) 时进行买入
	 * @return
	 */
	public static BigDecimal getBuyFactor(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = buy_factor_adjust_locks.get(coinType);
		try {
			lock.readLock().lock();
			return buy_factor.get(coinType);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 降低买入因子
	 * 每当买入一次，降低一次
	 * @param coinType
	 */
	public static void reduceBuyFactor(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = buy_factor_adjust_locks.get(coinType);
		try {
			lock.writeLock().lock();
			BigDecimal factory = buy_factor.get(coinType);
			// 新的因子为  原因子 * (1 - 调正比例)
			buy_factor.put(coinType, factory.multiply(BigDecimal.ONE.subtract(buy_factor_adjust_rate.get(coinType))));
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 提高买入因子
	 * 每当卖出一次，提高一次
	 * @param coinType
	 */
	public static void increaseBuyFactor(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = buy_factor_adjust_locks.get(coinType);
		try {
			lock.writeLock().lock();
			BigDecimal factory = buy_factor.get(coinType);
			// 新的因子为  原因子 / (1 - 调正比例)
			buy_factor.put(coinType, factory.divide(BigDecimal.ONE.subtract(buy_factor_adjust_rate.get(coinType))));
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.writeLock().unlock();
		}
	}

	/**
	 * 获取最大买单数量
	 * @param doge
	 * @return
	 */
	public static int getMaxBuyOrder(BtcContants.CoinTypes coinType) {
		return max_buy_order.get(coinType);
	}

}
