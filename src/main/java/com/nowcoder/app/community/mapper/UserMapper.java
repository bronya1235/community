package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.pojo.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: Bao
 * @Date: 2022/7/6-07-06-21:19
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@Mapper
public interface UserMapper {
	/*
	* 用户数据库的常见操作
	* 根据id，用户名，邮箱查询用户
	* 注册用户，更新密码，更新用户名，更新头像等等
	* */
	User selectById(int id);

	User selectByUsername(String username);

	User selectByEmail(String email);

	int insertUser(User user);

	int updateStatus(int id, int status);

	int updateHeader(int id, String headerUrl);

	int updatePassword(int id, String password);
}
