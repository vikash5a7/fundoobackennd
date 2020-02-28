package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.Collaborator;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;

public interface ICollabratorRepositary {
	public Collaborator saveCollabrator(Collaborator collaborator);

	public List<NoteInformation> getCollabrator(Long id);

}
