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
			order.setType(BtcContants.CoinTypes.Doge.getValue());
			order.setNum(calculateNum());
			order.setFlag(BtcContants.BuyOrderFlag.Created.getValue());
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
			CallApiUtils.saveBuyOrder(order);
			buyOrderService.create(order);
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
