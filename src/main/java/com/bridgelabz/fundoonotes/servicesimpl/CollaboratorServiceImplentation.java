package com.bridgelabz.fundoonotes.servicesimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.controller.CollabratorController;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repositoryimpl.NoteRepository;
import com.bridgelabz.fundoonotes.repositoryimpl.UserRepositoryImplementation;
import com.bridgelabz.fundoonotes.services.CollaboratorService;
import com.bridgelabz.fundoonotes.util.JwtGenerator;

@Service
public class CollaboratorServiceImplentation implements CollaboratorService {
	private static final Logger LOG = LoggerFactory.getLogger(CollabratorController.class);

	@Autowired
	private JwtGenerator generate;
	@Autowired
	UserRepositoryImplementation userRepo;
	@Autowired
	NoteRepository noteRepository;


	@Override
	public NoteInformation addCollaborator(Long noteId, String email, String token) {
		LOG.trace("inside the Collabrator service of addCollarator" + email + token);
		UserInformation user = new UserInformation();
		try {
			Long id = generate.parseJWT(token);
			user = userRepo.getUserById(id);
		} catch (Exception e) {
			throw new UserException("User Don't exit");
		}
		if (user != null) {
			LOG.info("Main User is : " + user);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				LOG.trace("Note is " + note);
				note.getColabUser().add(user);
			} else {
				throw new UserException("Not notes Present with given id" + noteId);
			}
		} else {
			throw new UserException("User Does not exit");
		}
		return null;
	}
}
