package com.bridgelabz.fundoonotes.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Data
public class Collaborator {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long Id;

	private String email;
	@NotNull
	@ManyToOne
	@JoinColumn(name = "noteInformationId")
	private NoteInformation note;
	@Override
	public String toString() {
		return "Collaborator [Id=" + Id + ", email=" + email + ", note=" + note + "]";
	}
}
