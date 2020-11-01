package com.me.pr.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.me.pr.model.services.GeneralService;
import com.me.pr.view.Message;
import com.me.pr.view.UrlUtil;

@RequestMapping("/posts")
@Controller
public class PostController {
	
   private final Logger logger = LoggerFactory.getLogger(BlogController.class);
   private GeneralService blogService;
   private MessageSource messageSource;

   @Autowired
   public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;

   }
   @Autowired
   public void setBlogService(GeneralService blogService) {
		this.blogService = blogService;
		}
     
   
   @GetMapping(value = "/{id}")
   public String show(@PathVariable("id") Long id, Model uiModel) {
		 logger.info("Showing post");
		 uiModel.asMap().clear();
	        Post post = blogService.findPostById(id);
	        uiModel.addAttribute("post", post);

	        return "posts/show";
	    }   
   
   @GetMapping(value = "/comm/{postid}", params = "comm")
   public String createCommentForm(Model uiModel, @PathVariable Long postid) {
	   Comment comment = new Comment();
	   Post post = blogService.findPostById(postid);
	   comment.setPost(post);
	   uiModel.addAttribute("comment", comment);
	   return "posts/createcomment";
   }
   
   @RequestMapping(value = "/comm/{postid}", params = "comm", method = RequestMethod.POST)
   public String createComment(@ModelAttribute("comment")Comment comment, BindingResult bindingResult, Model uiModel, 
   		HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes, 
   		Locale locale, @PathVariable Long postid, Authentication authentication) {
       
	   logger.info("Creating comment");
       uiModel.asMap().clear();
       redirectAttributes.addFlashAttribute("message", new Message("success",
               messageSource.getMessage("contact_save_success", new Object[]{}, locale)));

       Post post = blogService.findPostById(postid);
       comment.setPost(post);
       comment.setUserName(authentication.getName());
       comment = blogService.saveComment(comment);
       post.getComments().add(comment);
       post = blogService.savePost(post);   
       return "redirect:/posts/" + UrlUtil.encodeUrlPathSegment(comment.getPost().getId().toString(),
               httpServletRequest);
   }
   
   @GetMapping(value = "/{id}", params = "post")
   public String updateForm(@PathVariable("id") Long id, Model uiModel) {
	   Post post = blogService.findPostById(id);
       uiModel.addAttribute("post", post);
       return "posts/update";
   }
   
   @RequestMapping(value = "/{id}", params = "post", method = RequestMethod.POST)
   public String update(@PathVariable("id") Long id, @Valid @ModelAttribute("post") Post post, BindingResult bindingResult, Model uiModel,
			HttpServletRequest httpServletRequest, RedirectAttributes redirectAttributes,
	        Locale locale) {
	        logger.info("Updating post");	        
	        if (bindingResult.hasErrors()) {
	            uiModel.addAttribute("message", new Message("error",
	                    messageSource.getMessage("blog_save_fail", new Object[]{}, locale)));
	            uiModel.addAttribute("post", post);
	            return "posts/update";
	        }	        
	        uiModel.asMap().clear();  
	        redirectAttributes.addFlashAttribute("message", new Message("success",
	                messageSource.getMessage("blog_save_success", new Object[]{}, locale)));	
	        post = blogService.savePost(post);	       
	        return "redirect:/posts/" + UrlUtil.encodeUrlPathSegment(post.getId().toString(),
	                httpServletRequest);
	        }
   
   @GetMapping(value = "/{id}", params = "del")
   public String delete(@PathVariable Long id) {
       blogService.deletePostById(id);
     
       return "redirect:/posts/";
   }
   
   
   
}