package com.qiuxs.btctrade.util;

import org.apache.http.entity.ContentType;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qiuxs.btctrade.constants.BtcContants;
import com.qiuxs.btctrade.context.BtcContextManager;
import com.qiuxs.btctrade.context.dto.AccountState;
import com.qiuxs.btctrade.order.entity.BuyOrder;
import com.qiuxs.btctrade.order.entity.SaleOrder;
import com.qiuxs.fdn.Constant;
import com.qiuxs.fdn.utils.net.HttpClientUtil;
import com.qiuxs.fdn.utils.security.MD5Util;
import com.qiuxs.fdn.utils.security.SecurityUtil;

public class CallApiUtils {

	/**
	 * 挂买单
	 *  并将买单ID设置进买单对象
	 * @param order 
	 * 	买单
	 * @return
	 */
	public static String saveBuyOrder(BuyOrder order) {
		StringBuilder params = new StringBuilder();
		params.append("coin=").append(order.getCoinType().toString())
				.append("&amount=").append(order.getNum())
				.append("&price=").append(order.getPrice());
		JSONObject res = callPrivateApi(BtcContants.Apis.BUY, params.toString());
		order.setBtcOrderId(res.getString("id"));
		return order.getBtcOrderId();
	}

	/**
	 * 挂卖单
	 * @param order
	 * @return
	 */
	public static String saveSaleOrder(SaleOrder order) {
		StringBuilder params = new StringBuilder();
		params.append("coin=").append(order.getCoinType().toString())
				.append("&amount=").append(order.getNum())
				.append("&price=").append(order.getPrice());
		JSONObject res = callPrivateApi(BtcContants.Apis.SALE, params.toString());
		order.setBtcOrderId(res.getString("id"));
		return order.getBtcOrderId();
	}

	/**
	 * 获取订单信息
	 * @param orderId
	 *  订单ID
	 * @return
	 */
	public static BtcOrderDetailDTO fetchOrder(String orderId) {
		StringBuilder params = new StringBuilder();
		params.append("id=").append(orderId);
		JSONObject res = callPrivateApi(BtcContants.Apis.FETCH_ORDER, params.toString());
		return JSON.toJavaObject(res, BtcOrderDetailDTO.class);
	}

	/**
	 * 获取账户状态
	 * @return
	 */
	public static AccountState getAccountState() {
		String params = "";
		JSONObject res = callPrivateApi(BtcContants.Apis.ACCOUNT_INFO, params);
		return JSON.toJavaObject(res, AccountState.class);
	}

	/**
	 * 统一调取Api
	 * @param api
	 * @param params
	 * 	参数字符串
	 *  不需要包括自增数，version，及签名
	 * @return
	 */
	private static JSONObject callPrivateApi(String api, String params) {
		if (params != null && params.length() > 0) {
			params += "&";
		}
		params += "key=" + BtcContextManager.getPublicKey();
		params += "&nonce=" + System.currentTimeMillis();
		params += "&version=2";
		params += "&signature=" + sign(params);
		JSONObject res = HttpClientUtil.postStringRetJson(api, params, ContentType.create("application/x-www-form-urlencoded", Constant.UTF8));
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
