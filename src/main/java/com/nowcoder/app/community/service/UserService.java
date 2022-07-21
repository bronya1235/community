package com.nowcoder.app.community.service;

import com.nowcoder.app.community.mapper.LoginTicketMapper;
import com.nowcoder.app.community.mapper.UserMapper;
import com.nowcoder.app.community.pojo.LoginTicket;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.MailClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-16:36
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class UserService implements CommunityConstant {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private LoginTicketMapper loginTicketMapper;
	@Autowired
	private MailClient mailClient;
	@Autowired
	private TemplateEngine templateEngine;
	@Value("${community.path.domain}")
	private String domain;
	@Value("${server.servlet.context-path}")
	private String contextPath;

	//通过id查询用户
	public User findUserById(int userId) {
		return userMapper.selectById(userId);
	}
	//通过用户名查询用户
	public User findUserByUsername(String username){
		return userMapper.selectByUsername(username);
	}

	//注册业务层
	public Map<String, Object> register(User user) {
		Map<String, Object> map = new HashMap<>();
		//参数检查
		if (user == null) {
			//参数异常，报错
			throw new IllegalArgumentException("参数为空");
		}
		//这部分内容使用动态的检查比较好，这里先这么写一下
		//用户名检查，是否为空，是否已经存在
		if (StringUtils.isBlank(user.getUsername())) {
			map.put("usernameMsg", "账号不能为空");
			return map;
		}
		if (userMapper.selectByUsername(user.getUsername()) != null) {
			map.put("usernameMsg", "用户名已存在");
			return map;
		}
		//密码、邮箱检查，是否为空,邮箱是否已经被使用
		if (StringUtils.isBlank(user.getPassword())) {
			map.put("passwordMsg", "密码不能为空");
			return map;
		}
		if (StringUtils.isBlank(user.getEmail())) {
			map.put("emailMsg", "邮箱不能为空");
			return map;
		}
		if (userMapper.selectByEmail(user.getEmail()) != null) {
			map.put("emailMsg", "邮箱已经被使用");
			return map;
		}
		//检查通过，开始注册，一一设置user内部的详细信息
		//密码处理，生成salt，拼接到原password上，使用md5加密，覆盖密码
		String salt = Community_tool.getUUID().substring(0, 5);
		user.setSalt(salt);
		user.setPassword(Community_tool.md5(user.getPassword() + salt));
		user.setType(0);
		user.setStatus(0);
		user.setActivationCode(Community_tool.getUUID());
		user.setHeaderUrl(String.format("http://images.nowcoder.com/head/%dt.png",
				new Random().nextInt(1000)));
		user.setCreateTime(new Date());
		userMapper.insertUser(user);


		//发送激活邮件
		Context context = new Context();
		context.setVariable("email", user.getEmail());
		//http://localhost:8080/community/activation/用户id/激活码
		String url = domain + contextPath + "/activation/" + user.getId() + "/" + user.getActivationCode();
		context.setVariable("url", url);
		String content = templateEngine.process("/mail/activation", context);
		mailClient.sendMail(user.getEmail(), "注册激活邮件", content);

		return map;
	}
	//激活业务层
	public int activation(int userid, String code) {
		User user = userMapper.selectById(userid);
		if (user.getStatus() == 1) {
			return ACTIVATION_REPEAT;
		} else if (user.getActivationCode().equals(code)) {
			userMapper.updateStatus(userid, 1);
			return ACTIVATION_SUCCESS;
		} else {
			return ACTIVATION_FAILURE;
		}
	}

	//登录业务层
	public Map<String, Object> login(String username, String password, int expiredSeconds) {
		HashMap<String, Object> map = new HashMap<>();
		//检查用户信息
		//验证账号是否存在，是否激活
		User user = userMapper.selectByUsername(username);
		if (user == null) {
			map.put("usernameMsg", "该账号不存在");
			return map;
		}
		//验证账号是否激活，或者被拉黑
		if (user.getStatus() == 0) {
			map.put("usernameMsg", "该账号还未激活");
			return map;
		}
		//验证密码是否正确
		password = Community_tool.md5(password + user.getSalt());
		if (!user.getPassword().equals(password)) {
			map.put("passwordMsg", "密码不正确");
			return map;
		}
		//执行登录逻辑
		//1、生成登录凭证
		LoginTicket loginTicket = new LoginTicket();
		loginTicket.setUserId(user.getId());
		loginTicket.setTicket(Community_tool.getUUID());
		loginTicket.setStatus(0);
		loginTicket.setExpired(new Date(System.currentTimeMillis() + expiredSeconds * 1000L));
		loginTicketMapper.insertLoginTicket(loginTicket);
		map.put("ticket", loginTicket.getTicket());
		return map;
	}
	//退出登录业务层
	public void logout(String ticket){
		loginTicketMapper.updateLoginTicket(ticket, 1);
	}
	//查询登录状态
	public LoginTicket findLoginTicket(String ticket){
		return loginTicketMapper.selectByTicket(ticket);
	}
	//更新修改头像的路径
	public int updateHeader(int userid,String headerUrl){
		return userMapper.updateHeader(userid, headerUrl);
	}
}
