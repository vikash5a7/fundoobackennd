package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.request.NoteUpdation;
import com.bridgelabz.fundoonotes.request.ReminderDto;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.services.NoteService;
import com.bridgelabz.fundoonotes.services.UserServices;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080")
public class NoteController {

	private static final Logger LOG = LoggerFactory.getLogger(NoteController.class);

	@SuppressWarnings("unused")
	@Autowired
	private UserServices uservice;

	@Autowired
	private NoteService service;



	/**
	 * Creating notes..
	 *
	 * @param information
	 * @param token
	 * @return response of the nodes...created or not?
	 * @throws Exception
	 */

	@PostMapping("/note/create")
	public ResponseEntity<Response> createNote(@RequestBody NoteDto information, @RequestHeader String token)
			throws Exception {
		NoteInformation createNote = service.createNote(information, token);
		if (createNote != null) {
			LOG.info("Created Node Details" + information.getDescription());
			return ResponseEntity.status(HttpStatus.CREATED).body(new Response("note created", 200, createNote));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("not verified user", 400));
		}
	}

	/**
	 * Updating note....
	 *
	 * @param note
	 * @param token
	 * @return repose
	 * @throws Exception
	 */

	@PutMapping("/note/update")
	public ResponseEntity<Response> update(@RequestBody NoteUpdation note, @RequestHeader("token") String token)
			throws Exception {
		LOG.trace("inside update controller.... and id is of node " + note.getId());
		NoteInformation updateNote = service.updateNote(note, token);
		if (updateNote != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note updated", 200, updateNote));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Note is not updated", 400));
		}
	}

	/**
	 * Implementation of archive functionality
	 *
	 * @param id
	 * @param token
	 * @return The response of the archive
	 */
	@PostMapping("/note/archive/{id}")
	public ResponseEntity<Response> archieve(@PathVariable long id, @RequestHeader("token") String token) {

		LOG.trace("inside delete controller" + id);
		NoteInformation archievNote = service.archievNote(id, token);
		if (archievNote != null) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new Response("Note updated", 200, archievNote.isArchieved()));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Note is not updated", 400));
		}
	}

	/**
	 * Implementation of the pin based on the id
	 *
	 * @param id
	 * @param token
	 * @return response of the note
	 */

	@PostMapping("/note/pin/{id}")
	public ResponseEntity<Response> notePining(@PathVariable long id, @RequestHeader("token") String token) {
		LOG.trace("Inside the notePin and id is: " + id);
		NoteInformation notePin = service.notePin(id, token);
		if (notePin != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note updated", 200, notePin.isPinned()));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Note is not updated", 400));
		}
	}

	/**
	 * Deleting Node....
	 *
	 * @param id
	 * @param token
	 * @return response
	 */

	@DeleteMapping("/note/delete/{id}")
	public ResponseEntity<Response> delete(@PathVariable long id, @RequestHeader("token") String token) {
		LOG.trace("Inside the delete notes id: " + id);
		service.deleteNote(id, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("note deleted", 200));

	}

	/**
	 * Deleting Note Permanently
	 *
	 * @param id
	 * @param token
	 * @return
	 */

	@DeleteMapping("/note/deletePermanently/{id}")
	public ResponseEntity<Response> deletePermenently(@PathVariable long id, @RequestHeader("token") String token) {
		LOG.trace("inside delete controller" + id);
		boolean deleteNotePemenetly = service.deleteNotePemenetly(id, token);
		if (deleteNotePemenetly) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("Note updated", 200, deleteNotePemenetly));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Note is not Delete", 400));
		}
	}

	/**
	 * Fetching all nodes controller
	 *
	 * @param token
	 * @return
	 */

	@GetMapping("/note/fetchNote")
	public ResponseEntity<Response> getAllNotes(@RequestHeader("token") String token) {
		LOG.trace("inside delete controller");
		List<NoteInformation> notes = service.getAllNotes(token);
		if (notes != null)
			return ResponseEntity.status(HttpStatus.OK).body(new Response("The notes are", 200, notes));
		else
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Response("Not found", 404));
	}

	/**
	 * Fetching trashed notes controller
	 *
	 * @param token
	 * @return response
	 */
	@GetMapping("/note/fetchTrashedNote")
	public ResponseEntity<Response> getTrashedNotes(@RequestHeader("token") String token) {
		LOG.trace("inside fetchTrashedNote controller");
		List<NoteInformation> notes = service.getTrashedNotes(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("The trashed notes are", 200, notes));

	}

	/**
	 * fetching get archive notes controller
	 *
	 * @param token
	 * @return Response
	 */
	@GetMapping("/note/fetcharchivenote")
	public ResponseEntity<Response> getArchiveNote(@RequestHeader("token") String token) {
		LOG.trace("inside getArchiveNote controller");
		List<NoteInformation> notes = service.getArchiveNote(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("The archieved notes are", 200, notes));

	}

	/**
	 * fetching pinned notes controller
	 *
	 * @param token
	 * @return Response
	 */
	@GetMapping("/note/fetchpinnednote")
	public ResponseEntity<Response> getPinnedNote(@RequestHeader("token") String token) {
		LOG.trace("inside getArchiveNote controller");
		List<NoteInformation> notes = service.getAllPinnedNotes(token);
		if (notes != null) {
			return ResponseEntity.status(HttpStatus.OK).body(new Response("The pinned notes are", 200, notes));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Not found", 404));
		}

	}

	/**
	 * Adding color controller
	 *
	 * @param noteId
	 * @param colour
	 * @param token
	 * @return Response
	 */
	@PostMapping("/note/addColour")
	public ResponseEntity<Response> addColour(@RequestParam("noteId") Long noteId,
			@RequestParam("colour") String colour, @RequestHeader("token") String token) {
		NoteInformation addColour = service.addColour(noteId, token, colour);
		if(addColour!=null)
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("colour added", 200, addColour.getColour()));
		else
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Not saved", 400));
	}

	/**
	 * Remainder controller
	 *
	 * @param noteId
	 * @param token
	 * @param reminder
	 * @return Response
	 */

	@PostMapping("/note/addreminder")
	public ResponseEntity<Response> addReminder(@RequestParam("noteId") Long noteId,
			@RequestHeader("token") String token, @RequestBody ReminderDto reminder) {
		LOG.trace("inside addReminder controller");
		NoteInformation addReminderInfo = service.addReminder(noteId, token, reminder);
		if (addReminderInfo != null) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("Reminder added", 200, reminder.getReminder()));

		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Response("Not added", 400));
		}
	}

	/**
	 * Remove reminder controller
	 *
	 * @param noteId
	 * @param token
	 * @return Response
	 */

	@PostMapping("/note/removereminder")
	public ResponseEntity<Response> removeReminder(@RequestParam("noteId") Long noteId,
			@RequestHeader("token") String token) {
		LOG.trace("inside removeReminder controller");
		service.removeReminder(noteId, token, null);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("Reminder removed", 200, noteId));

	}

	/**
	 * Searching controller
	 *
	 * @param title
	 * @param token
	 * @return response
	 */
	@GetMapping("/note/search")
	public ResponseEntity<Response> search(@RequestParam("title") String title, @RequestHeader("token") String token) {
		LOG.trace("inside search controller");
		List<NoteInformation> notes = service.searchNotesByTitle(title, token);
		if (notes != null) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(new Response("The note you are looking for is", 200, notes));
		}
		else {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new Response("Couldn't get note with given title", 404, title));
		}
	}
}
