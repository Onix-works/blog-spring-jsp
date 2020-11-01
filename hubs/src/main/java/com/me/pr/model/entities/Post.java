package com.me.pr.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.validator.constraints.NotEmpty;



@Entity
public class Post implements Serializable {
	
	private Long id;
	private String name;
	private Blog blog;
	private String btext;
	private List<Comment> comments;

	public Post() {
		super();
		this.blog = new Blog();
		this.comments = new ArrayList<Comment>();
	}

	public Post(Blog blog) {
		super();
		this.blog = blog;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch=FetchType.LAZY)
	public Blog getBlog() {
		if (blog == null)
			throw new RuntimeException("Post has null Blog");
		return blog;
	}

	public void setBlog(Blog blog) {
		this.blog = blog;
	}
	@NotEmpty
	@Column(length = 4000)
	public String getBtext() {
		return btext;
	}

	
	public void setBtext(String btext) {
		this.btext = btext;
	}

	@Override
	public String toString() {
		return "|Post name: "+ name + " id: " + id + " text " + btext + " blogname: " + blog.getName() + " comments: " + comments;
	}
	
	@NotEmpty
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@OneToMany(cascade = CascadeType.ALL,mappedBy = "post",fetch=FetchType.LAZY)
	public List<Comment> getComments() {
		if (comments == null)
			throw new RuntimeException("Post has null Comments");
			//return new ArrayList<Comment>();
		return new ArrayList<Comment>(comments);
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	
	

}
