package qiuxs.utils.upyun;

import java.io.File;
import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;

/**
 * 上传会话
 * @author qiuxs
 *
 */
public class Session {

	/** 配置中心 */
	private ConfigCenter confc;

	public Session(ConfigCenter configCenter) {
		if (StringUtils.isBlank(configCenter.getOperator())) {
			throw new RuntimeException("operator can't be null");
		}
		this.confc = configCenter;
	}

	/**
	 * 使用输入流的方式上传文件
	 * @param targetPath
	 * 		目标路径
	 * @param fileName
	 * 		文件名
	 * @param in
	 * 		流
	 * @return
	 * 		true：成功
	 * 		false：失败
	 */
	public boolean put(String targetPath, String fileName, InputStream in) {
		return false;
	}

	/**
	 * 使用File对象上传文件
	 * @param targetPath
	 * 		目标路径
	 * @param file
	 * 		文件对象
	 * @return
	 * 		true：成功
	 * 		false：失败
	 */
	public boolean put(String targetPath, File file) {
		return false;
	}

}
