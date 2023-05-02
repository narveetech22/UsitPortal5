package com.narvee.usit.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListRecruiter;

public interface IRecruiterRepository extends JpaRepository<Recruiter, Long> {
	//public List<Recruiter> findByEmail(String name);
	
	public Recruiter findByEmail(String email);

	@Query(value = "select r.recruiter, r.usnumber,r.email,  r.status, r.createddate from \r\n"
			+ "recruiter  r, vendor v where r.vendorid = v.id and r.vendorid = :id", nativeQuery = true)
	public List<VendorAssRecruiterDTO> findByVendorVmsid(long id);

	@Modifying
	@Query("UPDATE Recruiter c SET c.rec_stat = 'Rejected',c.status='Rejected', c.remarks=:remarks  WHERE c.recid = :id")
	public int rejectRecruiter(@Param("remarks") String remarks, @Param("id") Long id);

	@Modifying
	@Query("UPDATE Recruiter c SET c.status = :status,c.remarks = :rem  WHERE c.recid = :id")
	public int toggleStatus(@Param("status") String status, @Param("id") Long id, @Param("rem") String rem);

	// public Recruiter(Long recid, String recruiter, String usnumber, String
	// innumber, String country, String state, String iplogin, String fed_id, long
	// vmsid, String status, LocalDate createddate, long addedby, long role, String
	// addedbyname) {

//	@Query("SELECT new com.narvee.usit.entity.Recruiter(v.recid, v.recruiter, v.usnumber, v.innumber, v.country, v.state, v.iplogin, v.fed_id, v.status, v.createddate, v.addedby, v.role,  u.fullname,v.rec_stat) from "
//			+ "Recruiter v , Users u  where v.addedby = u.userid AND v.rec_stat!='Rejected'")
	// public List<Recruiter> getall();

	@Modifying
	@Query("UPDATE Recruiter c SET c.rec_stat = :status  WHERE c.recid = :id")
	public int approveStatus(@Param("status") String status, @Param("id") Long id);

	@Query(value = "select r.extension,r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and r.rec_stat!='Rejected' order by r.id desc", nativeQuery = true)
	public List<ListRecruiter> getallrecruiter();
	
	@Query(value = "select r.extension, r.addedby,r.recruitertype,r.id,r.recruiter,r.remarks, r.usnumber,u.fullname,u.pseudoname, r.email, r.createddate, r.status, r.rec_stat, v.company from recruiter r , vendor v, users u where u.userid = r.addedby and v.id = r.vendorid and (r.rec_stat!='Rejected' or r.addedby=:userid) order by r.id desc", nativeQuery = true)
	public List<ListRecruiter> getallrecruiterById(long userid);

}
