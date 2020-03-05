package com.bridgelabz.fundoonotes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.dto.NoteDto;
import com.bridgelabz.fundoonotes.request.NoteUpdation;
import com.bridgelabz.fundoonotes.request.ReminderDto;

public interface NoteService {

	NoteInformation createNote(NoteDto information, String token);

	NoteInformation updateNote(NoteUpdation information, String token);

	void deleteNote(long id, String token);

	List<NoteInformation> getAllNotes(String token);

	List<NoteInformation> getTrashedNotes(String token);

	boolean deleteNotePemenetly(long id, String token);

	NoteInformation archievNote(long id, String token);

	List<NoteInformation> getArchiveNote(String token);

	NoteInformation addColour(Long noteId, String token, String colour);

	NoteInformation addReminder(Long noteId, String token, ReminderDto reminder);

	NoteInformation removeReminder(Long noteId, String token, ReminderDto reminder);

	NoteInformation notePin(long id, String token);

	List<NoteInformation> searchByTitle(String title, String token);

	List<NoteInformation> getAllPinnedNotes(String token);

	List<NoteInformation> searchNotesByTitle(String token, String title);

	NoteInformation getSingleNoteById(long noteId);
}
