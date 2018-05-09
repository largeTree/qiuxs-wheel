package com.qiuxs.rocketmq.consumer;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * Hello world!
 *
 */
public class Consumer {

	public static void main(String[] args) {
		DefaultMQPushConsumer consumer = null;

		try {
			consumer = new DefaultMQPushConsumer("qiuxs-consumer");

			consumer.setNamesrvAddr("192.168.0.106:9876");
			consumer.setInstanceName("qiuxs-consumer-1");

			consumer.subscribe("demo-topic", "tag1");
			
			CountDownLatch count = new CountDownLatch(96136);
			
			consumer.registerMessageListener(new MessageListenerConcurrently() {
				@Override
				public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
					for (MessageExt msg : msgs) {
						System.out.println("topic : " + msg.getTopic());
						System.out.println("tags : " + msg.getTags());
						System.out.println("body : " + new String(msg.getBody()));
					}
					count.countDown();
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
			});
			long start = System.currentTimeMillis();
			consumer.start();
			count.await();
			System.out.println("finish in time : " + (System.currentTimeMillis() - start));
		} catch (MQClientException | InterruptedException e) {
			e.printStackTrace();
		} finally {
			consumer.shutdown();
		}
	}
}
