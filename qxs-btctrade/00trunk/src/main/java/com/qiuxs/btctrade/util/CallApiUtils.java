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
	 * @param coinType
	 * 	币种
	 * @param amount
	 * 	数量
	 * @param price
	 *  单价
	 * @return
	 */
	public static boolean saveBuyOrder(BuyOrder order) {
		return false;
	}

	/**
	 * 获取账户状态
	 * @return
	 */
	public static AccountState getAccountState() {
		String params = "key=" + BtcContextManager.getPublicKey();
		JSONObject res = callPrivateApi(BtcContants.Apis.ACCOUNT_INFO, params);
		if (res.containsKey("result") && !res.getBooleanValue("result")) {
			throw new RuntimeException(res.getString("message"));
		}
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
		return HttpClientUtil.postStringRetJson(api, params, ContentType.APPLICATION_FORM_URLENCODED);
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
