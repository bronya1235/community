package com.nowcoder.app.community.kafka;

/**
 * @Author: Bao
 * @Date: 2022/7/25-07-25-20:35
 * @Description com.nowcoder.app.community.kafka
 * @Function
 */

import com.nowcoder.app.community.CommunityApplication;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class KafkaTest {
	@Autowired
	private KafkaProducer kafkaProducer;

	public void testKafka(){
		kafkaProducer.sendMessage("test", "hello");
		kafkaProducer.sendMessage("test", "world");
		try {
			Thread.sleep(1000*10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
@Component
class KafkaProducer{
	@Autowired
	private KafkaTemplate kafkaTemplate;
	public void sendMessage(String topic,String content){
		kafkaTemplate.send(topic,content);
	}
}

@Component
class KafkaConsumer{
	@KafkaListener(topics = {"test"})
	public void handleMessage(ConsumerRecord record){
		System.out.println(record.value());
	}
}