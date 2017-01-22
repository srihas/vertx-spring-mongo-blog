package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.Optional;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dto.User;
import com.mongodb.WriteResult;

@Service
public class UserDAOImpl extends BasicDAO<User, Integer> implements UserDAO {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public UserDAOImpl(Class<User> entityClass, Datastore ds) {
		super(entityClass, ds);
	}
	
	@Override
	public Key<User> save(User entity) {
		Key<User> save = super.save(entity);
		return null;
	};
	
	@Override
	public boolean createUser(User user) {
		boolean isSuccess = false;
		if(isUserIdAvailable(user.getUserName())) {
			save(user);
			isSuccess = true;
		} else {
			logger.info("User exists with name : "+user.getUserName());
		}
		return isSuccess;
	}
	
	@Override
	public boolean deleteUser(String userId) {
		WriteResult result = deleteByQuery(createQuery().field("userName").equal(userId));
		return result.wasAcknowledged();
	}
	
	@Override
	public boolean deleteAllUsers() {
		WriteResult result = deleteByQuery(createQuery());
		return result.wasAcknowledged();
	}
	
	@Override
	public Optional<User> getUserById(String userId) {
		User user = createQuery().field("userName").equal(userId).get();
		return user!=null?Optional.of(user):Optional.empty();
	}

	@Override
	public List<User> getAllUsers() {
		return createQuery().asList();
	}
	
	@Override
	public List<String> getAllRegisteredUserNames() {
		return getCollection().distinct("userName");
	}
	
	@Override
	public boolean isUserIdAvailable(String userId) {
		if(createQuery().field("userName").equal(userId).count()!=0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public long getUserCount() {
		return count();
	}

	@Override
	public boolean isValid(String userName, String password) {
		return createQuery().field("userName").equal(userName).field("password").equal(password).count()==1;
	}

}
