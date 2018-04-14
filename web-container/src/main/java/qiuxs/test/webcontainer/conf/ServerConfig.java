package qiuxs.test.webcontainer.conf;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import qiuxs.test.webcontainer.ResourceUtil;

public class ServerConfig {

	private static JSONObject config;

	public static void init() {
		String configText = ResourceUtil.readConfAsText("conf/server.json");
		config = JSON.parseObject(configText);
	}

	public static String getWebappDir() {
		String dir = "./webapps";
		if (config.containsKey("webappDir")) {
			dir = config.getString("webappDir");
		}
		return dir;
	}

	public static int getPort() {
		if (config.containsKey("port")) {
			return config.getIntValue("port");
		}
		return 80;
	}

	public static int getShutdownPort() {
		if (config.containsKey("shutdownPort")) {
			return config.getIntValue("shutdownPort");
		}
		return 81;
	}

	public static String getWelcomeFile() {
		if (config.containsKey("welcomeFile")) {
			return config.getString("welcomeFile");
		}
		return "index.html";
	}

}
