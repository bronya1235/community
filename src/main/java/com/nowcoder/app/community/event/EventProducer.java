package com.nowcoder.app.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.app.community.pojo.Event;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: Bao
 * @Date: 2022/7/26-07-26-10:06
 * @Description com.nowcoder.app.community.event
 * @Function
 */
@Component
public class EventProducer {
	@Autowired
	private KafkaTemplate kafkaTemplate;
	//处理事件
	public void fireEvent(Event event){
		kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
	}
}
