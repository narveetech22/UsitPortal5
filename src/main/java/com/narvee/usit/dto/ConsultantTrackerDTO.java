package com.narvee.usit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ConsultantTrackerDTO {

	public String getRemarks();

	public String getAddedby();

	public String getUpdatedby();

	public String getAddedbysudo();

	public String getUpdatedbysudo();

	public String getPseudoname();

	public String getStatus();

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getUpdateddate();

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDate getCreateddate();

}
