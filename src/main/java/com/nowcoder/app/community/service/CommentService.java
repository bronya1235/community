package com.nowcoder.app.community.service;

import com.nowcoder.app.community.filters.SensitiveFilter;
import com.nowcoder.app.community.mapper.CommentMapper;
import com.nowcoder.app.community.pojo.Comment;
import com.nowcoder.app.community.pojo.DiscussPost;
import com.nowcoder.app.community.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * @Author: Bao
 * @Date: 2022/7/18-07-18-18:33
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class CommentService implements CommunityConstant {
	@Autowired
	private CommentMapper commentMapper;
	@Autowired
	private DiscussPostService discussPostService;
	@Autowired
	private SensitiveFilter sensitiveFilter;

	public List<Comment> findCommentsByEntity(int entityType, int entityId, int offset, int limit){
		return commentMapper.selectCommentsByEntity(entityType, entityId, offset, limit);
	}

	public int findCommentsCountByEntity(int entityType, int entityId){
		return commentMapper.selectCommentsCountByEntity(entityType, entityId);
	}
	//数据库事务处理，因为涉及到两个数据库的参数修改，当其中一个出错时，需要把另一个进行回滚
	@Transactional(isolation = Isolation.READ_UNCOMMITTED,propagation = Propagation.REQUIRED)
	public int addComment(Comment comment){
		/*需要进行敏感词的过滤
		* */
		if(comment==null){
			throw new IllegalArgumentException("参数不能为空");
		}
		//去标签
		comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
		//去敏感词
		comment.setContent(sensitiveFilter.filter(comment.getContent()));
		//插入数据
		int rows = commentMapper.insertComment(comment);
		//更新帖子的数量
		if(comment.getEntityType()==ENTITY_TYPE_POST){
			int i = commentMapper.selectCommentsCountByEntity(ENTITY_TYPE_POST, comment.getEntityId());
			discussPostService.updateCommentCount(comment.getEntityId(), i);
		}
		return rows;
	}
}
