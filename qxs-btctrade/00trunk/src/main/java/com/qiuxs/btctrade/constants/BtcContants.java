package com.qiuxs.btctrade.constants;

public class BtcContants {

	/**
	 * 虚拟币类型枚举
	 * @author qiuxs
	 *
	 */
	public static enum CoinTypes {
		/** 比特比 */
		Btc(1, "btc"),
		/** 以太币 */
		Eth(2, "eth"),
		/** 以太经典 */
		Etc(3, "etc"),
		/** 莱特币 */
		Ltc(4, "ltc"),
		/** 狗狗币 */
		Doge(5, "doge"),
		/** 元宝币 */
		Ybc(6, "ybc");

		private int value;
		private String code;

		CoinTypes(int val, String code) {
			this.value = val;
			this.code = code;
		}

		public int getValue() {
			return this.value;
		}

		public String getCode() {
			return this.code;
		}

	}

	/**
	 * 买单状态枚举
	 * @author qiuxs
	 *
	 */
	public static enum BuyOrderFlag {
		/** 创建 */
		Created(0, "created"),
		/** 已取消 */
		Canceled(1, "canceled"),
		/** 已完成 */
		Finished(2, "finished");

		private int value;
		private String code;

		BuyOrderFlag(int value, String code) {
			this.value = value;
			this.code = code;
		}

		public int getValue() {
			return this.value;
		}

		public String getCode() {
			return this.code;
		}
	}

	/**
	 * Api地址
	 * @author qiuxs
	 *
	 */
	public static class Apis {
		public static final String DOGE_MARKET = "https://api.btctrade.com/api/ticker?coin=doge";
		public static final String ACCOUNT_INFO = "https://api.btctrade.com/api/balance/";
	}

}
