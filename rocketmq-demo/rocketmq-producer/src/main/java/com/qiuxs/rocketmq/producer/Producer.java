package com.qiuxs.rocketmq.producer;

import java.util.concurrent.CountDownLatch;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * Hello world!
 *
 */
public class Producer {
	public static void main(String[] args) {
		try {
			// 构建生产者，参数生产者组名
			final DefaultMQProducer producer = new DefaultMQProducer("qiuxs-producer");
			// namesrv地址
			producer.setNamesrvAddr("192.168.0.106:9876");
			// 实例名
			producer.setInstanceName("qiuxs-producer-1");
			producer.start();

			StringBuilder data = new StringBuilder();
			int dataSize = 51200;
			for (int i = 0; i < dataSize; i++) {
				data.append("1");
			}

			long start = System.currentTimeMillis();
			CountDownLatch count = new CountDownLatch(10);
			for (int i = 0; i < 10; i++) {
				new Thread(() -> {
					for (int j = 0; j < 10000; j++) {
						// 构造消息体
						Message msg = new Message("demo-topic", "tag1", "1", data.toString().getBytes());
						// 发送消息
						SendResult sendResult;
						try {
							sendResult = producer.send(msg);
							System.out.println(sendResult);
						} catch (MQClientException e) {
							e.printStackTrace();
						} catch (RemotingException e) {
							e.printStackTrace();
						} catch (MQBrokerException e) {
							e.printStackTrace();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					count.countDown();
				}).start();
			}
			count.await();
			System.out.println("finish in times : " + (System.currentTimeMillis() - start));
			producer.shutdown();
		} catch (MQClientException | InterruptedException e) {
			e.printStackTrace();
		} finally {
		}
	}
}
