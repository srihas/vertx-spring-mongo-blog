package com.cisco.cmad.projects.blog.verticle;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dao.UserDAO;
import com.cisco.cmad.projects.blog.dto.User;
import com.cisco.cmad.projects.blog.service.LoginService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

@Service
@Qualifier("userDatabaseVerticle")
public class UserDatabaseVerticle extends AbstractVerticle {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private Validator validator;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private UserDAO userDao;
	
	@Override
	public void start() throws Exception {
		
		vertx.eventBus().consumer("user.register", message -> {
			logger.info("Received register request : "+message.body().toString());
			User item = Json.decodeValue(message.body().toString(), User.class);
			logger.info("json is "+item);
			
			Set<ConstraintViolation<User>> constraintViolations = validator.validate(item);
			Set<String> messages = new HashSet<>(constraintViolations.size());
		    messages.addAll(constraintViolations.stream()
		            .map(constraintViolation -> String.format("%s", constraintViolation.getMessage()))
		            .collect(Collectors.toList()));
		    logger.info("messages "+messages);
		    
		    boolean isSuccess = false; 
		    // Valid registration details
		    if(constraintViolations.size()==0) {
		    	User user = Json.decodeValue(message.body().toString(), User.class);
				isSuccess = userDao.createUser(user);
		    } else {
		    	logger.info("Input is not valid");
		    }
		    
			message.reply(isSuccess);
		});
		
		vertx.eventBus().consumer("user.info", message -> {
			String userName = message.body().toString();
			logger.info("Getting user info for "+userName);
			Optional<User> userOptional = userDao.getUserById(userName);
			User user = null;
			if(userOptional.isPresent()) {
				user = userOptional.get();
			}
			logger.info("User info : "+user);
			if(user==null) {
				user = loginService.getCurrentUser();
			}
			message.reply(Json.encodePrettily(user));
		});
		
		vertx.eventBus().consumer("users.list",message -> {
			logger.info("Getting list of users");
			List<User> users = userDao.getAllUsers();
			logger.info("Users list : "+users);
			message.reply(Json.encodePrettily(users));
		});
		
	}

}
