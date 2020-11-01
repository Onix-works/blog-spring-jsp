package com.me.pr.model.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.querydsl.core.types.Predicate;

public interface GeneralService {
	
	Blog saveBlog(Blog blog);
	Blog findBlogById(Long id);
	List<Blog> findAllBlogs();
	void deleteBlogById(Long id);
	Page<Blog> findAllByPage(Pageable pageable);
	Page<Blog> findAllByPageQuery(Predicate query, Pageable page);
	
	Post savePost(Post post);
	Post findPostById(Long id);
	List<Post> findAllPosts();
	void deletePostById(Long id);
	
	Comment saveComment(Comment comment);
	Comment findCommentById(Long id);
	List<Comment> findAllComments();
	void deleteCommentById(Long id);
	
	
	
		
}
