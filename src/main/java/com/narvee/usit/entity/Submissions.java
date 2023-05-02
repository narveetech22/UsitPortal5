package com.narvee.usit.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.*;
import javax.persistence.Table;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "submission")
@NoArgsConstructor
@AllArgsConstructor
public class Submissions {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "subid")
	private Long submissionid;

	@Column(length = 150)
	private String endclient;

//	@OneToOne
//	@JoinColumn(name = "reqid")
//	private Requirements requirement;

	@OneToOne
	@JoinColumn(name = "consultantid")
	private ConsultantInfo consultant;

	@Column(name = "position")
	private String position;

	@Column(name = "projectlocation")
	private String projectlocation;

	@Column(name = "submissionrate")
	private String submissionrate;

	@Column(name = "implpartner")
	private String implpartner;
	
	@OneToOne
	@JoinColumn(name = "vendorid")
	private VendorDetails vendor;

	@Column(name = "ratetype", length = 55)
	private String ratetype;

	@Column(name = "emplname", length = 155)
	private String emplname;

	@Column(name = "empcontact", length = 18)
	private String empcontact;

	@Column(name = "empmail", length = 125)
	private String empmail;

	@Column(name = "updatedby")
	private long updatedby;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Column(name = "created_date", nullable = false, updatable = false)
	@CreationTimestamp()
	private LocalDate createddate;

	@Column(name = "updateddate")
	@UpdateTimestamp
	private LocalDateTime updateddate;

	@Column(name = "status", length = 25)
	private String status = "Active";

	@OneToOne // (cascade = CascadeType.DETACH)
	@JoinColumn(name = "userid")
	private Users user;

	@Column(length = 255)
	private String remarks;

	@Column(length = 155)
	private String relocationassistance;

	@Column(length = 25)
	private String flg;

	@Column(length = 35)
	private String substatus = "Submitted";

}
