package com.qiuxs.btctrade.works;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.btctrade.constants.BtcContants;

@Service
public class DogecoinQuotationFetchWorker extends BaseCoinMarketFetchWorker {

	protected DogecoinQuotationFetchWorker() {
		super(BtcContants.DOGE_COIN_MARKET_API);
	}

	@Override
	protected void postFetch(JSONObject data) {
		// save 
	}


}
