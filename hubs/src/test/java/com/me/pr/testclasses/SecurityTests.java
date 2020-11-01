package com.me.pr.testclasses;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import com.me.pr.model.entities.Blog;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:webapp/WEB-INF/spring/root-context.xml")
@WebAppConfiguration("webapp")
@DirtiesContext
public class SecurityTests {
	
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;
	
	
	@Before
	public void setup() throws Exception {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
				.apply(springSecurity())
				.build();
	}

	
	@Test
	public void unprotectedMethodNoCredentialsTest() throws Exception {

		this.mockMvc.perform(get("/blogs/1"))
	    .andExpect(status().isOk())
		.andExpect(unauthenticated());

	}
	
	@Test
	public void protectedMethodNoTAuthenticatedTest() throws Exception {

		Blog blog = new Blog();
		blog.setName("testCreate");
		 
			this.mockMvc.perform(post("/blogs/?form")
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.flashAttr("blog", blog)
					)
			.andExpect(status().isFound())
			.andExpect(unauthenticated());
			
			assertNull(
				blog.getId()	
			);
	}
		
	@Test
	public void loginWrongCredentialsTest() throws Exception {

		this.mockMvc.perform(formLogin().user("wrong@me.ru").password("user")		
				)
		.andExpect(status().isFound())
		.andExpect(unauthenticated());
	
	}
	
	@Test
	public void loginNoTValidCredentialsTest() throws Exception {

		this.mockMvc.perform(formLogin().user("wrong").password("wrong")		
				)
		.andExpect(status().isFound())
		.andExpect(unauthenticated());
	
	}
	
	@Test
	public void loginCorectCredentialsTest() throws Exception {

		this.mockMvc.perform(formLogin().user("me@me.ru").password("user")		
				)
		.andExpect(status().isFound())
		.andExpect(authenticated());
	
	}
	
	@Test
	@WithMockUser(username = "me@me.ru", password = "user")
	public void protectedMethodAuthenticatedTest() throws Exception {

		Blog blog = new Blog();
		blog.setName("testCreate");
		
		this.mockMvc.perform(post("/blogs/?form")
					.contentType(MediaType.MULTIPART_FORM_DATA)
					.flashAttr("blog", blog)
					)
				.andExpect(status().isFound())
				.andExpect(authenticated());
		 
		assertNotNull(
			blog.getId()	
		);
	}

}
