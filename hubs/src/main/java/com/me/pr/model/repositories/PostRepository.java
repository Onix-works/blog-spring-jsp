package com.me.pr.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.pr.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {}