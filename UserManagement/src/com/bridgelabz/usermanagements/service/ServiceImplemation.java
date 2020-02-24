package com.bridgelabz.usermanagements.service;

import com.bridgelabz.usermanagements.dao.UserDao;
import com.bridgelabz.usermanagements.model.User;
import com.bridgelabz.usermanagements.repositary.DbConnectionProvider;

public class ServiceImplemation implements Service {

	UserDao dao = new UserDao(DbConnectionProvider.getInstanceOfDb().connectionProvider());

	/**
	 * Implemented logging feature
	 * 
	 * @param useremail and userPassword
	 * @return user if valid user or else null
	 */
	@Override
	public User loginByUserEmailAndPassword(String userEmail, String userPassword) {

		return dao.getUserByEmailAndPassword(userEmail, userPassword);
	}

	/**
	 * Implemented new user registration feature
	 * 
	 * @param new user details
	 * @return return true and false;
	 */
	@Override
	public boolean userRegistred(User newUser) {
		return dao.saveUser(newUser);
	}

}
