package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.entity.Email;

public interface IEmailRepository extends JpaRepository<Email, Serializable> {

	@Query("Select e from Email e WHERE Concat(e.subject) like %?1%")
	public List<Email> getAllEmailBasedOnFilter(String keyword);

	@Modifying
	@Query("delete from Email e WHERE e.id in (:ids)")
	public void deletemailById(List<Long> ids);

	public List<Email> findBySubject(String subject);

}
