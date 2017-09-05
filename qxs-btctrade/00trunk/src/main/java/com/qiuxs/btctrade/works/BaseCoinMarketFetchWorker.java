package com.qiuxs.btctrade.works;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.market.dao.BtcMarketDao;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.fdn.utils.converter.JsonUtils;
import com.qiuxs.fdn.utils.net.HttpClientUtil;
import com.qiuxs.frm.service.AbstractDataAccessService;

/**
 * 拉取币种行情定时任务基类
 * @author qiuxs
 *
 */
public abstract class BaseCoinMarketFetchWorker<Svc extends AbstractDataAccessService<Long, BtcMarket, BtcMarketDao>> {

	private static Logger log = Logger.getLogger(BaseCoinMarketFetchWorker.class);

	/** Api地址 */
	private String api;
	/** 虚拟币类型 */
	private BtcContants.CoinTypes coinType;

	private ExecutorService thread_pool = null;

	protected BaseCoinMarketFetchWorker(String coinApi, BtcContants.CoinTypes coinType) {
		this.api = coinApi;
		this.coinType = coinType;
		this.thread_pool = Executors.newFixedThreadPool(3);
	}

	@Scheduled(cron = "0/1 * *  * * ? ")
	public final void work() {
		JSONObject data = HttpClientUtil.getRetJson(this.api);
		log.info("fetched date ---->> " + data.toJSONString());
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
			getSvc().create(market);
		});
	}

	/**
	 * 关闭线程池
	 */
	protected void closeThreadPool() {
		this.thread_pool.shutdown();
	}

	/**
	 * 执行自定义操作
	 * @param data
	 */
	protected abstract void postFetch(BtcMarket data);

	/**
	 * 子类实现 返回用来保存对象的服务类
	 * @return
	 */
	protected abstract Svc getSvc();

	/**
	 * 必须在此方法中调用
	 * super.closeThreadPool();
	 * 并声明@PreDestroy
	 */
	protected abstract void close();

}
