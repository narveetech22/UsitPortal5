package com.narvee.usit.emailextractionTemp;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IEmailExtractRepository extends JpaRepository<ExtractEmail, Serializable> {
	public List<ExtractEmail> findByFrommail(String mail);

	@Query(value = "select mailfrom from extractemail order by mailfrom", nativeQuery = true)
	public Set<String> dulicatecheck();

	@Query(value = "select mailfrom, mailcc, company, mailsubject from extractemail order by mailfrom", nativeQuery = true)
	public List<ListExtractMailDTO> listAll();

	@Query(value = "select max(receiveddate)as Date from extractemail where mailto = :mailto", nativeQuery = true)
	public Date maxDateFunction(String mailto);
	
	public List<ExtractEmail> findBySubject(String subject);
	
	//Written JPQL query for fetching all data from database
//	@Query(value = "SELECT e, a from extractemail e left join e.attachments a")
//	public List<Object[]> findAllEmailsWithAttachments();

}
