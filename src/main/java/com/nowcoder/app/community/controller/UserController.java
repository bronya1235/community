package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.annotation.LoginRequired;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @Author: Bao
 * @Date: 2022/7/14-07-14-15:44
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
@RequestMapping("/user")
public class UserController {
	//日志
	private Logger logger = LoggerFactory.getLogger(UserController.class);
	//上传路径
	@Value("${community.path.upload}")
	private String uploadPath;
	//域名
	@Value("${community.path.domain}")
	private String domain;
	//
	@Value("${server.servlet.context-path}")
	private String contextPath;

	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;

	@LoginRequired
	@GetMapping("/setting")
	public String getUserSettingPage() {
		return "site/setting";
	}
	@LoginRequired
	@PostMapping("/upload")
	public String uploadHeader(MultipartFile image, Model model) {
		//检查是否存在图片
		if (image == null) {
			model.addAttribute("imageMsg", "您还没有选择图片！");
			return "site/setting";
		}
		//为文件提供一个名字
		String fileName = image.getOriginalFilename();
		String suffix = fileName.substring(fileName.lastIndexOf("."));
		if (StringUtils.isBlank(suffix)) {
			model.addAttribute("imageMsg", "图片格式不正确！");
			return "site/setting";
		}
		fileName = Community_tool.getUUID() + suffix;
		//确定文件存放的目录
		File destiny = new File(uploadPath + "/" + fileName);
		try {
			image.transferTo(destiny);
		} catch (IOException e) {
			logger.error("上传文件失败" + e.getMessage());
			throw new RuntimeException("上传文件失败，服务器发生异常！", e);
		}
		//更新当前用户的头像的路径(web访问路径)
		//这个路径是自定义的http://localhost:8080/community/user/header/xxx.png
		User user = hostHolder.getUser();
		String headerUrl = domain + contextPath + "/user/header/" + fileName;
		userService.updateHeader(user.getId(), headerUrl);
		return "redirect:/index";
	}

	@GetMapping("/header/{fileName}")
	public void getHeader(@PathVariable("fileName") String fileName, HttpServletResponse response) {
		String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
		fileName = uploadPath + "/" + fileName;
		response.setContentType("image/" + suffix);
		try (ServletOutputStream outputStream = response.getOutputStream();
		     FileInputStream fis = new FileInputStream(fileName);) {
			byte[] buffer = new byte[1024];
			int b = 0;
			while ((b=fis.read(buffer))!=-1){
				outputStream.write(buffer,0,b);
			}
		} catch (IOException e) {
			logger.error("读取头像失败：" + e.getMessage());
		}
	}
}
