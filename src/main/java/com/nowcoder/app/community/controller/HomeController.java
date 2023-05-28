package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.pojo.DiscussPost;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.DiscussPostService;
import com.nowcoder.app.community.service.LikeService;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-16:41
 * @Description com.nowcoder.app.community.controller
 * @Function 首页的表现层控制器
 */
@Controller
public class HomeController implements CommunityConstant {
	@Autowired
	private DiscussPostService discussPostService;
	@Autowired
	private UserService userService;
	@Autowired
	private LikeService likeService;
	@GetMapping("/index")
	public String getIndexPage(Model model, Page page){
		//方法调用前，会实例化model和page，并会自动把page封装到model中
		page.setRows(discussPostService.findDiscussPostRows(0));
		page.setPath("/index");
		List<DiscussPost> list = discussPostService.findDiscussPosts(0, page.getOffset(), page.getLimit());
		List<Map<String,Object>> discussPosts =new ArrayList<>();
		for(DiscussPost post:list){
			Map<String, Object> map = new HashMap<>();
			map.put("post", post);
			User user = userService.findUserById(post.getUserId());
			map.put("user", user);
			map.put("likeCount",likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
			discussPosts.add(map);
		}
		model.addAttribute("discussPosts", discussPosts);
		return "index";
	}
	@GetMapping("/error")
	public String getErrorPage(){
		return "error/500";
	}
}
