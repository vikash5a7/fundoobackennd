package com.bridgelabz.fundoonotes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;

public interface ElasticSearchService {
	String createNote(NoteInformation note);
	void updateNote(Long noteId);
	String deleteNote(Long noteId);
	List<NoteInformation> searchbytitle(String title);

	NoteInformation findById(Long Id);
}
