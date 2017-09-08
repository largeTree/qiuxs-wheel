package com.qiuxs.btctrade.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.qiuxs.btctrade.constants.BtcContants;

/**
 * 各种利率缓存
 * 一些计算方法
 * @author qiuxs
 *
 */
public class RateUtils {

	/** 平台手续费率 */
	private static final Map<String, BigDecimal> btc_fee_rate = new HashMap<String, BigDecimal>();
	static {
		// 初始化各币种费率
		btc_fee_rate.put(BtcContants.CoinTypes.doge.toString(), BigDecimal.valueOf(0.001));
	}

	/** 最低价个增长率
	 *  当前价 > 最低销售价 * (1 + 增长率) 时卖出
	 *  */
	private static final Map<String, BigDecimal> my_sale_price_add_rate = new HashMap<String, BigDecimal>();
	static {
		// 狗狗币增长率超过百分之5时卖出
		my_sale_price_add_rate.put(BtcContants.CoinTypes.doge.toString(), BigDecimal.valueOf(0.05D));
	}

	/**
	 * 计算本次交易需要的手续费
	 * @param coinType
	 * 	币种
	 * @param money
	 *  总额
	 * @return
	 */
	public static BigDecimal calculateFee(BtcContants.CoinTypes coinType, BigDecimal money) {
		return money.multiply(getBtcFeeRate(coinType));
	}

	/**
	 * 获取指定虚拟币的费率
	 * @param coinType
	 * @return
	 */
	public static BigDecimal getBtcFeeRate(BtcContants.CoinTypes coinType) {
		return btc_fee_rate.get(coinType.toString());
	}

	/**
	 * 获取设定的价格增长率
	 * 当前价 > 最低销售价 * (1 + 增长率) 时卖出
	 * @param coinType
	 * @return
	 */
	public static BigDecimal getSalePriceAddRate(BtcContants.CoinTypes coinType) {
		return my_sale_price_add_rate.get(coinType.toString());
	}

}
