package com.qiuxs.btctrade.works;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.market.dao.BtcMarketDao;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.market.service.BtcMarketService;
import com.qiuxs.fdn.utils.converter.JsonUtils;
import com.qiuxs.fdn.utils.net.HttpClientUtil;
import com.qiuxs.frm.service.AbstractDataAccessService;

/**
 * 拉取币种行情定时任务基类
 * @author qiuxs
 *
 */
public abstract class BaseCoinMarketFetchWorker<Svc extends AbstractDataAccessService<Long, BtcMarket, BtcMarketDao>> {

	/** Api地址 */
	private String api;
	/** 虚拟币类型 */
	private BtcContants.CoinTypes coinType;

	@Resource
	private BtcMarketService svc;

	private ExecutorService thread_pool = null;

	protected BaseCoinMarketFetchWorker(String coinApi, BtcContants.CoinTypes coinType) {
		this.api = coinApi;
		this.coinType = coinType;
		this.thread_pool = Executors.newFixedThreadPool(3);
	}

	/**
	 * 每秒执行一次，服务器启动后5秒开始执行第一次
	 */
	@Scheduled(fixedDelay = 1000, initialDelay = 5000)
	public final void work() {
		JSONObject data = HttpClientUtil.getRetJson(this.api);
		System.out.println(data);
		BtcMarket market = JsonUtils.fromJson(data.toJSONString(), BtcMarket.class);
		market.setType(coinType.getValue());
		callSave(market);
		this.postFetch(market);
	}

	/**
	 * 触发保存行情任务
	 * @param market
	 */
	private void callSave(final BtcMarket market) {
		thread_pool.submit(() -> {
			BaseCoinMarketFetchWorker.this.svc.create(market);
		});
	}

	/**
	 * 关闭线程池
	 */
	@PreDestroy
	protected void closeThreadPool() {
		this.thread_pool.shutdown();
	}

	/**
	 * 执行自定义操作
	 * @param data
	 */
	protected abstract void postFetch(BtcMarket data);

}
