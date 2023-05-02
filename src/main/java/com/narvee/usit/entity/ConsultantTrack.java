package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity
@Table
@Data
public class ConsultantTrack {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String status;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
	@OneToOne
	@JoinColumn(name="updatedby")
	private Users updatedby;
	@OneToOne
	@JoinColumn(name="addedby")
	private Users addedby;
	private String remarks;
	//@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "updateddate", nullable = false, updatable = true)
	@UpdateTimestamp
	private LocalDateTime updateddate;
	
}
