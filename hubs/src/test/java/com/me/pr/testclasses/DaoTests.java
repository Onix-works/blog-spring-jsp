package com.me.pr.testclasses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.me.pr.model.entities.QBlog;
import com.me.pr.model.entities.UserEntity;
import com.me.pr.model.services.GeneralService;
import com.me.pr.model.services.IUserService;
import com.me.pr.validation.ImageConverter;
import com.me.pr.view.UserDto;
import com.querydsl.core.types.dsl.BooleanExpression;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/app-context.xml")
@DirtiesContext
public class DaoTests {

	@Autowired
	private GeneralService blogService;

	@Autowired
	private IUserService userService;

	@Test
	public void generalService_saveTest() throws Exception {

		Blog blog1 = createAndPersistTestBlog();

		assertNotNull(blog1.getId());
		assertEquals(blog1.getName(), "Test Blog");

	}
	
	

	@Test
	public void generalService_findAllBlogsTest() throws Exception {
		Blog blog1 = createAndPersistTestBlog();

		List<Blog> blog = blogService.findAllBlogs();

		

		assertFalse(blog.isEmpty());
	}

	@Test
	public void generalService_findBlogByIdTest() throws Exception {
		Blog blog1 = createAndPersistTestBlog();

		Blog blog = blogService.findBlogById(blog1.getId());

		assertEquals(blog.getName(), "Test Blog");
	}
	
	@Test
	public void generalService_deleteBlogByIdTest() throws Exception {
		
		final Long id;
		
		Blog blog1 = createAndPersistTestBlog();
		
		id = blog1.getId();
		
		blogService.deleteBlogById(blog1.getId());

		assertThrows(NoSuchElementException.class, () -> {
			blogService.findBlogById(id);
			}
		);
	}
	
	@Test
	public void generalService_findAllByPageTest() throws Exception {

		Blog blog1 = createAndPersistTestBlog();
		
		PageRequest pageRequest = PageRequest.of(0, 5);	
		Page<Blog> blogPage = blogService.findAllByPage(pageRequest);
	
		assertFalse(blogPage.isEmpty());
	
	}
	
	@Test
	public void generalService_findAllByPageQueryTest() throws Exception {

		Blog blog1 = createAndPersistTestBlog();
		
		
		PageRequest pageRequest = PageRequest.of(0, 5);	
		QBlog blog = QBlog.blog;
        BooleanExpression query = blog.name.like("%Test%");
        Page<Blog> blogPage = blogService.findAllByPageQuery(query, pageRequest);
	
		assertFalse(blogPage.isEmpty());
	
	}
	
	@Test
	public void generalService_savePostTest() throws Exception {
		
		Post post = createAndPersistTestPostWithBlog();

		assertNotNull(post.getId());
		assertEquals(post.getName(), "Test Post");

	}
	
	@Test
	public void generalService_findAllPostsTest() throws Exception {
		Post post = createAndPersistTestPostWithBlog();

		List<Post> posts = blogService.findAllPosts();

		assertFalse(posts.isEmpty());
	}
	
	@Test
	public void generalService_findPostByIdTest() throws Exception {
		Post post = createAndPersistTestPostWithBlog();

		Post post2 = blogService.findPostById(post.getId());

		assertEquals(post2.getName(), "Test Post");
	}
	
	@Test
	public void generalService_deletePostByIdTest() throws Exception {
		Post post = createAndPersistTestPostWithBlog();

		blogService.deletePostById(post.getId());

		assertThrows(NoSuchElementException.class, () -> {
			blogService.findPostById(post.getId());
			}
		);
	}
	
	@Test
	public void generalService_saveCommentTest() throws Exception {
		Comment comment = createAndPersistTestCommentWithBlogAndPost();

		blogService.saveComment(comment);

		assertNotNull(comment.getId());
		assertEquals(comment.getText(), "Test Comment");
	}
	
	@Test
	public void generalService_findCommentByIdTest() throws Exception {
		Comment comment = createAndPersistTestCommentWithBlogAndPost();

		comment = blogService.findCommentById(comment.getId());

		assertEquals(comment.getText(), "Test Comment");
	}
	
	@Test
	public void generalService_findAllCommentsTest() throws Exception {
		
		Comment comment = createAndPersistTestCommentWithBlogAndPost();
		comment = blogService.findCommentById(comment.getId());
        List<Comment> comments = blogService.findAllComments();

		assertFalse(comments.isEmpty());
	}
	
	@Test
	public void generalService_deleteCommentByIdTest() throws Exception {
		
		Comment comment = createAndPersistTestCommentWithBlogAndPost();
		blogService.deleteCommentById(comment.getId());

		assertThrows(NoSuchElementException.class, () -> {
			blogService.findCommentById(comment.getId());
			}
		);
	}
	
	@Test
	public void userService_registerUserTest() throws Exception {
		
		 UserDto user = new UserDto();
		   user.setEmail("test@test.com");
		   user.setPassword("test");
		   user.setMatchingPassword("test");
		   UserEntity userEntity = userService.registerUser(user);

		assertNotNull(userEntity.getId());
		assertEquals(userEntity.getEmail(), "test@test.com");
	}
	
	
	
	private Blog createAndPersistTestBlog() {
		Blog blog1 = new Blog();
		blog1.setName("Test Blog");

		try {
			File file = new ClassPathResource("/images/flower.png").getFile();
			byte[] fileContent = Files.readAllBytes(file.toPath());
			try {
				Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
				blog1.setPhoto(blob);
			} catch (SQLException ex1) {
				ex1.printStackTrace();
			}
			;
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}

		blog1 = blogService.saveBlog(blog1);
		
		return blog1;
	}
	
	private Post createAndPersistTestPostWithBlog() {
		Blog blog1 = new Blog();
		blog1.setName("Test Blog");

		try {
			File file = new ClassPathResource("/images/flower.png").getFile();
			byte[] fileContent = Files.readAllBytes(file.toPath());
			try {
				Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
				blog1.setPhoto(blob);
			} catch (SQLException ex1) {
				ex1.printStackTrace();
			}
			;
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}

		blog1 = blogService.saveBlog(blog1);
		
		  Post post1 = new Post();
		   post1.setName("Test Post");
		   post1.setBlog(blog1);
		   
		   try {
			   post1.setBtext(new String(Files.readAllBytes(
					   new ClassPathResource("/texts/tulips").getFile().toPath())));
		   } catch(IOException ex2) {
			   ex2.printStackTrace();
		   }
		   
		   post1 = blogService.savePost(post1);
		   blog1.getPosts().add(post1);
		   blog1 = blogService.saveBlog(blog1);
		   return post1;
	}
	
	private Comment createAndPersistTestCommentWithBlogAndPost() {
		Blog blog1 = new Blog();
		blog1.setName("Test Blog");

		try {
			File file = new ClassPathResource("/images/flower.png").getFile();
			byte[] fileContent = Files.readAllBytes(file.toPath());
			try {
				Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
				blog1.setPhoto(blob);
			} catch (SQLException ex1) {
				ex1.printStackTrace();
			}
			;
		} catch (IOException ex2) {
			ex2.printStackTrace();
		}

		blog1 = blogService.saveBlog(blog1);
		
		  Post post1 = new Post();
		   post1.setName("Test Post");
		   post1.setBlog(blog1);
		   
		   try {
			   post1.setBtext(new String(Files.readAllBytes(
					   new ClassPathResource("/texts/tulips").getFile().toPath())));
		   } catch(IOException ex2) {
			   ex2.printStackTrace();
		   }
		   
		   post1 = blogService.savePost(post1);
		   blog1.getPosts().add(post1);
		   blog1 = blogService.saveBlog(blog1);
		  
		   Comment comment1 = new Comment(post1,"Test Comment", "Test Comment");
		   comment1 = blogService.saveComment(comment1);
		   post1.getComments().add(comment1);
		   post1 = blogService.savePost(post1);
		   
		   return comment1;
	}
	
	


}
