package com.me.pr.model.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;
import com.me.pr.model.entities.Blog;
import com.me.pr.model.entities.Comment;
import com.me.pr.model.entities.Post;
import com.me.pr.model.repositories.BlogRepository;
import com.me.pr.model.repositories.CommentRepository;
import com.me.pr.model.repositories.PostRepository;
import com.me.pr.model.repositories.RepositoryProvider;
import com.querydsl.core.types.Predicate;

@Service("blogService")
@Repository
@Transactional
public class GeneralServiceImp implements GeneralService {
	
	
	private BlogRepository blogRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;
	
	@Autowired
	public GeneralServiceImp(RepositoryProvider repositoryProvider) {
		this.blogRepository = repositoryProvider.getBlogRepostory();
		this.postRepository = repositoryProvider.getPostRepository();
		this.commentRepository = repositoryProvider.getCommentRepository();
	}

	

	@Override
	public List<Blog> findAllBlogs() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(blogRepository.findAll());
	}

	
	@Override
	public Blog findBlogById(Long id) {
		// TODO Auto-generated method stub

	   return blogRepository.findById(id).get();
	}
	

	@Override
	public Blog saveBlog(Blog blog) {
		// TODO Auto-generated method stub
		return  blogRepository.save(blog);
	}
	
	@Override
	public void deleteBlogById(Long id) {
		// TODO Auto-generated method stub
		blogRepository.deleteById(id);
		return;
	}
	
	@Override
	public List<Post> findAllPosts() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(postRepository.findAll());
	}
	
	@Override
	public Post findPostById(Long id) {
		// TODO Auto-generated method stub
		return  postRepository.findById(id).get();
	}
	
	
	@Override
	public Post savePost(Post post) {
		// TODO Auto-generated method stub
		return  postRepository.save(post);
	}
	
	@Override
	public void deletePostById(Long id) {
		// TODO Auto-generated method stub
		postRepository.deleteById(id);
		return ;
	}
	
	@Override
	public Comment findCommentById(Long id) {
		// TODO Auto-generated method stub
		return  commentRepository.findById(id).get();
	}
	
	@Override
	public List<Comment> findAllComments() {
		// TODO Auto-generated method stub
		return Lists.newArrayList(commentRepository.findAll());
	}
	
	@Override
	public Comment saveComment(Comment comment) {
		// TODO Auto-generated method stub
		return  commentRepository.save(comment);
	}
	
	@Override
	public void deleteCommentById(Long id) {
		// TODO Auto-generated method stub
		commentRepository.deleteById(id);
		return ;
	}
	
	@Override
	public Page<Blog> findAllByPage(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }
	
	@Override
	public Page<Blog> findAllByPageQuery(Predicate query, Pageable page){
	    
		return blogRepository.findAll(query, page);
	}


}
