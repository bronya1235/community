package com.nowcoder.app.community.pojo;

import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * @Author: Bao
 * @Date: 2022/7/7-07-07-13:56
 * @Description com.nowcoder.app.community.pojo
 * @Function 讨论贴数据存储
 */
public class DiscussPost {
	private  int id;
	private  int userId;
	private String title;
	private String content;
	private int type;
	private int status;
	private Date createTime;
	private int commentCount;
	private double score;

	public DiscussPost() {
	}

	public DiscussPost(int userId, String title, String content, int type, int status, Date createTime, int commentCount, double score) {
		this.userId = userId;
		this.title = title;
		this.content = content;
		this.type = type;
		this.status = status;
		this.createTime = createTime;
		this.commentCount = commentCount;
		this.score = score;
	}

	@Override
	public String toString() {
		return "DiscussPost{" +
				"id=" + id +
				", userId=" + userId +
				", title='" + title + '\'' +
				", content='" + content + '\'' +
				", type=" + type +
				", status=" + status +
				", createTime=" + createTime +
				", commentCount=" + commentCount +
				", score=" + score +
				'}';
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public int getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}
}
