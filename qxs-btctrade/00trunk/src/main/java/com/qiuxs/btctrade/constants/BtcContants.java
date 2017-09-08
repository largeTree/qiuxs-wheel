package com.qiuxs.btctrade.constants;

public class BtcContants {

	/**
	 * 虚拟币类型枚举
	 * @author qiuxs
	 *
	 */
	public static enum CoinTypes {
		/** 比特比 */
		btc(1),
		/** 以太币 */
		eth(2),
		/** 以太经典 */
		etc(3),
		/** 莱特币 */
		ltc(4),
		/** 狗狗币 */
		doge(5),
		/** 元宝币 */
		ybc(6);

		private int value;

		CoinTypes(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

		public static CoinTypes valueOf(int type) {
			if (type <= 0 || type > CoinTypes.values().length) {
				throw new IllegalArgumentException("错误的CoinType");
			}
			return CoinTypes.values()[type - 1];
		}

	}

	/**
	 * 买单状态枚举
	 * @author qiuxs
	 *
	 */
	public static enum BuyOrderFlag {
		/** 创建 */
		open(0),
		/** 已取消 */
		cancelled(1),
		/** 已完成 */
		closed(2);

		private int value;

		BuyOrderFlag(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}

	}

	/**
	 * Api地址
	 * @author qiuxs
	 *
	 */
	public static class Apis {
		public static final String FETCH_ORDER = "https://api.btctrade.com/api/fetch_order/";
		public static final String BUY = "https://api.btctrade.com/api/buy/";
		public static final String DOGE_MARKET = "https://api.btctrade.com/api/ticker?coin=doge";
		public static final String ACCOUNT_INFO = "https://api.btctrade.com/api/balance/";
	}

}
