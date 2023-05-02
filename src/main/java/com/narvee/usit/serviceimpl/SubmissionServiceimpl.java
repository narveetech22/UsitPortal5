package com.narvee.usit.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.repository.ISubmissionRepository;
import com.narvee.usit.repository.InterviewRepository;
import com.narvee.usit.service.ISubmissionService;

@Transactional
@Service
public class SubmissionServiceimpl implements ISubmissionService {

	private static final Logger logger = LoggerFactory.getLogger(SubmissionServiceimpl.class);
	@Autowired
	private ISubmissionRepository repository;

	@Autowired
	private EmailService emailService;

	@Autowired
	private InterviewRepository intrepo;

	@Override
	public Submissions findByConsultantConsultantidAndProjectlocationAndEndclient(long cid, String location,
			String client) {

		return repository.findByConsultantConsultantidAndProjectlocationAndEndclient(cid, location, client);
	}

	@Override
	public Submissions saveSubmission(Submissions submission) {
		logger.info("!!! inside class : SalesSubmissionImpl, !! method : saveSubmission");
		try {
			emailService.sendsubmissionEmail(submission);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		/* Kiran Commented*/
		return repository.save(submission);
	}

	@Override
	public Optional<Submissions> getSubmissionByID(long sid) {
		return repository.findById(sid);
	}

	@Override
	public boolean deleteSubmission(long sid) {
		List<Interview> submission = intrepo.findBySubmissionSubmissionid(sid);
		if (submission == null || submission.isEmpty()) {
			repository.deleteById(sid);
			return true;
		} else {
			return false;
		}
	}

	@Value("${consultant-access-spadmin}")
	private String spadmin;

	@Value("${consultant-access-admin}")
	private String admin;

	@Value("${consultant-access-manager}")
	private String manager;
	
	@Override
	public List<SubmissionDTO> getAllSubmission(String flg,String role, long userid) {
//		if (flg.equalsIgnoreCase("sales")) {
//			return repository.getsalessubmission();
//		} else {
//			return repository.getrecruitingsubmission();
//		}
//		
		if (spadmin.equalsIgnoreCase(role) || admin.equalsIgnoreCase(role) || manager.equalsIgnoreCase(role)) {
			return repository.getsalessubmission();
		}else {
			return repository.getsubmissionsByRole(userid);
		}

	}

	@Override
	public Page<Submissions> findPaginated(int pageNo, int pageSize) {
		return null;
	}

	@Override
	public List<Submissions> findByConsultantSidAndProjectlocationAndEndclient(long cid, String location,
			String endclient) {
		return null;
	}

	@Override
	public int toggleStatus(String status, String remarks, Long id) {
		return 0;
	}

	@Override
	public List<Object[]> getsubdetails() {
		return null;
	}

	@Override
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPosition(long cid,
			String location, long vmsid, String position) {
		return repository.findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPosition(cid, location, vmsid, position);
	}

	@Override
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPositionAndSubmissionidNot(
			long cid, String location, long vmsid, String position, long subid) {
		return repository.findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPositionAndSubmissionidNot(cid, location, vmsid, position, subid);
	}

}
