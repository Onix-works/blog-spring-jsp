package com.me.pr.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service("repositoryProvider")
public class RepositoryProvider {
	private BlogRepository blogRepostory;
	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private UserRepository userRepository;
	
	public UserRepository getUserRepository() {
		return userRepository;
	}
	@Autowired
	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}
	public BlogRepository getBlogRepostory() {
		return blogRepostory;
	}
	@Autowired
	public void setBlogRepostory(BlogRepository blogRepostory) {
		this.blogRepostory = blogRepostory;
	}
	public PostRepository getPostRepository() {
		return postRepository;
	}
	@Autowired
	public void setPostRepository(PostRepository postRepository) {
		this.postRepository = postRepository;
	}
	public CommentRepository getCommentRepository() {
		return commentRepository;
	}
	@Autowired
	public void setCommentRepository(CommentRepository commentRepository) {
		this.commentRepository = commentRepository;
	}
	
	
	
	

}
