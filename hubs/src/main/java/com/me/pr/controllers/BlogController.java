package com.me.pr.controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import javax.validation.Valid;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.common.collect.Lists;
import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Post;
import com.me.pr.model.entities.QBlog;
import com.me.pr.model.services.GeneralService;
import com.me.pr.model.services.IUserService;
import com.me.pr.validation.ImageConverter;
import com.me.pr.view.BlogGrid;
import com.me.pr.view.Message;
import com.me.pr.view.UrlUtil;
import com.me.pr.view.UserDto;
import com.querydsl.core.types.dsl.BooleanExpression;


@RequestMapping("/blogs")
@Controller
public class BlogController {
	
   private final Logger logger = LoggerFactory.getLogger(BlogController.class);
   private GeneralService blogService;
   private IUserService userService;
   private MessageSource messageSource;


   @Autowired
   public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;

   }
   @Autowired
   public void setBlogService(GeneralService blogService) {
		this.blogService = blogService;
	}
   @Autowired
   public void setUserService(IUserService userService) {
		this.userService = userService;
	}
   

	@GetMapping
	public String list(Model uimodel) {
		logger.info("Listing blogs");
		List<Blog> blogs = blogService.findAllBlogs();
		uimodel.addAttribute("blogs", blogs);
		
		return "blogs/list";
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public String show(@PathVariable("id") Long id, Model uiModel) {
		logger.info("showing blog");
		Blog blog = blogService.findBlogById(id);
	    uiModel.addAttribute("blog", blog);
	    
	    return "blogs/show";
	    }
	
	@PreAuthorize("isAuthenticated()") 
	@RequestMapping(value = "/{id}", params = "form", method = RequestMethod.POST)
	public String update(@Valid Blog blog, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
	        Locale locale, @RequestParam(value="file", required=false) Part file) {
		
		    logger.info("Updating blog");	        
	        if (bindingResult.hasErrors()) {
	            uiModel.addAttribute("message", new Message("error",
	                    messageSource.getMessage("save_fail", new Object[]{}, locale)));
	            uiModel.addAttribute("blog", blog);
	            return "blogs/update";
	        }
	        uiModel.asMap().clear();
	        redirectAttributes.addFlashAttribute("message", new Message("success",
	                messageSource.getMessage("save_success", new Object[]{}, locale)));
	        if (file != null) {
	            logger.info("File name: " + file.getName());
	            logger.info("File size: " + file.getSize());
	            logger.info("File content type: " + file.getContentType());
	            byte[] fileContent = null;
	            try {
	            	InputStream inputStream = file.getInputStream();
	                if (inputStream == null) logger.info("File inputstream is null");
	                fileContent = IOUtils.toByteArray(inputStream);
	            	try {
	           		Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
	        	    blog.setPhoto(blob);
	           	   }
	            	catch (SQLException ex1) {
	           		ex1.printStackTrace();
	           	   }
	            }
	        	catch(IOException ex2) {
	        		   ex2.printStackTrace();
	        	   };
	        }
	                	        
	        blogService.saveBlog(blog);
	        
	        return "redirect:/blogs/" + UrlUtil.encodeUrlPathSegment(blog.getId().toString(),
	                httpServletRequest);
	        }
	
	 @PreAuthorize("isAuthenticated()") 
	 @RequestMapping(value = "/{id}", params = "form", method = RequestMethod.GET)
	    public String updateForm(@PathVariable("id") Long id, Model uiModel) {
	        uiModel.addAttribute("blog", blogService.findBlogById(id));
	        
	        return "blogs/update";
	    }
	 
	 @PreAuthorize("isAuthenticated()") 
	 @RequestMapping(method = RequestMethod.POST)
	    public String create(@Valid Blog blog, BindingResult bindingResult, Model uiModel, 
	    		HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, 
	    		Locale locale, @RequestParam(value="file", required=false) Part file ) {
		 
	        logger.info("Creating blog");
	        if (bindingResult.hasErrors()) {
	            uiModel.addAttribute("message", new Message("error",
	                    messageSource.getMessage("save_fail", new Object[]{}, locale)));
	            uiModel.addAttribute("blog", blog);
	            return "blogs/create";
	        }	      
	        uiModel.asMap().clear();
	        redirectAttributes.addFlashAttribute("message", new Message("success",
	                messageSource.getMessage("save_success", new Object[]{}, locale)));
	        
	        if (file != null) {
	            logger.info("File name: " + file.getName());
	            logger.info("File size: " + file.getSize());
	            logger.info("File content type: " + file.getContentType());
	            byte[] fileContent = null;
	            try {
	            	InputStream inputStream = file.getInputStream();
	                if (inputStream == null) logger.info("File inputstream is null");
	                fileContent = IOUtils.toByteArray(inputStream);
	            	try {
	           		Blob blob = ImageConverter.resizeBytesImage(fileContent, 500, 500);
	        	    blog.setPhoto(blob);
	           	   }
	            	catch (SQLException ex1) {
	           		logger.info("EXCEPTION: " + ex1);
	           	   }
	            }
	        	catch(IOException ex2) {
	        		   logger.info("EXCEPTION: " + ex2);
	        	   };
	        }

	        blogService.saveBlog(blog);
	        logger.info("Blog id: " + blog.getId());
	        logger.info("Blog name: " + blog.getName());  
	        
	        return "redirect:/blogs/";
	    }

	    @PreAuthorize("isAuthenticated()") 
	    @RequestMapping(params = "form", method = RequestMethod.GET)
	    public String createForm(Model uiModel) {
	        Blog blog = new Blog();
	        uiModel.addAttribute("blog", blog); 
	        
	        return "blogs/create";
	    }
	    
	    @PreAuthorize("isAuthenticated()")
	    @GetMapping(value = "/{+}", params = "post")
	    public String createPostForm(Model uiModel) {
	    	Post post = new Post();
	     	uiModel.addAttribute("post", post); 
	     	
	    	return "blogs/createpost";
	    }
	    
	    @PreAuthorize("isAuthenticated()") 
	    @PostMapping(value = "/{blogid}", params = "post")
	    public String createPost(@Valid Post post, BindingResult bindingResult, Model uiModel, 
	    		HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, 
	    		Locale locale, @PathVariable Long blogid) {
	    	
	        logger.info("Creating post");
	        if (bindingResult.hasErrors()) {
	            uiModel.addAttribute("message", new Message("error",
	                    messageSource.getMessage("save_fail", new Object[]{}, locale)));
	            uiModel.addAttribute("post", post);
	            return "blogs/createpost";
	        }	      
	        uiModel.asMap().clear();
	        redirectAttributes.addFlashAttribute("message", new Message("success",
	                messageSource.getMessage("save_success", new Object[]{}, locale)));

	        Blog blog = blogService.findBlogById(blogid);
	        post.setBlog(blog);
	        blogService.savePost(post);	 
	        
	        return "redirect:/posts/" + UrlUtil.encodeUrlPathSegment(post.getId().toString(),
	                httpServletRequest);
	    }
	    
	    @PreAuthorize("isAuthenticated()") 
	    @GetMapping(value = "/{blogid}", params = "del")
	    public String delete(@PathVariable Long blogid) {
	        blogService.deleteBlogById(blogid);	  
	        
	        return "redirect:/blogs/";
	    }
	    
	    @ResponseBody
	    @RequestMapping(value = "/listgrid", method = RequestMethod.GET, produces="application/json")
	    public BlogGrid listGrid(@RequestParam(value = "page", required = false) Integer page,
	                                @RequestParam(value = "rows", required = false) Integer rows,
	                                @RequestParam(value = "sidx", required = false) String sortBy,
	                                @RequestParam(value = "sord", required = false) String order) {

	        // Process order by
	        Sort sort = null;
	        String orderBy = sortBy;
	        if (orderBy != null && orderBy.equals("birthDateString"))
	            orderBy = "birthDate";

	        if (orderBy != null && order != null) {
	            if (order.equals("desc")) {
	                sort =  Sort.by(Sort.Direction.DESC, orderBy);
	            } else
	                sort =  Sort.by(Sort.Direction.ASC, orderBy);
	        }

	        // Constructs page request for current page
	        PageRequest pageRequest = null;

	        if (sort != null) {
	            pageRequest = PageRequest.of(page - 1, rows, sort);
	        } else {
	            pageRequest = PageRequest.of(page - 1, rows);
	        }

	        Page<Blog> blogPage = blogService.findAllByPage(pageRequest);

	        // Construct the grid data that will return as JSON data
	        BlogGrid blogGrid = new BlogGrid();

	        blogGrid.setCurrentPage(blogPage.getNumber() + 1);
	        blogGrid.setTotalPages(blogPage.getTotalPages());
	        blogGrid.setTotalRecords(blogPage.getTotalElements());

	        blogGrid.setBlogData(Lists.newArrayList(blogPage.iterator()));

	        return blogGrid;
	    } 
	    
	    @RequestMapping(params = "search", method = RequestMethod.GET)
	    public String searchForm(Model uiModel, 
	    		@RequestParam(value = "search", required = false) String search) {
	    	uiModel.addAttribute("search", search);
	    	
	        return "blogs/search";
	    }
	    
	    @ResponseBody
	    @RequestMapping(value = "/search", method = RequestMethod.GET, produces="application/json")
	    public  BlogGrid searchListGrid(@RequestParam(value = "page", required = false) Integer page,
                @RequestParam(value = "rows", required = false) Integer rows,
                @RequestParam(value = "sidx", required = false) String sortBy,
                @RequestParam(value = "sord", required = false) String order,
	            @RequestParam(value = "search", required = false) String search) {

	        // Process order by
	        Sort sort = null;
	        String orderBy = sortBy;
	        if (orderBy != null && orderBy.equals("birthDateString"))
	            orderBy = "birthDate";
	        if (orderBy != null && order != null) {
	            if (order.equals("desc")) {
	                sort =  Sort.by(Sort.Direction.DESC, orderBy);
	            } else
	                sort =  Sort.by(Sort.Direction.ASC, orderBy);
	        }

	        // Constructs page request for current page
	        PageRequest pageRequest = null;
	        if (sort != null) {
	            pageRequest = PageRequest.of(page - 1, rows, sort);
	        } else {
	            pageRequest = PageRequest.of(page - 1, rows);
	        }	        
	        QBlog blog = QBlog.blog;
	        BooleanExpression query = blog.name.like("%"+(String)search+"%");
	        Page<Blog> blogPage = blogService.findAllByPageQuery(query, pageRequest);

	        // Construct the grid data that will return as JSON data
	        BlogGrid blogGrid = new BlogGrid();
	        blogGrid.setCurrentPage(blogPage.getNumber() + 1);
	        blogGrid.setTotalPages(blogPage.getTotalPages());
	        blogGrid.setTotalRecords(blogPage.getTotalElements());
	        blogGrid.setBlogData(Lists.newArrayList(blogPage.iterator()));

	        return blogGrid;
	    } 
	    
	    @RequestMapping(value = "/photo/{id}", method = RequestMethod.GET)
	    @ResponseBody
	    public byte[] downloadPhoto(@PathVariable("id") Long id) throws SQLException {
	        
	    	Blog blog = blogService.findBlogById(id);
	    	if (blog.getPhoto() != null) {
	            try {
					logger.info("Downloading photo for id: {} with size: {}", blog.getId(),
					        blog.getPhoto().length());
				} catch (SQLException e) {
					logger.info("SQLException");
					e.printStackTrace();
				}
	        }
	        else
	        	logger.info("No Photo");
	        Blob photo = blog.getPhoto();
        
		    return photo.getBytes(1, (int) photo.length());
			
	    }
	    
	    @GetMapping("/registration")
	    public String registrationForm(WebRequest request, Model uimodel) {
	        UserDto userDto = new UserDto();
	        uimodel.addAttribute("userDto", userDto);
	        
	        return "registration";
	    }
	    
	    @PostMapping("/registration")
	    public String registration
	        (@Valid UserDto userDto,BindingResult bindingResult, Model uiModel,
	        HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, 
	  	    Locale locale) {
	    	
	    	 if (bindingResult.hasErrors()) {
		            uiModel.addAttribute("message", new Message("error",
		                    messageSource.getMessage("save_fail", new Object[]{}, locale)));
		            uiModel.addAttribute("userDto", userDto);
		            return "registration";
		        }
	    	userService.registerUser(userDto);
	        uiModel.asMap().clear();

	        return "redirect:/blogs/";
	    }
	       
	    
}
  

   


