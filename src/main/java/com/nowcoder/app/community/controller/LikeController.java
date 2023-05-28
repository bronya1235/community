package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.event.EventProducer;
import com.nowcoder.app.community.pojo.Event;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.LikeService;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import com.nowcoder.app.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-14:14
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
@RequestMapping("/like")
public class LikeController implements CommunityConstant {
	@Autowired
	private LikeService likeService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private EventProducer eventProducer;

	@PostMapping("/add")
	@ResponseBody
	public String addLike(int entityType,int entityId,int entityUserId,int postId){
		/*异步请求，返回一个json对象，需要获取到entityType和entityId
		* */
		User user = hostHolder.getUser();
		likeService.addLike(user.getId(), entityType, entityId,entityUserId);
		//点赞数量
		long count = likeService.findEntityLikeCount(entityType, entityId);
		//点赞状态
		int likeStatus = likeService.findEntityLikeStatus(user.getId(), entityType, entityId);
		Map<String,Object> map = new HashMap<>();
		map.put("likeCount", count);
		map.put("likeStatus", likeStatus);
		//点赞事件
		if(likeStatus==1){
			Event event = new Event().setTopic(TOPIC_LIKE)
					.setUserId(user.getId())
					.setEntityType(entityType)
					.setEntityId(entityId)
					.setEntityUserId(entityUserId)
					.setData("postId", postId);
			eventProducer.fireEvent(event);
		}
		return Community_tool.getJsonString(0, null, map);
	}
}
