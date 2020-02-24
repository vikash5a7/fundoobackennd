package com.bridgelabz.usermanagements.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.bridgelabz.usermanagements.config.DBconfig;
import com.bridgelabz.usermanagements.model.User;

public class UserDao {
	static final Logger LOGGER = Logger.getLogger(UserDao.class);

	private Connection con;

	/**
	 * constructor using
	 * 
	 * @param con
	 */
	public UserDao(Connection con) {
		this.con = con;
	}

	/**
	 * method to insert the user..
	 * 
	 * @param user
	 * @return boolean value
	 */

	public boolean saveUser(User user) {
		boolean f = false;

		try {

			PreparedStatement ptmStatements = this.con.prepareStatement(DBconfig.getQuerry());
			ptmStatements.setString(1, user.getName());
			ptmStatements.setString(2, user.getEmail());
			ptmStatements.setString(3, user.getPassword());
			ptmStatements.setString(4, user.getGender());
			ptmStatements.setString(5, user.getUserInfo());

			ptmStatements.executeUpdate();
			f = true;

		} catch (Exception e) {
			LOGGER.error(e);
		}
		return f;

	}

	/**
	 * get user by email and password
	 * 
	 * @param email
	 * @param password
	 * @return
	 */
	public User getUserByEmailAndPassword(String email, String password) {
		User user = null;
		try {
			String q = "select * from user where email=? and password=? ";
			PreparedStatement pts = con.prepareStatement(q);
			pts.setString(1, email);
			pts.setString(2, password);
			ResultSet set = pts.executeQuery();
			if (set.next()) {
				user = new User();
				user.setId(set.getInt("id"));
				user.setName(set.getString("name"));
				user.setEmail(set.getString("email"));
				user.setPassword(set.getString("password"));
				user.setGender(set.getString("gender"));
				user.setUserInfo(set.getString("userInfo"));
				user.setPic(set.getString("pPic"));
				user.setDateTime(set.getTimestamp("rdate"));

			}
		} catch (SQLException e) {
			LOGGER.error(e);
		}

		return user;

	}

}
