package com.bridgelabz.fundoonotes.services;

import java.util.List;

import com.bridgelabz.fundoonotes.Entity.LabelInformation;
import com.bridgelabz.fundoonotes.Entity.NoteInformation;
import com.bridgelabz.fundoonotes.dto.LabelDto;
import com.bridgelabz.fundoonotes.request.LabelUpdate;

public interface LabelService {
	
	void createLabel(LabelDto label,String token);
	
	void editLabel(LabelUpdate label, String userid);
	
	void deleteLabel(LabelUpdate info, String token);
	
	void addLabel(Long labelId, Long noteId, String token);
	
	
	List<LabelInformation> getLabel(String token);
	
	List<NoteInformation> getAllNotes(String token,Long labelId);
	
	void removeLabel(Long labelId, Long noteId, String token);
	
	void createLabelAndMap(LabelDto label, String token, Long noteId);

}
