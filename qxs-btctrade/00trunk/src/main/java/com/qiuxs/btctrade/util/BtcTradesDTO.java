package com.qiuxs.btctrade.util;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 已成交状态DTO
 * @author qiuxs
 *
 */
public class BtcTradesDTO {

	/** 成教总数量 */
	@JSONField(name = "sum_number")
	private BigDecimal sumNumber;
	/** 成交额 */
	@JSONField(name = "sum_money")
	private BigDecimal sumMoney;
	/** 成交均价 */
	@JSONField(name = "avg_price")
	private BigDecimal avgPrice;

	public BigDecimal getSumNumber() {
		return sumNumber;
	}

	public void setSumNumber(BigDecimal sumNumber) {
		this.sumNumber = sumNumber;
	}

	public BigDecimal getSumMoney() {
		return sumMoney;
	}

	public void setSumMoney(BigDecimal sumMoney) {
		this.sumMoney = sumMoney;
	}

	public BigDecimal getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

}
