package com.qiuxs.btctrade.works;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.OrderCacheHolder;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.entity.SaleOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.order.service.SaleOrderService;
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

	@Resource
	private SaleOrderService saleOrderSvc;

	/** 运行状态标记 */
	private boolean isRunning = true;

	/** 买单状态检查器 */
	private List<Thread> buy_order_ck_threads = new ArrayList<Thread>(BtcContants.CoinTypes.values().length);
	{
		buy_order_ck_threads.add(new Thread(new BuyOrderStatusCheckRunner(BtcContants.CoinTypes.doge)));
	}

	/** 卖单状态检查器 */
	private List<Thread> sale_order_ck_threads = new ArrayList<Thread>(BtcContants.CoinTypes.values().length);
	 {
		sale_order_ck_threads.add(new Thread(new SaleOrderStatusCheckRunner(BtcContants.CoinTypes.doge)));
	}

	@PostConstruct
	public void init() {
		log.info("start order check threads");
		// 买单检查线程
		for (Thread t : buy_order_ck_threads) {
			t.start();
		}
		// 卖单检查线程
		for (Thread t : sale_order_ck_threads) {
			t.start();
		}
	}

	@PreDestroy
	public void destory() {
		isRunning = false;
		log.info("interrupt order check threads");
		// 买单检查线程
		for (Thread t : buy_order_ck_threads) {
			t.interrupt();
		}
		// 卖单检查线程
		for (Thread t : sale_order_ck_threads) {
			t.interrupt();
		}
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
				// 获取当前缓存的买单列表
				try {
					List<BuyOrder> orders = OrderCacheHolder.getBuyOrders(this.type);
					if (orders.size() == 0) {
						TimeUnit.SECONDS.sleep(1);
						continue;
					}
					List<BuyOrder> removeList = new ArrayList<BuyOrder>();
					for (BuyOrder order : orders) {
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
						removeList.add(order);
					}
					if (removeList.size() > 0) {
						OrderCacheHolder.removeBuyOrders(this.type, removeList);
					}
				} catch (Exception e) {
					log.error("update order flag Error:" + e.getLocalizedMessage(), e);
				}
			}
		}
	}

	/**
	 * 卖单检查线程实现
	 * @author qiuxs
	 *
	 */
	class SaleOrderStatusCheckRunner implements Runnable {
		/** 币种 */
		private BtcContants.CoinTypes type;

		public SaleOrderStatusCheckRunner(BtcContants.CoinTypes type) {
			this.type = type;
		}

		public void run() {
			while (isRunning()) {
				try {
					List<SaleOrder> orders = OrderCacheHolder.getSaleOrders(this.type);
					if (orders.size() == 0) {
						TimeUnit.SECONDS.sleep(1);
						continue;
					}
					List<SaleOrder> removeList = new ArrayList<SaleOrder>();
					for (SaleOrder order : orders) {
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
						saleOrderSvc.update(order);
						removeList.add(order);
					}
					if (removeList.size() > 0) {
						OrderCacheHolder.removeSaleOrders(this.type, removeList);
					}
				} catch (Exception e) {
					log.error("update order flag Error:" + e.getLocalizedMessage(), e);
				}
			}
		}
	}
}
