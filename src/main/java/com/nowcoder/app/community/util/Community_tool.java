package com.nowcoder.app.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * @Author: Bao
 * @Date: 2022/7/12-07-12-15:44
 * @Description com.nowcoder.app.community.util
 * @Function
 */
public class Community_tool {
	//生成随机字符串
	public static String getUUID(){
		return UUID.randomUUID().toString().replaceAll("-", "");
	}

	//MD5算法加密
	public static String md5(String key){
		if(StringUtils.isBlank(key)){
			return null;
		}else {
			return DigestUtils.md5DigestAsHex(key.getBytes());
		}
	}
	public static String getValue(HttpServletRequest request,String key){
		if(request==null||key==null){
			throw new IllegalArgumentException("参数为空，非法");

		}
		Cookie[] cookies = request.getCookies();
		if(cookies!=null){
			for (Cookie cookie : cookies) {
				if(cookie.getName().equals(key)){
					return cookie.getValue();
				}
			}
		}
		return null;
	}
}
