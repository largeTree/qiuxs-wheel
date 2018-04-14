package qiuxs.test.webcontainer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import qiuxs.test.webcontainer.conf.ServerConfig;

/**
 * Hello world!
 *
 */
public class Starter {

	private static final ExecutorService pool = Executors.newFixedThreadPool(100);

	private static ServerSocket server;

	public static void main(String[] args) {
		ResourceUtil.setRootLocation(Starter.class.getClassLoader().getResource("").toString());
		ServerConfig.init();
		startShutdownListener();
		try {
			server = new ServerSocket(ServerConfig.getPort());
			System.out.println("startup success...");
			System.out.println("listen port :" + ServerConfig.getPort());
			while (true) {
				Socket client = server.accept();
				pool.submit(new RequestThread(client));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static void startShutdownListener() {
		new Thread(() -> {
			try {
				ServerSocket shutdown = new ServerSocket(ServerConfig.getShutdownPort());
				System.out.println("shutdown port : " + ServerConfig.getShutdownPort());
				shutdown.accept();
				pool.shutdown();
				server.close();
				shutdown.close();
				System.out.println("exix...bye");
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();
	}
}
