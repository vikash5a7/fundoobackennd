package com.bridgelabz.fundoonotes.servicesimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.Collaborator;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.controller.CollabratorController;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.ICollabratorRepositary;
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

	@Autowired
	ICollabratorRepositary collabratoeRepo;

	@Override
	public NoteInformation addCollaborator(Long noteId, String email, String token) {
		LOG.trace("inside the Collabrator service of addCollarator" + email + token);
		UserInformation user = new UserInformation();
		Collaborator collaborator = new Collaborator();
		Long id;
		try {
			id = generate.parseJWT(token);
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
				collaborator.setEmail(email);
				collaborator.setId(id);
				collaborator.setNote(note);
				collabratoeRepo.saveCollabrator(collaborator);
			} else {
				throw new UserException("Not notes Present with given id" + noteId);
			}
		} else {
			throw new UserException("User Does not exit");
		}
		return null;
	}

	@Override
	public void deleteCollabotar(Long cId, String token, Long noteId) {
		LOG.trace("inside the Collabrator service of deleteCollaborar");
		UserInformation user = new UserInformation();
		Collaborator collaborator = new Collaborator();
		Long id;
		try {
			id = generate.parseJWT(token);
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
				collaborator.setId(id);
				collaborator.setNote(note);
				collabratoeRepo.saveCollabrator(collaborator);
			} else {
				throw new UserException("Not notes Present with given id" + noteId);
			}
		} else {
			throw new UserException("User Does not exit");
		}
	}

	@Override
	public List<NoteInformation> getCollabrator(String token) {
		Long id;
		UserInformation user = new UserInformation();
		LOG.trace("User is :" + user);
		try {
			id = generate.parseJWT(token);
			LOG.trace("Id of User is: " + id);
			user = userRepo.getUserById(id);
			if (user != null) {
				List<NoteInformation> list = collabratoeRepo.getCollabrator(id);
				LOG.trace("note fetched is" + " " + list.get(0));
			}
		} catch (Exception e) {
			throw new UserException("User Don't exit");
		}
		return null;
	}
}
