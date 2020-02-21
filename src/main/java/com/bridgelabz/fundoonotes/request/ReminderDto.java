package com.bridgelabz.fundoonotes.request;

import java.time.LocalDateTime;

public class ReminderDto {

	private LocalDateTime reminder;

	public LocalDateTime getReminder() {
		return reminder;
	}

	public void setReminder(LocalDateTime reminder) {
		this.reminder = reminder;
	}
}
