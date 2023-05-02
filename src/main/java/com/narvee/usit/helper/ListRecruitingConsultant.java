package com.narvee.usit.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface ListRecruitingConsultant {

	public LocalDate getCreateddate();

	public String getname();

	public String getExperience();

	public String getEmail();

	public String getvisa_status();

	public String getFullname();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public LocalDateTime getUpdateddate();

	public String getStatus();

	public long getId();

	public long getUserid();

	public String getTechnologyarea();

}
