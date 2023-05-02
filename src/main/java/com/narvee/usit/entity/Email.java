package com.narvee.usit.entity;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMultipart;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
@Entity
@Table(name = "Narveemail")
public class Email {

	@Id
//	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mailsubject", columnDefinition = "MEDIUMTEXT")
	private String subject;

	@Column(name = "body", columnDefinition = "MEDIUMTEXT")
	private String body;

	@Column(name = "mailfrom", columnDefinition = "MEDIUMTEXT")
	private String from;

	@Column(name = "mailto", columnDefinition = "MEDIUMTEXT")
	private String tomail;

	@Column(name = "mailcc", columnDefinition = "MEDIUMTEXT")
	private String ccmail;

	@Column(name = "attachment", columnDefinition = "TEXT")
	private String attachment;

	private String subjectcategory;

	@Column(name = "company", columnDefinition = "TEXT")
	private String company;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	public Date sentdate;

}
