package com.nowcoder.app.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.app.community.pojo.Event;
import com.nowcoder.app.community.pojo.Message;
import com.nowcoder.app.community.service.MessageService;
import com.nowcoder.app.community.util.CommunityConstant;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/26-07-26-10:10
 * @Description com.nowcoder.app.community.event
 * @Function
 */
@Component
public class EventConsumer implements CommunityConstant {
	private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);
	@Autowired
	private MessageService messageService;

	@KafkaListener(topics = {TOPIC_COMMENT,TOPIC_LIKE,TOPIC_FOLLOW})
	public void handleEventMessage(ConsumerRecord record){
		if(record==null||record.value()==null){
			logger.error("消息内容不能为空");
			return;
		}
		//把消息转换成Event格式
		Event event = JSONObject.parseObject(record.value().toString(), Event.class);
		if(event==null){
			logger.error("消息格式错误");
			return;
		}
		//开始发送站内通知，与之前的message有所不同
		Message message = new Message();
		message.setFromId(SYSTEM_USERID);
		message.setToId(event.getEntityUserId());
		message.setConversationId(event.getTopic());
		message.setCreateTime(new Date());
		HashMap<String, Object> map = new HashMap<>();
		map.put("userId", event.getUserId());
		map.put("entityType", event.getEntityType());
		map.put("entityId", event.getEntityId());
		if(!event.getData().isEmpty()){
			for(Map.Entry<String,Object> entry:event.getData().entrySet()){
				map.put(entry.getKey(), entry.getValue());
			}
		}
		message.setContent(JSONObject.toJSONString(map));
		messageService.addMessage(message);
	}
}
