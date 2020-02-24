package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.NoteInformation;

/**
 * Notes repository are here..
 *
 * @author vikash kumar
 * @version 1.1
 * @date 22-02-2020
 *
 */
public interface INoteRepositary {
	public NoteInformation save(NoteInformation noteInformation);

	public NoteInformation findById(long id);

	public boolean isPined(long id);

	public boolean deleteNote(long id, long useid);

	public List<NoteInformation> getTrashedNotes(long userid);

	public boolean updateColour(long id, long userid, String colour);

}
