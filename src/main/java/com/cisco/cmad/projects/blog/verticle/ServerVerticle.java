package com.cisco.cmad.projects.blog.verticle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cisco.cmad.projects.blog.dto.Blog;
import com.cisco.cmad.projects.blog.dto.User;
import com.cisco.cmad.projects.blog.service.LoginService;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.AuthProvider;
import io.vertx.ext.auth.shiro.ShiroAuth;
import io.vertx.ext.auth.shiro.ShiroAuthRealmType;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.FormLoginHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

@Service
@Qualifier("serverVerticle")
public class ServerVerticle extends AbstractVerticle {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private LoginService loginService;

	@Value("${server.port}")
	private Integer port;

	@Override
	public void start(Future<Void> future) throws Exception {
		logger.info("starting..."+vertx);
		Router router = Router.router(vertx);
		
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		
		router.route("/static/*").handler(StaticHandler.create("web").setCachingEnabled(false));
		
		router.route().handler(CookieHandler.create());
		router.route().handler(BodyHandler.create());
		router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		
		// Simple auth service which uses a properties file for user/role info
	    AuthProvider authProvider = ShiroAuth.create(vertx, ShiroAuthRealmType.PROPERTIES, new JsonObject());
	    
	    // We need a user session handler too to make sure the user is stored in the session between requests
	    router.route().handler(UserSessionHandler.create(authProvider));
	    
		router.get("/*").handler(RedirectAuthHandler.create(authProvider, "/static/index.html"));
		
		// Handles the actual login
	    router.route("/loginhandler").handler(FormLoginHandler.create(authProvider));
	    
	    // Any requests to URI starting '/private/' require login
	    router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/static/index.html"));
	    // Serve the static private pages from directory 'private'
	    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));
	    
	    // Implement logout
	    router.route("/logout").handler(context -> {
	      context.clearUser();
	      // Redirect back to the index page
	      context.response().putHeader("location", "/").setStatusCode(302).end();
	    });
	    
		router.post("/api/register").handler(rctx -> {
			logger.info(rctx.getBodyAsJson().toString());
			vertx.eventBus().send("user.register", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/favorites").handler(rctx -> {
			String userName = loginService.getCurrentUser().getUserName();
			vertx.eventBus().send("blog.list.fav",userName, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/users").handler(rctx -> {
			vertx.eventBus().send("users.list","", r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/user/info").handler(rctx -> {
			String userName = loginService.getCurrentUser().getUserName();
			vertx.eventBus().send("user.info",userName, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.post("/api/blog/create").handler(rctx -> {
			User user = loginService.getCurrentUser();
			Blog blog = Json.decodeValue(rctx.getBodyAsString(), Blog.class);
			blog.setUserName(user.getUserName());
			
			vertx.eventBus().send("blog.create", Json.encodePrettily(blog), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/blogs/all").handler(rctx -> {
			vertx.eventBus().send("blog.list","", r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/blog/search").handler(rctx -> {
			User user = loginService.getCurrentUser();
			String title = rctx.request().getParam("title");
			logger.info("Title : "+title);
			Blog blog = new Blog(title,"",user.getUserName());
			vertx.eventBus().send("blog.search",Json.encodePrettily(blog), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		router.get("/api/blogs").handler(rctx -> {
			User user = loginService.getCurrentUser();
			vertx.eventBus().send("user.blog.list",user.getUserName(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
		/*router.get("/api/word/:name").handler(rctx -> {
			String name = rctx.request().getParam("name");
			// final DictionaryItem item = new
			// DictionaryItem(name,"dummy","anonym");
			vertx.eventBus().send("com.cisco.cmad.projects.dictionary.word.search", name, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
			
			 * rctx.response().setStatusCode(200).putHeader("content-type",
			 * "application/json; charset=utf-8") .end();
			 
		});
		router.route("/api/word").handler(BodyHandler.create());
		router.post("/api/word").handler(rctx -> {
			vertx.eventBus().send("com.cisco.cmad.projects.dictionary.word.save", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});*/
		
		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", port),
				result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				});
	}

	@Override
	public void stop() throws Exception {
		logger.info("stopping...");
		super.stop();
	}
}
