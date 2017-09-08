package com.qiuxs.btctrade.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.order.entity.BuyOrder;

/**
 * 单据检查工具类
 * @author qiuxs
 *
 */
public class OrderCheckUtils {

	/**
	 * 各币种最小购买数量
	 */
	private static Map<BtcContants.CoinTypes, BigDecimal> buy_order_min_num = new HashMap<BtcContants.CoinTypes, BigDecimal>();
	static {
		buy_order_min_num.put(BtcContants.CoinTypes.doge, BigDecimal.valueOf(100D));
	}

	/**
	 * 检查买单最小数量限制
	 * @param order
	 * @return
	 */
	public static boolean checkBuyOrderMinNum(BuyOrder order) {
		return buy_order_min_num.get(order.getTypeEnum()).compareTo(order.getNum()) < 0;
	}

	/**
	 * 判断是否可以进行购买
	 * @param order
	 * @return
	 */
	public static boolean checkCanBuy(BuyOrder order) {
		// 购买数量必须大于指定数量
		return checkBuyOrderMinNum(order);
	}

}
