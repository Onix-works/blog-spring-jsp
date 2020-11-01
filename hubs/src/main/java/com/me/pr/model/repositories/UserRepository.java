package com.me.pr.model.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.me.pr.model.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

	UserEntity findByEmail(String email);
	@Override
    void delete(UserEntity user);
}