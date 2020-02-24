package com.bridgelabz.fundoonotes.repository;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.LabelInformation;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;

public interface ILabelRepository {

	public LabelInformation save(LabelInformation labelInformation);

	public NoteInformation saveNote(NoteInformation noteInformation);

	public LabelInformation fetchLabel(Long userid, String labelname);

	public LabelInformation fetchLabelById(Long id);

	public int deleteLabel(Long i);

	public List<LabelInformation> getAllLabel(Long id);

	public LabelInformation getLabel(Long id);
}
