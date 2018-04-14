package qiuxs.test.webcontainer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import qiuxs.test.webcontainer.conf.ServerConfig;

public class ResourceUtil {

	private static String rootLocation;

	private static String webappLocation;

	public static void setRootLocation(String rootLocation) {
		if (rootLocation.startsWith("file:/")) {
			rootLocation = rootLocation.substring(6);
		}
		ResourceUtil.rootLocation = rootLocation;
	}

	public static String getWebappsDir() {
		if (webappLocation == null) {
			String confWebappDir = ServerConfig.getWebappDir();
			if (confWebappDir != null && confWebappDir.startsWith(".")) {
				ResourceUtil.webappLocation = rootLocation + confWebappDir.substring(2);
			} else {
				ResourceUtil.webappLocation = ServerConfig.getWebappDir();
			}
		}
		return webappLocation;
	}

	public static byte[] readWebappResourceAsBytes(String target) {
		try (InputStream in = readAsStream(getWebappsDir(), target);) {
			if (in == null) {
				return null;
			}
			byte[] data = new byte[in.available()];
			in.read(data);
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String readConfAsText(String target) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(readAsStream(rootLocation, target)))) {
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static InputStream readAsStream(String dir, String target) {
		String src = dir + "/" + target;
		File file = new File(src);
		if (file != null && file.exists()) {
			try {
				return new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			return null;
		}
	}

}
