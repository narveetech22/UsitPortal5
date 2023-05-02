package com.narvee.usit.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Users {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userid;
	private String firstname;
	private String lastname;
	private String fullname;
	private String pseudoname;
	
	private String email;
	private int isactive;
	private String password;
	private String designation;
	private String status = "Active";
	private String department;
	@Column(name = "remarks", length = 400)
	private String remarks;
	@UpdateTimestamp
	private LocalDateTime updateddate;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name = "createddate", nullable = false, updatable = false)
	@CreationTimestamp
	private LocalDate createddate;
	@OneToOne(cascade = CascadeType.DETACH)
	private Roles role;

	private String resetPasswordToken;

	private long manager;

	private long teamlead;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "companycontactnumber")
	private PhoneNumberFormat companycontactnumber;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "personalcontactnumber")
	private PhoneNumberFormat personalcontactnumber;

	
	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
	@Column(name = "lastLogin")
	private LocalDateTime lastLogin;
	
	@Column(name = "lastLogout")
    private LocalDateTime lastLogout;
	
    private String systemip;
    private String systemname;
    
    private boolean locked= false;
    
}
