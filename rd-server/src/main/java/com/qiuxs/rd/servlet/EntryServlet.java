package com.qiuxs.rd.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

@WebServlet(urlPatterns = { "/*" })
public class EntryServlet extends HttpServlet {

	private static final long serialVersionUID = -1098608417510917968L;

	private static final String TOURL = "http://qiuxiangshi.wicp.net:8081/wx";

	private HttpClient client = HttpClientBuilder.create().build();

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("----------------------  start  --------------------------");

		// 请求方法
		String method = req.getMethod();
		System.out.println("requestMethod:" + method);

		// 请求参数
		Enumeration<String> names = req.getParameterNames();
		Map<String, String> params = new HashMap<>();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			params.put(key, req.getParameter(key));
		}
		System.out.println("params:" + params.toString());

		// 请求头
		names = req.getHeaderNames();
		Map<String, String> headers = new HashMap<>();
		while (names.hasMoreElements()) {
			String key = names.nextElement();
			if ("Content-Length".equals(key)) {
				continue;
			}
			headers.put(key, req.getHeader(key));
		}
		System.out.println("headers:" + headers.toString());
		String remoteAddr = req.getRemoteAddr();
		System.out.println("remoteAddr:" + remoteAddr);
		// 发送代理
		sendProxy(method, params, headers, resp);
		System.out.println("----------------------  end  --------------------------\n\n");
	}

	private void sendProxy(String method, Map<String, String> params, Map<String, String> headers,
			HttpServletResponse response) {
		if ("post".equals(method.toLowerCase())) {
			HttpPost post = new HttpPost(TOURL);
			setHeader(headers, post);
			List<NameValuePair> nvps = new ArrayList<>();
			for (Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, String> entry = iter.next();
				nvps.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
			}
			try {
				post.setEntity(new UrlEncodedFormEntity(nvps, "utf-8"));
				HttpResponse res = this.client.execute(post);
				sendRes(response, res);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				post.completed();
			}
		} else if ("get".equals(method.toLowerCase())) {
			StringBuilder queryString = new StringBuilder("?");
			for (Iterator<Map.Entry<String, String>> iter = params.entrySet().iterator(); iter.hasNext();) {
				Map.Entry<String, String> entry = iter.next();
				queryString.append(entry.getKey()).append("=").append(entry.getValue());
				if (iter.hasNext()) {
					queryString.append("&");
				}
			}
			HttpGet get = new HttpGet(TOURL + queryString.toString());
			setHeader(headers, get);
			try {
				HttpResponse res = this.client.execute(get);
				sendRes(response, res);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void setHeader(Map<String, String> headers, HttpRequestBase req) {
		for (Iterator<Map.Entry<String, String>> iter = headers.entrySet().iterator(); iter.hasNext();) {
			Map.Entry<String, String> entry = iter.next();
			req.addHeader(entry.getKey(), entry.getValue());
		}
	}

	private void sendRes(HttpServletResponse response, HttpResponse res) {
		BufferedReader in = null;
		try {
			response.setStatus(res.getStatusLine().getStatusCode());
			in = new BufferedReader(new InputStreamReader(res.getEntity().getContent(), "utf-8"));
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			while ((line = in.readLine()) != null) {
				sb.append(line + NL);
			}
			response.getWriter().write(sb.toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
