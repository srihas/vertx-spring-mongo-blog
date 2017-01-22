package com.cisco.cmad.projects.blog;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.cisco.cmad.projects.blog.config.SpringConfiguration;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;

public class ApplicationStarter {
	
	public static void main(String[] args) {
		VertxOptions options = new VertxOptions(); 
		options.setMaxEventLoopExecuteTime(Long.MAX_VALUE);
		final Vertx vertx = Vertx.factory.vertx(options);
		ApplicationContext context = new AnnotationConfigApplicationContext(SpringConfiguration.class);
		
		Verticle serverVerticle =  (Verticle) context.getBean("serverVerticle");
		Verticle userDatabaseVerticle =  (Verticle) context.getBean("userDatabaseVerticle");
		Verticle blogServiceVerticle = (Verticle) context.getBean("blogServiceVerticle");
		
		vertx.deployVerticle(serverVerticle);
		vertx.deployVerticle(userDatabaseVerticle, new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(blogServiceVerticle, new DeploymentOptions().setWorker(true));
		
	}

}
