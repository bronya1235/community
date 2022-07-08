package com.nowcoder.app.community;

import jdk.internal.dynalink.beans.StaticClass;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
//绑定配置类
@ContextConfiguration(classes = CommunityApplication.class)
//继承一个类
class CommunityApplicationTests implements ApplicationContextAware {
	private static final Logger logger = LoggerFactory.getLogger(CommunityApplicationTests.class);
	private ApplicationContext applicationContext;



	@Test
	void contextLoads() {
		logger.debug("DEBUG LOG");
		logger.info("info log");
		logger.warn("warn log");
		logger.error("error log");
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
