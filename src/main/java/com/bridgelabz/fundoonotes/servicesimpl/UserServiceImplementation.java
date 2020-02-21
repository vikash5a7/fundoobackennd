package com.bridgelabz.fundoonotes.servicesimpl;

import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.Entity.PasswordUpdate;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.configurations.RabbitMQSender;
import com.bridgelabz.fundoonotes.dto.UserDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.request.LoginInformation;
import com.bridgelabz.fundoonotes.responses.MailObject;
import com.bridgelabz.fundoonotes.responses.MailResponse;
import com.bridgelabz.fundoonotes.services.UserServices;
import com.bridgelabz.fundoonotes.util.JwtGenerator;
import com.bridgelabz.fundoonotes.util.MailServiceProvider;

@Service
public class UserServiceImplementation implements UserServices {

	private UserInformation userInformation= new UserInformation();

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

	

	@Transactional
	@Override
	public boolean register(UserDto information) {
		System.out.println("inside service");
		UserInformation user = repository.getUser(information.getEmail());

		if (user == null) {

			userInformation = modelMapper.map(information, UserInformation.class);
			userInformation.setCreatedDate(LocalDateTime.now());
			String epassword = encryption.encode(information.getPassword());
			userInformation.setPassword(epassword);
			userInformation.setVerified(false);
			userInformation = repository.save(userInformation);
			System.out.println("id" + " " + userInformation.getUserId());
			System.out.println("token" + " " + generate.jwtToken(userInformation.getUserId()));
			String mailResponse = response.formMessage("http://localhost:8080/verify",
					generate.jwtToken(userInformation.getUserId()));
			System.out.println(mailResponse);
			mailObject.setEmail(information.getEmail());
			mailObject.setMessage(mailResponse);
			mailObject.setSubject("verification");

			rabbitMQSender.send(mailObject);
			return true;

		} else {
			throw new UserException("user already exist with the same mail id");

		}

	}

	@Transactional
	@Override
	public UserInformation login(LoginInformation information) {
		UserInformation user = repository.getUser(information.getEmail());
		System.out.println("inside service " + user);
		if (user != null) {

			if ((user.isVerified() == true) && encryption.matches(information.getPassword(), user.getPassword())) {
				System.out.println(generate.jwtToken(user.getUserId()));
				return user;
			} else {
				String mailResponse = response.formMessage("http://localhost:3000/verify",
						generate.jwtToken(user.getUserId()));

				MailServiceProvider.sendEmail(information.getEmail(), "verification", mailResponse);

				return null;
			}

		} else {
			return null;

		}

	}

	@Transactional
	@Override
	public boolean update(PasswordUpdate information, String token) {
		if (information.getNewPassword().equals(information.getConfirmPassword())) {

			Long id = null;
			try {
				System.out.println("in update method" + "   " + generate.parseJWT(token));
				id = (long) generate.parseJWT(token);
				String epassword = encryption.encode(information.getConfirmPassword());
				information.setConfirmPassword(epassword);
				return repository.upDate(information, id);
			} catch (Exception e) {
				throw new UserException("invalid credentials");
			}

		}

		else {
			throw new UserException("invalid password");
		}

	}

	public String generateToken(Long id) {
		return generate.jwtToken(id);

	}

	@Transactional
	@Override
	public boolean verify(String token) throws Exception {
		System.out.println("id in verification" + (long) generate.parseJWT(token));
		Long id = (long) generate.parseJWT(token);
		repository.verify(id);
		return true;
	}

	@Override
	public boolean isUserExist(String email) {
		try {
			UserInformation user = repository.getUser(email);
			if (user.isVerified() == true) {
				String mailResponse = response.formMessage("http://localhost:3000/updatePassword",
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
      
	@Transactional
	@Override
	public List<UserInformation> getUsers() {
		System.out.println("inside get users");
		List<UserInformation> users = repository.getUsers();
		UserInformation user = users.get(0);
		List<NoteInformation> note = user.getColaborateNote();
		return users;
	}

	
	@Transactional
	@Override
	public UserInformation getSingleUser(String token) {
		Long id;
		try {
			 id = (long) generate.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("User doesn't exist");
		}
		
		UserInformation user=repository.getUserById(id);
		System.out.println(user.getColaborateNote().toString());
		return null;
	}

}
