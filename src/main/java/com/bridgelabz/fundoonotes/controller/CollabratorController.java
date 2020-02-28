package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.request.CollabratoreReq;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.services.CollaboratorService;

@Controller
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080")
public class CollabratorController {
	private static final Logger LOG = LoggerFactory.getLogger(CollabratorController.class);

	@Autowired
	private CollaboratorService collaboratorService;

	@PostMapping("/addCollabrator")
	public ResponseEntity<Response> addCollaborator(@RequestParam("noteId") Long noteId,
			@RequestBody CollabratoreReq collabratoreReq, @RequestHeader("token") String token) {
		LOG.trace("Inside the collabrator...");
		LOG.info("Note id and email" + collabratoreReq.getEmail() + noteId);
		collaboratorService.addCollaborator(noteId, collabratoreReq.getEmail(), token);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("collaborator added", 200, null));
	}

	@PostMapping("/getcollabrator")
	public ResponseEntity<Response> getCollabrator(@RequestHeader("token") String token) {
		LOG.trace("Inside the get collabrator...");
		List<NoteInformation> note = collaboratorService.getCollabrator(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("collaborator note are: ", 200, note));
	}

	@PostMapping("/delcollabrator")
	public ResponseEntity<Response> deleteCollaborator(@RequestParam("Collbrator Id") Long cId,
			@RequestHeader("token") String token, @RequestParam("noteId") Long noteId) {
		LOG.trace("Inside the delcollabrator...");
		collaboratorService.deleteCollabotar(cId, token, noteId);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("Collabrator Deleted", 200, null));

	}

}
