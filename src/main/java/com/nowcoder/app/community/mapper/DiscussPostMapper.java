package com.nowcoder.app.community.mapper;

import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.nowcoder.app.community.pojo.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-15:37
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@Mapper
public interface DiscussPostMapper {
	//查询帖子列表，对应于几种业务情况（1、首页的所有帖子，2、首页帖子的分页显示3、用户帖子的分页显示）
	//3个参数，用户id为0，说明不区分用户id，显示所有帖子，offset 和limit用来控制分页
	List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);
	//动态的sql需要学习，@param用于给参数取别名
	//用于查询一共有多少个帖子，在分页显示的实现中比较重要
	int selectDiscussPostRows(@Param("userId") int userId);

	int insertDiscussPost(DiscussPost discussPost);

	DiscussPost selectDiscussPostById(int id);

	int updateCommentCount(int id,int commentCount);
}
