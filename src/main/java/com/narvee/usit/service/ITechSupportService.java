package com.narvee.usit.service;

import java.util.List;

import com.narvee.usit.dto.TechAndSupportDTO;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.TechAndSupport;

public interface ITechSupportService {
	public boolean saveTechSupp(TechAndSupport roles);
	
	public boolean updateTechSupp(TechAndSupport roles);

	public List<TechAndSupportDTO> getAll(String search);

	public List<TechAndSupportDTO> all();

	public TechAndSupport getTechSupp(Long id);

	public boolean deleteSupp(Long id);

	public int changeStatus(String status, Long id, String remarks);
}
