package com.bridgelabz.usermanagements.model;

import java.sql.Timestamp;

/**
 * @author mobicomp
 *
 */
public class User {
	private int id;
	private String name;
	private String email;
	private String password;
	private String gender;
	private Timestamp dateTime;
	private String userInfo;
	private String pic;

	public User(String name, String email, String password, String gender, String userInfo) {
		super();
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.userInfo = userInfo;
	}

	public User(int id, String name, String email, String password, String gender, String userInfo) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.userInfo = userInfo;
		
	}

	public User(String name, String email, String password, String gender, Timestamp dateTime, String userInfo,
			String pic) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.gender = gender;
		this.dateTime = dateTime;
		this.userInfo = userInfo;
		this.pic = pic;
	}

	public User() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Timestamp getDateTime() {
		return dateTime;
	}

	public void setDateTime(Timestamp dateTime) {
		this.dateTime = dateTime;
	}

	public String getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(String userInfo) {
		this.userInfo = userInfo;
	}

	public String getPic() {
		return pic;
	}

	public void setPic(String pic) {
		this.pic = pic;
	}

}
