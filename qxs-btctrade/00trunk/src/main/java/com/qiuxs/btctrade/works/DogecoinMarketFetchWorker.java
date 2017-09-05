package com.qiuxs.btctrade.works;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.market.service.BtcMarketService;

@Service
public class DogecoinMarketFetchWorker extends BaseCoinMarketFetchWorker<BtcMarketService> {

	@Resource
	private BtcMarketService svc;

	protected DogecoinMarketFetchWorker() {
		super(BtcContants.Apis.DOGE, BtcContants.CoinTypes.Doge);
	}

	@Override
	protected void postFetch(BtcMarket data) {

	}

	@Override
	protected BtcMarketService getSvc() {
		return this.svc;
	}

	/**
	 * 
	 * @see BaseCoinMarketFetchWorker#close()
	 */
	@PreDestroy
	@Override
	protected void close() {
		super.closeThreadPool();
	}

}
