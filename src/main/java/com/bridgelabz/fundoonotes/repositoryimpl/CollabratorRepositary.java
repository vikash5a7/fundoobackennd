package com.bridgelabz.fundoonotes.repositoryimpl;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.Entity.Collaborator;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.controller.CollabratorController;
import com.bridgelabz.fundoonotes.repository.ICollabratorRepositary;

@Repository
public class CollabratorRepositary implements ICollabratorRepositary {
	private static final Logger LOG = LoggerFactory.getLogger(CollabratorController.class);

	@Autowired
	private EntityManager entityManager;

	@Override
	public Collaborator saveCollabrator(Collaborator collaborator) {
		LOG.trace("inside the collabratore repo..." + collaborator.getEmail());
		Session session = entityManager.unwrap(Session.class);
		session.save(collaborator);
		LOG.info("Collabrator is: " + collaborator);
		return collaborator;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<NoteInformation> getCollabrator(Long id) {
		LOG.trace("Inside the get collabratore repo ");
		Session session = entityManager.unwrap(Session.class);

		@SuppressWarnings("rawtypes")
		List resultList = session.createQuery("from NoteInformation where noteInformationId=:id")
				.getResultList();
		LOG.info("List are" + resultList);
		return resultList;
	}
}
