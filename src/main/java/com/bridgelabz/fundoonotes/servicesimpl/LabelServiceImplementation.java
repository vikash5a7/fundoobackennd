package com.bridgelabz.fundoonotes.servicesimpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.Entity.LabelInformation;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.Entity.UserInformation;
import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.exception.UserException;
import com.bridgelabz.fundoonotes.repository.IUserRepository;
import com.bridgelabz.fundoonotes.repositoryimpl.LabelRepository;
import com.bridgelabz.fundoonotes.repositoryimpl.NoteRepository;
import com.bridgelabz.fundoonotes.request.LabelUpdate;
import com.bridgelabz.fundoonotes.services.LabelService;
import com.bridgelabz.fundoonotes.util.JwtGenerator;

@Service
public class LabelServiceImplementation implements LabelService {
	@Autowired
	private LabelRepository repository;

	private LabelInformation labelInformation = new LabelInformation();

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private JwtGenerator tokenGenerator;

	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private ModelMapper modelMapper;

	/**
	 * Creating label..
	 *
	 * @param label
	 * @param toke
	 * @return Nothing
	 */

	@Transactional
	@Override
	public void createLabel(LabelDto label, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}

		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			Optional<LabelInformation> labelInfo = repository.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo.isPresent()) {
				labelInformation=modelMapper.map(label, LabelInformation.class);
				labelInformation.getLabelId();
				labelInformation.getName();
				labelInformation.setUserId(user.getUserId());
				repository.save(labelInformation);
			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
	}

	/**
	 * Creating create label and map
	 *
	 * @param label
	 * @param token
	 * @param noteId
	 */

	@Transactional
	@Override
	public void createLabelAndMap(LabelDto label, String token,Long noteId) {
		Long id = null;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("user does not exist");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			Optional<LabelInformation> labelInfo = repository.fetchLabel(user.getUserId(), label.getName());
			if (labelInfo.isPresent()) {
				BeanUtils.copyProperties(label, LabelInformation.class);
				labelInformation.setUserId(user.getUserId());
				repository.save(labelInformation);
				NoteInformation note=noteRepository.findById(noteId);
				note.getList().add(labelInformation);
				noteRepository.save(note);
			} else {
				throw new UserException("label with the given name is already present");
			}
		} else {
			throw new UserException("Note does not exist with the given id");
		}
	}

	/**
	 * Adding the label
	 *
	 * @param label  id
	 * @param noteId
	 * @param token
	 *
	 */
	@Transactional
	@Override
	public void addLabel(Long labelId, Long noteId, String token) {
		NoteInformation note = noteRepository.findById(noteId);
		Long id = null;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("user is not present ");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			LabelInformation label = repository.fetchLabelById(labelId);
		label.getList().add(note);
		repository.save(label);
		}
	}

	/**
	 * Remove label
	 *
	 * @param label id
	 * @param note  id
	 * @param token
	 */

	@Transactional
	@Override
	public void removeLabel(Long labelId, Long noteId, String token) {
		Long id = null;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("user is not present ");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			NoteInformation note = noteRepository.findById(noteId);
			LabelInformation label = repository.fetchLabelById(labelId);
			boolean remove = note.getList().remove(label);
			if (remove) {
				noteRepository.save(note);
			}
		}

	}

	/**
	 * edit label
	 *
	 * @param label
	 * @param token
	 */


	@Transactional
	@Override
	public void editLabel(LabelUpdate label, String token) {
		Long id = null;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("user is not present ");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			LabelInformation labelInfo = repository.fetchLabelById(label.getLabelId());
			if (labelInfo != null) {
				labelInfo.setName(label.getLabelName());
				repository.save(labelInfo);
			} else {
				throw new UserException("label with the given id does not exist");
			}
		} else {
			throw new UserException("user does not exist");
		}
	}

	/**
	 * Deleting label
	 *
	 * @param info
	 * @param token
	 */

	@Transactional
	@Override
	public void deleteLabel(LabelUpdate info, String token) {
		Long id = null;

		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {

			throw new UserException("User does not exist");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			LabelInformation labelInfo = repository.fetchLabelById(info.getLabelId());
			if (labelInfo != null) {
				repository.deleteLabel(info.getLabelId());
			} else {
				throw new UserException("Note does not exist");
			}
		}
	}

	/**
	 * getting all label
	 *
	 * @param token
	 */

	@Override
	public List<LabelInformation> getLabel(String token) {
		Long id;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("note does not exist");
		}
		List<LabelInformation> labels = repository.getAllLabel(id);
		return labels;

	}

	/**
	 * Getting all notes
	 *
	 * @param token
	 * @param label id
	 */
	@Override
	public List<NoteInformation> getAllNotes(String token, Long labelId) {
		Long id;
		List<NoteInformation> list = null;
		try {
			id = (long) tokenGenerator.parseJWT(token);
		} catch (Exception e) {
			throw new UserException("user is not present ");
		}
		UserInformation user = userRepository.getUserById(id);
		if (user != null) {
			LabelInformation label = repository.getLabel(labelId);
			list = label.getList();
		System.out.println("label is"+list);
		}
		return list;
	}

}
