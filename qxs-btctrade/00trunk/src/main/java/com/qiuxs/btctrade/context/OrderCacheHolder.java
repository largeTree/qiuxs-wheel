package com.qiuxs.btctrade.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.entity.SaleOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.order.service.SaleOrderService;
import com.qiuxs.core.context.ApplicationContextHolder;
import com.qiuxs.fdn.utils.MapUtils;

/**
 * 买单 卖单缓存
 * @author qiuxs
 *
 */
public class OrderCacheHolder {

	/**
	 * 初始化数据库中状态为开放的买单和买单
	 */
	public static void init() {
		BuyOrderHolder.init();
		SaleOrderHolder.init();
	}

	/**
	 * 获取一个买单
	 * @param coinType
	 * @return
	 */
	public static List<BuyOrder> getBuyOrders(BtcContants.CoinTypes coinType) {
		return BuyOrderHolder.getBuyOrders(coinType);
	}

	/**
	 * 获取买单缓存数量
	 * @param coinType
	 * @return
	 */
	public static int getBuyOrderCount(BtcContants.CoinTypes coinType) {
		return BuyOrderHolder.getBuyOrderCount(coinType);
	}

	/**
	 * 指定单据数量减少1
	 * @param coinType
	 * @return
	 */
	public static int getAndDecrement(BtcContants.CoinTypes coinType) {
		return BuyOrderHolder.getAndDecrement(coinType);
	}

	/**
	 * 添加一个买单
	 * @param order
	 */
	public static void addBuyOrder(BuyOrder order) {
		BuyOrderHolder.addOrder(order);
	}

	/**
	 * 移除多个买单
	 * @param coinType
	 * @param removeList
	 */
	public static void removeBuyOrders(BtcContants.CoinTypes coinType, List<BuyOrder> orders) {
		BuyOrderHolder.removeBuyOrder(coinType, orders);
	}

	/**
	 * 获取一个卖单
	 * @param coinType
	 * @return
	 */
	public static List<SaleOrder> getSaleOrders(BtcContants.CoinTypes coinType) {
		return SaleOrderHolder.getSaleOrders(coinType);
	}

	/**
	 * 添加一个卖单
	 * @param order
	 */
	public static void addSaleOrder(SaleOrder order) {
		SaleOrderHolder.addOrder(order);
	}

	/**
	 * 移除多个卖单
	 * @param coinType
	 * @param removeList
	 */
	public static void removeSaleOrders(BtcContants.CoinTypes coinType, List<SaleOrder> orders) {
		SaleOrderHolder.removeSaleOrder(coinType, orders);
	}

	/**
	 * 买单缓存
	 * @author qiuxs
	 *
	 */
	private static class BuyOrderHolder {
		/** 各币种买单缓存 */
		private static Map<BtcContants.CoinTypes, LinkedList<BuyOrder>> buy_order = new HashMap<BtcContants.CoinTypes, LinkedList<BuyOrder>>();
		/** 各币种对应的买单锁 */
		private static Map<BtcContants.CoinTypes, ReadWriteLock> buy_order_locks = new HashMap<BtcContants.CoinTypes, ReadWriteLock>();
		static {
			for (BtcContants.CoinTypes coinType : BtcContants.CoinTypes.values()) {
				buy_order_locks.put(coinType, new ReentrantReadWriteLock());
			}
		}

		/** 各币种的买单数量 */
		private static Map<BtcContants.CoinTypes, AtomicInteger> buy_order_count = new HashMap<BtcContants.CoinTypes, AtomicInteger>();

		/**
		 * 初始化缓存  
		 * 加载数据库中处于开放状态的单据
		 */
		private static void init() {
			BuyOrderService buyOrderService = ApplicationContextHolder.getBean(BuyOrderService.class);
			// 加载数据库中处于打开状态的单据，根据ID从小到大排序，将ID小的先放入缓存
			List<BuyOrder> orders = buyOrderService.findByWhere(MapUtils.genMap("flag", BtcContants.BuyOrderFlag.open.getValue()));
			for (Iterator<BuyOrder> iter = orders.iterator(); iter.hasNext();) {
				BuyOrder order = iter.next();
				BuyOrderHolder.addOrder(order);
			}

			for (BtcContants.CoinTypes coinType : BtcContants.CoinTypes.values()) {
				// 初始化当前买进因子，减少初始因子（已买但未卖出单据数量）次 
				Long countL = buyOrderService.getCountByWhere(MapUtils.genMap("flag", BtcContants.BuyOrderFlag.closed.getValue(), "type", coinType.getValue()));
				int countI = countL == null ? 0 : countL.intValue();
				// 设置当前币种已存在切未卖出的单据数量
				buy_order_count.put(coinType, new AtomicInteger(countI));
				if (countI == 0) {
					continue;
				}
				for (int i = 0; i < countI; i++) {
					BtcContextManager.reduceBuyFactor(coinType);
				}
			}
		}

		/**
		 * 指定单据数量增加1
		 * @param coinType
		 */
		private static int getAndIncrement(BtcContants.CoinTypes coinType) {
			return buy_order_count.get(coinType).incrementAndGet();
		}

		/**
		 * 指定单据数量减少1
		 * @param coinType
		 * @return
		 */
		private static int getAndDecrement(BtcContants.CoinTypes coinType) {
			return buy_order_count.get(coinType).decrementAndGet();
		}

		/**
		 * 添加一个买单
		 * @param coinType
		 * @param order
		 * @return 
		 * @return
		 */
		private static void addOrder(BuyOrder order) {
			getBuyWriteLock(order.getCoinType()).lock();
			getBuyOrdersWithoutLock(order.getCoinType()).add(order);
			getAndIncrement(order.getCoinType());
			getBuyWriteLock(order.getCoinType()).unlock();
		}

		/**
		 * 获取指定币种买单缓存
		 * @param type
		 * @return
		 */
		private static List<BuyOrder> getBuyOrders(BtcContants.CoinTypes coinType) {
			getBuyReadLock(coinType).lock();
			LinkedList<BuyOrder> cache = getBuyOrdersWithoutLock(coinType);
			List<BuyOrder> list = new LinkedList<BuyOrder>(cache);
			getBuyReadLock(coinType).unlock();
			return list;
		}

		/**
		 * 获取指定币种买单缓存
		 * 无读锁
		 * @param coinType
		 * @return
		 */
		private static LinkedList<BuyOrder> getBuyOrdersWithoutLock(BtcContants.CoinTypes coinType) {
			if (coinType == null) {
				throw new IllegalArgumentException("coinType can't be Null");
			}
			if (!buy_order.containsKey(coinType)) {
				buy_order.put(coinType, new LinkedList<BuyOrder>());
			}
			return buy_order.get(coinType);
		}

		/**
		 * 获取指定币种已买但未卖出数量
		 * @param coinType
		 * @return
		 */
		private static int getBuyOrderCount(BtcContants.CoinTypes coinType) {
			getBuyReadLock(coinType).lock();
			LinkedList<BuyOrder> buyOrders = getBuyOrdersWithoutLock(coinType);
			int count = buyOrders.size();
			// 已卖但为买出数量   = 已买未成交 + 已买且已成交
			count += buy_order_count.get(coinType).get();
			getBuyReadLock(coinType).unlock();
			return count;
		}

		/**
		 * 移除一个买单集合
		 * @param coinType
		 * @param removeList
		 */
		private static void removeBuyOrder(BtcContants.CoinTypes coinType, List<BuyOrder> removeList) {
			getBuyWriteLock(coinType).lock();
			getBuyOrdersWithoutLock(coinType).removeAll(removeList);
			buy_order_count.get(coinType).set(buy_order_count.get(coinType).get() - removeList.size());
			getBuyWriteLock(coinType).unlock();
		}

		/**
		 * 获取买单读锁
		 * @param coinType
		 * @return
		 */
		private static Lock getBuyReadLock(BtcContants.CoinTypes coinType) {
			return buy_order_locks.get(coinType).readLock();
		}

		/**
		 * 获取买单写锁
		 * @param coinType
		 * @return
		 */
		private static Lock getBuyWriteLock(BtcContants.CoinTypes coinType) {
			return buy_order_locks.get(coinType).writeLock();
		}
	}

	/**
	 * 卖单缓存
	 * @author qiuxs
	 *
	 */
	private static class SaleOrderHolder {
		/** 各币种卖单缓存 */
		private static Map<BtcContants.CoinTypes, LinkedList<SaleOrder>> sale_order = new HashMap<BtcContants.CoinTypes, LinkedList<SaleOrder>>();
		/** 各币种对应的卖单锁 */
		private static Map<BtcContants.CoinTypes, ReadWriteLock> sale_order_locks = new HashMap<BtcContants.CoinTypes, ReadWriteLock>();
		static {
			for (BtcContants.CoinTypes coinType : BtcContants.CoinTypes.values()) {
				sale_order_locks.put(coinType, new ReentrantReadWriteLock());
			}
		}

		/**
		 * 获取指定币种卖单缓存
		 * @param coinType
		 * 币种
		 * @return
		 */
		private static LinkedList<SaleOrder> getSaleOrdersWithoutLock(BtcContants.CoinTypes coinType) {
			if (coinType == null) {
				throw new IllegalArgumentException("coinType can't be Null");
			}
			if (!sale_order.containsKey(coinType)) {
				sale_order.put(coinType, new LinkedList<SaleOrder>());
			}
			return sale_order.get(coinType);
		}

		/**
		 * 初始化卖单缓存
		 */
		private static void init() {
			SaleOrderService saleOrderService = ApplicationContextHolder.getBean(SaleOrderService.class);
			// 加载数据库中处于打开状态的单据，根据ID从小到大排序，将ID小的先放入缓存
			List<SaleOrder> orders = saleOrderService.findByWhere(MapUtils.genMap("flag", BtcContants.BuyOrderFlag.open.getValue()));
			for (Iterator<SaleOrder> iter = orders.iterator(); iter.hasNext();) {
				SaleOrderHolder.addOrder(iter.next());
			}
		}

		/**
		 * 获取指定币种卖单缓存
		 * @param type
		 * @return
		 */
		private static List<SaleOrder> getSaleOrders(BtcContants.CoinTypes coinType) {
			getSaleReadLock(coinType).lock();
			LinkedList<SaleOrder> cache = getSaleOrdersWithoutLock(coinType);
			List<SaleOrder> list = new LinkedList<SaleOrder>(cache);
			getSaleReadLock(coinType).unlock();
			return list;
		}

		/**
		 * 添加一个买单
		 * @param saleOrder
		 */
		private static void addOrder(SaleOrder saleOrder) {
			getSaleWriteLock(saleOrder.getCoinType()).lock();
			getSaleOrdersWithoutLock(saleOrder.getCoinType()).add(saleOrder);
			getSaleWriteLock(saleOrder.getCoinType()).unlock();
		}

		/**
		 * 移除一个卖单集合
		 * @param coinType
		 * @param removeList
		 */
		private static void removeSaleOrder(BtcContants.CoinTypes coinType, List<SaleOrder> removeList) {
			getSaleWriteLock(coinType).lock();
			getSaleOrdersWithoutLock(coinType).removeAll(removeList);
			getSaleWriteLock(coinType).unlock();
		}

		/**
		 * 获取买单读锁
		 * @param coinType
		 * @return
		 */
		private static Lock getSaleReadLock(BtcContants.CoinTypes coinType) {
			return sale_order_locks.get(coinType).readLock();
		}

		/**
		 * 获取买单写锁
		 * @param coinType
		 * @return
		 */
		private static Lock getSaleWriteLock(BtcContants.CoinTypes coinType) {
			return sale_order_locks.get(coinType).writeLock();
		}
	}

}
