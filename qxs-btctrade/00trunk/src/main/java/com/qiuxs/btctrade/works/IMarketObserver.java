package com.qiuxs.btctrade.works;

import com.qiuxs.btctrade.market.entity.BtcMarket;

/**
 * 行情监听器
 * @author qiuxs
 *
 */
public interface IMarketObserver {

	/**
	 * 更新行情
	 * @param market
	 */
	public void updateMarket(BtcMarket market);

}
