package com.bridgelabz.fundoonotes.Entity;

import lombok.Data;

@Data
public class PasswordUpdate {
	String email;
	String newPassword;
	String confirmPassword;
}
