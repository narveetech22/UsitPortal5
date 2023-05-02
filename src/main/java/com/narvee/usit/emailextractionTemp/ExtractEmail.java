package com.narvee.usit.emailextractionTemp;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.mail.internet.MimeMultipart;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.narvee.usit.entity.EmailAttachment;

import lombok.Data;

@Data
@Entity
@Table(name = "Extractemail")
public class ExtractEmail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "mailsubject", columnDefinition = "TEXT") // , columnDefinition = "MEDIUMTEXT"
	private String subject;

	@Column(name = "body", columnDefinition = "LONGTEXT")
	private String body;

	@Column(name = "mailfrom", columnDefinition = "MEDIUMTEXT")
	private String frommail;

	@Column(name = "attachment", columnDefinition = "TEXT")
	private String attachment;

	@Column(name = "mailto", columnDefinition = "TEXT")
	private String tomail;

	@Column(name = "mailcc", columnDefinition = "TEXT") // , columnDefinition = "MEDIUMTEXT"
	private String ccmail;

	@Column(name = "company", columnDefinition = "MEDIUMTEXT")
	private String company;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd  HH:mm")
	private Date receiveddate;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd  HH:mm")
	private Date sentdate;

	// one to many mapping
	@OneToMany(mappedBy = "extractEmail", cascade = CascadeType.ALL)
	@JsonManagedReference
	private List<EmailAttachment> attachments = new ArrayList<>();

}
