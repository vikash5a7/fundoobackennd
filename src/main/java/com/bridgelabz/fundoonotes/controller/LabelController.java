package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.fundoonotes.Entity.LabelInformation;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.request.LabelUpdate;
import com.bridgelabz.fundoonotes.responses.Response;
import com.bridgelabz.fundoonotes.services.LabelService;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:8080")
public class LabelController {
	private static final Logger LOG = LoggerFactory.getLogger(LabelController.class);

	@Autowired
	private LabelService service;

	/**
	 * creating Label controller...
	 *
	 * @param label
	 * @param token
	 * @return Response
	 */

	@PostMapping("/label/create")
	public ResponseEntity<Response> createLabel(@RequestBody LabelDto label, @RequestHeader("token") String token) {
		LOG.trace("Create lable controller...");
		LOG.trace(label.getName());
		service.createLabel(label, token);
		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("label created", 200, label));
	}

	/**
	 * Create label and mapping
	 *
	 * @param label
	 * @param token
	 * @param noteId
	 * @return response
	 */


	@PostMapping("/label/createandmap")
	public ResponseEntity<Response> createLabelAndMap(@RequestBody LabelDto label, @RequestHeader("token") String token,@RequestParam("noteId") Long noteId) {
		LOG.trace("Create createLabelAndMap controller...");
		LOG.trace("label is" + label.getName());
		System.out.println("note id is"+noteId);
		service.createLabelAndMap(label, token, noteId);

		return ResponseEntity.status(HttpStatus.CREATED).body(new Response("label created", 200, label));
	}

	/**
	 * adding label controller
	 *
	 * @param labelId
	 * @param token
	 * @param noteId
	 * @return response
	 */

	@PostMapping("/label/addlabel")
	public ResponseEntity<Response> addLabel(@RequestParam("labelId") Long labelId,
			@RequestHeader("token") String token, @RequestParam("noteId") Long noteId) {
		LOG.trace("Create addLabel controller...");
		System.out.println(labelId);
		service.addLabel(labelId, noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label added", 200));

	}

	/**
	 * removing label
	 *
	 * @param labelId
	 * @param token
	 * @param noteId
	 * @return
	 */

	@PostMapping("/label/removelabel")
	public ResponseEntity<Response> removeLabel(@RequestParam("labelId") Long labelId,
			@RequestHeader("token") String token, @RequestParam("noteId") Long noteId) {
		LOG.trace("Create removeLabel controller...");
		System.out.println(labelId);
		service.removeLabel(labelId, noteId, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label removed", 200));

	}

	/**
	 * giving the information about the label
	 *
	 * @param labelInfo
	 * @param token
	 * @return response
	 */

	@PutMapping("/label/update")
	public ResponseEntity<Response> updateLabel(@RequestBody LabelUpdate labelInfo,
			@RequestHeader("token") String token) {
		LOG.trace("Create updateLabel controller...");
		service.editLabel(labelInfo, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label updated", 200, labelInfo));
	}

	/**
	 * Delete label controller
	 *
	 * @param labelInfo
	 * @param token
	 * @return response
	 */

	@PostMapping("/label/delete")
	public ResponseEntity<Response> delete(@RequestBody LabelUpdate labelInfo, @RequestHeader("token") String token) {
		LOG.trace("Create delete controller...");
		service.deleteLabel(labelInfo, token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("label deleted", 200, labelInfo));

	}

	/**
	 * get label controller
	 *
	 * @param token
	 * @return response
	 */

	@GetMapping("/labels/getAllLabel")
	public ResponseEntity<Response> get(@RequestHeader("token") String token) {
		LOG.trace("Create get controller...");
		List<LabelInformation> labels = service.getLabel(token);
		return ResponseEntity.status(HttpStatus.OK).body(new Response("All the labels that you are using", 200, labels));

	}

	/**
	 * getting label controller
	 *
	 * @param token
	 * @param labelId
	 * @return response
	 */

	@GetMapping("/label/getLabelNotes")
	public ResponseEntity<Response> getNotes(@RequestHeader("token") String token,@RequestParam("id") Long labelId) {
		LOG.trace("Create getNotes controller...");
		List<NoteInformation> list = service.getAllNotes(token, labelId);

	return ResponseEntity.status(HttpStatus.OK).body(new Response("The result is", 200, list));

	}

}
