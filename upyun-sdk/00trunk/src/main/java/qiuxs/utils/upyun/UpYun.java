package qiuxs.utils.upyun;

public class UpYun {
	/**
	 * 默认的编码格式
	 */
	private static final String UTF8 = "UTF-8";

	/**
	 * 路径的分割符
	 */
	private final String SEPARATOR = "/";

	private final String AUTHORIZATION = "Authorization";
	private final String DATE = "Date";
	private final String CONTENT_LENGTH = "Content-Length";
	private final String CONTENT_MD5 = "Content-MD5";
	private final String CONTENT_SECRET = "Content-Secret";
	private final String MKDIR = "mkdir";

	private final String X_UPYUN_WIDTH = "x-upyun-width";
	private final String X_UPYUN_HEIGHT = "x-upyun-height";
	private final String X_UPYUN_FRAMES = "x-upyun-frames";
	private final String X_UPYUN_FILE_TYPE = "x-upyun-file-type";
	private final String X_UPYUN_FILE_SIZE = "x-upyun-file-size";
	private final String X_UPYUN_FILE_DATE = "x-upyun-file-date";

	private final String METHOD_HEAD = "HEAD";
	private final String METHOD_GET = "GET";
	private final String METHOD_PUT = "PUT";
	private final String METHOD_DELETE = "DELETE";

	/**
	 * 接入点定义
	 * @author qiuxs
	 *
	 */
	public enum ApiDomain {

		/** 自动选择接入点 */
		ED_AUTO("v0.api.upyun.com"),
		/** 电信接入点 */
		ED_TELECOM("v1.api.upyun.com"),
		/** 联通接入点 */
		ED_CNC("v2.api.upyun.com"),
		/** 移动铁通接入点 */
		ED_CTT("v3.api.upyun.com");

		private String value;

		ApiDomain(String domain) {
			this.value = domain;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}
}
