package com.bridgelabz.fundoonotes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.request.LoginInformation;
import com.bridgelabz.fundoonotes.request.PasswordUpdate;

public interface UserServices {

	UserInformation login(LoginInformation information);

	boolean register(UserDto ionformation);
	boolean verify(String token) throws Exception;
	boolean isUserExist(String email);
	boolean update(PasswordUpdate information, String token);
	List<UserInformation> getUsers();
	UserInformation getSingleUser(String token);

	

}
