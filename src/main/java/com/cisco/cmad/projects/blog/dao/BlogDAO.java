package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.Optional;

import com.cisco.cmad.projects.blog.dto.Blog;

public interface BlogDAO {

	List<Blog> getAllBlogs();

	List<Blog> getBlogsByUserId(String userId);

	List<Blog> getFavouriteBlogs(String userId);
	
	void setFavourite(String blogId, boolean favourite);
	
	Optional<Blog> getBlogById(String id);

	boolean deleteAllBlogs();

	boolean deleteBlogById(String id);

	boolean createBlog(Blog blog);

	List<Blog> getBlogsByTitle(String title, String userId);
	
}
