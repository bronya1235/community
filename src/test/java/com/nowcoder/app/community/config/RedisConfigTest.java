package com.nowcoder.app.community.config;

import com.nowcoder.app.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-10:39
 * @Description com.nowcoder.app.community.config
 * @Function
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
class RedisConfigTest {
	@Autowired
	private RedisTemplate<String,Object> template;
	@Test
	void redisTemplate() {
		String redisKey = "test:count";
		template.opsForValue().set(redisKey, 1);
		System.out.println(template.opsForValue().get(redisKey));
		System.out.println(template.opsForValue().increment(redisKey));
		System.out.println(template.opsForValue().decrement(redisKey));
	}
}