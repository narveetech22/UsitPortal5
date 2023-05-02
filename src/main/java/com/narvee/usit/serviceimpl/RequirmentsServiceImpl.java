package com.narvee.usit.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.AssignRequirement;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Requirements;
import com.narvee.usit.entity.Users;
import com.narvee.usit.helper.GetRecruiter;
import com.narvee.usit.repository.IAssignRequirementRepository;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.repository.IRequirmentRepository;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.service.IRequirmentService;

@Transactional
@Service
public class RequirmentsServiceImpl implements IRequirmentService {

	private static final Logger logger = LoggerFactory.getLogger(RequirmentsServiceImpl.class);
	@Autowired
	private IRequirmentRepository repository;

	@Autowired
	IAssignRequirementRepository repo;

//	@Autowired
//	IRecSubmissionRepository submissionrepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private IConsultantRepository consrepo;

	@Autowired
	private IUsersRepository userRepo;

	@Override
	public List<Requirements> getAllRequirments() {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : getAllRequirments");
		return repository.findAll();// repository.getAllRequirments();
	}

	@Override
	public boolean deleteRequirmentsByID(long reqID) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : deleteRequirmentsByID");
		//List<ConsultantInfo> submission = consrepo.findByRequirementsRequirementid(reqID);
		//if (submission == null || submission.isEmpty()) {
			repo.deletereq(reqID);
			repository.deleteById(reqID);
			return true;
		//} else {
		//	return false;
		//}

		// findByRequirementsRequirementid
//		List<ReqSubmission> submission = submissionrepo.findByRequirementsRequirementid(reqID);
//		List<RecConsultant> cons = consrepo.findByRequirementsRequirementid(reqID);
//		System.out.println(submission);
//		System.out.println(cons);
//		if ((submission == null || submission.isEmpty()) && (cons == null || cons.isEmpty())) {			
		// repository.deleteById(reqID);
//			return true;
//		} else {
		// return true;
		// }

	}

	@Override
	public void initmethod(Requirements requirements) {
		Optional<Requirements> findByReqnumber = repository.findByReqnumber(requirements.getReqnumber());
		if (findByReqnumber.isEmpty()) {
			repository.save(requirements);
		}
	}

	// saving requirement
	@Override
	public Requirements saveRequirments(Requirements requirements) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : saveRequirments");
		Requirements save = repository.save(requirements);

		Users user = userRepo.findById(requirements.getUsers().getUserid()).get();
		String requirementraisedby = user.getEmail();

		String subject = requirements.reqnumber + " - " + requirements.getJobtitle() + " At "
				+ requirements.getLocation();

		Set<AssignRequirement> recipients = requirements.getEmpid();
		List<Long> tomail = new ArrayList();
		recipients.forEach(rc -> {
			rc.getUserid();
			tomail.add(rc.getUserid());
		});
		String[] usermails = userRepo.findrequirementemail(tomail);

		if (save.getRequirementid() != null) {
			//emailService.sendrequirementMail(usermails, requirementraisedby, subject, requirements.getJobdescription());
		}
		requirements.getEmpid().forEach(ee -> {
			AssignRequirement asn2 = new AssignRequirement();
			asn2.setUserid(ee.getUserid());
			asn2.setFullname(ee.getFullname());
			asn2.setReqid(save.getRequirementid());
			repo.save(asn2);
		});
		return save;
	}

	@Override
	public Requirements updateRequirment(Requirements requirements) {
		System.out.println(requirements);
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : updateRequirment");
		Requirements save = repository.save(requirements);
		// long addedby = requirements.getUsers().getUserid();
		// getting emial of updated by user mail
		Users user = userRepo.findById(requirements.getUpdatedby()).get();
		String requirementraisedby = user.getEmail();
		String subject = requirements.reqnumber + " - " + requirements.getJobtitle() + " At "
				+ requirements.getLocation() + " has been modified by " + user.getFullname();

		// getting assigned users mail ids
		Set<AssignRequirement> recipients = requirements.getEmpid();

		List<Long> tomail = new ArrayList();
		recipients.forEach(rc -> {
			rc.getUserid();
			tomail.add(rc.getUserid());
		});
		String[] usermails = userRepo.findrequirementemail(tomail);
		if (usermails.length != 0) {
			//emailService.sendrequirementMail(usermails, requirementraisedby, subject, requirements.getJobdescription());
		}
		repo.deletereq(requirements.getRequirementid());
		requirements.getEmpid().forEach(ee -> {
			AssignRequirement asn2 = new AssignRequirement();
			asn2.setUserid(ee.getUserid());
			asn2.setFullname(ee.getFullname());
			asn2.setReqid(save.getRequirementid());
			repo.save(asn2);
		});
		return repository.save(requirements);
	}

	@Override
	public Requirements getRequrimentByID(long reqID) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : getRequrimentByID");
		Optional<Requirements> requirements = repository.findById(reqID);
		if (requirements.isPresent()) {
			System.out.println("=====================");
			System.out.println(requirements.get());
			System.out.println("=====================");

			return requirements.get();
		}
		
		
		return null;
	}

	@Override
	public boolean updateRequirments(Requirements requirements) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : updateRequirments");
		Requirements req = repository.save(requirements);
		if (req != null) {
			return true;
		}
		return false;
	}

	@Override
	public Page<Requirements> findPaginated(int pageNo, int pageSize) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : findPaginated");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Requirements> findAll = repository.findAll(pageable);
		return findAll;
	}

	@Override
	public List<Object[]> getrequirements() {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : getrequirements");
		return repository.getrequirements();
	}

	@Override
	public String findmaxReqNumber() {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : findmaxReqNumber");
		return repository.findmaxReqNumber();
	}

	@Override
	public List<GetRecruiter> getAssignedemploy(Long id) {
		logger.info("!!! inside class : RecRequirmentsServiceImpl, method : getAssignedemploy");
		return repo.getRecruiterbyid(id);
	}

}