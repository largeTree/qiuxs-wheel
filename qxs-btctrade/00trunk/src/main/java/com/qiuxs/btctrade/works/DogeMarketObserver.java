package com.qiuxs.btctrade.works;

import java.math.BigDecimal;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.btctrade.context.OrderCacheHolder;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.service.BuyOrderService;
import com.qiuxs.btctrade.util.CallApiUtils;
import com.qiuxs.btctrade.util.OrderCheckUtils;
import com.qiuxs.btctrade.util.RateUtils;

/**
 * 行情变动观察者
 * @author qiuxs
 *
 */
@Service
public class DogeMarketObserver implements IMarketObserver {

	@Resource
	private BuyOrderService buyOrderService;

	/** 当前行情 */
	private BtcMarket market;

	@Override
	public void updateMarket(BtcMarket market) {
		this.market = market;
		// 判断当前是否可买入
		if (canBuy()) {
			BuyOrder order = new BuyOrder();
			order.setType(BtcContants.CoinTypes.doge.getValue());
			order.setNum(calculateNum());
			order.setFlag(BtcContants.BuyOrderFlag.open.getValue());
			order.setPrice(this.market.getSell());
			order.setMoney(order.getPrice().multiply(order.getNum()));
			order.setBtcFee(RateUtils.calculateFee(order.getTypeEnum(), order.getMoney()));
			/*
			 *	计算最低卖价
			 *	公式  b > (a(1 + R) / (1 - R)) * (1 + U)
			 *	b ： 最低卖价 
			 *	a : 买价
			 *  R ：手续费率
			 *  U ：最低卖价增长率
			 *  四舍五入取保留五位小数
			 */
			BigDecimal salePrice = order.getPrice().multiply(BigDecimal.ONE.add(RateUtils.getBtcFeeRate(order.getTypeEnum())))
					.divide(BigDecimal.ONE.subtract(RateUtils.getBtcFeeRate(order.getTypeEnum())), BigDecimal.ROUND_HALF_UP)
					.multiply(BigDecimal.ONE.add(RateUtils.getSalePriceAddRate(order.getTypeEnum()))).setScale(5, BigDecimal.ROUND_HALF_UP);
			order.setSalePrice(salePrice);

			if (!OrderCheckUtils.checkCanBuy(order)) {
				return;
			}
			// 保存Btc挂单
			CallApiUtils.saveBuyOrder(order);
			// 持久化买单
			buyOrderService.create(order);
			// 缓存买单 供检查线程使用
			OrderCacheHolder.addBuyOrder(order);
		}
		doSaleIfNecessary();
	}

	/**
	 * 检查是否有需要卖出的单据
	 */
	private void doSaleIfNecessary() {
		// TODO  检查是否有需要卖出的单据
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
		// 当前缓存的买单数量小于最大买单数量，卖一价小于（最低价*购买价因子）时买入
		if (OrderCacheHolder.getBuyOrderCount(BtcContants.CoinTypes.doge) < BtcContextManager.getMaxBuyOrder(BtcContants.CoinTypes.doge)
				&& this.market.getSell().compareTo(this.market.getLow().multiply(BtcContextManager.getBuyFactor(BtcContants.CoinTypes.doge))) < 1) {
			return true;
		}
		return false;
	}

}
