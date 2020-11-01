package com.me.pr.model.repositories;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.me.pr.model.entities.Blog;

public interface BlogRepository extends PagingAndSortingRepository<Blog, Long>,
QuerydslPredicateExecutor<Blog> {}
