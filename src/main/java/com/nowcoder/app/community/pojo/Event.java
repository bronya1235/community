package com.nowcoder.app.community.pojo;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/26-07-26-9:57
 * @Description com.nowcoder.app.community.pojo
 * @Function
 */
public class Event {
	private String topic;//点赞、评论、
	private int userId;//谁触发的事件
	private int entityType;//对象实体的类型
	private int entityId;//对象实体的id
	private int entityUserId;//对象实体属于哪个用户
	private Map<String,Object> data = new HashMap<>();

	public String getTopic() {
		return topic;
	}

	public Event setTopic(String topic) {
		this.topic = topic;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public Event setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public int getEntityType() {
		return entityType;
	}

	public Event setEntityType(int entityType) {
		this.entityType = entityType;
		return this;
	}

	public int getEntityId() {
		return entityId;
	}

	public Event setEntityId(int entityId) {
		this.entityId = entityId;
		return this;
	}

	public int getEntityUserId() {
		return entityUserId;
	}

	public Event setEntityUserId(int entityUserId) {
		this.entityUserId = entityUserId;
		return this;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public Event setData(String key,Object value) {
		this.data.put(key, value);
		return this;
	}
}
