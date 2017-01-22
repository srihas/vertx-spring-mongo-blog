package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dto.Blog;
import com.mongodb.WriteResult;

@Service
public class BlogDAOImpl extends BasicDAO<Blog, Integer> implements BlogDAO {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	public BlogDAOImpl(Class<Blog> entityClass, Datastore ds) {
		super(entityClass, ds);
	}

	@Override
	public List<Blog> getAllBlogs() {
		return createQuery().asList();
	}

	@Override
	public List<Blog> getBlogsByUserId(String userId) {
		return createQuery().field("userName").equal(userId).asList();
	}
	
	@Override
	public List<Blog> getFavouriteBlogs(String userId) {
		return createQuery().field("userName").equal(userId).field("favorite").equal(Boolean.TRUE).asList();
	}

	@Override
	public void setFavourite(String blogId, boolean favourite) {
		Query<Blog> query = createQuery().field("_id").equal(blogId);
		logger.info("Found blog with id ["+blogId+"] as "+query.get());
		UpdateOperations<Blog> ops = createUpdateOperations().set("favorite", true);
		update(query, ops);
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Blog> getBlogsByTitle(String title, String userId) {
		List<Blog> blogList = createQuery().field("title").contains(title).field("userName").equal(userId).retrievedFields(true, "title").asList();
		//List<Blog> blogTitles = blogList.stream().map(b -> b.getTitle()).collect(Collectors.toList());
		return blogList;
	}

	@Override
	public boolean deleteAllBlogs() {
		WriteResult result = deleteByQuery(createQuery());
		return result.wasAcknowledged();
	}
	
	@Override
	public boolean deleteBlogById(String id) {
		WriteResult result = deleteByQuery(createQuery().field("_id").equal(id));
		return result.wasAcknowledged();
	}

	@Override
	public boolean createBlog(Blog blog) {
		Key<Blog> save = save(blog);
		if(null!=save) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Optional<Blog> getBlogById(String id) {
		Blog blog = createQuery().field("_id").equal(id).get();
		return blog!=null?Optional.of(blog):Optional.empty();
	}

}
