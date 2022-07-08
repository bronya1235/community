package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.CommunityApplication;
import com.nowcoder.app.community.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Bao
 * @Date: 2022/7/6-07-06-21:47
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@SpringBootTest
//绑定配置类
@ContextConfiguration(classes = CommunityApplication.class)
//继承一个类
class UserMapperTest {
	@Autowired
	private UserMapper userMapper;

	@Test
	void selectById() {
		System.out.println(userMapper.selectById(101));
	}

	@Test
	void selectByUsername() {
		System.out.println(userMapper.selectByUsername("liubei"));
	}

	@Test
	void selectByEmail() {
		System.out.println(userMapper.selectByEmail("nowcoder101@sina.com"));
	}

	@Test
	void insertUser() {
		User user = new User("包包", "123456", "dasdsaa", "qq.com", 1, 1, null, null, null);
		int i = userMapper.insertUser(user);
		System.out.println(i);
		System.out.println(userMapper.selectById(user.getId()));
	}

	@Test
	void updateStatus() {
		userMapper.updateStatus(150, 2);
	}

	@Test
	void updateHeader() {
		userMapper.updateHeader(150, "http1254325346");
	}

	@Test
	void updatePassword() {
		userMapper.updatePassword(150, "dsadaghasdasdsa");
	}
}