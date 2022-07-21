package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.pojo.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/19-07-19-14:39
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@Mapper
public interface MessageMapper {
	//查询当前用户的会话列表，针对每个会话只返回一条最新的私信,支持私信
	List<Message> selectConversations(int userId,int offset,int limit);
	//查询当前用户的会话数量
	int selectConversationCount(int userId);
	//查询某个会话所包含的私信列表
	List<Message> selectLetters(String conversationId,int offset,int limit);
	//查询某个私信会话所包含的私信数量
	int selectLetterCount(String conversationId);
	//查询未读私信的数量，需要考虑两个，总数量和单个会话的未读私信
	int selectLetterUnreadCount(int userId,String conversationId);
	//添加私信的方法
	int insertMessage(Message message);
	//设置消息的状态，批量设置
	int updateStatus(List<Integer> ids,int status);
}
