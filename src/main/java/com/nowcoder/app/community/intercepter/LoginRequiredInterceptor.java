package com.nowcoder.app.community.intercepter;

import com.nowcoder.app.community.annotation.LoginRequired;
import com.nowcoder.app.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @Author: Bao
 * @Date: 2022/7/14-07-14-20:57
 * @Description com.nowcoder.app.community.intercepter
 * @Function
 */
@Component
public class LoginRequiredInterceptor implements HandlerInterceptor {
	@Autowired
	private HostHolder hostHolder;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(handler instanceof HandlerMethod){
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			LoginRequired annotation = method.getAnnotation(LoginRequired.class);
			if(annotation!=null&&hostHolder.getUser()==null){
				response.sendRedirect(request.getContextPath()+"/login");
				return false;
			}
		}
		return true;
	}
}
