package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Entity
@Table
@Data
public class ConsultantInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long consultantid;

	@Column(name = "consultantname", length = 100)
	private String consultantname;

	@Column(name = "firstname", length = 35)
	private String firstname;

	@Column(name = "lastname", length = 35)
	private String lastname;

	@Column(name = "contactnumber", length = 18)
	private String contactnumber;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "number")
	private PhoneNumberFormat number;

	@Column(name = "consultantemail", length = 80)
	private String consultantemail;

	private String position;
	private String currentlocation;

	@OneToOne
	@JoinColumn(name = "visa_status")
	private Visa visa;

	@Column(name = "experience", length = 3)
	private String experience;

	// if relocation to other reason
	@Column(name = "relocation", length = 20)
	private String relocation;

	@Column(name = "relocatother", length = 250)
	private String relocatOther;

	@OneToOne(fetch = FetchType.LAZY) // (cascade = CascadeType.DETACH)
	@JoinColumn(name = "techid")
	private Technologies technology;

	@Column(name = "ratetype", length = 20)
	private String ratetype;

	@Column(name = "hourlyrate", length = 20)
	private String hourlyrate;

	@Column(name = "skills", columnDefinition = "TEXT")
	private String skills;

	@Column(name = "summary", columnDefinition = "MEDIUMTEXT")
	private String summary;

	@Column(name = "priority", length = 10)
	private String priority;

//	// educational background for recruiting'
//	@Column(name = "qualification", length = 150)
//	private String qualification;

	@OneToOne
	@JoinColumn(name = "qid")
	private Qualification qualification;

//	@Column(name = "specialization", length = 100)
//	private String specialization;

	@Column(name = "university", length = 220)
	private String university;

	@Column(name = "yop", length = 6)
	private String yop;

	@Column(name = "linkedin", length = 250)
	private String linkedin;

	@Column(name = "passportnumber", length = 20)
	private String passportnumber;

	@Column(name = "projectavailabity", length = 50)
	private String projectavailabity;

	@Column(name = "availabilityforinterviews", length = 100)
	private String availabilityforinterviews;

	// upload files
	@Column(name = "resume", length = 200)
	private String resume;

	@Column(name = "h1bcopy", length = 100)
	private String h1bcopy;

	@Column(name = "dlcopy", length = 100)
	private String dlcopy;

	// recruiting multiple file uploads
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "consultant_id")
	private List<ConsultantFileUploads> fileupload;
	// ConsultantTrack

	// @OneToMany(cascade = CascadeType.ALL)
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "consultantid", nullable = false)
	private List<ConsultantTrack> track;

	// recruiting current mployer details
	@Column(name = "companyname", length = 200)
	private String companyname;

	@Column(name = "refname", length = 100)
	private String refname;

	@Column(name = "refname1", length = 40)
	private String refname1;

	@Column(name = "refemail", length = 40)
	private String refemail;

	@Column(name = "refemail1", length = 40)
	private String refemail1;

	@Column(name = "refcont", length = 40)
	private String refcont;

	@Column(name = "refcont1", length = 40)
	private String refcont1;

	@Column(name = "remarks", length = 255)
	private String remarks;

	/*
	 * @OneToOne(cascade = CascadeType.DETACH, optional = true)
	 * 
	 * @JoinColumn(name = "requirementid", nullable = true) private Requirements
	 * requirements;
	 */

	@OneToOne
	@JoinColumn(name = "addedby")
	private Users addedby;

	@OneToOne
	@JoinColumn(name = "updatedbyid")
	private Users updatedby;

	@Column(name = "consultantflg", length = 20)
	private String consultantflg;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	// @CreationTimestamp
	private LocalDateTime createddate;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "updateddate", nullable = false, updatable = true)
	// @UpdateTimestamp
	private LocalDateTime updateddate;

	@PrePersist
	public void setCreateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String formattedDate = now.format(formatter);
		this.createddate = now;
		this.updateddate = now;
	}

	@PreUpdate
	public void setUpdateddate() {
		ZoneId newYork = ZoneId.of("America/Chicago");
		LocalDateTime now = LocalDateTime.now(newYork);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		String formattedDate = now.format(formatter);
		this.updateddate = now;
	}

	@Column(name = "status", length = 30)
	private String status = "Active";

	private String comment;

}
