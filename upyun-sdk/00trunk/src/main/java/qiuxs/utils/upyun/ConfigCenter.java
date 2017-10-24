package qiuxs.utils.upyun;

public class ConfigCenter {

	/** 空间名/服务名 */
	private String bucket;
	/** 操作员 */
	private String operator;
	/** 密码MD5值 */
	private String pwdMd5;
	/** 接入点 */
	private UpYun.ApiDomain domain;

	/**
	 * @param url
	 * 		又拍云地址
	 * @param operator
	 * 		操作员
	 * @param password
	 * 		操作员密码
	 */
	public ConfigCenter(UpYun.ApiDomain domain, String bucket, String operator, String password) {
		this.domain = domain;
		this.bucket = bucket;
		this.operator = operator;
		this.pwdMd5 = UpYunUtils.md5(password);
	}

	String getBucket() {
		return this.bucket;
	}

	String getOperator() {
		return this.operator;
	}

	String getPwdMd5() {
		return this.pwdMd5;
	}

	UpYun.ApiDomain getDomain() {
		return this.domain;
	}

	public Session build() {
		return new Session(this);
	}

}
