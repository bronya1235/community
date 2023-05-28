package com.nowcoder.app.community.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author: Bao
 * @Date: 2022/7/20-07-20-19:37
 * @Description com.nowcoder.app.community.aspect
 * @Function
 */
@Component
//整体称之为一个切面
@Aspect
public class ServiceLogAspect {
	private static final Logger logger = LoggerFactory.getLogger(ServiceLogAspect.class);
	//表示com.nowcoder.app.community.service包下所有类的任意参数和返回值类型的方法
	//切入点
	@Pointcut("execution(* com.nowcoder.app.community.service.*.*(..))")
	public void pointcut(){

	}
	@Before("pointcut()")
	//切入点方法之前运行
	public void advice(JoinPoint joinPoint){
		//自定义一下日志的格式用户[IP地址]，在[时间]，访问了包下的哪个service方法
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		if(requestAttributes ==null){
			return;
		}
		HttpServletRequest request = requestAttributes.getRequest();
		String ip = request.getRemoteHost();
		String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		String method = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
		logger.info(String.format("用户[%s]在[%s]访问了[%s].", ip,time,method));
	}

}
