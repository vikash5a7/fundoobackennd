package com.bridgelabz.fundoonotes.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
			@RequestParam("email") String email, @RequestHeader("token") String token) {
		LOG.trace("Inside the collabrator...");
		LOG.info("Note id and email" + email + noteId);
		collaboratorService.addCollaborator(noteId, email, token);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("collaborator added", 200, null));
	}
}
