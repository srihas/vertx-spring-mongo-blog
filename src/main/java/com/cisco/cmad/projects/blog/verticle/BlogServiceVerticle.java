package com.cisco.cmad.projects.blog.verticle;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dao.BlogDAO;
import com.cisco.cmad.projects.blog.dto.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

@Service
@Qualifier("blogServiceVerticle")
public class BlogServiceVerticle extends AbstractVerticle {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BlogDAO blogDao;
	
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("blog.create", message -> {
			logger.info("Received request to create blog: "+message.body().toString());
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			
			blogDao.createBlog(blog);
			
			message.reply(true);
		});
		
		vertx.eventBus().consumer("blog.list", message -> {
			logger.info("Getting all blogs");
			List<Blog> blogs = blogDao.getAllBlogs();
			logger.info("Blogs : "+blogs);
			message.reply(Json.encodePrettily(blogs));
		});
		
		vertx.eventBus().consumer("blog.search", message -> {
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			String userId = blog.getUserName();
			String title = blog.getTitle();
			logger.info("Searching blogs");
			List<Blog> blogs = blogDao.getBlogsByTitle(title, userId);
			logger.info("Blogs : "+blogs);
			blogs.add(new Blog("Hello "+title, "Nothing", userId));
			message.reply(Json.encodePrettily(blogs));
		});
		
		vertx.eventBus().consumer("user.blog.list", message -> {
			String userId = message.body().toString();
			logger.info("Getting all blogs created by user");
			List<Blog> blogs = blogDao.getBlogsByUserId(userId);
			logger.info("Blogs : "+blogs);
			message.reply(Json.encodePrettily(blogs));
		});
		
		vertx.eventBus().consumer("blog.list.fav", message -> {
			String userId = message.body().toString();
			logger.info("Getting all blogs created by user : "+userId);
			List<Blog> blogs = blogDao.getFavouriteBlogs(userId);
			logger.info("Blogs : "+blogs);
			blogs.add(new Blog("Hello", "Nothing", userId));
			message.reply(Json.encodePrettily(blogs));
		});
	}

}
