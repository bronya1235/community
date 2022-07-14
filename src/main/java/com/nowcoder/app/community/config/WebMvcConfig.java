package com.nowcoder.app.community.config;

import com.nowcoder.app.community.intercepter.LoginRequiredInterceptor;
import com.nowcoder.app.community.intercepter.LoginTicketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: Bao
 * @Date: 2022/7/14-07-14-14:17
 * @Description com.nowcoder.app.community.config
 * @Function
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	@Autowired
	private LoginTicketInterceptor loginTicketInterceptor;

	@Autowired
	private LoginRequiredInterceptor loginRequiredInterceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginTicketInterceptor)
				.excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpeg");
		registry.addInterceptor(loginRequiredInterceptor)
				.excludePathPatterns("/**/*.css","/**/*.js","/**/*.png","/**/*.jpeg");
	}
}
