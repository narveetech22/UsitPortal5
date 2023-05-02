package com.narvee.usit.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface SubmissionDTO {

	public String getConsultantname();

	public long getConsultantid();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getUpdateddate();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getCreateddate();

	public String getEmpcontact();

	public String getRatetype();
	
	public long getUserid();

	public String getSubmissionrate();

	public String getVendor();

	public String getStatus();

	public String getEndclient();

	public String getRelocationassistance();

	public String getSubstatus();

	public String getImplpartner();

	public long getSubmissionid();

	public String getReqnumber();

	public String getEmplname();

	public String getPosition();

	public String getProjectlocation();

	public String getFLg();

	public String getFullname();

	public String getPseudoname();

	public String getRemarks();

}
