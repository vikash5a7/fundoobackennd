package com.bridgelabz.fundoonotes.dto;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class UserDto {
	private String name;
	private String email;
	private String password;
	private Long mobileNumber;
}
