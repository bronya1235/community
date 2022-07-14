package com.nowcoder.app.community.util;

import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.UserService;
import org.springframework.stereotype.Component;

/**
 * @Author: Bao
 * @Date: 2022/7/13-07-13-21:57
 * @Description com.nowcoder.app.community.util
 * @Function 持有用户信息，用于代替session对象
 */
@Component
public class HostHolder {
	private ThreadLocal<User> users = new ThreadLocal<>();
	public void setUser(User user){
		users.set(user);
	}
	public User getUser(){
		return users.get();
	}
	public void clear(){
		users.remove();
	}
}
