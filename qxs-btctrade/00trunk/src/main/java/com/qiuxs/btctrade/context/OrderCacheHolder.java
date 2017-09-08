package com.qiuxs.btctrade.context;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.netty.util.internal.ConcurrentHashMap;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.core.context.ApplicationContextHolder;
import com.qiuxs.fdn.exception.utils.Exceptions;
import com.qiuxs.fdn.utils.MapUtils;

/**
 * 买单 卖单缓存
 * @author qiuxs
 *
 */
public class OrderCacheHolder {

	/** 各币种买单缓存 */
	private static Map<BtcContants.CoinTypes, LinkedList<BuyOrder>> buy_order = new ConcurrentHashMap<BtcContants.CoinTypes, LinkedList<BuyOrder>>();
	/** 各币种对应的锁 */
	private static Map<BtcContants.CoinTypes, ReadWriteLock> buy_order_locks = new HashMap<BtcContants.CoinTypes, ReadWriteLock>();
	static {
		buy_order_locks.put(BtcContants.CoinTypes.doge, new ReentrantReadWriteLock());
	}

	/**
	 * 初始化缓存  
	 * 加载数据库中处于开放状态的单据
	 */
	public static void init() {
		BuyOrderService buyOrderService = ApplicationContextHolder.getBean(BuyOrderService.class);
		// 加载数据库中处于打开状态的单据，根据ID从小到大排序，将ID小的先放入缓存
		List<BuyOrder> orders = buyOrderService.findByWhere(MapUtils.genMap("flag", BtcContants.BuyOrderFlag.open.getValue(), "orderBy", "id"));
		for (Iterator<BuyOrder> iter = orders.iterator(); iter.hasNext();) {
			addBuyOrder(iter.next());
		}
	}

	/**
	 * 添加一个买单
	 * @param coinType
	 * @param order
	 * @return 
	 * @return
	 */
	public static void addBuyOrder(BuyOrder order) {
		ReadWriteLock lock = null;
		try {
			lock = buy_order_locks.get(order.getTypeEnum());
			lock.writeLock().lock();
			LinkedList<BuyOrder> orders = getBuyOrders(order.getTypeEnum());
			orders.addFirst(order);
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.writeLock().unlock();
		}

	}

	/**
	 * 获取指定币种买单缓存
	 * @param coinType
	 * @return
	 */
	public static LinkedList<BuyOrder> getBuyOrders(BtcContants.CoinTypes coinType) {
		if (coinType == null) {
			throw new IllegalArgumentException("coinType can't be Null");
		}
		if (!buy_order.containsKey(coinType)) {
			buy_order.put(coinType, new LinkedList<BuyOrder>());
		}
		return buy_order.get(coinType);
	}

	/**
	 * 获取指定币种买单数量
	 * @param coinType
	 * @return
	 */
	public static int getBuyOrderCount(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = null;
		try {
			lock = buy_order_locks.get(coinType);
			lock.readLock().lock();
			LinkedList<BuyOrder> buyOrders = getBuyOrders(coinType);
			return buyOrders == null ? 0 : buyOrders.size();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 获取一个买单
	 * @param coinType
	 * @return
	 */
	public static BuyOrder getLast(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = null;
		try {
			lock = buy_order_locks.get(coinType);
			lock.readLock().lock();
			LinkedList<BuyOrder> orders = getBuyOrders(coinType);
			if (orders.size() == 0) {
				return null;
			}
			return orders.getLast();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.readLock().unlock();
		}
	}

	/**
	 * 移除最后一个元素
	 * @param coinType
	 */
	public static void removeLast(BtcContants.CoinTypes coinType) {
		ReadWriteLock lock = null;
		try {
			lock = buy_order_locks.get(coinType);
			lock.writeLock().lock();
			getBuyOrders(coinType).removeLast();
		} catch (Exception e) {
			throw Exceptions.unchecked(e);
		} finally {
			lock.writeLock().unlock();
		}
	}
}
