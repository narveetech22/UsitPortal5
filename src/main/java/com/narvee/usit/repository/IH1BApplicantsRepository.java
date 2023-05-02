package com.narvee.usit.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.H1bApplicantDTO;
import com.narvee.usit.entity.H1BApplicants;
public interface IH1BApplicantsRepository extends JpaRepository<H1BApplicants, Serializable> {
		public Optional<H1BApplicants> findByEmail(String name);
	    public Optional<H1BApplicants> findByEmailAndApplicantidNot(String name, Long id);
	    
	    @Modifying
		@Query("UPDATE H1BApplicants c SET c.passportdoc= :passportdoc  WHERE c.applicantid = :id") 
		public int update(@Param("passportdoc") String passportdoc, @Param("id") Long id);
	    
	    @Query(value ="select h.applicantid, h.employeename, h.contactnumber, h.everifydoc, h.i9doc, h.email, h.receiptnumber, h.petitioner, h.servicecenter, h.lcanumber,\r\n"
	    		+ "h.noticetype, h.createddate, h.updateddate from h1bapplicants h where h.everifydoc is null or h.i9doc is null and TIMESTAMPDIFF(MINUTE,h.createddate,NOW()) >= 2\r\n"
	    		+ "order by h.createddate asc",nativeQuery= true) 
	    public List<H1bApplicantDTO> findByCreateddateAndEverifydocandI9docIsNull();
	    
	}

