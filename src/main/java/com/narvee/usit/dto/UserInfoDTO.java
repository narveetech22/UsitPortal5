package com.narvee.usit.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public interface UserInfoDTO {

	public String getPersonalcontactnumber();

	public String getEmail();

	public String getFullname();

	public String getDesignation();

	public String getDepartment();

	public String getManager();

	public String getTeamlead();

	public long getUserid();

	public String getStatus();

	public String getPseudoname();

}
