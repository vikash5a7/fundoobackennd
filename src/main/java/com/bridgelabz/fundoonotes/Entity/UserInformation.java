package com.bridgelabz.fundoonotes.Entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;


@Entity
@Table(name = "usersdetail")
@Data
public class UserInformation {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	private String fname;
	private String lname;
	private String email;
	private String password;
	private Long mobileNumber;
	private LocalDateTime createdDate;
	private boolean isVerified;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "userId")
	private List<NoteInformation> note;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Collaborator_Note", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "note_id") })
	private List<NoteInformation> colaborateNote;
}
