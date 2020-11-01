package com.me.pr.security;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.me.pr.model.entities.UserEntity;
import com.me.pr.model.repositories.UserRepository;

@Service("myUserDetailsService")
@Repository
@Transactional
public class MyUserDetailsService implements UserDetailsService  {
	
	private final Logger logger = LoggerFactory.getLogger(MyUserDetailsService.class);
  
    @Autowired
    private UserRepository userRepository;
   
    public UserDetails loadUserByUsername(String email)
      throws UsernameNotFoundException {
  
    	UserEntity user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException(
              "No user found with username: "+ email);
        }
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;
        User secUser = new User
                (user.getEmail(), 
                        user.getPassword(), enabled, accountNonExpired, 
                        credentialsNonExpired, accountNonLocked, 
                        getAuthorities(user.getRoles()));
        logger.info("Authorizing user:" + secUser.toString());
        
        return  secUser;
    }
     
    private static List<GrantedAuthority> getAuthorities (List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }
}