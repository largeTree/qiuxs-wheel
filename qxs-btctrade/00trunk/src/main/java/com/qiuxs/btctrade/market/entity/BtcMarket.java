/*
 * 文件名称: BtcMarket.java
 * 版权信息: Copyright 2001-2017 hangzhou ecool technology Co., LTD. All right reserved.
 * ----------------------------------------------------------------------------------------------
 * 修改历史:
 * ----------------------------------------------------------------------------------------------
 * 修改原因: 新增
 * 修改人员: qiuxs
 * 修改日期: 2017-9-5
 * 修改内容: 
 */
package com.qiuxs.btctrade.market.entity;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.util.ICoinTypeable;
import com.qiuxs.fdn.entity.EcEntity;

/**
 * 虚拟币行情表对象类
 * @author qiuxs created on 2017-9-5
 * @since
 */
public class BtcMarket extends EcEntity<Long> implements ICoinTypeable{
	private static final long serialVersionUID = 1L;

	/** 币种 */
	private Integer type;

	/** 高价 */
	private java.math.BigDecimal high;

	/** 低价 */
	private java.math.BigDecimal low;

	/** 卖一加 */
	private java.math.BigDecimal sell;

	/** 买一价 */
	private java.math.BigDecimal buy;

	/** 上次价 */
	private java.math.BigDecimal last;

	/** 销量 */
	private java.math.BigDecimal vol;

	/** 时间 */
	private Long time;

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public java.math.BigDecimal getHigh() {
		return high;
	}

	public void setHigh(java.math.BigDecimal high) {
		this.high = high;
	}

	public java.math.BigDecimal getLow() {
		return low;
	}

	public void setLow(java.math.BigDecimal low) {
		this.low = low;
	}

	public java.math.BigDecimal getSell() {
		return sell;
	}

	public void setSell(java.math.BigDecimal sell) {
		this.sell = sell;
	}

	public java.math.BigDecimal getBuy() {
		return buy;
	}

	public void setBuy(java.math.BigDecimal buy) {
		this.buy = buy;
	}

	public java.math.BigDecimal getLast() {
		return last;
	}

	public void setLast(java.math.BigDecimal last) {
		this.last = last;
	}

	public java.math.BigDecimal getVol() {
		return vol;
	}

	public void setVol(java.math.BigDecimal vol) {
		this.vol = vol;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	@Override
	public BtcContants.CoinTypes getCoinType() {
		return BtcContants.CoinTypes.valueOf(this.type);
	}

}
