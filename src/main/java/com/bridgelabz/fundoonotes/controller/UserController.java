package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.request.LoginInformation;
import com.bridgelabz.fundoonotes.request.PasswordUpdate;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.responses.UsersDetailRes;
import com.bridgelabz.fundoonotes.services.UserServices;
import com.bridgelabz.fundoonotes.util.JwtGenerator;


@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {
	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);



	@Autowired
	private UserServices service;

	@Autowired
	private JwtGenerator generate;

	/**
	 * This is used for Registration purpose
	 * @param information of the user
	 * @return Response as success or fail
	 */

	@PostMapping("/user/registration")
	@ResponseBody
	public ResponseEntity<Response> registration(@RequestBody UserDto information) {
		LOG.trace("Registration Started......");
		boolean result = service.register(information);
		if (result) {
			LOG.info("User Registered SucessFully...");
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("registration successfull", 200, information));

		} else {
			LOG.info("User not registred already exit...");

			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new Response("User Already Exist", 400, information));
		}

	}
	/**
	 * This is used for the login the user based on there credentials
	 * @param loging information (user email and password)
	 * @return response and the token
	 */

	@PostMapping("/user/login")
	public ResponseEntity<UsersDetailRes> login(@RequestBody LoginInformation information) {
		LOG.trace("Login started....");
		// sending to login.....
		UserInformation userInformation = service.login(information);
		LOG.trace("inside the login controller");
		if (userInformation!=null) {
			String token=generate.jwtToken(userInformation.getUserId());
			return ResponseEntity.status(HttpStatus.ACCEPTED).header("login successfull", information.getEmail())
					.body(new UsersDetailRes(token, 200, information));
		}
		else {
			throw new UserException(" Invalide credentials");
		}
	}
	/**
	 * This is for the user verify.......
	 * @param token
	 * @return response as success and fail
	 * @throws Exception
	 */

	@GetMapping("/user/verify/{token}")
	public ResponseEntity<Response> userVerification(@PathVariable("token") String token) throws Exception {
		LOG.trace("Verifying the user based on there valid token....");
		LOG.info("token for verification" + token);
		boolean update = service.verify(token);
		if (update) {
			LOG.info("verification Done");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("verified", 200));
		} else {
			LOG.info("verification Not Done");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("not verified", 400));
		}
	}
	/**
	 * This is used for forgetting password
	 * @param email
	 * @return response exit or not
	 */

	@PostMapping("user/forgotpassword")
	public ResponseEntity<Response> forgogPassword(@RequestParam("email") String email) {
		LOG.info("email: "+email);
		LOG.trace("forget password...");
		boolean result = service.isUserExist(email);
		if (result) {
			LOG.trace("User exit....");

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("user exist", 200));
		} else {
			LOG.trace("user doesn't exit...");

			return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Response("user does not exist with given email id", 400));
		}

	}
	/**
	 * This is used for forgetting password
	 * @param email
	 * @return response exit or not
	 */

	@PutMapping("user/update/{token}")
	public ResponseEntity<Response> update(@PathVariable("token") String token, @RequestBody PasswordUpdate update) {
		LOG.trace("inside password verification controller  " +update.getConfirmPassword());
		LOG.info("verfication token " +token);

		System.out.println("inside controller  " +token);
		boolean result = service.update(update, token);
		if (result) {
			return ResponseEntity.status(HttpStatus.ACCEPTED)
					.body(new Response("password updated successfully", 200));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new Response("password doesn't match", 401));
		}

	}

	/**
	 * This is for the users from the database
	 *
	 * @return response
	 * @param nothing
	 */
	@GetMapping("user/getusers")
	@Cacheable(value="users")
	public ResponseEntity<Response> getUsers(){
		LOG.trace("inside Get all user controller  ");
		List<UserInformation> users=service.getUsers();
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(new Response("The user's are", 200, users));
	}
	/**
	 * This is used for the get one user based on there token
	 * @param token
	 * @return response
	 */

	@GetMapping("user/getOneUser")
	public ResponseEntity<Response> getOneUsers(@RequestHeader("token") String token){
		LOG.trace("inside the get one user controller");
	UserInformation user=service.getSingleUser(token);
		return ResponseEntity.status(HttpStatus.ACCEPTED)
				.body(new Response("user is", 200, user));
	}
}
