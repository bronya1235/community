package com.nowcoder.app.community.service;

import com.nowcoder.app.community.filters.SensitiveFilter;
import com.nowcoder.app.community.mapper.DiscussPostMapper;
import com.nowcoder.app.community.pojo.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-16:32
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class DiscussPostService {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Autowired
	private SensitiveFilter sensitiveFilter;

	public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}

	public int findDiscussPostRows(int userId) {

		return discussPostMapper.selectDiscussPostRows(userId);
	}
	//发布帖子
	public int addDiscussPost(DiscussPost post){
		if(post==null){
			throw new IllegalArgumentException("参数不能为空");
		}
		//转义HTML标记
		post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
		post.setContent(HtmlUtils.htmlEscape(post.getContent()));
		//过滤敏感词
		post.setTitle(sensitiveFilter.filter(post.getTitle()));
		post.setContent(sensitiveFilter.filter(post.getContent()));
		return discussPostMapper.insertDiscussPost(post);
	}
	//查看帖子详情
	public DiscussPost findDiscussPostById(int id){
		return discussPostMapper.selectDiscussPostById(id);
	}
	//更新帖子的评论数量
	public int updateCommentCount(int id,int commentCount){
		return discussPostMapper.updateCommentCount(id, commentCount);
	}
}
