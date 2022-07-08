package com.nowcoder.app.community.service;

import com.nowcoder.app.community.mapper.UserMapper;
import com.nowcoder.app.community.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-16:36
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class UserService {
	@Autowired
	private UserMapper userMapper;
	public User findUserById(int userId){
		return userMapper.selectById(userId);
	}
}
