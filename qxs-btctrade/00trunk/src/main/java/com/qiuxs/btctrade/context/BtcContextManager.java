package com.qiuxs.btctrade.context;

import java.math.BigDecimal;
import java.util.Map;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.constants.BtcContants.CoinTypes;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.util.CallApiUtils;
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
		buy_factor.put(BtcContants.CoinTypes.doge, BigDecimal.valueOf(1.2D));
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
	 * 当价格跌至 卖一价*因子时进行买入操作
	 * @return
	 */
	public static BigDecimal getBuyFactor(BtcContants.CoinTypes coinType) {
		return buy_factor.get(coinType);
	}

	/**
	 * 获取最大买单数量
	 * @param doge
	 * @return
	 */
	public static int getMaxBuyOrder(CoinTypes coinType) {
		return max_buy_order.get(coinType);
	}

}
