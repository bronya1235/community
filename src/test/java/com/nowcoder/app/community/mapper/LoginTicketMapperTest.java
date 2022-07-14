package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.CommunityApplication;
import com.nowcoder.app.community.pojo.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Bao
 * @Date: 2022/7/13-07-13-16:49
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@SpringBootTest
//绑定配置类
@ContextConfiguration(classes = CommunityApplication.class)
class LoginTicketMapperTest {
	@Autowired
	private LoginTicketMapper mapper;
	@Test
	void insertLoginTicket() {
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(123);
		loginTicket.setTicket("324kfjlsalda");
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date());
		System.out.println(mapper.insertLoginTicket(loginTicket));
		System.out.println(loginTicket);
	}

	@Test
	void selectByTicket() {
		System.out.println(mapper.selectByTicket("324kfjlsalda"));
	}

	@Test
	void updateLoginTicket() {
		System.out.println(mapper.updateLoginTicket("324kfjlsalda", 1));
	}
}