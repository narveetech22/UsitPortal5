package com.narvee.usit.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface VendorAssRecruiterDTO {
	public String getRecruiter();
	public String getUsnumber();
	public String getEmail();
	public String getCountry();
	public String getLocation();
	public String getStatus();
	public String getFedid();
	public String getCity();
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getCreateddate();
}
