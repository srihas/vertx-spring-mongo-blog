package com.cisco.cmad.projects.blog.config;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.cisco.cmad.projects.blog.dto.Blog;
import com.cisco.cmad.projects.blog.dto.User;
import com.mongodb.MongoClient;

/**
 * Simple Java Spring configuration to be used for the Spring example
 * application. This configuration is mainly composed of a database
 * configuration and initial population via the script "products.sql" of the
 * database for querying by our Spring service bean.
 *
 * The Spring service bean and repository are scanned for
 * via @EnableJpaRepositories and @ComponentScan annotations
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" })
@ComponentScan({"com.cisco.cmad.projects.blog.verticle",
				"com.cisco.cmad.projects.blog.service",
				"com.cisco.cmad.projects.blog.dao"})
public class SpringConfiguration {

	@Bean(name="validator")
	@Autowired
	public Validator getValidator() {
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		return factory.getValidator();
	}

	@Bean(name="datastore")
	@Autowired
	public Datastore getDataStore() {
		Datastore store = getMorphia().createDatastore(getMongoClient(), "blog-datastore");
		return store;
	}
	
	@Bean(name="mongo")
	@Autowired
	public MongoClient getMongoClient() {
		return new MongoClient("localhost", 27017);
	}
	
	@Bean(name="morphia")
	@Autowired
	public Morphia getMorphia() {
		return new Morphia();
	}
	
	@Bean
	public Class<User> getUserDtoClass() {
		return User.class;
	}
	
	@Bean
	public Class<Blog> getBlogClass() {
		return Blog.class;
	}
	
}
