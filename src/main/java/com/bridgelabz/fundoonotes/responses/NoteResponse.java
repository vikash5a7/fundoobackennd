package com.bridgelabz.fundoonotes.responses;

import com.bridgelabz.fundoonotes.dto.NoteDto;

public class NoteResponse {

	public NoteDto getNote() {
		return note;
	}

	public void setNote(NoteDto note) {
		this.note = note;
	}

	private NoteDto note;

	public NoteResponse(NoteDto note) {
		this.note = note;

	}

}
