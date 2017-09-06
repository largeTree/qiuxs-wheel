package com.qiuxs.btctrade.works;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.market.service.BtcMarketService;

@Service
public class DogecoinMarketFetchWorker extends BaseCoinMarketFetchWorker<BtcMarketService> {

	@Resource
	private DogeMarketObserver marketObserver;

	protected DogecoinMarketFetchWorker() {
		super(BtcContants.Apis.DOGE_MARKET, BtcContants.CoinTypes.Doge);
	}

	@Override
	protected void postFetch(BtcMarket data) {
		marketObserver.updateMarket(data);
	}

}
