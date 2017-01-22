package com.cisco.cmad.projects.blog.dao;

import java.util.List;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cisco.cmad.projects.blog.config.SpringConfigurationTest;
import com.cisco.cmad.projects.blog.dto.Blog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes=SpringConfigurationTest.class)
public class BlogDAOImplTest {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private BlogDAO blogDao;
	
	@Test
	public void createBlogTest() {
		Blog blog = new Blog("My First blog","Some content","admin");
		Assert.assertTrue(blogDao.createBlog(blog));
		Blog blog2 = blogDao.getBlogById(blog.getId()).get();
		Assert.assertEquals(blog.getId(), blog2.getId());
		Assert.assertEquals(blog.getTitle(), blog2.getTitle());
		Assert.assertTrue(blogDao.deleteBlogById(blog.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog.getId()).isPresent());
	}
	
	@Test
	public void setFavouriteTest() {
		Blog blog = new Blog("My First blog","Some content","admin");
		blog.setFavorite(false);
		blogDao.createBlog(blog);
		blogDao.setFavourite(blog.getId(), true);
		Blog blog2 = blogDao.getBlogById(blog.getId()).get();
		Assert.assertTrue(blog2.isFavorite());
		blogDao.deleteBlogById(blog.getId());
		Assert.assertTrue(blogDao.deleteBlogById(blog.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog.getId()).isPresent());
	}
	
	@Test
	public void getFavouriteBlogsByIdTest() {
		String userId = UUID.randomUUID().toString();
		Blog blog = new Blog("My First favorite blog","Some content",userId);
		blogDao.createBlog(blog);
		blogDao.setFavourite(blog.getId(), true);
		Assert.assertEquals(1, blogDao.getFavouriteBlogs(userId).size());
		Assert.assertEquals(blog.getTitle(), blogDao.getFavouriteBlogs(userId).get(0).getTitle());
		
		Blog blog2 = new Blog("My Second favorite blog","Some content",userId);
		blogDao.createBlog(blog2);
		blogDao.setFavourite(blog2.getId(), true);
		Assert.assertEquals(2, blogDao.getFavouriteBlogs(userId).size());
		
		blogDao.deleteBlogById(blog.getId());
		Assert.assertTrue(blogDao.deleteBlogById(blog.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog.getId()).isPresent());
		
		blogDao.deleteBlogById(blog2.getId());
		Assert.assertTrue(blogDao.deleteBlogById(blog2.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog2.getId()).isPresent());
	}
	
	@Test
	public void getBlogByTitleTest() {
		String userId = "test";
		Blog blog = new Blog(UUID.randomUUID().toString(),"Some content",userId);
		blogDao.createBlog(blog);
		List<Blog> blogsByTitle = blogDao.getBlogsByTitle(blog.getTitle().substring(blog.getTitle().indexOf("-"), blog.getTitle().length()),userId);
		Assert.assertEquals(1, blogsByTitle.size());
		
		Blog blog2 = new Blog(UUID.randomUUID().toString()+blog.getTitle(),"Some content","test");
		blogDao.createBlog(blog2);
		blogsByTitle = blogDao.getBlogsByTitle(blog.getTitle().substring(blog.getTitle().indexOf("-"), blog.getTitle().length()),userId);
		Assert.assertEquals(2, blogsByTitle.size());
		
		blogDao.deleteBlogById(blog.getId());
		Assert.assertTrue(blogDao.deleteBlogById(blog.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog.getId()).isPresent());
		
		blogDao.deleteBlogById(blog2.getId());
		Assert.assertTrue(blogDao.deleteBlogById(blog2.getId()));
		Assert.assertFalse(blogDao.getBlogById(blog2.getId()).isPresent());
	}
	
	@Test
	public void getAllBlogs() {
		List<Blog> allBlogs = blogDao.getAllBlogs();
		logger.info("blogs : "+allBlogs);
	}
	
}
