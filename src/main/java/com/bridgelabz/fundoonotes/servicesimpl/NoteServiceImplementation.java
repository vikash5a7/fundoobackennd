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
import com.bridgelabz.fundoonotes.redishrepo.RedisRepositary;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.repositoryimpl.NoteRepository;
import com.bridgelabz.fundoonotes.request.NoteUpdation;
import com.bridgelabz.fundoonotes.request.ReminderDto;
import com.bridgelabz.fundoonotes.services.ElasticSearchService;
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
	private ElasticSearchService elasticService;

	@Autowired
	private NoteRepository noteRepository;

	private NoteInformation noteinformation = new NoteInformation();
	@Autowired
	private RedisRepositary redisRepo;


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
	public NoteInformation createNote(NoteDto information, String token) {
		NoteInformation saveNote;
		try {
			LOG.trace("inside NoteServiceImplementation of createNote ");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User createNote Id: "+ userid);
			user = repository.getUserById(userid);
			LOG.info("inside service.. User details are " + user);
			if (user != null) {
				noteinformation = modelMapper.map(information, NoteInformation.class);
				noteinformation.setCreatedDateAndTime(LocalDateTime.now());
				noteinformation.setColour("white");
				user.getNote().add(noteinformation);
				saveNote = noteRepository.save(noteinformation);
//
//				if (saveNote != null) {
//					redisRepo.addItem(noteinformation);
//					System.out.println("inserted");
//				} else {
//					System.out.println("Something went wrong");
//				}
				if (saveNote != null) {
					String createNote = elasticService.createNote(saveNote);
					LOG.info("Create Node" + createNote);
				}
			} else {
				throw new UserException("note is not present with the given id ");}
		}catch (Exception e) {
			throw new UserException("user is not present with the given id ");
		}
		return saveNote;
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
	public NoteInformation updateNote(NoteUpdation information, String token) {
		NoteInformation saveInfo;
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
				saveInfo = noteRepository.save(note);
				if (saveInfo != null) {
					elasticService.updateNote(information.getId());
					LOG.trace("Note Update in elastic..");
				}
			} else {
				throw new UserException("Note is not present");
			}

		} catch (Exception e) {
			throw new UserException("User is not present");
		}
		return saveInfo;
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
	public void deleteNote(long noteId, String token) {
		LOG.trace("inside NoteServiceImplementation of deleteNote ");
		try {
			LOG.trace("inside NoteServiceImplementation of updateNote ");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User Id: "+userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				note.setTrashed(!note.isTrashed());
				noteRepository.save(note);
				elasticService.updateNote(noteId);
			}
			else {
				throw new Exception("Note is note present");
			}
		}catch(Exception e){
			LOG.warn("User Note Found with this user token.." + token);
		}
	}

	/**
	 * Archive function is implemented
	 *
	 * @param id of the notes and token
	 * @return nothing
	 */

	@Transactional
	@Override
	public NoteInformation archievNote(long noteId, String token) {
		LOG.trace("inside NoteServiceImplementation of archieveNote ");
		NoteInformation arInformation;
		try {
			LOG.trace("inside NoteServiceImplementation of archievNote ");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User Id: " + userid);
		} catch (Exception e) {
			throw new UserException("user is not present");
		}
		LOG.info("Id of the archive is: " + noteId);
		NoteInformation note = noteRepository.findById(noteId);
		if (note != null) {
		note.setArchieved(!note.isArchieved());
			arInformation = noteRepository.save(note);
			elasticService.updateNote(noteId);
		} else {
			throw new UserException("Note is not presented with given id: " + noteId);
		}
		return arInformation;
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
	public NoteInformation notePin(long noteId, String token) {
		NoteInformation notePinInfo;
		try {
			LOG.trace("Inside the Note Service Implemnetation pin..");
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("User Id: " + userid);
		} catch (Exception e) {
			throw new UserException("User is not present");
		}
		NoteInformation note = noteRepository.findById(noteId);
		if (note != null) {
		note.setPinned(!note.isPinned());
			notePinInfo = noteRepository.save(note);
		elasticService.updateNote(noteId);
		} else {
			throw new UserException("Note is note present with given Note id in this user" + noteId + note);
		}
		return notePinInfo;
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
	public boolean deleteNotePemenetly(long noteId, String token) {
		LOG.trace("Inside the Note Service deleteNotePemenetly ..");
		try {
			Long userid = (long) tokenGenerator.parseJWT(token);
			LOG.trace("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				List<LabelInformation> labels = note.getList();
				if(labels!=null) {
				labels.clear();
				}
				noteRepository.deleteNote(noteId, userid);
				elasticService.deleteNote(noteId);
				LOG.trace("Delete..");
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
				List<NoteInformation> list = noteRepository.getTrashedNotes(userId);
				LOG.trace("note fetched is" + " " + list.get(0));
				return list;
			} else {
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
	public NoteInformation addColour(Long noteId, String token, String colour) {
		LOG.trace("Inside the Note Service addColour ..");
		NoteInformation noteInfoColour;
		Long userid;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
				note.setColour(colour);
			noteInfoColour = noteRepository.save(note);
			elasticService.updateNote(noteId);
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
		return noteInfoColour;
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
	public NoteInformation addReminder(Long noteId, String token, ReminderDto reminder) {
		LOG.trace("Inside the Note Service addReminder ..");
		Long userid;
		NoteInformation noteInfoRemainder;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				note.setReminder(reminder.getReminder());
				noteInfoRemainder = noteRepository.save(note);
				elasticService.updateNote(noteId);
			} else {
				LOG.warn("user is not valid:");
				throw new UserException("note doesn't exist");
			}
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
		return noteInfoRemainder;
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
	public NoteInformation removeReminder(Long noteId, String token, ReminderDto reminder) {
		LOG.trace("Inside the Note Service removeReminder ..");
		Long userid;
		NoteInformation noteInformation;
		try {
			userid = (long) tokenGenerator.parseJWT(token);
			LOG.info("user id" + " " + userid);
			NoteInformation note = noteRepository.findById(noteId);
			if (note != null) {
				note.setReminder(LocalDateTime.now());
				noteInformation = noteRepository.save(note);
				elasticService.updateNote(noteId);
			} else {
				LOG.warn("user is not valid:");
				throw new UserException("note doesn't exist");
			}
		} catch (Exception e) {
			LOG.warn("user is not valid:");
			throw new UserException("authentication failed");
		}
		return noteInformation;
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

	@Override
	public NoteInformation getSingleNoteById(long noteId) {
		NoteInformation note = elasticService.findById(noteId);
		if (note != null) {
			return note;
		} else {
			throw new UserException("Note not found");
		}

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

		List<NoteInformation> notes = elasticService.searchbytitle(title);
		if (notes != null) {
			return notes;
		}
		else {
			return null;
		}
	}
//		try
//		{
//			Long userId = tokenGenerator.parseJWT(token);
//			UserInformation user = repository.getUserById(userId);
//			LOG.info("user :" + user);
//			if (user != null) {
//				LOG.info("user logged in" + user.getUserId());
//				List<NoteInformation> list11 = noteRepository.getNotes(userId);
//				if (list11 != null) {
//					LOG.info("" + list11);
//					return list11.stream().filter(note -> note.getTitle().equalsIgnoreCase(title))
//							.collect(Collectors.toList());
//				}
//			}
//		}
//		catch (Exception e) {
//			LOG.warn("error " + e);
//			LOG.info("user not exit or authentcation fail");
//		}
//		return null;


	@Override
	public List<NoteInformation> searchByTitle(String title, String token) {
		// TODO Auto-generated method stub
		return null;
	}

}
