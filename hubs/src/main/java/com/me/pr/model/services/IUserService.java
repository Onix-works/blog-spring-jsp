package com.me.pr.model.services;

import com.me.pr.exceptions.UserAlreadyExistException;
import com.me.pr.model.entities.UserEntity;
import com.me.pr.view.UserDto;

public interface IUserService {
	UserEntity registerUser(UserDto userDto)     
      throws UserAlreadyExistException;

	
}