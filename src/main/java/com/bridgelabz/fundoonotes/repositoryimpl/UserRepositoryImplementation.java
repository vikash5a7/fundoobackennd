package com.bridgelabz.fundoonotes.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.Entity.PasswordUpdate;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.repository.IUserRepository;

@Repository
public class UserRepositoryImplementation implements IUserRepository {

	private static final Logger LOG = LoggerFactory.getLogger(UserRepositoryImplementation.class);

	@Autowired
	private EntityManager entityManager;


	/**
	 *  This is for saving the data in database
	 *  @param All the user information
	 *  @return {@link UserInformation}
	 */
	@Override
	public UserInformation save(UserInformation userInformation) {
		LOG.trace("inside the save repo..");

		Session session = entityManager.unwrap(Session.class);
		session.saveOrUpdate(userInformation);
		return userInformation;

	}
	/**
	 * Getting data from the database based on there email
	 * @param email
	 * @return unique result of users details
	 */

	@Override
	public UserInformation getUser(String email) {
		LOG.trace("inside the getUser repo..");

		Session session = entityManager.unwrap(Session.class);
		@SuppressWarnings("rawtypes")
		Query q = session.createQuery(" FROM UserInformation where email=:email");
		q.setParameter("email", email);
		return (UserInformation) q.uniqueResult();

	}

	/**
	 * Getting the userInformation based on there Id
	 * @return user details
	 * @param id of the user
	 */
	@Override
	public UserInformation getUserById(Long id) {
		LOG.trace("inside the getUserById repo..");

		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery(" FROM UserInformation where id=:id");
		q.setParameter("id", id);
		return (UserInformation) q.uniqueResult();

	}
	/**
	 *  Here updating password
	 *  @param password and id
	 *  @return true and false
	 */

	@Override
	public boolean upDate(PasswordUpdate information, Long id) {

		LOG.trace("inside the upDate repo..");
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("update UserInformation set password=:p" + " " + " " + "where id=:i");
		q.setParameter("p", information.getConfirmPassword());
		q.setParameter("i", id);

		int status = q.executeUpdate();
		if (status > 0) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * verifying the user
	 * @param id
	 * @return boolean value..
	 *
	 */

	@Override
	public boolean verify(Long id) {
		LOG.trace("inside the verify repo..");
		Session session = entityManager.unwrap(Session.class);
		Query q = session.createQuery("update UserInformation set is_verified=:p" + " " + " " + "where id=:i");
		q.setParameter("p", true);
		q.setParameter("i", id);

		int status = q.executeUpdate();
		if (status > 0) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * Getting all the users at one time from the database
	 * @param
	 * @return all the users from the database
	 */
	@Override
	public List<UserInformation> getUsers() {
		LOG.trace("inside the verify getUsers..");
		Session currentsession = entityManager.unwrap(Session.class);
		List<UserInformation> usersList = currentsession.createQuery("from UserInformation").getResultList();
		return  usersList;
	}
}
