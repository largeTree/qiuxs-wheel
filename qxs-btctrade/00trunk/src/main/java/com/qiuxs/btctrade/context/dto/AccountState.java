package com.qiuxs.btctrade.context.dto;

import java.math.BigDecimal;

/**
 * 账户状态实体
 * @author qiuxs
 *
 */
public class AccountState {

	/** 账户ID */
	private int uid;
	/** 是否实名认证 */
	private int nameauth;
	/** 是否绑定手机 */
	private int moflag;
	/** 折合资产，人民币 */
	private BigDecimal asset;
	/** 人民币余额 */
	private BigDecimal cny_balance;
	/** 人民币冻结余额 */
	private BigDecimal cny_reserved;
	/** 比特比余额 */
	private BigDecimal btc_balance;
	/** 比特比冻结余额 */
	private BigDecimal btc_reserved;
	/** 以太坊余额 */
	private BigDecimal eth_balance;
	/** 以太坊冻结余额 */
	private BigDecimal eth_reserved;
	/** 以太经典余额 */
	private BigDecimal etc_balance;
	/** 以太经典冻结余额 */
	private BigDecimal etc_reserved;
	/** 莱特币余额 */
	private BigDecimal ltc_balance;
	/** 莱特币冻结余额 */
	private BigDecimal ltc_reserved;
	/** 狗狗币余额 */
	private BigDecimal doge_balance;
	/** 狗狗币冻结余额 */
	private BigDecimal doge_reserved;
	/** 元宝币余额 */
	private BigDecimal ybc_balance;
	/** 元宝币冻结余额 */
	private BigDecimal ybc_reserved;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getNameauth() {
		return nameauth;
	}

	public void setNameauth(int nameauth) {
		this.nameauth = nameauth;
	}

	public int getMoflag() {
		return moflag;
	}

	public void setMoflag(int moflag) {
		this.moflag = moflag;
	}

	public BigDecimal getAsset() {
		return asset;
	}

	public void setAsset(BigDecimal asset) {
		this.asset = asset;
	}

	public BigDecimal getCny_balance() {
		return cny_balance;
	}

	public void setCny_balance(BigDecimal cny_balance) {
		this.cny_balance = cny_balance;
	}

	public BigDecimal getCny_reserved() {
		return cny_reserved;
	}

	public void setCny_reserved(BigDecimal cny_reserved) {
		this.cny_reserved = cny_reserved;
	}

	public BigDecimal getBtc_balance() {
		return btc_balance;
	}

	public void setBtc_balance(BigDecimal btc_balance) {
		this.btc_balance = btc_balance;
	}

	public BigDecimal getBtc_reserved() {
		return btc_reserved;
	}

	public void setBtc_reserved(BigDecimal btc_reserved) {
		this.btc_reserved = btc_reserved;
	}

	public BigDecimal getEth_balance() {
		return eth_balance;
	}

	public void setEth_balance(BigDecimal eth_balance) {
		this.eth_balance = eth_balance;
	}

	public BigDecimal getEth_reserved() {
		return eth_reserved;
	}

	public void setEth_reserved(BigDecimal eth_reserved) {
		this.eth_reserved = eth_reserved;
	}

	public BigDecimal getEtc_balance() {
		return etc_balance;
	}

	public void setEtc_balance(BigDecimal etc_balance) {
		this.etc_balance = etc_balance;
	}

	public BigDecimal getEtc_reserved() {
		return etc_reserved;
	}

	public void setEtc_reserved(BigDecimal etc_reserved) {
		this.etc_reserved = etc_reserved;
	}

	public BigDecimal getLtc_balance() {
		return ltc_balance;
	}

	public void setLtc_balance(BigDecimal ltc_balance) {
		this.ltc_balance = ltc_balance;
	}

	public BigDecimal getLtc_reserved() {
		return ltc_reserved;
	}

	public void setLtc_reserved(BigDecimal ltc_reserved) {
		this.ltc_reserved = ltc_reserved;
	}

	public BigDecimal getDoge_balance() {
		return doge_balance;
	}

	public void setDoge_balance(BigDecimal doge_balance) {
		this.doge_balance = doge_balance;
	}

	public BigDecimal getDoge_reserved() {
		return doge_reserved;
	}

	public void setDoge_reserved(BigDecimal doge_reserved) {
		this.doge_reserved = doge_reserved;
	}

	public BigDecimal getYbc_balance() {
		return ybc_balance;
	}

	public void setYbc_balance(BigDecimal ybc_balance) {
		this.ybc_balance = ybc_balance;
	}

	public BigDecimal getYbc_reserved() {
		return ybc_reserved;
	}

	public void setYbc_reserved(BigDecimal ybc_reserved) {
		this.ybc_reserved = ybc_reserved;
	}

}
