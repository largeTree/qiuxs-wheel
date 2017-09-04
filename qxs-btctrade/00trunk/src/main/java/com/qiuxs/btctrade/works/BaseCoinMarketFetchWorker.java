package com.qiuxs.btctrade.works;

import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.fdn.utils.net.HttpClientUtil;

/**
 * 拉取币种行情定时任务基类
 * @author qiuxs
 *
 */
public abstract class BaseCoinMarketFetchWorker {

	/**
	 * 
	 */
	private String api;

	protected BaseCoinMarketFetchWorker(String coinApi) {
		this.api = coinApi;
	}

	@Scheduled(cron = "0/1 * *  * * ? ")
	public final void work() {
		JSONObject data = HttpClientUtil.getRetJson(this.api);
		this.postFetch(data);
	}

	protected abstract void postFetch(JSONObject data);

}
