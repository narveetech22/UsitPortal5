package com.narvee.usit.entity;

import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Table
@Data
public class ConsultantFileUploads {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long docid;
	private String filename;
	@Column(name = "consultant_id")
	private long consultantid;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
}
