package com.bridgelabz.fundoonotes.services;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;


public interface CollaboratorService {
	NoteInformation addCollaborator(Long noteId, String email, String token);

}
