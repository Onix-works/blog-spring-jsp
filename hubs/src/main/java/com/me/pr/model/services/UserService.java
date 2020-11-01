package com.me.pr.model.services;


import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me.pr.exceptions.UserAlreadyExistException;
import com.me.pr.model.entities.UserEntity;
import com.me.pr.model.repositories.UserRepository;
import com.me.pr.view.UserDto;

@Service("userService")
@Transactional
public class UserService implements IUserService{
    
    private UserRepository repository;
    
    @Autowired
	public void setRepository(UserRepository repository) {
		this.repository = repository;
	}


    public UserEntity registerUser(UserDto userDto) 
      throws UserAlreadyExistException {
         
        if (repository.findByEmail(userDto.getEmail()) != null) {   
            throw new UserAlreadyExistException(
              "There is an account with that email address:  "
              + userDto.getEmail());
        }
        UserEntity user = new UserEntity();    
        user.setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setRoles(Arrays.asList("ROLE_USER"));
        return repository.save(user);       
    }
   
}