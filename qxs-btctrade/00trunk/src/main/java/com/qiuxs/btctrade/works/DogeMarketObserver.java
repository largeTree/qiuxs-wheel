package com.qiuxs.btctrade.works;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.btctrade.context.OrderCacheHolder;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.market.entity.BtcMarket;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.util.BtcFeeCalculateUtil;

/**
 * 行情变动观察者
 * @author qiuxs
 *
 */
@Service
public class DogeMarketObserver implements IMarketObserver {

	/** 当前行情 */
	private BtcMarket market;

	@Override
	public void updateMarket(BtcMarket market) {
		this.market = market;
		// 判断当前是否可买入
		if (canBuy()) {
			BuyOrder order = new BuyOrder();
			order.setType(BtcContants.CoinTypes.Doge.getValue());
			order.setNum(calculateNum());
			order.setFlag(BtcContants.BuyOrderFlag.Created.getValue());
			order.setPrice(this.market.getSell());
			order.setMoney(order.getPrice().multiply(order.getNum()));
			order.setBtcFee(BtcFeeCalculateUtil.calculateFee(BtcContants.CoinTypes.Doge, order.getMoney()));
			// 计算最低卖价
			order.setSalePrice(order.getPrice().multiply(BigDecimal.ONE.add(BtcFeeCalculateUtil.getRate(BtcContants.CoinTypes.Doge)))
					.divide(BigDecimal.ONE.subtract(BtcFeeCalculateUtil.getRate(BtcContants.CoinTypes.Doge))));
		}
	}

	/***
	 * 计算本单买的数量
	 * @return
	 */
	private BigDecimal calculateNum() {
		AccountState state = BtcContextManager.getAccountState();
		BigDecimal currentOrderMoney = state.getCny_balance().multiply(BigDecimal.valueOf(0.1D));
		BigDecimal num = currentOrderMoney.divide(this.market.getSell());
		return num;
	}

	/**
	 * 判断当前是否可以买入
	 * @return
	 */
	private boolean canBuy() {
		// 当前缓存的买单数量不大于最大买单数量，卖一价小于（最低价*购买价因子）时买入
		if (OrderCacheHolder.getBuyOrderCount(BtcContants.CoinTypes.Doge) <= BtcContextManager.getMaxBuyOrder(BtcContants.CoinTypes.Doge)
				&& this.market.getSell().compareTo(this.market.getLow().multiply(BtcContextManager.getBuyFactor(BtcContants.CoinTypes.Doge))) < 1) {
			return true;
		}
		return false;
	}

}
