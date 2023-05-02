package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.entity.Requirements;
import com.narvee.usit.helper.GetRecruiter;

public interface IRequirmentService {

	public Requirements saveRequirments(Requirements requirements);

	public Requirements updateRequirment(Requirements requirements);
	public void initmethod(Requirements requirements);

	public Requirements getRequrimentByID(long reqID);

	public List<Requirements> getAllRequirments();

	public boolean updateRequirments(Requirements requirements);

	public boolean deleteRequirmentsByID(long reqID);

	public Page<Requirements> findPaginated(int pageNo, int pageSize);

//	public List<Object[]> getRequirmentByFiletr(String keyword);

	public List<Object[]> getrequirements();

	public String findmaxReqNumber();

	public List<GetRecruiter> getAssignedemploy(Long id);
	
}