package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.pojo.Comment;
import com.nowcoder.app.community.pojo.DiscussPost;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.service.CommentService;
import com.nowcoder.app.community.service.DiscussPostService;
import com.nowcoder.app.community.service.LikeService;
import com.nowcoder.app.community.service.UserService;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.Community_tool;
import com.nowcoder.app.community.util.HostHolder;
import com.nowcoder.app.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @Author: Bao
 * @Date: 2022/7/18-07-18-10:26
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPortController implements CommunityConstant {
	@Autowired
	private DiscussPostService discussPostService;
	@Autowired
	private UserService userService;

	@Autowired
	private HostHolder hostHolder;

	@Autowired
	private CommentService commentService;
	@Autowired
	private LikeService likeService;

	@PostMapping("/add")
	@ResponseBody
	public String addDiscussPost(String title,String content){
		/*
		* 发帖子的业务逻辑
		* 1、检查登录状态，因为这个页面发布帖子是一个弹出框，是客户端本地的操作，没办法在提交以前检查登录状态
		* 2、就是一系列的请求实现，主要是页面上的jquery的不太熟，前端需要简单学习
		* */
		User user = hostHolder.getUser();
		if(user==null){
			return Community_tool.getJsonString(403, "用户未登录");
		}
		DiscussPost post = new DiscussPost();
		post.setUserId(user.getId());
		post.setTitle(title);
		post.setContent(content);
		post.setCreateTime(new Date());
		discussPostService.addDiscussPost(post);
		return Community_tool.getJsonString(0, "发布成功");
	}
	@GetMapping("/detail/{discussPostId}")
	public String findDiscussPostById(@PathVariable("discussPostId") int id,
	                                  Model model,
	                                  Page page){
		//帖子信息
		DiscussPost post = discussPostService.findDiscussPostById(id);
		model.addAttribute("post", post);
		//发帖人信息
		User user = userService.findUserById(post.getUserId());
		model.addAttribute("user", user);
		//处理评论，页面会反馈的是当前页面，和每页限制数目，需要补充设置一些参数
		//点赞
		model.addAttribute("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_POST, post.getId()));
		model.addAttribute("likeStatus", hostHolder.getUser()==null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, post.getId()));
		page.setLimit(5);
		page.setPath("/discuss/detail/"+id);
		page.setRows(post.getCommentCount());
		//查询到帖子的评论
		List<Comment> comments = commentService.findCommentsByEntity(
				ENTITY_TYPE_POST, id, page.getOffset(), page.getLimit());
		//评论显示对象的的列表
		List<Map<String,Object>> commentViewList = new ArrayList<>();
		if(comments!=null){
			for (Comment comment : comments) {
				//评论view Object
				HashMap<String, Object> mapVO = new HashMap<>();
				//评论的内容
				mapVO.put("comment", comment);
				//评论的作者
				mapVO.put("user", userService.findUserById(comment.getUserId()));
				//某评论的赞数量
				mapVO.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, comment.getId()));
				//某评论是否已赞
				mapVO.put("likeStatus", hostHolder.getUser()==null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, comment.getId()));
				/*注意：前一部分处理的是对帖子的评论，还需要处理对这些评论的回复，可以分成两类
				* 1、单纯回复上述"comment",是没有具体的target目标的
				* 2、在评论底下对某个人的回复进行回复，属于是两个人的之间的私聊，他是针对某一条回复的
				* */
				//每条评论底下的回复
				List<Comment> replyList = commentService.findCommentsByEntity(
						ENTITY_TYPE_COMMENT, comment.getId(), 0, Integer.MAX_VALUE);
				List<Map<String,Object>> replyViewList = new ArrayList<>();
				if(replyList!=null){
					for (Comment reply : replyList) {
						HashMap<String, Object> replyVO = new HashMap<>();
						//回复的内容
						replyVO.put("reply", reply);
						//回复的作者的用户信息
						replyVO.put("user", userService.findUserById(reply.getUserId()));
						//回复的目标，如果没有就是回复评论，如果有就是回复具体某个人等同于评论底下的私聊
						User target = reply.getTargetId()==0?null:userService.findUserById(reply.getTargetId());
						replyVO.put("target", target);
						//某评论的赞数量
						replyVO.put("likeCount", likeService.findEntityLikeCount(ENTITY_TYPE_COMMENT, reply.getId()));
						//某评论是否已赞
						replyVO.put("likeStatus", hostHolder.getUser()==null?0:likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_COMMENT, reply.getId()));
						replyViewList.add(replyVO);
					}
				}
				mapVO.put("replyViewList", replyViewList);
				//这里再查询一下，每一条评论，有多少个回复
				int replyCount = commentService.findCommentsCountByEntity(ENTITY_TYPE_COMMENT, comment.getId());
				mapVO.put("replyCount",replyCount);
				commentViewList.add(mapVO);
			}
		}
		model.addAttribute("comments", commentViewList);
		return "site/discuss-detail";
	}
}
