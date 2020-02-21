package com.bridgelabz.fundoonotes.Entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class LabelInformation {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int labelId;

	private String name;

	private long userId;

	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "Label_Note", joinColumns = { @JoinColumn(name = "label_id") }, inverseJoinColumns = {
			@JoinColumn(name = "note_id") })

	@JsonBackReference
	private List<NoteInformation> list;

	public int getLabelId() {
		return labelId;
	}

	public void setLabelId(int labelId) {
		this.labelId = labelId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public List<NoteInformation> getList() {
		return list;
	}

	public void setList(List<NoteInformation> list) {
		this.list = list;
	}

}
