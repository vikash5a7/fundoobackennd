package com.bridgelabz.fundoonotes.request;

import lombok.Data;

@Data
public class NoteUpdation {

	private long id;
	private String title;
	private String description;
	private boolean isArchieved;
	private boolean isPinned;
	private boolean isTrashed;
}
