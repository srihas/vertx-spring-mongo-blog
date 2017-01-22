package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.Optional;

import com.cisco.cmad.projects.blog.dto.User;

public interface UserDAO {
	 
	Optional<User> getUserById(String userId);

	List<User> getAllUsers();

	List<String> getAllRegisteredUserNames();

	boolean isUserIdAvailable(String userId);

	boolean createUser(User user);

	boolean deleteUser(String userId);

	boolean deleteAllUsers();

	long getUserCount();
	
	boolean isValid(String userName, String password);

}
