package com.nowcoder.app.community.service;

import com.nowcoder.app.community.mapper.DiscussPostMapper;
import com.nowcoder.app.community.pojo.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

	public List<DiscussPost> findDiscussPosts(int userId,int offset,int limit){
		return discussPostMapper.selectDiscussPosts(userId, offset, limit);
	}
	public  int findDiscussPostRows(int userId){
		return discussPostMapper.selectDiscussPostRows(userId);
	}
}
