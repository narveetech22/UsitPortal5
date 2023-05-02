package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.narvee.usit.entity.Visa;

public interface IVisaRepository extends JpaRepository<Visa, Serializable> {
	@Query(value = "select v.id, v.visa_status from visa v ", nativeQuery = true)
	public List<Object[]> getvisaidname();

	public Optional<Visa> findByVisastatus(String visa);

	public Optional<Visa> findByVisastatusAndVidNot(String visa, Long id);
	
	
	@Query(value = "select v.id, v.visa_status from visa v  where v.visa_status not in('GC', 'GC EAD', 'US CITIZEN')", nativeQuery = true)
	public List<Object[]> geth1Via();
}
