package com.bridgelabz.fundoonotes.Entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Profile {

	public Profile() {
		super();
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getProfilePicName() {
		return profilePicName;
	}

	public void setProfilePicName(String profilePicName) {
		this.profilePicName = profilePicName;
	}

	public UserInformation getUserLabel() {
		return userLabel;
	}

	public void setUserLabel(UserInformation userLabel) {
		this.userLabel = userLabel;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String profilePicName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserInformation userLabel;

	public Profile(String profilePicName, UserInformation userLabel) {
		super();
		this.profilePicName = profilePicName;
		this.userLabel = userLabel;
	}

}