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
	 * Api地址
	 * @author qiuxs
	 *
	 */
	public static class Apis {
		public static final String DOGE = "https://api.btctrade.com/api/ticker?coin=doge";
	}

}
