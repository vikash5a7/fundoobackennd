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

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "usersdetail")
public class UserInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long userId;
	
	private String name;
	
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
	
	@JsonIgnore
	private List<NoteInformation> colaborateNote;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(Long mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public boolean isVerified() {
		return isVerified;
	}

	public void setVerified(boolean isVerified) {
		this.isVerified = isVerified;
	}

	public List<NoteInformation> getNote() {
		return note;
	}

	public void setNote(List<NoteInformation> note) {
		this.note = note;
	}

	public List<NoteInformation> getColaborateNote() {
		return colaborateNote;
	}

	public void setColaborateNote(List<NoteInformation> colaborateNote) {
		this.colaborateNote = colaborateNote;
	}

}
