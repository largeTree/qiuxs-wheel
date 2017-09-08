package com.qiuxs.btctrade.util;

import java.math.BigDecimal;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.qiuxs.btctrade.constants.BtcContants;

/**
 * Btc订单详情DTO
 * @author qiuxs
 *
 */
public class BtcOrderDetailDTO {

	/** 订单ID */
	private String id;
	/** 时间 */
	private Date datetime;
	/** 单据类型 
	 * 	sell:销售
	 *  buy:购买
	 *  */
	private String type;
	/** 挂单价格 */
	private BigDecimal price;
	/** 挂单数量 */
	@JSONField(name = "amount_original")
	private BigDecimal amountOriginal;
	/** 剩余数量 */
	@JSONField(name = "amount_outstanding")
	private BigDecimal amountOutstanding;
	/** 单据状态
	 * 	open:开放
	 * 	closed:结束
	 *  cancelled:撤销  
	 *  */
	private String status;
	/** 已成交状态 */
	private BtcTradesDTO trades;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getAmountOriginal() {
		return amountOriginal;
	}

	public void setAmountOriginal(BigDecimal amountOriginal) {
		this.amountOriginal = amountOriginal;
	}

	public BigDecimal getAmountOutstanding() {
		return amountOutstanding;
	}

	public void setAmountOutstanding(BigDecimal amountOutstanding) {
		this.amountOutstanding = amountOutstanding;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public BtcTradesDTO getTrades() {
		return trades;
	}

	public void setTrades(BtcTradesDTO trades) {
		this.trades = trades;
	}

	/**
	 * 是否结束
	 * @return
	 */
	public boolean isClosed() {
		return BtcContants.BuyOrderFlag.closed.toString().equals(this.status);
	}

	/**
	 * 是否已撤销
	 * @return
	 */
	public boolean isCancelled() {
		return BtcContants.BuyOrderFlag.cancelled.toString().equals(this.status);
	}

	/**
	 * 是否开放状态
	 * @return
	 */
	public boolean isOpen() {
		return BtcContants.BuyOrderFlag.open.toString().equals(this.status);
	}

	/**
	 * 获取整形订单状态
	 * @return
	 */
	public int getFlag() {
		return BtcContants.BuyOrderFlag.valueOf(this.status).getValue();
	}
}
