package com.narvee.usit.service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.usit.entity.Technologies;

public interface ITechnologyService {

	public boolean saveTechnologies(Technologies technologies);

	public List<Technologies> getAllTechnologies();

	public List<Object[]> gettechnologies();

	public Technologies getTechnologyByID(long id);

	public boolean deleteTechnologiesById(long id) throws SQLIntegrityConstraintViolationException;

	Page<Technologies> getUsers(String name, int page, int size);

	Page<List<Technologies>> findPaginated(int pageNo, int pageSize);

	public int changeStatus(String status, long id, String remarks);
	
	
	public String getTechnologySkillsByID(long id);
}
