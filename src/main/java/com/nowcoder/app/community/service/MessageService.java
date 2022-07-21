package com.nowcoder.app.community.service;

import com.nowcoder.app.community.filters.SensitiveFilter;
import com.nowcoder.app.community.mapper.MessageMapper;
import com.nowcoder.app.community.pojo.Message;
import com.nowcoder.app.community.util.Community_tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/19-07-19-16:18
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;

	@Autowired
	private SensitiveFilter sensitiveFilter;
	//查询当前用户的会话列表，针对每个会话只返回一条最新的私信,支持私信
	public List<Message> findConversations(int userId, int offset, int limit){
		return messageMapper.selectConversations(userId, offset, limit);
	}
	//查询当前用户的会话数量
	public int findConversationCount(int userId){
		return messageMapper.selectConversationCount(userId);
	}
	//查询某个会话所包含的私信列表
	public List<Message> findLetters(String conversationId,int offset,int limit){
		return messageMapper.selectLetters(conversationId, offset, limit);
	}
	//查询某个私信会话所包含的私信数量
	public int findLetterCount(String conversationId){
		return messageMapper.selectLetterCount(conversationId);
	}
	//查询未读私信的数量，需要考虑两个，总数量和单个会话的未读私信
	public int findLetterUnreadCount(int userId,String conversationId){
		return messageMapper.selectLetterUnreadCount(userId, conversationId);
	}
	//添加message的方法
	public int addMessage(Message message){
		//需要对message进行敏感词过滤
		if(message==null){
			throw new IllegalArgumentException("参数不能为空");
		}
		//标签过滤
		message.setContent(HtmlUtils.htmlEscape(message.getContent()));
		//敏感词过滤
		message.setContent(sensitiveFilter.filter(message.getContent()));
		//插入数据
		return messageMapper.insertMessage(message);
	}
	public int readMessage(List<Integer> ids){
		return messageMapper.updateStatus(ids, 1);
	}
}
