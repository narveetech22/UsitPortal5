package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.entity.Submissions;

public interface ISubmissionRepository extends JpaRepository<Submissions, Serializable> {

	@Modifying
	@Query(value = "UPDATE submission c  SET c.substatus= :status  WHERE c.consultantid = :id ", nativeQuery = true)
	public int changeSubmissionStatus(@Param("id") Long id, @Param("status") String status);

	public List<Submissions> findByConsultantConsultantid(long id);
	
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPosition(long cid, String location,long vmsid,
			String position);
	
	public Optional<Submissions> findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPositionAndSubmissionidNot(long cid, String location,long vmsid,
			String position, long subid);

	public Submissions findByConsultantConsultantidAndProjectlocationAndEndclient(long id, String location,
			String clinet);

	@Query(value = "select u.userid,u.fullname,u.pseudoname, c.consultantname,c.consultantid, s.created_date as createddate ,s.empcontact,s.emplname,s.endclient,s.implpartner, s.position, s.projectlocation,\r\n"
			+ "s.ratetype,s.remarks,s.status,s.submissionrate,s.updateddate,s.relocationassistance,s.flg,s.substatus,\r\n"
			+ " s.subid as submissionid,  r.reqnumber, r.vendor from requirment r, submission s, consultant_info c, users u\r\n"
			+ "where r.id = s.reqid  and s.consultantid = c.consultantid and u.userid = s.userid and flg='Recruiting' order by s.subid desc", nativeQuery = true)

	public List<SubmissionDTO> getrecruitingsubmission();

	@Query(value = "select  u.userid,u.fullname,u.pseudoname,c.consultantname,c.consultantid, s.created_date as createddate ,\r\n"
			+ "s.empcontact,s.emplname,s.endclient,s.implpartner, s.position, s.projectlocation,\r\n"
			+ "s.ratetype,s.remarks,s.status,s.submissionrate,s.updateddate,v.company as vendor,s.relocationassistance,s.flg,s.substatus,\r\n"
			+ " s.subid as submissionid from  submission s, consultant_info c, users u, vendor v \r\n"
			+ "where v.id = s.vendorid and s.consultantid = c.consultantid and u.userid = s.userid  and flg='sales' and c.status='Active' order by s.subid desc", nativeQuery = true)
	public List<SubmissionDTO> getsalessubmission();
	
	@Query(value = "select  u.userid,u.fullname,u.pseudoname,c.consultantname,c.consultantid, s.created_date as createddate ,\r\n"
			+ "s.empcontact,s.emplname,s.endclient,s.implpartner, s.position, s.projectlocation,\r\n"
			+ "s.ratetype,s.remarks,s.status,s.submissionrate,s.updateddate,v.company as vendor,s.relocationassistance,s.flg,s.substatus,\r\n"
			+ " s.subid as submissionid from  submission s, consultant_info c, users u, vendor v \r\n"
			+ "where  v.id = s.vendorid and s.consultantid = c.consultantid and u.userid = s.userid and u.userid=:userid  and flg='sales' and c.status='Active' order by s.subid desc", nativeQuery = true)
	public List<SubmissionDTO> getsubmissionsByRole(@Param("userid")long userid);

}
