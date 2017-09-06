package com.qiuxs.btctrade.context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.order.entity.BuyOrder;

public class OrderCacheHolder {

	/** 各币种买单缓存 */
	private static Map<String, List<BuyOrder>> buy_order = new HashMap<String, List<BuyOrder>>();

	/**
	 * 获取指定币种买单缓存
	 * @param coinType
	 * @return
	 */
	public static List<BuyOrder> getBuyOrders(BtcContants.CoinTypes coinType) {
		return buy_order.get(coinType.getCode());
	}

	/**
	 * 获取指定币种买单数量
	 * @param coinType
	 * @return
	 */
	public static int getBuyOrderCount(BtcContants.CoinTypes coinType) {
		List<BuyOrder> buyOrders = getBuyOrders(coinType);
		return buyOrders == null ? 0 : buyOrders.size();
	}
}
