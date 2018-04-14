package qiuxs.test.webcontainer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import qiuxs.test.webcontainer.conf.ServerConfig;
import qiuxs.test.webcontainer.util.ExceptionUtils;

// 请求报文
//POST /index HTTP/1.1
//Host: localhost
//Connection: keep-alive
//Content-Length: 17
//Postman-Token: 90c05daf-d508-11f6-56d5-518fd4004567
//Cache-Control: no-cache
//Origin: chrome-extension://fhbjgbiflinjbdggehcddcbncdddomop
//User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.101 Safari/537.36
//Content-Type: text/plain;charset=UTF-8
//Accept: */*
//Accept-Encoding: gzip, deflate, br
//Accept-Language: zh-CN,zh;q=0.8
//
//adasdasdadadasdda
//

public class RequestThread implements Runnable {

	private static final String RESP_TEXT_404;
	static {
		StringBuilder resp = new StringBuilder();
		resp.append("HTTP/1.1 404 NOT FOUND\n");
		resp.append("Date: ").append(new Date()).append("\n");
		resp.append("Content-Type: text/html;charset=UTF-8\n");
		resp.append("\n");
		resp.append("<html>\n");
		resp.append("<head>\n");
		resp.append("<title>404</title>\n");
		resp.append("</head>\n");
		resp.append("<body>\n");
		resp.append("<h3>404 NOT FOUND</h3>\n");
		resp.append("</body>\n");
		resp.append("</html>\n");
		RESP_TEXT_404 = resp.toString();
	}

	private static final String RESP_HEADER_200;
	static {
		StringBuilder resp = new StringBuilder();
		resp.append("HTTP/1.1 200 OK\n");
		resp.append("Date: ").append(new Date()).append("\n");
		resp.append("Content-Type: {{contentType}}\n");
		resp.append("\n");
		RESP_HEADER_200 = resp.toString();
	}

	private static final String RESP_500;
	static {
		StringBuilder resp = new StringBuilder();
		resp.append("HTTP/1.1 500 SERVER ERROR\n");
		resp.append("Date: ").append(new Date()).append("\n");
		resp.append("Content-Type: text/html;charset=utf-8\n");
		resp.append("\n");
		resp.append("<html>\n");
		resp.append("<head>\n");
		resp.append("<title>500 ERROR</title>\n");
		resp.append("</head>\n");
		resp.append("<body>\n");
		resp.append("<h3>500 SERVER ERROR</h3>\n");
		resp.append("<p>{{errorContent}}</p>");
		resp.append("</body>\n</html>\n");
		RESP_500 = resp.toString();
	}

	private Socket socket;
	private BufferedReader reader;
	private OutputStream out;

	public RequestThread(Socket socket) {
		this.socket = socket;
		try {
			reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			out = this.socket.getOutputStream();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		handRequest();
	}

	private void handRequest() {
		try {
			List<String> reportLine = new ArrayList<String>();
			String line = null;
			// post数据
			char[] data = null;
			// post数据长度
			int length = 0;
			String contentType = "text/html;charset=UTF-8";
			while (true) {
				line = reader.readLine();
				reportLine.add(line);
				if (line.startsWith("Content-Length")) {
					length = Integer.parseInt(line.split(":")[1].trim());
				}
				if (line.startsWith("Content-Type")) {
					contentType = line.split(":")[1].trim();
				}
				if (line.length() == 0) {
					data = new char[length];
					reader.read(data, 0, length);
					break;
				}
			}
			String targetResource = reportLine.get(0).split(" ")[1].substring(1);
			if (targetResource == null || targetResource.length() == 0) {
				targetResource = ServerConfig.getWelcomeFile();
			}
			if (targetResource.endsWith(".css")) {
				contentType = "text/css;charset=UTF-8";
			} else if (targetResource.endsWith(".js")) {
				contentType = "application/x-javascript;charset=UTF-8";
			}
			byte[] resData = ResourceUtil.readWebappResourceAsBytes(targetResource);
			if (resData == null) {
				doResopnse(RESP_TEXT_404.getBytes());
			} else {
				String header = RESP_HEADER_200.replace("{{contentType}}", contentType);
				byte[] headerData = header.getBytes();
				byte[] mergedResData = new byte[headerData.length + resData.length];
				System.arraycopy(headerData, 0, mergedResData, 0, headerData.length);
				System.arraycopy(resData, 0, mergedResData, headerData.length, resData.length);
				doResopnse(mergedResData);
			}
		} catch (Exception e) {
			String content = ExceptionUtils.toString(e).replace("\n", "<br />").replace("\t", "&emsp;&emsp;");
			String body = RESP_500.replace("{{errorContent}}", content);
			doResopnse(body.getBytes());
		}
	}

	private void doResopnse(byte[] data) {
		try {
			out.write(data);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
				out.close();
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
