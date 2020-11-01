package com.me.pr.model.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
public class Comment implements Serializable {
	
	private Long id;
	private String text;
	private  String userName;
	private Post post;
	
	public Comment() {
		super();
		this.post = new Post();
	}
	
	public Comment(Post post, String text, String username) {
		super();
		this.post = post;
		this.text = text;
		this.userName = username;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@NotEmpty
	public String getText() {
		return text;
	}
	
	
	public void setText(String text) {
		this.text = text;
	}
	
	@ManyToOne( fetch=FetchType.LAZY)
	public Post getPost() {
		if (post == null)

			throw new RuntimeException("Comment has null Post");
		
		return post;
	}
	public void setPost(Post post) {
		this.post = post;
	}
	@NotEmpty
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return ("ID " + id + "POSTNAME " + post.getName() + "TEXT " + text);
	}


}
