package com.narvee.usit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface SalesConsultantDTO {
	public long getvisaid();
	
	public long getTechid();
	
	public long getConsultantid();
	
	public long getAddedby();

	public String getSalesemp();

	public String getSkills();

	public String getSpecialization();

	public String getState();

	public String getSummary();

	public String getCity();

	public String getCompanyname();

	public String getConsultantemail();

	public String getConsultantname();

	public String getContactnumber();

	public String getRatetype();

	public String getRelocatother();

	public String getRelocation();

	public String getRemarks();

	public String getResume();

	public String getUniversity();

	public String getFullname();

	public String getPseudoname();

	public String getAvailabilityforinterviews();

	public String getEmpcontact();

	public String getEmpemail();

	public String getExperience();

	public String getLinkedin();

	public String getPriority();

	public String getProjectavailabity();

	public String getQualification();

	public String getYop();

	public String getPassportnumber();

	public String getTechnologyarea();

	public String getVisa_status();

	public String getHourlyrate();
	
	public String getStatus();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getUpdateddate();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getCreateddate();

}
