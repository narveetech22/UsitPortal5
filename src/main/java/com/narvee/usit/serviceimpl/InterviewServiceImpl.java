package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.entity.Users;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.repository.ISubmissionRepository;
import com.narvee.usit.repository.InterviewRepository;
import com.narvee.usit.service.IinterviewService;

@Transactional
@Service
public class InterviewServiceImpl implements IinterviewService {

	@Autowired
	private InterviewRepository repository;

	@Autowired
	private IConsultantRepository consRepo;

	@Autowired
	private EmailService emailService;

	@Override
	public Interview saveIterview(Interview interviews) {
		Interview con1 = repository.save(interviews);
		// System.out.println(con1);
//		try {
//			emailService.interviewFollowupmail(interviews);
//		} catch (UnsupportedEncodingException | MessagingException e) {
//			e.printStackTrace();
//		}
		return con1;
	}

	@Override
	public boolean deleteInterviewById(long id) {
		repository.deleteById(id);
		return true;
	}
	@Value("${consultant-access-spadmin}")
	private String spadmin;

	@Value("${consultant-access-admin}")
	private String admin;

	@Value("${consultant-access-manager}")
	private String manager;
	@Override
	public List<ListInterview> getAllDetailsInterview(String flg,String role, long userid) {
		//return repository.getall(flg);
		if (spadmin.equalsIgnoreCase(role) || admin.equalsIgnoreCase(role) || manager.equalsIgnoreCase(role)) {
			return repository.getall(flg);
		}else {
			return repository.getallbyRole(flg,userid);
		}
		
	}

	@Override
	public Interview getInterviewByID(long id) {
		Optional<Interview> interview = repository.findById(id);
		if (interview.isPresent()) {
			return interview.get();
		}
		return null;
	}

	@Override
	public Page<Interview> findPaginated(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Interview> findAll = repository.findAll(pageable);
		return findAll;
	}

	@Override
	public ConsultantInfo getSalesConsById(long conid) {
		return consRepo.findById(conid).get();
	}

//	@Value("${consultant-access-spadmin}")
//	private String spadmin;
//
//	@Value("${consultant-access-admin}")
//	private String admin;
//
//	@Value("${consultant-access-manager}")
//	private String manager;
	
	@Override
	public List<Object[]> submissiondetails(String flg,long id, String role) {
		//return repository.submissiondetails(flg);
		if (spadmin.equalsIgnoreCase(role) || admin.equalsIgnoreCase(role) || manager.equalsIgnoreCase(role)) {
			return repository.submissiondetails(flg);
		}else {
			return repository.submissiondetails(flg,id);
		}
	}

//	@Override
//	public Page<ListInterview> findPaginateded(int pageNo, int pageSize) {
//		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
//		Page<ListInterview> findAll = repository.getallbypage(pageable);
//		return findAll;
//	}

	@Override
	public Page<List<ListInterview>> findPaginateded(int pageNo, int pageSize) {
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<List<ListInterview>> findAll = repository.getallbypage(pageable);
		return findAll;
	}

}
