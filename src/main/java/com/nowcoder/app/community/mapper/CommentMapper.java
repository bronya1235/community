package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/18-07-18-18:10
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@Mapper
public interface CommentMapper {
	/*
	 * 查询评论需要支持分页
	 * */
	List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);
	//查询一共有多少条数据
	int selectCommentsCountByEntity(int entityType, int entityId);
	//插入评论数据
	int insertComment(Comment comment);
}
