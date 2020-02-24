package com.bridgelabz.usermanagements.config;

public class DBconfig {

	private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/vicky";
	private static final String DATABSE_USER_NAME = "root";
	private static final String DATABASE_PASSWORD = "VIKASkumar@123";
	private static final String DRIVER_NAME = "com.mysql.cj.jdbc.Driver";
	private static final String QUERRY = "insert into user(name,email,password, gender, userInfo) values(?,?,?,?,?)";

	public static String getQuerry() {
		return QUERRY;
	}

	public static String getDatabaseUrl() {
		return DATABASE_URL;
	}

	public static String getDatabseUserName() {
		return DATABSE_USER_NAME;
	}

	public static String getDatabasePassword() {
		return DATABASE_PASSWORD;
	}

	public static String getDriverName() {
		return DRIVER_NAME;
	}
}
