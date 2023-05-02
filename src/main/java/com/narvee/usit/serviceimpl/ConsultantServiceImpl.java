package com.narvee.usit.serviceimpl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.ConsultantTrackerDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.entity.ConsultantFileUploads;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.ConsultantTrack;
import com.narvee.usit.entity.Qualification;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.entity.Visa;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.ListRecruitingConsultant;
import com.narvee.usit.repository.IConsultantFileUploadsRepository;
import com.narvee.usit.repository.IConsultantRepository;
import com.narvee.usit.repository.IConsultantTrackRepository;
import com.narvee.usit.repository.IQualificationRepository;
import com.narvee.usit.repository.ISubmissionRepository;
import com.narvee.usit.repository.IVisaRepository;
import com.narvee.usit.repository.TechnologyRepository;
import com.narvee.usit.service.IConsultantService;
import com.narvee.usit.service.ITechnologyService;

@Service
@Transactional
public class ConsultantServiceImpl implements IConsultantService {

	private static final Logger logger = LoggerFactory.getLogger(ConsultantServiceImpl.class);

	// @Value("${file.upload.folder}")
	@Value("${file.upload-dir}")
	private String filesPath;

	@Autowired
	private IConsultantRepository repository;

	@Autowired
	private IConsultantFileUploadsRepository filerepository;

	@Autowired
	private IConsultantTrackRepository ctrackrepo;

	@Autowired
	ISubmissionRepository subrepo;

	@Autowired
	private EmailService emailService;

	@Override
	public ConsultantInfo findByContactnumber(String contactnumber) {
		return repository.findByContactnumber(contactnumber);
	}

	@Override
	public ConsultantInfo findByContactnumberAndConsultantidNot(String contactnumber, Long id) {
		return repository.findByContactnumberAndConsultantidNot(contactnumber, id);
	}

	@Override
	public ConsultantInfo saveConsultant(ConsultantInfo consultant) {
		logger.info("Saving Consultant");
		String flg = consultant.getConsultantflg();
		if (flg.equalsIgnoreCase("Recruiting")) {
			return repository.saveAndFlush(consultant);

			// }
		} else {
			List<ConsultantTrack> ctrlist = new ArrayList();
			ConsultantTrack ctrack = new ConsultantTrack();
			ctrack.setStatus(consultant.getStatus());
			ctrack.setRemarks(consultant.getRemarks());
			ctrack.setAddedby(consultant.getAddedby());
			ctrlist.add(ctrack);
			consultant.setTrack(ctrlist);
			try {
				ConsultantInfo consu = repository.saveAndFlush(consultant);
				emailService.consultantEntry(consu);
				return consu;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (MessagingException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	@Override
	public ConsultantInfo updateConsultant(ConsultantInfo consultant) {
		logger.info("Saving Consultant");
		String flg = consultant.getConsultantflg();
		if (flg.equalsIgnoreCase("Recruiting")) {
			return repository.saveAndFlush(consultant);
		} else {
			List<ConsultantTrack> ctrlist = new ArrayList();
			ConsultantTrack ctrack = new ConsultantTrack();
			ctrack.setStatus(consultant.getStatus());
			ctrack.setRemarks(consultant.getRemarks());
			ctrack.setAddedby(consultant.getAddedby());
			ctrack.setUpdatedby(consultant.getUpdatedby());
			ctrlist.add(ctrack);
			consultant.setTrack(ctrlist);
			try {
				
				ConsultantInfo consu = repository.save(consultant);
				System.out.println(consultant);
				emailService.consultantEntry(consu);
				return consu;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				return null;
			} catch (MessagingException e) {
				e.printStackTrace();
				return null;
			}
		}

	}

	@Override
	public ConsultantInfo getconsulatntbyId(Long id) {
		logger.info("get single getconsulatntbyId");
		Optional<ConsultantInfo> consultant = repository.findById(id);
		// System.out.println(consultant);
		if (consultant.isPresent()) {
			return consultant.get();
		}
		return null;
	}

	@Override
	public boolean deleteconsultant(Long id) {
		logger.info("delete Consultant");
		// check whether consultant associated with submission or not before deleting
		List<Submissions> submission = subrepo.findByConsultantConsultantid(id);
		if (submission == null || submission.isEmpty()) {
			filerepository.queryfordelete(id);
			repository.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public int activeinactiveConsultant(String status, String remarks, Long id, String flg) {
		logger.info("activeinactiveConsultant Consultant");
		return repository.changeConsultantStatus(status, id, remarks, flg);
	}

	@Override
	public ConsultantInfo findByEmail(String email) {
		logger.info("Calling custom method to check duplicate consultant with email");
		return repository.findByConsultantemail(email);
	}

	@Override
	public int update(String resume, String h1b, String dl, Long id) {
		logger.info("Saving COnsultant files");
		Integer upd = repository.update(resume, id);
		if (upd == null) {
			return 0;
		} else {
			return 1;
		}
	}

	@Override
	public int update(String resume, long id) {
		return repository.update(resume, id);
	}

	@Override
	public ConsultantFileUploads uploadfiles(String files, long id) {
		ConsultantFileUploads c = new ConsultantFileUploads();
		c.setConsultantid(id);
		c.setFilename(files);
		ConsultantFileUploads upd = filerepository.save(c);
		return upd = filerepository.save(c);
	}

	@Override
	public List<ConsultantInfo> findByConsultantemailAndConsultantidNot(String email, Long id) {
		return repository.findByConsultantemailAndConsultantidNot(email, id);
	}

	@Override
	public List<ListRecruitingConsultant> getConsultantlist() {
		return repository.getlistofconsultants();
	}

	@Value("${consultant-access-spadmin}")
	private String spadmin;

	@Value("${consultant-access-admin}")
	private String admin;

	@Value("${consultant-access-manager}")
	private String manager;

	@Value("${consultant-access-teamlead}")
	private String teamlead;

	@Override
	public List<ConsultantDTO> findallconsultant(String flg, String role, long userid) {
		// System.out.println(flg + "---------------------------------------" + role);
		/*
		 * if (spadmin.equalsIgnoreCase(role) || admin.equalsIgnoreCase(role) ||
		 * manager.equalsIgnoreCase(role) || teamlead.equalsIgnoreCase(role)) { if
		 * (flg.equalsIgnoreCase("sales")) { return repository.managerRoleList(flg); }
		 * else { return repository.managerRoleList(flg); }
		 * 
		 * } else { if (flg.equalsIgnoreCase("presales")) {
		 * System.out.println("l---------------------------------------"); return
		 * repository.normalRoleList(flg, userid); } else {
		 * System.out.println("============================="); return
		 * repository.managerRoleList(flg); }
		 * 
		 * }
		 */
		// StringBuilder qry = "1";
		// StringBuilder qry=new StringBuilder("1");
		if (flg.equalsIgnoreCase("presales")) {
			if (!role.equalsIgnoreCase("Employee")) {
				if (role.equalsIgnoreCase(admin) || role.equalsIgnoreCase(spadmin)) {
					return repository.adminRoleList(flg);
				} else {
					return repository.managerRoleList(flg, userid);
				}
			} else {
				return repository.normalRoleList(flg, userid);
			}
		} else {
			return repository.adminRoleList(flg);
		}
	}

	@Override
	public Resource downloadfile(long id, String doctype) throws FileNotFoundException {
		ConsultantInfo model = repository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("File does not exist" + id));

		String filename = null;
		if (doctype.equalsIgnoreCase("resume"))
			filename = model.getResume();
		else if (doctype.equalsIgnoreCase("h1b"))
			filename = model.getH1bcopy();
		else
			filename = model.getDlcopy();

		try {
			Path file = Paths.get(filesPath).resolve(filename);
			logger.info(" filesPath ", filesPath);
			logger.info(" Path ", file);
			Resource resource = new UrlResource(file.toUri());
			logger.info(" resource location ", resource);
			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public Resource download(long id) throws FileNotFoundException {
		ConsultantFileUploads model = filerepository.findById(id)
				.orElseThrow(() -> new FileNotFoundException("File does not exist" + id));
		// String filesPath = "D:/stores2/";
		String filename = model.getFilename();
		// System.out.println(filename);
		try {
			Path file = Paths.get(filesPath).resolve(filename);
			Resource resource = new UrlResource(file.toUri());

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new RuntimeException("Could not read the file!");
			}
		} catch (MalformedURLException e) {
			throw new RuntimeException("Error: " + e.getMessage());
		}
	}

	@Override
	public List<Object[]> getconsultInfo(String flg) {
		return repository.getconsultInfo(flg);
	}

	@Override
	public int movedtoconsultant(long id, String flg, String remarks, long userid) {
		String status = "Active";

		if (flg.equalsIgnoreCase("presales")) {
			status = "InActive";
		} else if (flg.equalsIgnoreCase("sales")) {

			status = "Active";
		}
		ConsultantInfo consultant = repository.findById(id).get();
		List<ConsultantTrack> ctrlist = new ArrayList();
		ConsultantTrack ctrack = new ConsultantTrack();
		ctrack.setStatus(status);
		ctrack.setRemarks(remarks);
		ctrack.setAddedby(consultant.getAddedby());
		Users user = new Users();
		user.setUserid(userid);
		ctrack.setUpdatedby(user);
		ctrlist.add(ctrack);
		consultant.setTrack(ctrlist);
		repository.save(consultant);
		return repository.movedtoconsultant(id, flg, remarks, status, userid);
	}

	@Override
	public int removeFile(long id, String type) {
		ConsultantInfo entity = repository.findById(id).get();
		if (type.equalsIgnoreCase("resume")) {
			entity.setResume(null);
			repository.save(entity);

		} else if (type.equalsIgnoreCase("h1b")) {
			entity.setH1bcopy(null);
			repository.save(entity);
		} else {
			entity.setDlcopy(null);
			repository.save(entity);
		}
		return 1;
	}

	@Override
	public int removeMultipleFile(long id) {
		Optional<ConsultantFileUploads> entity = filerepository.findById(id);
		if (entity.isEmpty()) {
			return 0;
		} else {
			filerepository.deleteById(id);
			return 1;
		}

	}

	@Override
	public List<ConsultantTrackerDTO> consultantTracker(long id) {
		return repository.consultantTracker(id);
	}

	@Override
	public List<ConsultantReportDTO> consutantReport(DateSearcherDto dateSearcherDto) {
		if (dateSearcherDto.getGroupby().equalsIgnoreCase("date"))
			return repository.consutantReportGroupbyDate(dateSearcherDto.getStartDate(), dateSearcherDto.getEndDate());
		else if (dateSearcherDto.getGroupby().equalsIgnoreCase("employee"))
			return repository.consutantReportGroupbyEmployee(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate());
		else
			return repository.consutantReportGroupbyConsultant(dateSearcherDto.getStartDate(),
					dateSearcherDto.getEndDate());
	}

	@Override
	public List<ConsultantDTO> reportDrillDown(String status, LocalDate startDate) {
		if (status.equalsIgnoreCase("status")) {
			List<String> param = List.of("Initiated", "Completed", "Verified", "Active", "InActive", "Rejected");
			return repository.reportDrillDown(param, startDate);
		} else {
			List<String> param = List.of(status);
			return repository.reportDrillDown(param, startDate);
		}
	}

	@Override
	public void uploadExcel(MultipartFile file) {
		try {
			List<ConsultantInfo> excelData = ExcelUploads.convertExcelToListOfVendor(file.getInputStream());
			saveExcelData(excelData);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Autowired
	private IVisaRepository visarepositoty;

	@Autowired
	private TechnologyRepository technologyrepo;

	@Autowired
	private IQualificationRepository qualificationrepo;

	public void saveExcelData(List<ConsultantInfo> excelData) {
		// repository.saveAll(excelData);
		Visa visa = null;
		Qualification qualification = null;
		Technologies technologies = null;
		ConsultantInfo entity = null;
		Long visaid = null;
		Long techid = null;
		Long qualid = null;
		List<ConsultantInfo> entity1 = new ArrayList();
		for (ListIterator<ConsultantInfo> it = excelData.listIterator(); it.hasNext();) {
			visa = new Visa();
			qualification = new Qualification();
			technologies = new Technologies();
			entity = new ConsultantInfo();
			ConsultantInfo value = it.next();
			value.setConsultantflg("presales");
			value.setStatus("Tagged");
			Users user = new Users();
			user.setUserid(1);
			value.setAddedby(user);
			// checking for visa availability
			if (value.getVisa() != null) {
				Optional<Visa> visaEntity = visarepositoty.findByVisastatus(value.getVisa().getVisastatus());
				if (visaEntity.isEmpty()) {
					visa.setVisastatus(value.getVisa().getVisastatus());
					visaid = visarepositoty.save(visa).getVid();
					visa.setVid(visaid);
					value.setVisa(visa);
				} else {
					visaid = visaEntity.get().getVid();
					visa.setVid(visaid);
					value.setVisa(visa);
				}
			} else {
				visa.setVid(1L);
				value.setVisa(visa);
			}
			// for Technology duplicate checking
			if (value.getTechnology() != null) {
				Optional<Technologies> technEntity = technologyrepo
						.findByTechnologyarea(value.getTechnology().getTechnologyarea());
				if (technEntity.isPresent()) {
					techid = technEntity.get().getId();
					technologies.setId(techid);
					value.setTechnology(technologies);
				} else {
					technologies.setTechnologyarea(value.getTechnology().getTechnologyarea());
					techid = technologyrepo.save(technologies).getId();
					technologies.setId(techid);
					value.setTechnology(technologies);
				}
			} else {
				technologies.setId(14L);
				value.setTechnology(technologies);
			}
			// for Qualification checking
			if (value.getQualification() != null) {
				Optional<Qualification> qualEntity = qualificationrepo.findByName(value.getQualification().getName());
				if (qualEntity.isPresent()) {
					qualid = qualEntity.get().getId();
					qualification.setId(qualid);
					value.setQualification(qualification);
				} else {
					qualification.setName(value.getQualification().getName());
					qualid = qualificationrepo.save(qualification).getId();
					value.setQualification(qualification);
				}
			} else {
				qualification.setId(6L);
				value.setQualification(qualification);
			}

			entity1.add(value);
		}
		List<ConsultantInfo> entity2 = new ArrayList();

		for (ListIterator<ConsultantInfo> it = entity1.listIterator(); it.hasNext();) {
			ConsultantInfo centity = it.next();
			ConsultantInfo listEntity = repository.findByContactnumberOrConsultantemail(centity.getContactnumber(),
					centity.getConsultantemail());
			if (listEntity == null) {
				entity2.add(centity);
			} else {
			}
		}
		repository.saveAll(entity2);
	}

	//@Scheduled(fixedRate=10000)
	@Scheduled(cron = "0 0 0 */2 * ?")
	public void scheduleTask() {
		LocalDateTime Currentdate = LocalDateTime.now();
		List<ConsultantDTO> list = repository.scheduleForPendingConsultant();
		emailService.pendingConsultantInfo(list,Currentdate);
		logger.info("In scheduling");
		System.out.println("In scheduling");
	}

	
	 //@Scheduled(fixedRate=10000)
//		@Scheduled(cron = "0 0 0 7 * *")         //scheduling starts every day 7pm
		@Scheduled(cron = "0 */1 * * * *")         //scheduling starts every day 7pm
		public void dailyConsultantInfo() {
  System.out.println("-------------------------------------------end method");			
			LocalDateTime Currentdate = LocalDateTime.now();
			 ZoneId istZoneId = ZoneId.of("America/Chicago");
		        LocalDate startDateTime1 = LocalDate.now(istZoneId);
			
		        LocalDateTime startDateTime = LocalDate.now(istZoneId).atTime(LocalTime.of(13, 0));
				LocalDateTime endDateTime = LocalDate.now(istZoneId).atTime(LocalTime.of(14, 0));
				
		        
			//getting today's list of consultant's between 10am to 7pm
			List<ConsultantInfo> list = repository.findAll(); 
			List<ConsultantInfo> ne= new ArrayList<ConsultantInfo>();
			for (ConsultantInfo consultantDTO : list) {
                 if(consultantDTO.getCreateddate().isAfter(startDateTime) && consultantDTO.getCreateddate().isBefore(endDateTime)) {
					ne.add(consultantDTO);
					System.out.println("Consultant's daily list:"+consultantDTO.getCompanyname()+" "+consultantDTO.getConsultantemail());
				}
			}
			
		        System.out.println("startDateTime:"+startDateTime);
		        System.out.println("endDateTime:"+endDateTime);
			
//			emailService.pendingConsultantInfo(list,Currentdate);
			logger.info("In scheduling");
			System.out.println("In scheduling");
		}
}
