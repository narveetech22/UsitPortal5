package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "techsupport")
public class TechAndSupport {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@Column(name = "pseudo_name")
	private String pseudoname;

	private String mobile;

	@Column(name = "second_mobile")
	private String secmobile;

	private String email;

	@Column(name = "pre_company")
	private String precompany;

	private int experience;

	private String location;

	@OneToOne(fetch = FetchType.LAZY) // (cascade = CascadeType.DETACH)
	@JoinColumn(name = "techid")
	private Technologies technology;

	@Column(name = "skills", columnDefinition = "TEXT")
	private String skills;

	// private String status = "Active";

	@Column(name = "updatedby")
	private long updatedby;

	@Column(name = "updateddate")
	@UpdateTimestamp
	private LocalDateTime updateddate;

	@Column(name = "added_by", nullable = false, updatable = false)
	private long addedby = 1;

	@Column(name = "remarks", length = 250)
	private String remarks;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
	
	@Transient
	private String tech;

	private TechAndSupport() {

	}

	public TechAndSupport(Long id, String name, int experience, String skills, String email, String mobile,String tech) {
		this.id = id;
		this.name = name;
		this.experience = experience;
		this.skills = skills;
		this.email = email;
		this.mobile = mobile;
		this.tech = tech;
	}

}
