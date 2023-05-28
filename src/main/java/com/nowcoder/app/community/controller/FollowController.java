package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.event.EventProducer;
import com.nowcoder.app.community.pojo.Event;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.FollowService;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-19:12
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
public class FollowController implements CommunityConstant{
	@Autowired
	private FollowService followService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;
	@Autowired
	private EventProducer eventProducer;
	@PostMapping("/follow")
	@ResponseBody
	public String follow(int entityType,int entityId){
		User user = hostHolder.getUser();
		followService.addFollow(user.getId(), entityType, entityId);
		//触发关注日志
		Event event = new Event().setTopic(TOPIC_FOLLOW)
				.setUserId(user.getId())
				.setEntityType(entityType)
				.setEntityId(entityId)
				.setEntityUserId(entityId);
		eventProducer.fireEvent(event);
		return Community_tool.getJsonString(0, "关注成功");
	}

	@PostMapping("/unfollow")
	@ResponseBody
	public String unfollow(int entityType,int entityId){
		User user = hostHolder.getUser();
		followService.unFollow(user.getId(), entityType, entityId);
		return Community_tool.getJsonString(0, "取消成功");
	}

	@GetMapping("/followees/{userId}")
	public String followees(@PathVariable("userId") int userId,
	                        Page page,
	                        Model model){
		User targetUser= userService.findUserById(userId);
		if(targetUser==null){
			throw new RuntimeException("该用户不存在");
		}
		model.addAttribute("targetUser", targetUser);
		page.setLimit(5);
		page.setRows((int)followService.findFolloweeCount(userId, ENTITY_TYPE_USER));
		page.setPath("/followees/"+userId);
		List<Map<String, Object>> userList = followService.findFollowee(userId, page.getOffset(), page.getLimit());
		//还需粉丝与当前用户的关注状态
		if(userList!=null){
			for (Map<String, Object> map : userList) {
				User followUser = (User) map.get("user");
				map.put("hasFollowed",hasFollowed(followUser.getId()));
			}
		}
		model.addAttribute("users", userList);
		return "site/followee";
	}


	@GetMapping("/followers/{userId}")
	public String followers(@PathVariable("userId") int userId,
	                        Page page,
	                        Model model){
		User targetUser= userService.findUserById(userId);
		if(targetUser==null){
			throw new RuntimeException("该用户不存在");
		}
		model.addAttribute("targetUser", targetUser);
		page.setLimit(5);
		page.setRows((int)followService.findFollowerCount(userId, ENTITY_TYPE_USER));
		page.setPath("/followers/"+userId);
		List<Map<String, Object>> userList = followService.findFollower(userId, page.getOffset(), page.getLimit());
		//还需粉丝与当前用户的关注状态
		if(userList!=null){
			for (Map<String, Object> map : userList) {
				User followUser = (User) map.get("user");
				map.put("hasFollowed",hasFollowed(followUser.getId()));
			}
		}
		model.addAttribute("users", userList);
		return "site/follower";
	}

	private boolean hasFollowed(int userid){
		User user = hostHolder.getUser();
		if(user==null){
			return false;
		}
		return followService.findFolloweeStatus(user.getId(), ENTITY_TYPE_USER, userid);
	}
}
