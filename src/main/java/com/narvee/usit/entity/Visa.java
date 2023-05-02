package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Data
@Table(name = "visa")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Visa {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "Visa")
	@SequenceGenerator(name = "Visa", sequenceName = "Visa_seq")
	private Long vid;

	@Column(name = "visa_status")
	private String visastatus;

	@Column(name = "visa_description")
	private String description;

	@Column(name = "updated_date")
	@UpdateTimestamp
	private LocalDateTime updateddate;

	@Column(name = "added_by", nullable = false, updatable = false)
	private long addedby = 1;

	private long updatedby = 1;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
	
}
