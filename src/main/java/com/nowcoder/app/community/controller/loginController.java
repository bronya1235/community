package com.nowcoder.app.community.controller;

import com.google.code.kaptcha.Producer;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.CommunityConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.PublicKey;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/12-07-12-12:36
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
public class loginController implements CommunityConstant {
	@Autowired
	private UserService userService;
	@Autowired
	private Producer producer;
	@Autowired
	private static final Logger logger = LoggerFactory.getLogger(loginController.class);
	@Value("server.servlet.context-path")
	private String contextPath;
	@GetMapping("/register")
	public String getRegisterPage(){
		return "site/register";
	}
	@GetMapping("/login")
	public String getloginPage(){
		return "site/login";
	}
	@PostMapping("/register")
	public String register(Model model, User user){
		Map<String, Object> map = userService.register(user);
		if(map==null||map.isEmpty()){
			model.addAttribute("Msg", "注册成功，我们已经向您的邮箱发送了一封激活邮件，请尽快激活");
			model.addAttribute("target", "/index");
			return "site/operate-result";
		}else {
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			model.addAttribute("emailMsg", map.get("emailMsg"));
			return "site/register";
		}

	}

	@GetMapping("/activation/{userid}/{code}")
	public String activation(@PathVariable("userid") int userid,
	                         @PathVariable("code") String code,
	                         Model model){
		int activation = userService.activation(userid, code);
		if(activation==ACTIVATION_SUCCESS){
			model.addAttribute("Msg", "激活成功，您的账号已经可以正常使用了");
			model.addAttribute("target", "/login");
		}else if(activation==ACTIVATION_REPEAT){
			model.addAttribute("Msg", "激活无效，您的账号已经激活，请不要重复激活");
			model.addAttribute("target", "/index");
		}else {
			model.addAttribute("Msg", "激活失败，您提供的激活码不正确");
			model.addAttribute("target", "/index");
		}
		return "site/operate-result";
	}

	@GetMapping("/kaptcha")
	public void getKaptcha(HttpServletResponse response, HttpSession session){
		//生成文字
		String text = producer.createText();
		//生成图片
		BufferedImage image = producer.createImage(text);
		//保存session
		session.setAttribute("kaptcha", text);
		//设置响应类型
		response.setContentType("image/png");

		try(ServletOutputStream outputStream = response.getOutputStream()) {
			//输出数据
			ImageIO.write(image,"png",outputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@PostMapping("/login")
	public String login(Model model, String username,
	                    String password,String code,
	                    boolean remember,
	                    HttpSession session,
                        HttpServletResponse response
	                    ){
	//	表现层逻辑
	//  判断验证码
		String Kaptcha = (String) session.getAttribute("kaptcha");
		if(StringUtils.isBlank(Kaptcha)||StringUtils.isBlank(code)||(!Kaptcha.equalsIgnoreCase(code))){
			model.addAttribute("codeMsg", "验证码不正确");
			return "/site/login";
		}
	//  验证通过，开始登录
		int expiredSeconds = remember?REMEMBER_EXPIRED_SECONDS:DEFAULT_EXPIRED_SECONDS;
		Map<String, Object> map = userService.login(username, password, expiredSeconds);
		if(map.containsKey("ticket")){
			Cookie cookie = new Cookie("ticket", map.get("ticket").toString());
			cookie.setPath(contextPath);
			cookie.setMaxAge(expiredSeconds);
			response.addCookie(cookie);
			return "redirect:/index";
		}else {
			model.addAttribute("usernameMsg", map.get("usernameMsg"));
			model.addAttribute("passwordMsg", map.get("passwordMsg"));
			return "/site/login";
		}


	}
	@GetMapping("/logout")
	public String logout(@CookieValue("ticket") String ticket){
		userService.logout(ticket);
		return "redirect:/login";
	}

}
