package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.PasswordUpdate;
import com.bridgelabz.fundoonotes.Entity.UserInformation;

public interface IUserRepository {
	
	UserInformation save(UserInformation userInformation);
	
	UserInformation getUser(String email);
	
	boolean verify(Long id);
	
	boolean upDate(PasswordUpdate information, Long token);
	
	UserInformation getUserById(Long id );

	List<UserInformation> getUsers();

}
