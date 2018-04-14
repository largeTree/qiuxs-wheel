package qiuxs.test.webcontainer.util;

public class ExceptionUtils {

	public static String toString(Throwable e) {
		StringBuilder sb = new StringBuilder();
		sb.append(e.toString()).append("\n");
		do {
			for (StackTraceElement ele : e.getStackTrace()) {
				sb.append("\t").append("at ").append(ele.getClassName()).append(".").append(ele.getMethodName())
						.append("(");
				if (ele.isNativeMethod()) {
					sb.append("Native Method");
				} else {
					sb.append(ele.getFileName())
							.append(":")
							.append(ele.getLineNumber());
				}
				sb.append(")\n");
			}
			e = e.getCause();
		} while (e != null);
		return sb.toString();
	}

}
