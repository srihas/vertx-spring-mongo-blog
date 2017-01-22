package com.cisco.cmad.projects.blog.dao;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cisco.cmad.projects.blog.config.SpringConfigurationTest;
import com.cisco.cmad.projects.blog.dto.User;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringConfigurationTest.class)
public class UserDAOImplTest {
	
	@Autowired
	private UserDAO userDao;
	
	@Test
	public void createUserTest() {
		final String userName = UUID.randomUUID().toString();
		User user = new User(userName,"","Srihas","srihas.vit@gmail.com");
		Assert.assertTrue(userDao.createUser(user));
		
		user = new User(userName,"","sdfsdf","srihaskumar@gmail.com");
		Assert.assertFalse(userDao.createUser(user));
		
		userDao.deleteUser(userName);
	}
	
	@Test
	public void getUserByIdTest() {
		final String userName = UUID.randomUUID().toString();
		User user = new User(userName,"","Srihas","srihas.vit@gmail.com");
		Assert.assertTrue(userDao.createUser(user));
		User user2 = userDao.getUserById(user.getUserName()).get();
		Assert.assertEquals(user.getUserName(), user2.getUserName());
		Assert.assertEquals(user.getFirstName(), user2.getFirstName());
		userDao.deleteUser(userName);
	}
	
	@Test
	public void isUserIdAvailableTest() {
		final String userName = UUID.randomUUID().toString();
		Assert.assertTrue(userDao.isUserIdAvailable(userName));
		User user = new User(userName,"","Srihas","srihas.vit@gmail.com");
		Assert.assertTrue(userDao.createUser(user));
		Assert.assertFalse(userDao.isUserIdAvailable(userName));
	}
	
}
