package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.event.EventProducer;
import com.nowcoder.app.community.pojo.Comment;
import com.nowcoder.app.community.pojo.DiscussPost;
import com.nowcoder.app.community.pojo.Event;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.service.CommentService;
import com.nowcoder.app.community.service.DiscussPostService;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

/**
 * @Author: Bao
 * @Date: 2022/7/19-07-19-10:06
 * @Description com.nowcoder.app.community.controller
 * @Function
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {
	@Autowired
	private CommentService commentService;
	//用于得到当前用户的id
	@Autowired
	private HostHolder hostHolder;
	@Autowired
	private EventProducer eventProducer;
	@Autowired
	private DiscussPostService discussPostService;

	/*
	 * 分析，当添加完评论以后，应该重新返回到帖子的详情页，需要传过来一个帖子的id
	 * */
	@PostMapping("/add/{discussPostId}/{curPage}")
	public String addComment(@PathVariable("discussPostId") int postId,
	                         Comment comment,
	                         @PathVariable("curPage") int curPage ) {
		comment.setUserId(hostHolder.getUser().getId());
		comment.setStatus(0);
		comment.setCreateTime(new Date());
		commentService.addComment(comment);
		//触发评论事件
		//为什么需要设置帖子的id，因为有时候如果是评论之间互相进行评论，这时候实体是评论
		//但是如果你需要看详情，需要跳转到帖子的具体位置，甚至还要跳转到评论的具体位置
		//这就需要统计帖子的id，和分页的信息
		Event event = new Event().setTopic(TOPIC_COMMENT)
				.setUserId(comment.getUserId())
				.setEntityType(comment.getEntityType())
				.setEntityId(comment.getEntityId())
				.setData("postId", postId);
		//需要判断目标用户，决定通知发送给谁
		if(comment.getEntityType()==ENTITY_TYPE_POST){
			DiscussPost discussPostById = discussPostService.findDiscussPostById(postId);
			event.setEntityUserId(discussPostById.getUserId());
		}else if(comment.getEntityType()==ENTITY_TYPE_COMMENT){
			Comment commentById = commentService.findCommentById(comment.getEntityId());
			event.setEntityUserId(commentById.getUserId());
		}
		eventProducer.fireEvent(event);
		return "redirect:/discuss/detail/" + postId +"?curPage="+curPage;
	}
}
