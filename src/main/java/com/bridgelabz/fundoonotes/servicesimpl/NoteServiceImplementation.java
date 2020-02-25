package com.bridgelabz.fundoonotes.servicesimpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.LabelInformation;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.repositoryimpl.NoteRepository;
import com.bridgelabz.fundoonotes.request.NoteUpdation;
import com.bridgelabz.fundoonotes.request.ReminderDto;
import com.bridgelabz.fundoonotes.services.NoteService;
import com.bridgelabz.fundoonotes.util.JwtGenerator;

@Service
public class NoteServiceImplementation implements NoteService {
	private static final Logger LOG = LoggerFactory.getLogger(NoteServiceImplementation.class);
	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private IUserRepository repository;

	private UserInformation user = new UserInformation();

	@Autowired
	private NoteRepository noteRepository;

	private NoteInformation noteinformation = new NoteInformation();


	@Autowired
	private ModelMapper modelMapper;


	/**
	 * Creating note
	 *
	 * @param information and token
	 * @return repose as created or not.
	 */
	@Transactional
	@Override
	public void createNote(NoteDto information, String token) {
		try {
			LOG.trace("inside NoteServiceImplementation of createNote ");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User createNote Id: "+ userid);
			user = repository.getUserById(userid);
			LOG.info("inside service.. User details are " + user);
			if (user != null) {
				noteinformation = modelMapper.map(information, NoteInformation.class);
				noteinformation.setCreatedDateAndTime(LocalDateTime.now());
				noteinformation.setArchieved(false);
				noteinformation.setPinned(false);
				noteinformation.setTrashed(false);
				noteinformation.setColour("white");
				user.getNote().add(noteinformation);
				noteRepository.save(noteinformation);
			} else {
				throw new UserException("note is not present with the given id ");}
		}catch (Exception e) {
			throw new UserException("user is not present with the given id ");
		}

	}

	/**
	 * Updating the note
	 *
	 * @param information and token....
	 * @param token
	 * @return nothing
	 */
	@Transactional
	@Override
	public void updateNote(NoteUpdation information, String token) {
		try {
			LOG.trace("inside NoteServiceImplementation of updateNote ");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User Id: "+userid);
			NoteInformation note = noteRepository.findById(information.getId());
			if (note != null) {
				LOG.info("Note is " + note);
				note.setId(information.getId());
				note.setDescription(information.getDescription());
				note.setTitle(information.getTitle());
				note.setPinned(information.isPinned());
				note.setArchieved(information.isArchieved());
				note.setArchieved(information.isTrashed());
				note.setUpDateAndTime(LocalDateTime.now());
				noteRepository.save(note);
			} else {
				throw new UserException("Note is not present");
			}

		} catch (Exception e) {
			throw new UserException("User is not present");
		}
	}

	/**
	 * Deleting notes and putting into trash
	 *
	 * @param id
	 * @param token
	 * @return nothing
	 */

	@Transactional
	@Override
	public void deleteNote(long id, String token) {
		LOG.trace("inside NoteServiceImplementation of deleteNote ");
		NoteInformation note = noteRepository.findById(id);
		note.setTrashed(!note.isTrashed());
		noteRepository.save(note);

	}

	/**
	 * Archive function is implemented
	 *
	 * @param id of the notes and token
	 * @return nothing
	 */

	@Transactional
	@Override
	public void archievNote(long id, String token) {
		LOG.trace("inside NoteServiceImplementation of archieveNote ");
		LOG.info("Id of the archive is: " + id);
		NoteInformation note = noteRepository.findById(id);
		if (note != null) {
		note.setArchieved(!note.isArchieved());
		noteRepository.save(note);
		} else {
			throw new UserException("Note is not presented with given id: " + id);
		}
	}

	/**
	 * Note pin functionality
	 *
	 * @param id
	 * @param token
	 * @return nothing
	 */
	@Transactional
	@Override
	public void notePin(long id, String token) {
		LOG.trace("Inside the Note Service Implemnetation pin..");
		NoteInformation note = noteRepository.findById(id);
		note.setPinned(!note.isPinned());
		noteRepository.save(note);
	}

	/**
	 * Delete Note permanently implements
	 *
	 * @param id
	 * @param token
	 * @return boolean delete or not,
	 */

	@Transactional
	@Override
	public boolean deleteNotePemenetly(long id, String token) {
		LOG.trace("Inside the Note Service deleteNotePemenetly ..");
		try {
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.trace("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(id);
			if (note != null) {
				List<LabelInformation> labels = note.getList();
				if(labels!=null) {
				labels.clear();
				}
				noteRepository.deleteNote(id, userid);
			} else {
				throw new UserException("note is not present");
			}
		}
		catch (Exception e) {
			throw new UserException("user is not present");
		}
		return false;
	}

	/**
	 * List of Notes getting
	 *
	 * @param Token
	 * @return list of user
	 */

	@Override
	@Transactional
	public List<NoteInformation> getAllNotes(String token) {
		LOG.trace("Inside the Note Service getAllNotes ..");

		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);
			if (user != null) {
				System.out.println("user logged in"+user.getUserId());
				System.out.println("user ");
				List<NoteInformation> list11 = noteRepository.getNotes(userId);
			List<NoteInformation> collaboratedNotes=	user.getColaborateNote();
			if(collaboratedNotes!=null) {
			list11.addAll(collaboratedNotes);
			}
				System.out.println(list11.get(0));
				return list11;

			} else {
				throw new UserException("note doesn't exist");
			}
		} catch (Exception e) {
			throw new UserException("error occured");
		}
	}

	/**
	 * getting trashed notes
	 *
	 * @param token
	 * @return list of node or null
	 */
	@Override
	public List<NoteInformation> getTrashedNotes(String token) {
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			user = repository.getUserById(userId);
			if (user != null) {
				System.out.println(user);
				List<NoteInformation> list = noteRepository.getTrashedNotes(userId);
				System.out.println("note fetched is" + " " + list.get(0));
				return list;
			} else {
				System.out.println(user + "hello");
				throw new UserException("note does not exist");
			}
		} catch (Exception e) {
			throw new UserException("error occured");
		}

	}

	/**
	 * Getting archive notes
	 *
	 * @param token
	 * @return list of archive notes
	 */

	@Override
	public List<NoteInformation> getArchiveNote(String token) {
		LOG.trace("Inside the Note Service getArchiveNote ..");
		try {
			Long userId = (long) tokenGenerator.parseJWT(token);
			LOG.info("");
			user = repository.getUserById(userId);
			if (user != null) {
				LOG.info("User is : " + user);
				List<NoteInformation> list = noteRepository.getArchiveNotes(userId);
				LOG.info("note fetched is" + " " + list.get(0).toString());
				return list;
			} else {
				LOG.warn("user is not valid:");
				throw new UserException("Note doesn't exist or user not exit");
			}
		} catch (Exception e) {
			throw new UserException("error occured");
		}

	}

	/**
	 * Adding specific color to the nodes
	 *
	 * @param noteId
	 * @param token
	 * @return color
	 * @return nothing
	 */

	@Transactional
	@Override
	public void addColour(Long noteId, String token, String colour) {
		LOG.trace("Inside the Note Service addColour ..");
		Long userid;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
				note.setColour(colour);
				noteRepository.save(note);
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
	}

	/**
	 * Adding remainder to the notes
	 *
	 * @param noteId
	 * @param token
	 * @param time
	 * @return nothing
	 */

	@Transactional
	@Override
	public void addReminder(Long noteId, String token, ReminderDto reminder) {
		LOG.trace("Inside the Note Service addReminder ..");
		Long userid;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				note.setReminder(reminder.getReminder());
				noteRepository.save(note);
			} else {
				LOG.warn("user is not valid:");
				throw new UserException("note doesn't exist");
			}
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
	}

	/**
	 * removeReminderto the notes
	 *
	 * @param noteId
	 * @param token
	 * @param time
	 * @return nothing
	 */
	@Override
	public void removeReminder(Long noteId, String token, ReminderDto reminder) {
		LOG.trace("Inside the Note Service removeReminder ..");
		Long userid;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				note.setReminder(LocalDateTime.now());
				noteRepository.save(note);
			} else {
				LOG.warn("user is not valid:");
				throw new UserException("note doesn't exist");
			}
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
	}

	/**
	 * getAllPinnedNotes
	 *
	 * @param token
	 * @return list of pinned notes
	 */
	@Override
	@Transactional
	public List<NoteInformation> getAllPinnedNotes(String token) {
		LOG.trace("Inside the Note Service getAllPinnedNotes ..");
		Long userId;
		List<NoteInformation> allNotes;
		try {
			userId = (long) tokenGenerator.parseJWT(token);
			LOG.info("User Id: " + userId);
			user = repository.getUserById(userId);
			LOG.info("user id" + " " + user);
			if (user != null) {
				LOG.info("user logged in" + user.getUserId());
				List<NoteInformation> list11 = noteRepository.getNotes(userId);
			  if(list11!=null) {
			 allNotes=list11.stream().filter(note -> note.isPinned()==true).collect(Collectors.toList());
				  return allNotes;
			  }
			}
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("error occured");
		}
		return null;

	}

	/**
	 * Searching notes Based on there title
	 *
	 * @param title
	 * @param token
	 * @return list of notes
	 */

	@Override
	public List<NoteInformation> searchNotesByTitle(String title, String token) {
		LOG.trace("Inside the Note Service searchByTitle ..");
		try
		{
			Long userId = tokenGenerator.parseJWT(token);
			UserInformation user = repository.getUserById(userId);
			LOG.info("user :" + user);
			if (user != null) {
				LOG.info("user logged in" + user.getUserId());
				List<NoteInformation> list11 = noteRepository.getNotes(userId);
				if (list11 != null) {
					LOG.info("" + list11);
					return list11.stream().filter(note -> note.getTitle().equalsIgnoreCase(title))
							.collect(Collectors.toList());
				}
			}
		}
		catch (Exception e) {
			LOG.warn("error " + e);
			LOG.info("user not exit or authentcation fail");
		}
		return null;
	}

	@Override
	public List<NoteInformation> searchByTitle(String title, String token) {
		// TODO Auto-generated method stub
		return null;
	}
}
