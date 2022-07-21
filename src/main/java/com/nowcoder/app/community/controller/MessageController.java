package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.pojo.Message;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.MessageService;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Bao
 * @Date: 2022/7/19-07-19-16:30
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
@RequestMapping("/message")
public class MessageController {
	@Autowired
	private MessageService messageService;
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private UserService userService;

	//私信列表
	@GetMapping("/conversation/list")
	public String getConversation(Page page, Model model) {
		/*
		 * 分析，需要获取哪些参数，1是分页的数据，没有数据也有默认值，二是model数据即可
		 * */
		//获取当前用户
		User user = hostHolder.getUser();
		model.addAttribute("user", user);

		//处理分页
		page.setLimit(5);
		page.setPath("/message/conversation/list");
		page.setRows(messageService.findConversationCount(user.getId()));
		//获取会话信息,每一条信息都是当前会话的一条最新的消息
		List<Message> messages = messageService.findConversations(user.getId(), page.getOffset(), page.getLimit());
		List<Map<String, Object>> messageMap = new ArrayList<>();
		for (Message message : messages) {
			HashMap<String, Object> map = new HashMap<>();
			//添加当前会话的最新一条内容
			map.put("message", message);
			//当前会话总私信数目
			map.put("letterCount", messageService.findLetterCount(message.getConversationId()));
			//当前会话未读私信数目
			map.put("letterUnreadCount", messageService.findLetterUnreadCount(user.getId(), message.getConversationId()));
			//当前会话的对方用户
			int targetId = message.getFromId() == user.getId() ? message.getToId() : message.getFromId();
			map.put("target", userService.findUserById(targetId));
			messageMap.add(map);
		}
		model.addAttribute("conversations", messageMap);
		//当前用户未读私信总数目
		model.addAttribute("totalLetterUnreadCount", messageService.findLetterUnreadCount(user.getId(), null));
		return "site/letter";
	}

	@GetMapping("/conversation/detail/{conversationId}")
	public String getConversation(@PathVariable("conversationId") String conversationId, Page page, Model model) {
		/*
		 * 分析：
		 * 1、私信详情是对具体某个私信会话窗口的一个详细的补充，客户端需要传过来conversationId
		 * 2、支持分页
		 * 3、需要获取用户，用来判断对方的id；
		 * */
		//获取当前用户，仅仅用来获取target
		User user = hostHolder.getUser();
		//处理分页
		page.setLimit(5);
		page.setPath("/message/conversation/detail/" + conversationId);
		page.setRows(messageService.findLetterCount(conversationId));
		//获取私信列表
		List<Message> letters = messageService.findLetters(conversationId, page.getOffset(), page.getLimit());
		List<Map<String, Object>> lettersMap = new ArrayList<>();
		//处理信息已读
		List<Integer> unReadIds = new ArrayList<>();
		for (Message letter : letters) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("letter", letter);
			map.put("from", userService.findUserById(letter.getFromId()));
			lettersMap.add(map);
			if (letter.getToId() == user.getId() && letter.getStatus() == 0) {
				unReadIds.add(letter.getId());
			}
		}
		if (!unReadIds.isEmpty()) {
			messageService.readMessage(unReadIds);
		}
		model.addAttribute("letters", lettersMap);
		int targetId = letters.get(0).getFromId() == user.getId() ? letters.get(0).getToId() : letters.get(0).getFromId();
		model.addAttribute("target", userService.findUserById(targetId));
		return "site/letter-detail";
	}

	@PostMapping("/send")
	@ResponseBody
	public String sendMessage(String toUsername, String content) {
		/*
		 * 分析，页面传过来的是目标用户名和私信内容,需要对message对象进行补充
		 * 数据一json形式发送给服务器页面，提示他发送成功
		 * 发送私信以后，客户端应该进行页面跳转，跳转至私信会话详情页，因此需要返回一个访问url地址
		 * */
		User toUser = userService.findUserByUsername(toUsername);
		if (toUser == null) {
			return Community_tool.getJsonString(1, "目标用户不存在");
		}
		User user = hostHolder.getUser();
		Message message = new Message();
		message.setContent(content);
		message.setFromId(user.getId());
		message.setToId(toUser.getId());
		message.setStatus(0);
		message.setCreateTime(new Date());
		//设置会话id
		String conversationId = user.getId() <= toUser.getId() ? user.getId() + "_" + toUser.getId() : toUser.getId() + "_" + user.getId();
		message.setConversationId(conversationId);
		//插入消息
		messageService.addMessage(message);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("url", "/message/conversation/detail/" + conversationId);
		return Community_tool.getJsonString(0, "发布成功", map);
	}
}
