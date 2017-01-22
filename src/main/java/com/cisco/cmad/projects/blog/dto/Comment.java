package com.cisco.cmad.projects.blog.dto;

import java.util.Date;

public class Comment {

	String commentId;
	String userFullName;
	String userName;
	String title;
	Date createdDate;
	String content;
	
	public String getCommentId() {
		return commentId;
	}
	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Override
	public String toString() {
		return "Comment [commentId=" + commentId + ", userFullName=" + userFullName + ", userName=" + userName
				+ ", title=" + title + ", createdDate=" + createdDate + ", content=" + content + "]";
	}
	

}
