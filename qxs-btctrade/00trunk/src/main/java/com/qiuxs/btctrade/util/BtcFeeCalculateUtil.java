package com.qiuxs.btctrade.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.qiuxs.btctrade.constants.BtcContants;

public class BtcFeeCalculateUtil {

	private static final Map<String, BigDecimal> rate = new HashMap<String, BigDecimal>() {
		private static final long serialVersionUID = -2972967162363646849L;

		{
			// 初始化各币种费率
			rate.put(BtcContants.CoinTypes.Doge.getCode(), BigDecimal.valueOf(0.001));
		}
	};

	/**
	 * 计算本次交易需要的手续费
	 * @param coinType
	 * 	币种
	 * @param money
	 *  总额
	 * @return
	 */
	public static BigDecimal calculateFee(BtcContants.CoinTypes coinType, BigDecimal money) {
		return money.multiply(getRate(coinType));
	}

	/**
	 * 获取指定虚拟币的费率
	 * @param coinType
	 * @return
	 */
	public static BigDecimal getRate(BtcContants.CoinTypes coinType) {
		return rate.get(coinType.getCode());
	}

}
