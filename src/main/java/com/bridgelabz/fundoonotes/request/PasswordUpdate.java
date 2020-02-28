package com.bridgelabz.fundoonotes.request;

import lombok.Data;

@Data
public class PasswordUpdate {
	String email;
	String newPassword;
	String confirmPassword;
}
