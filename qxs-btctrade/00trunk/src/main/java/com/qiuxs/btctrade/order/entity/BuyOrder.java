/*
 * 文件名称: BuyOrder.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-6
 * 修改内容: 
 */
package com.qiuxs.btctrade.order.entity;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.fdn.entity.EcEntity;

import java.util.Date;

/**
 * 买单表对象类
 * @author qiuxs created on 2017-9-6
 * @since
 */
public class BuyOrder extends EcEntity<Long> {
	private static final long serialVersionUID = 1L;

	/**  虚拟币类型 */
	private Integer type;

	/** Btc挂单ID */
	private String btcOrderId;

	/** 买入价格 */
	private java.math.BigDecimal price;

	/** 买入数量 */
	private java.math.BigDecimal num;

	/** 总价 */
	private java.math.BigDecimal money;

	/** 手续费 */
	private java.math.BigDecimal btcFee;

	/** 预期销售价 */
	private java.math.BigDecimal salePrice;

	/** 单据状态 */
	private Integer flag;

	/** 完成时间 */
	private Date finishDate;

	/** 取消时间 */
	private Date cancelDate;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public BtcContants.CoinTypes getTypeEnum() {
		return BtcContants.CoinTypes.valueOf(this.getType());
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

	public java.math.BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(java.math.BigDecimal salePrice) {
		this.salePrice = salePrice;
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

}
