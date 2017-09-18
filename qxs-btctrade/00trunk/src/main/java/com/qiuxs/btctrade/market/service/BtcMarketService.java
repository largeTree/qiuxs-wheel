package com.qiuxs.btctrade.market.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

@Service
public class BtcMarketService extends AbstractBtcMarketService {

	/**
	 * 计算行情平均价
	 * @return
	 */
	public BigDecimal calSellAvg() {
		return this.getDao().calSellAvg();
	}

}
