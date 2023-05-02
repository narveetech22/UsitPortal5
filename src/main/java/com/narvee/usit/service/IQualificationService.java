package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.entity.Qualification;

public interface IQualificationService {

    public boolean saveQualification(Qualification qualification);
	
	public Qualification getQualificationById(Long id);
	
	public List<Qualification> getAllQualifications();
	
	public void deleteQualificationById(Long id);
	
	public boolean update(Qualification qualification);
}
