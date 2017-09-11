package com.qiuxs.btctrade.works;

import java.math.BigDecimal;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.btctrade.context.OrderCacheHolder;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.entity.SaleOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.order.service.SaleOrderService;
import com.qiuxs.btctrade.util.CallApiUtils;
import com.qiuxs.btctrade.util.OrderCheckUtils;
import com.qiuxs.btctrade.util.RateUtils;

/**
 * 狗币行情变动观察者
 * @author qiuxs
 *
 */
@Service
public class DogeMarketObserver implements IMarketObserver {

	private static Logger log = Logger.getLogger(DogeMarketObserver.class);

	@Resource
	private BuyOrderService buyOrderService;

	@Resource
	private SaleOrderService saleOrderService;

	/** 当前行情 */
	private BtcMarket market;

	@Override
	public void updateMarket(BtcMarket market) {
		this.market = market;
		doBuyIfNecessary();
		doSaleIfNecessary();
	}

	/**
	 * 执行买入，如果需要的话
	 */
	private void doBuyIfNecessary() {
		// 判断当前是否可买入
		if (canBuy()) {
			try {
				// 降低购买因子，从而提高购买难度，需要更加接近低价才会再次触发购买
				BtcContextManager.reduceBuyFactor(this.market.getCoinType());
				BuyOrder order = new BuyOrder();
				order.setType(this.market.getType());
				order.setNum(calculateNum());
				order.setFlag(BtcContants.BuyOrderFlag.open.getValue());
				order.setPrice(this.market.getSell());
				order.setMoney(order.getPrice().multiply(order.getNum()));
				order.setBtcFee(RateUtils.calculateFee(order.getCoinType(), order.getMoney()));
				/*
				 *	计算最低卖价
				 *	公式  b > (a(1 + R) / (1 - R)) * (1 + U)
				 *	b ： 最低卖价 
				 *	a : 买价
				 *  R ：手续费率
				 *  U ：最低卖价增长率
				 *  四舍五入取保留五位小数
				 */
				BigDecimal salePrice = order.getPrice().multiply(BigDecimal.ONE.add(RateUtils.getBtcFeeRate(order.getCoinType())))
						.divide(BigDecimal.ONE.subtract(RateUtils.getBtcFeeRate(order.getCoinType())), BigDecimal.ROUND_HALF_UP)
						.multiply(BigDecimal.ONE.add(RateUtils.getSalePriceAddRate(order.getCoinType()))).setScale(5, BigDecimal.ROUND_HALF_UP);
				order.setSalePrice(salePrice);

				if (!OrderCheckUtils.checkCanBuy(order)) {
					return;
				}
				// 保存Btc挂单
				CallApiUtils.saveBuyOrder(order);
				BtcContextManager.reduceBuyFactor(this.market.getCoinType());
				// 持久化买单
				buyOrderService.create(order);
				// 缓存买单 供检查线程使用
				OrderCacheHolder.addBuyOrder(order);
			} catch (Exception e) {
				// 扩大购买因子，降低购买难度
				BtcContextManager.increaseBuyFactor(this.market.getCoinType());
			}
		}
	}

	/**
	 * 卖出可以卖出的单据
	 */
	private void doSaleIfNecessary() {
		// TODO  检查是否有需要卖出的单据
		List<BuyOrder> canSaleOrders = buyOrderService.findCanSaleOrders(this.market);
		if (canSaleOrders.size() > 0) {
			for (BuyOrder order : canSaleOrders) {
				doSale(order);
			}
		}
	}

	/**
	 * 卖出一个单据并保存卖单数据及缓存
	 * @param order
	 */
	private void doSale(BuyOrder order) {
		try {
			SaleOrder saleOrder = new SaleOrder();
			saleOrder.setType(this.market.getType());
			saleOrder.setPrice(this.market.getBuy());
			saleOrder.setNum(order.getNum());
			// 总价
			saleOrder.setMoney(saleOrder.getNum().multiply(saleOrder.getPrice()));
			saleOrder.setBtcFee(RateUtils.calculateFee(saleOrder.getCoinType(), saleOrder.getMoney()));
			// 计算收益
			saleOrder.calculateProfit(order);
			// 保存Btc单据
			CallApiUtils.saveSaleOrder(saleOrder);
			// 设置为已卖出状态
			order.setFlag(BtcContants.BuyOrderFlag.saled.getValue());
			buyOrderService.update(order);
			// 加入缓存
			OrderCacheHolder.addSaleOrder(saleOrder);
			this.saleOrderService.create(saleOrder);
			// 降低购买难度
			BtcContextManager.increaseBuyFactor(order.getCoinType());
			OrderCacheHolder.getAndDecrement(order.getCoinType());
		} catch (Exception e) {
			log.error("ext=" + e.getLocalizedMessage(), e);
		}
	}

	/***
	 * 计算本单买的数量
	 * @return
	 */
	private BigDecimal calculateNum() {
		AccountState state = BtcContextManager.getAccountState();
		BigDecimal currentOrderMoney = state.getCny_balance().multiply(BigDecimal.valueOf(0.1D));
		BigDecimal num = currentOrderMoney.divide(this.market.getSell(), BigDecimal.ROUND_HALF_UP);
		return num;
	}

	/**
	 * 判断当前是否可以买入
	 * @return
	 */
	private boolean canBuy() {
		BigDecimal targetPrice = this.market.getLow().multiply(BigDecimal.ONE.add(BtcContextManager.getBuyFactor(BtcContants.CoinTypes.doge)))
				.setScale(5, BigDecimal.ROUND_HALF_UP);
		System.out.println("目标买价  ------>>> " + targetPrice + "\n卖一价      ------>>> " + this.market.getSell());
		// 当前缓存的买单数量小于最大买单数量，卖一价小于（最低价*(1 + 买入因子)）时买入
		if (OrderCacheHolder.getBuyOrderCount(BtcContants.CoinTypes.doge) < BtcContextManager.getMaxBuyOrder(BtcContants.CoinTypes.doge)
				&& this.market.getSell().compareTo(targetPrice) < 1) {
			return true;
		}
		return false;
	}

}
