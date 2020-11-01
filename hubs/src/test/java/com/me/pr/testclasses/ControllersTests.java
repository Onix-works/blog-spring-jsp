package com.me.pr.testclasses;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.NoSuchElementException;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import org.hamcrest.text.MatchesPattern;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.me.pr.model.services.GeneralService;
import com.me.pr.view.UserDto;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:webapp/WEB-INF/spring/root-context.xml")
@WebAppConfiguration("webapp")
@DirtiesContext
public class ControllersTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Autowired
	private GeneralService blogService;
	
	@Autowired
	private UserDetailsService userService;
  
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.apply(springSecurity())
				.build();
	}



	@Test
	public void webApplicationContextgetBeanTest() {
		ServletContext servletContext = wac.getServletContext();

		Assert.assertNotNull(servletContext);
		Assert.assertTrue(servletContext instanceof MockServletContext);
		Assert.assertNotNull(wac.getBean("transactionManager"));
	}

	@Test
	public void blogController_listTest() throws Exception {

		this.mockMvc.perform(get("/"))
		    .andExpect(status().isFound())
		    .andExpect(view().name("redirect:/blogs/"));

	}

	@Test
	public void blogController_showTest() throws Exception {

		this.mockMvc.perform(get("/blogs/1"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("blogs/show"))
			.andExpect(forwardedUrl("/WEB-INF/layouts/default.jspx"));

	}

	@Test
	@WithMockUser
	public void blogController_updateFormTest() throws Exception {

		this.mockMvc.perform(get("/blogs/1?form"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("blogs/update"))
		    .andExpect(model().attributeExists("blog"));

	}

	@Test
	@WithMockUser
	public void blogController_updateTest() throws Exception {

		Blog blog = blogService.findBlogById((long) 1);
		blog.setPhoto(null);
		blog.setName("testUpdate");

		this.mockMvc.perform(post("/blogs/1?form")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.flashAttr("blog", blog)
			)
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/blogs/1"));
		assertEquals(blogService.findBlogById((long) 1).getName(), "testUpdate");

	}

	@Test
	@WithMockUser
	public void blogController_createFormTest() throws Exception {

		this.mockMvc.perform(get("/blogs/?form"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("blogs/create"))
		    .andExpect(model().attributeExists("blog"));

	}

	@Test
	@WithMockUser
	public void blogController_createTest() throws Exception {

		Blog blog = new Blog();
		blog.setName("testCreate");

		this.mockMvc.perform(post("/blogs/?form")
				.contentType(MediaType.MULTIPART_FORM_DATA)
				.flashAttr("blog", blog)
				)
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/blogs/"));

		assertEquals(blogService.findBlogById(blog.getId()).getName(), "testCreate");

	}
	
	@Test
	@WithMockUser
	public void blogController_createPostFormTest() throws Exception {

		this.mockMvc.perform(get("/blogs/1?post"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("blogs/createpost"))
		    .andExpect(model().attributeExists("post"));

	}
	
	@Test
	@WithMockUser
	public void blogController_createPostTest() throws Exception {

		Post post = new Post();
		post.setName("testCreate");
		post.setBtext("testText");

		this.mockMvc.perform(post("/blogs/1?post")
				.flashAttr("post", post)
				)
				.andExpect(status().isFound())
				.andExpect(view().name(new MatchesPattern(Pattern.compile("redirect:/posts/\\d+"))));

		assertEquals(blogService.findPostById(post.getId()).getName(), "testCreate");
	}
	
	@Test
	@WithMockUser
	public void blogController_deleteTest() throws Exception {

		this.mockMvc.perform(get("/blogs/2?del"))
		    .andExpect(status().isFound())
		    .andExpect(view().name("redirect:/blogs/"));
		
		assertThrows(NoSuchElementException.class, () -> {
			blogService.findBlogById((long) 2);
			}
		);

	}
	
	@Test
	@WithMockUser
	public void blogController_listgridTest() throws Exception {

		this.mockMvc.perform(get("/blogs/listgrid")
				.param("page", "1")
				.param("rows", "5"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	@WithMockUser
	public void blogController_searchFormTest() throws Exception {

		this.mockMvc.perform(get("/blogs/")
				.param("search", "test"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("blogs/search"))
		    .andExpect(model().attribute("search", "test"));
	}
	
	@Test
	@WithMockUser
	public void blogController_searchListgridTest() throws Exception {

		this.mockMvc.perform(get("/blogs/search")
				.param("page", "1")
				.param("rows", "5")
				.param("search", "test"))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}
	
	@Test
	public void blogController_registrationFormTest() throws Exception {

		this.mockMvc.perform(get("/blogs/registration"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("registration"))
		    .andExpect(model().attributeExists("userDto"));
	}
	
	@Test
	public void blogController_registrationTest() throws Exception {

		UserDto userDto = new UserDto("12345", "12345", "email@test.com");

		this.mockMvc.perform(post("/blogs/registration")
				.flashAttr("userDto", userDto)
				)
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/blogs/"));

		assertNotNull(userService.loadUserByUsername("email@test.com"));

	}
	
	@Test
	public void postController_showTest() throws Exception {

		this.mockMvc.perform(get("/posts/1"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("posts/show"))
			.andExpect(forwardedUrl("/WEB-INF/layouts/default.jspx"))
			.andExpect(model().attributeExists("post"));

	}

	@Test
	@WithMockUser
	public void postController_updateFormTest() throws Exception {

		this.mockMvc.perform(get("/posts/1?post"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("posts/update"))
		    .andExpect(model().attributeExists("post"));

	}

	@Test
	@WithMockUser
	public void postController_updateTest() throws Exception {

		Post post = blogService.findPostById((long) 1);
		post.setName("testUpdate");

		this.mockMvc.perform(post("/posts/1?post")
			.contentType(MediaType.MULTIPART_FORM_DATA)
			.flashAttr("post", post)
			)
			.andExpect(status().isFound())
			.andExpect(view().name("redirect:/posts/1"));
		
		assertEquals(blogService.findPostById((long) 1).getName(), "testUpdate");

	}
	
	@Test
	@WithMockUser
	public void postController_createCommentFormTest() throws Exception {

		this.mockMvc.perform(get("/posts/comm/1?comm"))
		    .andExpect(status().isOk())
		    .andExpect(view().name("posts/createcomment"))
		    .andExpect(model().attributeExists("comment"));

	}
	
	@Test
	@WithMockUser
	public void postController_createCommentTest() throws Exception {

		Comment comment = new Comment();
		comment.setText("testText");
		comment.setUserName("testName");

		this.mockMvc.perform(post("/posts/comm/1?comm")
				.flashAttr("comment", comment)
				)
				.andExpect(status().isFound())
				.andExpect(view().name(new MatchesPattern(Pattern.compile("redirect:/posts/\\d+"))));

		assertEquals(blogService.findCommentById(comment.getId()).getText(), "testText");
	}
	
	@Test
	@WithMockUser
	public void postController_deleteTest() throws Exception {

		this.mockMvc.perform(get("/posts/2?del"))
		    .andExpect(status().isFound())
		    .andExpect(view().name("redirect:/posts/"));
		
		assertThrows(NoSuchElementException.class, () -> {
			blogService.findPostById((long) 2);
			}
		);

	}
	
}
