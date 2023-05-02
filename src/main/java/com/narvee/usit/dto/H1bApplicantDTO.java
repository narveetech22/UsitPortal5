package com.narvee.usit.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface H1bApplicantDTO {

	public Long getApplicantid();

	public String getEmployeename();

	public String getContactnumber();

	public String getEmail();
	
	public String getReceiptnumber();
	
	public String getPetitioner(); 
	
	public String getServicecenter();
	
	public String getLcanumber();

	public String getNoticetype();
	
	public String getEverifydoc();
	
	public String getI9doc();
	
	public String getLocation();
	
	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	public LocalDateTime getCreateddate();

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	public LocalDateTime getUpdateddate();

}
