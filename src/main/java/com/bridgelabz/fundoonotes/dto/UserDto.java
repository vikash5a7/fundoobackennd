package com.bridgelabz.fundoonotes.dto;

import lombok.Data;

@Data
public class UserDto {
	private String fname;
	private String lname;
	private String email;
	private String password;
	private Long mobileNumber;
}
