package com.bridgelabz.fundoonotes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;


public interface CollaboratorService {
	NoteInformation addCollaborator(Long noteId, String email, String token);
	void deleteCollabotar(Long cId, String token, Long noteId);
	List<NoteInformation> getCollabrator(String token);

}
