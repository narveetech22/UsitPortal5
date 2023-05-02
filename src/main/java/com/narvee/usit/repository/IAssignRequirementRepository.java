package com.narvee.usit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.entity.AssignRequirement;
import com.narvee.usit.helper.GetRecruiter;

public interface IAssignRequirementRepository extends JpaRepository<AssignRequirement, Long> {

//	@Query("UPDATE Users c SET c.status = :status,c.remarks = :rem  WHERE c.userid = :id")
//	public int toggleStatus(@Param("status") String status, @Param("id") Long id, @Param("rem") String rem);

	@Query(value = "select u.userid, u.fullname from assign_requirement u where u.reqid = :id ", nativeQuery = true)
	public List<GetRecruiter> getRecruiterbyid(@Param("id") Long id);
	
	@Modifying
	@Query(value = "delete from assign_requirement u where u.reqid=:id", nativeQuery = true)
	public void deletereq(@Param("id") Long id);
}
