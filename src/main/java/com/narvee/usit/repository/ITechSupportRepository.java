package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.TechAndSupportDTO;
import com.narvee.usit.entity.TechAndSupport;

public interface ITechSupportRepository extends JpaRepository<TechAndSupport, Serializable> {

	public TechAndSupport findByEmailAndIdNot(String email, long id);
	
	public TechAndSupport findByEmail(String email);

	// private TechAndSupport(Long id, String name, int experience, String skills,
	// String email, long mobile, int status) {

	// Long id, String name, int experience, String skills, String email, long
	// mobile, String status
	// @Query("SELECT new com.narvee.usit.entity.TechAndSupport(t.id, t.name,
	// t.experience, t.skills, t.email, t.mobile, t.status ) from TechAndSupport t")

	@Query(value = "select t.name, t.experience,s.technologyarea as technology,t.skills, t.email, t.mobile, t.second_mobile,t.id from techsupport t, technologies s\r\n"
			+ "where t.techid = s.id order by t.name", nativeQuery = true)
	// @Query("SELECT new com.narvee.usit.entity.TechAndSupport(t.id, t.name,
	// t.experience, t.skills, t.email, t.mobile,s.technologyarea ) from
	// TechAndSupport t, Technologies s where t.techid = s.id and CONCAT(t.name,
	// t.experience, t.skills, t.email, t.mobile, t.technology) like %?1%")
	public List<TechAndSupportDTO> getAll();

	@Modifying
	@Query("UPDATE TechAndSupport c SET c.remarks = :rem  WHERE c.id = :id")
	public int toggleStatus(@Param("id") Long id, @Param("rem") String rem);

}
