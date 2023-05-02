package com.narvee.usit.helper;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.ToString;

public interface ListInterview {

	public Long getConsid();

	public String getName();

	public String getEndclient();

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	public LocalDateTime getInterview_date();

	public String getRound();

	public String getPseudoname();

	public String getFlg();

	public String getMode();

	public String getVendor();

	public LocalDate getCreated_date();

	public Long getUserid();

	public String getFullname();

	public String getInterview_status();

	public String getTime_zone();

	public Long getIntrid();

	public Long getSubid();

}
