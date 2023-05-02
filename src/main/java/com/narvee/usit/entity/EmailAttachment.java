package com.narvee.usit.entity;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.narvee.usit.emailextractionTemp.ExtractEmail;

import lombok.Data;
@Entity
@Data
@Table(name = "EmailAttachment")
public class EmailAttachment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long eid;
	
	@Column(name = "filename")
	private String fileName;
	
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "extractEmail_id",referencedColumnName = "id")
	@JsonBackReference
	private ExtractEmail extractEmail;
	
}
