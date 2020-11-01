package com.me.pr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.SQLException;

import javax.annotation.PostConstruct;
import javax.sql.rowset.serial.SerialBlob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.RequestHandledEvent;
import org.w3c.dom.css.Counter;

import com.me.pr.controllers.BlogController;
import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.me.pr.model.services.GeneralService;
import com.me.pr.model.services.IUserService;
import com.me.pr.validation.ImageConverter;
import com.me.pr.view.UserDto;

@Component
public class TestData {
	

	public TestData(){
		super();
		this.counter = 0;
		logger.info("INITIALIZED");
	}
	   private final Logger logger = LoggerFactory.getLogger(TestData.class);
	   private GeneralService blogService;
	   private IUserService userService;
	   private int counter;
	   
	   @Autowired
	   public void setBlogService(GeneralService blogService) {
			this.blogService = blogService;
		}
	   @Autowired
	   public void setUserService(IUserService userService) {
			this.userService = userService;
		}


	  @PostConstruct
	  public void contextRefreshedEvent() {
		  logger.info("Inserting test data");
		   int counter = 0;
		   if (counter == 0) {
			   counter++;
		   
		   UserDto user = new UserDto();
		   user.setEmail("me@me.ru");
		   user.setPassword("user");
		   userService.registerUser(user);
		   
		   Blog blog1 = new Blog();
		   blog1.setName("Pretty Flowers");
		   
		   try {
		   File file = new ClassPathResource("/images/flower.png").getFile();
	   	   byte[] fileContent = Files.readAllBytes(file.toPath());
	   	   try {
	   		Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
		    blog1.setPhoto(blob);
	   	   } catch (SQLException ex1) {
	   		logger.info("EXCEPTION: " + ex1);
	   	   };
		   } catch(IOException ex2) {
			   logger.info("EXCEPTION: " + ex2);
		   };
		   
		   blog1 = blogService.saveBlog(blog1);
		   
		   Post post1 = new Post();
		   post1.setName("Tulip");
		   post1.setBlog(blog1);
		   
		   try {
			   post1.setBtext(new String(Files.readAllBytes(
					   new ClassPathResource("/texts/tulips").getFile().toPath())));
		   } catch(IOException ex2) {
			   logger.info("EXCEPTION: " + ex2);
		   }
		   
		   post1 = blogService.savePost(post1);
		   blog1.getPosts().add(post1);
		   blog1 = blogService.saveBlog(blog1);
		   
		   Comment comment1 = new Comment(post1,"That's quite informative! Do more pls!", "ILikeFlowers@hi.com");
		   Comment comment2 = new Comment(post1, "What kind of weirdos actually like that stuff??", "EdgyBoi@blergh.net" );
		   comment1 = blogService.saveComment(comment1);
		   comment2 = blogService.saveComment(comment2);
		   post1.getComments().add(comment1);
		   post1.getComments().add(comment2);
		   post1 = blogService.savePost(post1);
		   
		   
		   Post post2 = new Post();
		   post2.setName("Hydrangea");
		   post2.setBlog(blog1);
		   
		   try {
			   post2.setBtext(new String(Files.readAllBytes(
					   new ClassPathResource("/texts/hydrangea").getFile().toPath())));
		   } catch(IOException ex2) {
			   logger.info("EXCEPTION: " + ex2);
		   }
		   
		   post2 = blogService.savePost(post2);
		   blog1.getPosts().add(post2);
		   blog1 = blogService.saveBlog(blog1);
		   
		   Comment comment3 = new Comment(post2,"Thats suspisiously close to wikipedia article...", "JustABypasser@hi.com");
		   comment3 = blogService.saveComment(comment3);
		   post2.getComments().add(comment3);
		   post2 = blogService.savePost(post2);
		   
		   
		   Blog blog2 = new Blog();
		   blog2.setName("Demon summoning for dummies");
		   
		   try {
			   File file = new ClassPathResource("/images/pentagram.png").getFile();
		   	   byte[] fileContent = Files.readAllBytes(file.toPath());
		   	   try {
		   		Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
		   		blog2.setPhoto(blob);
		   	   } catch (SQLException ex1) {
		   		logger.info("EXCEPTION: " + ex1);
		   	   };
			   } catch(IOException ex2) {
				   logger.info("EXCEPTION: " + ex2);
			   };
			   
			   blog2 = blogService.saveBlog(blog2);
			   
			   Post post3 = new Post();
			   post3.setName("Your First Demon");
			   post3.setBlog(blog2);
			   
			   try {
				   post3.setBtext(new String(Files.readAllBytes(
						   new ClassPathResource("/texts/yourfirstdemon").getFile().toPath())));
			   } catch(IOException ex2) {
				   logger.info("EXCEPTION: " + ex2);
			   }
			   
			   post3 = blogService.savePost(post3);
			   blog2.getPosts().add(post3);
			   blog2 = blogService.saveBlog(blog2);
			   
			   Comment comment4 = new Comment(post3,"What kind of b******t is this?! It just summond a petty ghost - nothing more! Where is my succubus???", "EdgyBoi@blergh.net");
			   Comment comment5 = new Comment(post3, "Doesnt work properly in southern hemisphere apparently.", "greenfrog@inq.com" );
			   comment4 = blogService.saveComment(comment4);
			   comment5 = blogService.saveComment(comment5);
			   post3.getComments().add(comment4);
			   post3.getComments().add(comment5);
			   post3 = blogService.savePost(post3);
			   
			   Blog blog3 = new Blog();
			   blog3.setName("Some blog i guess idk");
			   try {
				   File file = new ClassPathResource("/images/question.jpg").getFile();
			   	   byte[] fileContent = Files.readAllBytes(file.toPath());
			   	   try {
			   		Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
			   		blog3.setPhoto(blob);
			   	   } catch (SQLException ex1) {
			   		logger.info("EXCEPTION: " + ex1);
			   	   };
				   } catch(IOException ex2) {
					   logger.info("EXCEPTION: " + ex2);
				   };
			   blog3 = blogService.saveBlog(blog3);
			   
			   Blog blog4 = new Blog();
			   blog4.setName("This one's just empty");
			   blog4 = blogService.saveBlog(blog4);
			   
			   Blog blog5 = new Blog();
			   blog5.setName("This one's just empty");
			   blog5 = blogService.saveBlog(blog5);
			   
			   Blog blog6 = new Blog();
			   blog6.setName("This one's just empty");
			   blog6 = blogService.saveBlog(blog6);
			   
			   
			   
		   }
		   }
}
