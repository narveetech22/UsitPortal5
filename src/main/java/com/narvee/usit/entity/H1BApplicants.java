package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Entity
@Table
@Data
public class H1BApplicants {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long applicantid;
	private String employeename;
	private String contactnumber;
	private String email;
	private LocalDate h1validfrom;
	private LocalDate h1validto;
	private String receiptnumber;
	private LocalDate everifydate;
	private LocalDate lasti9date;
	private String location;
	private String consulatepoe;
	private String petitioner;
	private String servicecenter;
	private String lcanumber;
	private String noticetype;
	@Column(length = 100)
	private String passportdoc;
	@Column(length = 100)
	private String everifydoc;
	@Column(length = 100)
	private String i9doc;
	@Column(length = 100)
	private String I797doc;
	@Column(length = 100)
	private String i94doc;
	@Column(length = 100)
	private String ssndoc;

	@OneToOne
	@JoinColumn(name = "visa_status")
	private Visa visa;

	@OneToOne
	@JoinColumn(name = "addedby")
	private Users addedby;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
//	@CreationTimestamp
	private LocalDateTime createddate;

	// @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "updateddate", nullable = false, updatable = true)
//	@UpdateTimestamp
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
	
}
