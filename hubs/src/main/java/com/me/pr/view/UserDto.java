package com.me.pr.view;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.me.pr.annotations.PasswordMatches;
import com.me.pr.annotations.ValidEmail;
@PasswordMatches
public class UserDto {     
    @NotNull
    @NotEmpty
    private String password;
    private String matchingPassword;
    @ValidEmail
    @NotNull
    @NotEmpty
    private String email;
    
    public UserDto() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UserDto(@NotNull String password, String matchingPassword, @NotNull String email) {
		super();
		this.password = password;
		this.matchingPassword = matchingPassword;
		this.email = email;
	}

	

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMatchingPassword() {
		return matchingPassword;
	}

	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "UserDto [password=" + password + ", matchingPassword=" + matchingPassword + ", email=" + email + "]";
	}
     
}