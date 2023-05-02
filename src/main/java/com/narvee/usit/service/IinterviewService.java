package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.helper.ListInterview;

public interface IinterviewService {

	public Interview saveIterview(Interview con);

	public List<ListInterview> getAllDetailsInterview(String flg,String role, long userid);
	// List<ListInterview> getall();

	public Interview getInterviewByID(long id);

	public boolean deleteInterviewById(long id);

	public Page<Interview> findPaginated(int pageNo, int pageSize);

	public ConsultantInfo getSalesConsById(long conid);

	public List<Object[]> submissiondetails(String flg,long id,String role);

	public Page<List<ListInterview>> findPaginateded(int pageNo, int pageSize);

}
