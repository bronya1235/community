package com.nowcoder.app.community.util;

import com.nowcoder.app.community.CommunityApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.Thymeleaf;
import org.thymeleaf.context.Context;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author: Bao
 * @Date: 2022/7/11-07-11-19:19
 * @Description com.nowcoder.app.community.util
 * @Function
 */
@SpringBootTest
//绑定配置类
@ContextConfiguration(classes = CommunityApplication.class)
class MailClientTest {
	@Autowired
	private MailClient mailClient;

	//手动调用thymeleaf模板引擎
	@Autowired
	private TemplateEngine templateEngine;
	@Test
	void sendMail() {
		Context context = new Context();
		context.setVariable("username", "Sunny");
		String process = templateEngine.process("/mail/demo", context);
		mailClient.sendMail("baoyepeng@sjtu.edu.cn", "test", process);
	}
}