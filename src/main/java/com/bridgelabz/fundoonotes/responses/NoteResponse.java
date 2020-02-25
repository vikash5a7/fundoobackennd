package com.bridgelabz.fundoonotes.responses;

import com.bridgelabz.fundoonotes.dto.NoteDto;

import lombok.Data;

@Data
public class NoteResponse {
	private NoteDto note;

	public NoteResponse(NoteDto note) {
		this.note = note;
	}
}
