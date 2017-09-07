package com.qiuxs.btctrade.util;

import org.apache.http.entity.ContentType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.fdn.Constant;
import com.qiuxs.fdn.utils.net.HttpClientUtil;
import com.qiuxs.fdn.utils.security.MD5Util;
import com.qiuxs.fdn.utils.security.SecurityUtil;

public class CallApiUtils {

	/**
	 * 挂买单
	 *  并将买单ID设置进买单对象
	 * @param coinType
	 * 	币种
	 * @param amount
	 * 	数量
	 * @param price
	 *  单价
	 * @return
	 */
	public static String saveBuyOrder(BuyOrder order) {
		StringBuilder params = new StringBuilder();
		params.append("key=").append(BtcContextManager.getPublicKey())
				.append("&coin=").append(BtcContants.CoinTypes.valueOf(order.getType()).toString().toLowerCase())
				.append("&amount=").append(order.getNum())
				.append("&price=").append(order.getPrice());
		JSONObject res = callPrivateApi(BtcContants.Apis.BUY, params.toString());
		order.setBtcOrderId(res.getString("id"));
		return order.getBtcOrderId();
	}

//	public static void main(String[] args) {
//		BtcContextManager.init();
//		BuyOrder order = new BuyOrder();
//		order.setType(BtcContants.CoinTypes.Doge.getValue());
//		order.setNum(BigDecimal.valueOf(100D));
//		order.setFlag(BtcContants.BuyOrderFlag.Created.getValue());
//		order.setPrice(BigDecimal.valueOf(0.012D));
//		order.setMoney(order.getPrice().multiply(order.getNum()));
//		order.setBtcFee(RateUtils.calculateFee(order.getTypeEnum(), order.getMoney()));
//		/*
//		 *	计算最低卖价
//		 *	公式  b > (a(1 + R) / (1 - R)) * (1 + U)
//		 *	b ： 最低卖价 
//		 *	a : 买价
//		 *  R ：手续费率
//		 *  U ：最低卖价增长率	 
//		 */
//		BigDecimal salePrice = order.getPrice().multiply(BigDecimal.ONE.add(RateUtils.getBtcFeeRate(order.getTypeEnum())))
//				.divide(BigDecimal.ONE.subtract(RateUtils.getBtcFeeRate(order.getTypeEnum())), BigDecimal.ROUND_HALF_UP)
//				.multiply(BigDecimal.ONE.add(RateUtils.getSalePriceAddRate(order.getTypeEnum()))).setScale(5, BigDecimal.ROUND_HALF_UP);
//		order.setSalePrice(salePrice);
//		order.setBtcOrderId(saveBuyOrder(order));
//		BuyOrderService svc = ApplicationContextHolder.getBean(BuyOrderService.class);
//		svc.create(order);
//	}

	/**
	 * 获取账户状态
	 * @return
	 */
	public static AccountState getAccountState() {
		String params = "key=" + BtcContextManager.getPublicKey();
		JSONObject res = callPrivateApi(BtcContants.Apis.ACCOUNT_INFO, params);
		return JSON.toJavaObject(res, AccountState.class);
	}

	/**
	 * 统一调取Api
	 * @param api
	 * @param params
	 * @return
	 */
	private static JSONObject callPrivateApi(String api, String params) {
		params += "&nonce=" + System.currentTimeMillis();
		params += "&version=2";
		params += "&signature=" + sign(params);
		JSONObject res = HttpClientUtil.postStringRetJson(api, params, ContentType.APPLICATION_FORM_URLENCODED, Constant.UTF8);
		if (res.containsKey("result") && !res.getBooleanValue("result")) {
			throw new RuntimeException(res.getString("message"));
		}
		return res;
	}

	/**
	 * 获取签名
	 * @param params
	 * @return
	 */
	private static String sign(String params) {
		return SecurityUtil.encodeSHA256(params, MD5Util.MD5Encode(BtcContextManager.getPrivateKey(), Constant.UTF8));
	}

}
