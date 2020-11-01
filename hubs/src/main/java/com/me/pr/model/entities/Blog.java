package com.me.pr.model.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.sql.Blob;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryEntity;


@Entity
@QueryEntity
public class Blog implements Serializable {
	private Long id;

	private String name;

	private List<Post> posts;

	//private List<Comment> comments;
	
	
	private Blob photo;

	public Blog() {
		super();
		this.posts = new ArrayList<Post>();
	}

	public Blog(String name) {
		super();
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotEmpty(message = "not empty")
	@Column(name = "NM", length = 50)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@JsonIgnore
	@OneToMany(cascade = CascadeType.ALL,mappedBy = "blog", fetch = FetchType.LAZY)
	public List<Post> getPosts() {
		if (posts == null)
			throw new RuntimeException("Blog has null Posts");
			//return new ArrayList<Post>();
		return new ArrayList<Post>(posts);
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}
	

	@Override
	public String toString() {
		return "|Blog name " + name + " posts " + posts.toString();
	}
	
	@Basic(fetch= FetchType.LAZY)
   	@JsonIgnore
	public Blob getPhoto() {
		return photo;
	}

	public void setPhoto(Blob photo) {
		this.photo = photo;
	}

}
