package com.bridgelabz.usermanagements.service;

import com.bridgelabz.usermanagements.model.User;

public interface Service {
	/**
	 * user will be login throw the valid email and password
	 * 
	 * @param userEmail
	 * @param userPassword
	 * @return user details
	 */
	public User loginByUserEmailAndPassword(String userEmail, String userPassword);

	/**
	 * A new user will be registered with all there new user data
	 * 
	 * @param newUser
	 * @return it's return true and false if successfully registered then it's will
	 *         return true or else it's return false
	 */
	public boolean userRegistred(User newUser);

}
