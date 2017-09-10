/*
 * 文件名称: SaleOrder.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-9
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.entity;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.util.ICoinTypeable;
import com.qiuxs.fdn.entity.EcEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 卖单表对象类
 * @author qiuxs created on 2017-9-9
 * @since
 */
public class SaleOrder extends EcEntity<Long> implements ICoinTypeable {
	private static final long serialVersionUID = 1L;

	/** 币种 */
	private Integer type;

	/** 卖单ID */
	private String btcOrderId;

	/** 卖价 */
	private java.math.BigDecimal price;

	/** 卖出数量 */
	private java.math.BigDecimal num;

	/** 总价 */
	private java.math.BigDecimal money;

	/** 手续费 */
	private java.math.BigDecimal btcFee;

	/** 收益 */
	private java.math.BigDecimal profit;

	/** 状态 */
	private Integer flag;

	/** 完成时间 */
	private Date finishDate;

	/** 撤销时间 */
	private Date cancelDate;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getBtcOrderId() {
		return btcOrderId;
	}

	public void setBtcOrderId(String btcOrderId) {
		this.btcOrderId = btcOrderId;
	}

	public java.math.BigDecimal getPrice() {
		return price;
	}

	public void setPrice(java.math.BigDecimal price) {
		this.price = price;
	}

	public java.math.BigDecimal getNum() {
		return num;
	}

	public void setNum(java.math.BigDecimal num) {
		this.num = num;
	}

	public java.math.BigDecimal getMoney() {
		return money;
	}

	public void setMoney(java.math.BigDecimal money) {
		this.money = money;
	}

	public java.math.BigDecimal getBtcFee() {
		return btcFee;
	}

	public void setBtcFee(java.math.BigDecimal btcFee) {
		this.btcFee = btcFee;
	}

	public java.math.BigDecimal getProfit() {
		return profit;
	}

	public void setProfit(java.math.BigDecimal profit) {
		this.profit = profit;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Date getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	public Date getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(Date cancelDate) {
		this.cancelDate = cancelDate;
	}

	@Override
	public BtcContants.CoinTypes getCoinType() {
		return BtcContants.CoinTypes.valueOf(this.getType());
	}

	/**
	 * 计算收益
	 * 卖总价 - (卖手续费 + 买总价 + 买手续费)
	 * @param buyOrder
	 */
	public void calculateProfit(BuyOrder buyOrder) {
		BigDecimal profit = this.money.subtract(this.btcFee.add(buyOrder.getMoney()).add(buyOrder.getBtcFee()));
		this.setProfit(profit);
	}

}
