package com.qiuxs.rd.servlet;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = { "/*" })
public class EntryServlet extends HttpServlet {

	private static final long serialVersionUID = -1098608417510917968L;

	private static final String TOURL = "http://qiuxiangshi.wicp.net:8081";

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		info("---------------------- Request start  --------------------------");
		String contextPath = req.getServletContext().getContextPath();
		info("contextPath : " + contextPath);
		// requestURI
		String requestURI = req.getRequestURI();
		requestURI = requestURI.substring(contextPath.length());
		info("requestURI : " + requestURI);

		// queryString
		String queryString = req.getQueryString();
		info("queryString : " + queryString);

		// 请求方法
		String method = req.getMethod();
		info("requestMethod : " + method);

		if ("post".equalsIgnoreCase(method)) {
			this.post(requestURI, queryString, req, resp);
		} else if ("get".equalsIgnoreCase(method)) {
			this.get(requestURI, queryString, req, resp);
		}

		info("---------------------- Request end  --------------------------\n\n");
	}

	private void get(String uri, String queryString, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String finalUrl = TOURL + uri;
		if (queryString != null && queryString.length() > 0) {
			finalUrl += ("?" + queryString);
		}
		// queryString
		HttpURLConnection conn = this.openConnection(finalUrl);
		conn.setRequestMethod("GET");
		// 请求头
		this.copyReqHeaders(req, conn);
		// 响应头
		this.copyRespHeaders(conn, resp);
		// 响应主题
		this.copyResponse(conn, resp);
	}

	private void post(String uri, String queryString, HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		String finalUrl = TOURL + uri;
		if (queryString != null && queryString.length() > 0) {
			finalUrl += ("?" + queryString);
		}
		HttpURLConnection conn = this.openConnection(finalUrl);
		conn.setRequestMethod("POST");

		InputStream fromIn = null;
		OutputStream toOut = null;

		try {
			this.copyReqHeaders(req, conn);
			toOut = conn.getOutputStream();
			fromIn = req.getInputStream();
			// 转发请求头
			// 请求主体
			this.copyStream(fromIn, toOut);
			// 响应头
			this.copyRespHeaders(conn, resp);
			// 响应主题
			this.copyResponse(conn, resp);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(fromIn);
			close(toOut);
		}
	}

	private void copyRespHeaders(HttpURLConnection conn, HttpServletResponse resp) {
		Map<String, List<String>> headerFields = conn.getHeaderFields();
		for (Iterator<Map.Entry<String, List<String>>> iter = headerFields.entrySet().iterator(); iter.hasNext();) {
			Entry<String, List<String>> entry = iter.next();
			List<String> values = entry.getValue();
			if (values.size() >= 1) {
				resp.setHeader(entry.getKey(), values.get(0));
			}
		}
	}

	private void copyResponse(HttpURLConnection conn, HttpServletResponse resp) {
		OutputStream fromOut = null;
		InputStream toIn = null;
		try {
			toIn = conn.getInputStream();
			fromOut = resp.getOutputStream();
			this.copyStream(toIn, fromOut);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close(toIn);
			close(fromOut);
		}
	}

	private void copyStream(InputStream in, OutputStream out) throws IOException {
		byte[] onecData = new byte[1024];
		int length = -1;
		while ((length = in.read(onecData)) != -1) {
			if (length < 1024) {
				out.write(onecData, 0, length);
			} else {
				out.write(onecData);
			}
		}
	}

	private void copyReqHeaders(HttpServletRequest req, HttpURLConnection conn) {
		Enumeration<String> names = req.getHeaderNames();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			// if ("Content-Length".equalsIgnoreCase(key)) {
			// continue;
			// }
			String value = req.getHeader(key);
			conn.setRequestProperty(key, value);
			info("copyHeader : key=" + key + "  value=" + value);
		}
	}

	private HttpURLConnection openConnection(String strUrl) throws IOException {
		URL url = new URL(strUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setDoOutput(true);
		conn.setDoInput(true);
		conn.setConnectTimeout(60 * 1000);
		conn.setReadTimeout(60 * 1000);
		return conn;
	}

	private void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void info(String log) {
		System.out.println(log);
	}

}
