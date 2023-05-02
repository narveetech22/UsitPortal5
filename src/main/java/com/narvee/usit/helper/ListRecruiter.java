package com.narvee.usit.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ListRecruiter {
	public Long getId();
	
	public Long getAddedby();
	
	public String getRecruitertype();
	
	public String getRecruiter();

	public String getUsnumber();

	public String getFullname();
	
	public String getPseudoname();
	
	public String getExtension();

	public String getEmail();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd  HH:mm")
	public LocalDateTime getCreateddate();

	public String getStatus();

	public String getRec_stat();

	public String getCompany();
	
	public String getRemarks();
}
