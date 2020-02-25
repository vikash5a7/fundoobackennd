package com.bridgelabz.fundoonotes.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class NoteUpdationDto {
	private long id;
	private String title;
	private String description;
	private boolean isArchieved;
	private boolean isPinned;
	private boolean isTrashed;
	private LocalDateTime createdDateAndTime;
	private LocalDateTime upDateAndTime;
}
