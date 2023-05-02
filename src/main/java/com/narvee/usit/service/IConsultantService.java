package com.narvee.usit.service;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.ConsultantTrackerDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.entity.ConsultantFileUploads;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.helper.ListRecruitingConsultant;

public interface IConsultantService {

	// duplicate with phone number
	public ConsultantInfo findByContactnumber(String contactnumber);

	public ConsultantInfo findByContactnumberAndConsultantidNot(String contactnumber, Long id);

	public ConsultantInfo saveConsultant(ConsultantInfo consultant);

	public List<ConsultantDTO> findallconsultant(String flg, String role, long userid);

	// checking consultant duplicate records before insertion
	public ConsultantInfo findByEmail(String email);

	public List<ConsultantInfo> findByConsultantemailAndConsultantidNot(String email, Long id);

	public ConsultantInfo updateConsultant(ConsultantInfo consultant);

	// used
	public Resource downloadfile(long id, String doctype) throws FileNotFoundException;

	public Resource download(long id) throws FileNotFoundException;

	// save consultant files
	public int update(String resume, String h1b, String dl, Long id);

	// to save recruiting consultant files
	public int update(String resume, long id);

	public int movedtoconsultant(long id, String flg, String remarks, long userid);

	// multiple files
	public ConsultantFileUploads uploadfiles(String files, long id);

	public List<ListRecruitingConsultant> getConsultantlist();

	public ConsultantInfo getconsulatntbyId(Long id);

	public boolean deleteconsultant(Long id);

	public int activeinactiveConsultant(String status, String remarks, Long id, String flg);

	// consultant drop down in submission registry
	public List<Object[]> getconsultInfo(String flg);

	public int removeFile(long id, String type);

	public int removeMultipleFile(long id);

	public List<ConsultantTrackerDTO> consultantTracker(long id);

	public List<ConsultantReportDTO> consutantReport(DateSearcherDto dateSearcherDto);

	public List<ConsultantDTO> reportDrillDown(String status, LocalDate startDate);
	
	
	public void uploadExcel(MultipartFile file);
	
	//public  void schedulingForPening();

}
