package com.cisco.cmad.projects.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dao.UserDAO;
import com.cisco.cmad.projects.blog.dto.User;

@Service
public class LoginService {
	
	@Autowired
	private UserDAO userDao;
	
	private User user = new User("srihas","12345","Srihas","srihas.vit@gmail.com");
	{
		user.setLastName("Kumar");
		user.setAreaOfInterest("Technology");
		user.setPhoneNumber("9632322991");
	}

	public boolean isValid(String userName, String password) {
		return userDao.isValid(userName, password);
	}
	
	public User getCurrentUser() {
		return user;
	}
}
