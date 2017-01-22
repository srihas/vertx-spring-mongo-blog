package com.cisco.cmad.projects.blog.dto;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;

@Entity
@Indexes(
	    @Index(value = "title", fields = @Field("title"))
	)
public class Blog {
	
	@Id
	private String _id;
	private String userName;
	private String title;
	private String content;
	private Date createdDate;
	private List<String> tags;
	private boolean favorite;
	List<Comment> comments;
	
	public Blog() {
		this.createdDate = new Date();
		_id = new ObjectId().toString();
	}
	
	public Blog(String title, String content, String userName) {
		_id = new ObjectId().toString();
		this.title = title;
		this.content = content;
		this.userName = userName;
		this.createdDate = new Date();
	}
	
	
	public String getId() {
		return _id;
	}
	public void setId(String id) {
		this._id = id;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	public boolean isFavorite() {
		return favorite;
	}
	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	@Override
	public String toString() {
		return "Blog [_id=" + _id + ", userName=" + userName + ", title=" + title + ", content=" + content
				+ ", createdDate=" + createdDate + ", tags=" + tags + ", favorite=" + favorite + ", comments="
				+ comments + "]";
	}
	
}
