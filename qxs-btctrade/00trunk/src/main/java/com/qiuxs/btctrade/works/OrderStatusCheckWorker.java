package com.qiuxs.btctrade.works;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.OrderCacheHolder;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.util.BtcOrderDetailDTO;
import com.qiuxs.btctrade.util.CallApiUtils;
import com.qiuxs.btctrade.util.IRunningCheckable;

/**
 * 挂单状态检查器
 * @author qiuxs
 *
 */
@Service
public class OrderStatusCheckWorker implements IRunningCheckable {

	private static Logger log = Logger.getLogger(OrderStatusCheckWorker.class);

	@Resource
	private BuyOrderService buyOrderSvc;

	/** 运行状态标记 */
	private boolean isRunning = true;

	/** 狗狗币买单状态检查器 */
	private Thread buy_order_ck_thread = new Thread(new BuyOrderStatusCheckRunner(BtcContants.CoinTypes.doge));

	@PostConstruct
	public void init() {
		log.info("start order check threads");
		buy_order_ck_thread.start();
	}

	@PreDestroy
	public void destory() {
		isRunning = false;
		log.info("interrupt order check threads");
		buy_order_ck_thread.interrupt();
	}

	/**
	 * 订单检查线程运行状态
	 * @return
	 */
	@Override
	public boolean isRunning() {
		return this.isRunning;
	}

	/**
	 * 买单检查线程实现
	 * @author qiuxs
	 *
	 */
	class BuyOrderStatusCheckRunner implements Runnable {

		/** 币种 */
		private BtcContants.CoinTypes type;

		public BuyOrderStatusCheckRunner(BtcContants.CoinTypes type) {
			this.type = type;
		}

		@Override
		public void run() {
			while (isRunning()) {
				BuyOrder order = OrderCacheHolder.getLast(this.type);
				try {
					if (order == null) {
						TimeUnit.SECONDS.sleep(1);
						continue;
					}
					String btcOrderId = order.getBtcOrderId();
					BtcOrderDetailDTO orderDetail = CallApiUtils.fetchOrder(btcOrderId);
					if (orderDetail.isOpen()) {
						continue;
					}
					if (orderDetail.isClosed()) {
						// 订单已完成
						order.setFinishDate(Calendar.getInstance().getTime());
					} else if (orderDetail.isCancelled()) {
						// 订单已撤销
						order.setCancelDate(Calendar.getInstance().getTime());
					}
					order.setFlag(orderDetail.getFlag());
					log.info("order" + btcOrderId + " status changed to " + orderDetail.getStatus());
					buyOrderSvc.update(order);
					OrderCacheHolder.removeLast(this.type);
				} catch (Exception e) {
					log.error("update order flag Error:" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

}
