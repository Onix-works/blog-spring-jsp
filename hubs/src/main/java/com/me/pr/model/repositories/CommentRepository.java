package com.me.pr.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.pr.model.entities.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {}