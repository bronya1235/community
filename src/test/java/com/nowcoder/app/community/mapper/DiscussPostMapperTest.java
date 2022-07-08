package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.CommunityApplication;
import com.nowcoder.app.community.pojo.DiscussPost;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-15:55
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@SpringBootTest
//绑定配置类
@ContextConfiguration(classes = CommunityApplication.class)
class DiscussPostMapperTest {
	@Autowired
	private DiscussPostMapper discussPostMapper;
	@Test
	void name() {
		int count = discussPostMapper.selectDiscussPostRows(0);
		System.out.println(count);
		List<DiscussPost> discussPosts = discussPostMapper.selectDiscussPosts(0, 2, 10);
		for (DiscussPost discussPost : discussPosts) {
			System.out.println(discussPost);
		}
	}
}