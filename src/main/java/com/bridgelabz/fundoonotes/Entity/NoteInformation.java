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

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Data;

@Document(indexName = "elasticsearch", shards = 2)
@Entity
@Data
public class NoteInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String title;
	private String description;
	private boolean isArchieved = false;
	private boolean isPinned = false;
	private boolean isTrashed = false;
	private LocalDateTime createdDateAndTime;
	private LocalDateTime upDateAndTime;
	private String colour;
	private LocalDateTime reminder;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Label_Note", joinColumns = { @JoinColumn(name = "note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "label_id") })
	private List<LabelInformation> list;
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Collaborator_Note", joinColumns = { @JoinColumn(name = "note_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
     private List<UserInformation> colabUser;
}
