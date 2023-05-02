package com.narvee.usit.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.entity.Submissions;
public interface ISubmissionService {
	
	
	// for dupliacte checking while inserting
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPosition(long cid, String location,long vmsid,
			String position);
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPositionAndSubmissionidNot(long cid, String location,long vmsid,
			String position, long subid);
	
	//requirement submission duplicate checking
	public Submissions findByConsultantConsultantidAndProjectlocationAndEndclient(long cid, String location,
			String client);
	
	public Submissions saveSubmission(Submissions submission);

	public Optional<Submissions> getSubmissionByID(long sid);

	public boolean deleteSubmission(long sid);

	public List<SubmissionDTO> getAllSubmission(String flg,String role, long userid);

	Page<Submissions> findPaginated(int pageNo, int pageSize);

	//sales submission duplicate checking
	List<Submissions> findByConsultantSidAndProjectlocationAndEndclient(long cid, String location,
			String endclient);

	public int toggleStatus(String status, String remarks, Long id);

	public List<Object[]> getsubdetails();
}
