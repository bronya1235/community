package com.nowcoder.app.community.controller;

import com.nowcoder.app.community.pojo.Comment;
import com.nowcoder.app.community.pojo.Page;
import com.nowcoder.app.community.service.CommentService;
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
public class CommentController {
	@Autowired
	private CommentService commentService;
	//用于得到当前用户的id
	@Autowired
	private HostHolder hostHolder;

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
		return "redirect:/discuss/detail/" + postId +"?curPage="+curPage;
	}
}
