package com.narvee.usit.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface IRecInterviewHelper {
	public String getCategory();
	public String getLocation();
	public String getReqnumber();
	public String getVendor();
	public long getId();
	public String getName();
	public String getEmail();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")	public LocalDateTime getInterview_date();
	public String getRound();
	public String getMode();
	public String getJobtitle();
	public LocalDate getCreateddate();
	public String getFullname();
	public String getInterview_status();
	public String getStatus();
	public String getTimezone();
}
