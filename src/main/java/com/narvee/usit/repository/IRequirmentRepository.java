package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.entity.Requirements;

public interface IRequirmentRepository extends JpaRepository<Requirements, Serializable> {

	public Optional<Requirements> findByReqnumber(String reqnumber);
	
	/*
	 * @Query(value =
	 * "SELECT new com.narvee.usit.entity.RecRequirements(r.recruiterId,r.postedOn,r.jobTitle,r.location,r.vendor,r.employmenttype) FROM RecRequirements r"
	 * ) public List<RecRequirements> getAllRequirments();
	 * 
	 * @Query(value =
	 * "SELECT new com.narvee.usit.entity.RecRequirements(r.recruiterId,r.postedOn,r.jobTitle,r.location,r.vendor,r.employmenttype) FROM RecRequirements r"
	 * ) public Page<RecRequirements> getAllRequrimentsByPageNo(Pageable pageable);
	 * 
	 * @Query(value =
	 * "SELECT r.postedOn, r.vendor, r.jobTitle,r.location,r.employmenttype FROM RecRequirements r Where CONCAT(r.postedOn, r.vendor, r.jobTitle,r.location,r.employmenttype) LIKE %?1%"
	 * ) public List<Object[]> getAllRequirmentsByFilter(String keyword);
	 */

	// public Page<RecRequirements> getAllRequrimentsByPageNo(Pageable pageable);

	@Query(value = "select c.id, c.jobtitle, c.location, c.reqnumber,c.jobskills from requirment c   where c.status='Active' order by c.reqnumber desc", nativeQuery = true)
	public List<Object[]> getrequirements();

	@Query(value = "SELECT max(maxnumber) as maxno FROM requirment", nativeQuery = true)
	public String findmaxReqNumber();

}
