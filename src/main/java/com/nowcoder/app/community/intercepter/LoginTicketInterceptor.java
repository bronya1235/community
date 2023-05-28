package com.nowcoder.app.community.intercepter;

import com.nowcoder.app.community.pojo.LoginTicket;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.MessageService;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * @Author: Bao
 * @Date: 2022/7/13-07-13-21:42
 * @Description com.nowcoder.app.community.intercepter
 * @Function 这个拦截器并不是用来拦截的，而是为了用户方便，在每个请求过来的时候，如果之前用户已经登录过了，就把用户信息持有一下
 */
@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
	@Autowired
	private UserService userService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private MessageService messageService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String ticket = Community_tool.getCookieValue(request, "ticket");
		if (ticket != null) {
			LoginTicket loginTicket = userService.findLoginTicket(ticket);
			//检查凭证是否有效
			if (loginTicket != null
					&& loginTicket.getStatus() == 0
					&& loginTicket.getExpired().after(new Date())) {
				User user = userService.findUserById(loginTicket.getUserId());
				//在本次请求中持有用户
				hostHolder.setUser(user);
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		User user = hostHolder.getUser();
		if (user != null && modelAndView != null) {
			modelAndView.addObject("loginUser", user);
			modelAndView.addObject("totalUnreadCount", messageService.findLetterUnreadCount(user.getId(), null)+messageService.findNoticeUnreadCount(user.getId(), null));
		}
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		hostHolder.clear();
	}
}
