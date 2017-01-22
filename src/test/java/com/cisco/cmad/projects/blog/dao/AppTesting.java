package com.cisco.cmad.projects.blog.dao;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cisco.cmad.projects.blog.config.SpringConfiguration;
import com.cisco.cmad.projects.blog.dto.Blog;
import com.cisco.cmad.projects.blog.dto.User;

import io.vertx.core.json.Json;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringConfiguration.class)
public class AppTesting {
	
	@Autowired
	private UserDAO userDao;
	
	@Autowired
	private BlogDAO blogDao;
	
	@Test
	public void getAllUsers() {
		List<User> allUsers = userDao.getAllUsers();
		System.out.println(Json.encodePrettily(allUsers));
	}
	
	@Test
	public void getAllBlogs() {
		List<Blog> allBlogs = blogDao.getAllBlogs();
		System.out.println(Json.encodePrettily(allBlogs));
	}

}
