package com.bridgelabz.fundoonotes.servicesimpl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.request.LoginInformation;
import com.bridgelabz.fundoonotes.request.PasswordUpdate;
import com.bridgelabz.fundoonotes.responses.MailObject;
import com.bridgelabz.fundoonotes.responses.MailResponse;
import com.bridgelabz.fundoonotes.services.UserServices;
import com.bridgelabz.fundoonotes.util.JwtGenerator;
import com.bridgelabz.fundoonotes.util.MailServiceProvider;
import com.bridgelabz.fundoonotes.util.RabbitMQSender;

@Service
public class UserServiceImplementation implements UserServices {

	private UserInformation userInformation= new UserInformation();
	private static final Logger LOG = LoggerFactory.getLogger(UserServiceImplementation.class);

	@Autowired
	private IUserRepository repository;

	@Autowired
	private BCryptPasswordEncoder encryption;

	@Autowired
	private JwtGenerator generate;

	@Autowired
	private MailResponse response;

	@Autowired
	private MailObject mailObject;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private RabbitMQSender rabbitMQSender;

	/**
	 * This is for Storing the informing of the user in data
	 * @param user information
	 * @return it's return true and false
	 * if user get registered then it's responsible for saving the data in data
	 *
	 */

	@Transactional
	@Override
	public boolean register(UserDto information) {
		LOG.trace("Inside userRegitration Service");
		UserInformation user = repository.getUser(information.getEmail());

		if (user == null) {
			userInformation = modelMapper.map(information, UserInformation.class);
			userInformation.setCreatedDate(LocalDateTime.now());
			String epassword = encryption.encode(information.getPassword());
			// setting the some extra information and encrypting the password
			userInformation.setPassword(epassword);
			userInformation.setVerified(false);
			// calling the save method
			userInformation = repository.save(userInformation);
			LOG.trace("id" + " " + userInformation.getUserId());
			LOG.trace("token" + " " + generate.jwtToken(userInformation.getUserId()));
			String mailResponse = response.formMessage("http://localhost:8080/user/verify", generate.jwtToken(userInformation.getUserId()));
			// setting the data to mail
			System.out.println(mailResponse);
			mailObject.setEmail(information.getEmail());
			mailObject.setMessage(mailResponse);
			mailObject.setSubject("Verification");
			rabbitMQSender.send(mailObject);
			return true;
		} else {
			throw new UserException("user already exist with the same mail id");

		}

	}

	/**
	 * This is responsible to handle the login if user is verify then only user can
	 * login if user is not verify then it's will send a link to to verify
	 *
	 * @param Login information
	 * @return null
	 */

	@Transactional
	@Override
	public UserInformation login(LoginInformation information) {
		LOG.trace("Inside Login Service");
		UserInformation user = repository.getUser(information.getEmail());
		LOG.info("user info.. " + user);
		if (user != null) {
			if ((user.isVerified() == true)) {
				if (encryption.matches(information.getPassword(), user.getPassword())) {
				System.out.println(generate.jwtToken(user.getUserId()));
				return user;
				} else {
					throw new UserException("Invalid password");
				}
			} else {
				String mailResponse = response.formMessage("http://localhost:8080/user/verify",
						generate.jwtToken(user.getUserId()));
				MailServiceProvider.sendEmail(information.getEmail(), "verification", mailResponse);
				throw new UserException("Please verify Your email id");
			}
		} else {
			throw new UserException("User Not present enter valid your email id");
		}
	}

	/**
	 * if user is not verify then it's will send a link to to verify
	 * @param PasswordUpdate information and token
	 * @return true and false
	 *
	 */
	@Transactional
	@Override
	public boolean update(PasswordUpdate information, String token) {
		LOG.trace("Inside Password Service");
		LOG.info("user Deatils are" + information);
		if (information.getNewPassword().equals(information.getConfirmPassword())) {
			LOG.trace("Password Matches..");
			Long id = null;
			try {
				LOG.info("in update method" + "   " + generate.parseJWT(token));
				id = (long) generate.parseJWT(token);
				UserInformation UpdateUser = repository.getUser(information.getEmail());
				if (id == UpdateUser.getUserId()) {
					String epassword = encryption.encode(information.getConfirmPassword());
					information.setConfirmPassword(epassword);
					return repository.upDate(information, id);
				} else {
					throw new UserException("Please Enter valid Email ");
				}
			} catch (Exception e) {
				throw new UserException("invalid credentials");}
		}
		else {
			System.out.println("Password Not match");
			throw new UserException("invalid password");
		}
	}

	/**
	 * Generating the token
	 *
	 * @param id
	 * @return generated token
	 */

	public String generateToken(Long id) {
		LOG.trace("Inside Generate password Service");
		return generate.jwtToken(id);

	}
	/**
	 * Verifying the user based on there token
	 * @param id
	 * @return generated token
	 */

	@Transactional
	@Override
	public boolean verify(String token) throws Exception {
		LOG.trace("Inside verify token Service");
		LOG.info("id in verification" + generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		repository.verify(id);
		return true;
	}

	/**
	 * checking the user is present or or not if present then it's will send a email
	 * to verify
	 *
	 * @param email
	 * @return boolean value
	 */
	@Override
	public boolean isUserExist(String email) {
		LOG.trace("Inside cheching user is exit or not ");
		try {
			UserInformation user = repository.getUser(email);
			if (user.isVerified() == true) {
				String mailResponse = response.formMessage("http://localhost:4200/update-password",
						generate.jwtToken(user.getUserId()));
				MailServiceProvider.sendEmail(user.getEmail(), "verification", mailResponse);
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new UserException("User doesn't exist");
		}
	}

	/**
	 * getting all the user
	 *
	 * @return list of the users from the database
	 */
	@Transactional
	@Override
	public List<UserInformation> getUsers() {
		LOG.trace("Inside all get user  Service");
		List<UserInformation> users = repository.getUsers();
		UserInformation user = users.get(0);
		user.getColaborateNote();
		return users;
	}

	/**
	 * by this we can get the single user
	 *
	 * @param it's taking the token
	 * @return returning the single user
	 */
	@Transactional
	@Override
	public UserInformation getSingleUser(String token) {
		LOG.trace("Inside getting single user service");
		Long id;
		try {
			 id = (long) generate.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("User doesn't exist");}
		UserInformation user=repository.getUserById(id);
		LOG.info(user.getColaborateNote().toString());
		return user;
	}

}
