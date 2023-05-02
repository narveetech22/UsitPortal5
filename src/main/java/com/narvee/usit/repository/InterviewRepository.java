package com.narvee.usit.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.helper.IRecInterviewHelper;
import com.narvee.usit.helper.ListInterview;

@Repository
public interface InterviewRepository extends JpaRepository<Interview, Serializable> {

	@Query(value = "select s.subid, c.consultantname, s.vendor, s.position,\r\n"
			+ " s.projectlocation, s.endclient from submission s, consultant_info c where s.consultantid = c.consultantid and s.userid=:id and c.consultantflg!='presales' and s.flg=:flg order by s.subid desc", nativeQuery = true)
	public List<Object[]> submissiondetails(String flg,long id);
	
	@Query(value = "select s.subid, c.consultantname, s.vendor, s.position,\r\n"
			+ " s.projectlocation, s.endclient from submission s, consultant_info c where s.consultantid = c.consultantid and c.consultantflg!='presales' and s.flg=:flg order by s.subid desc", nativeQuery = true)
	public List<Object[]> submissiondetails(String flg);

	@Query(value = " select i.flg, s.endclient,c.consultantid as consid, c.consultantname as name, i.interview_date, i.round, i.mode, s.vendor, s.created_date,\r\n"
			+ " u.userid, u.fullname,u.pseudoname, i.interview_status, i.time_zone, i.id as intrid, s.subid\r\n"
			+ " from interview i, consultant_info c, submission s , users u where i.submissionid = s.subid and\r\n"
			+ "  s.consultantid = c.consultantid and i.addedby = u.userid  and i.flg=:flg and c.status='Active' order by i.id desc", nativeQuery = true)
	public List<ListInterview> getall(String flg);
	
	@Query(value = " select i.flg, s.endclient,c.consultantid as consid, c.consultantname as name, i.interview_date, i.round, i.mode, s.vendor, s.created_date,\r\n"
			+ " u.userid, u.fullname,u.pseudoname, i.interview_status, i.time_zone, i.id as intrid, s.subid\r\n"
			+ " from interview i, consultant_info c, submission s , users u where i.submissionid = s.subid and\r\n"
			+ "  s.consultantid = c.consultantid and i.addedby = u.userid  and i.flg=:flg and u.userid=:userid and c.status='Active' order by i.id desc", nativeQuery = true)
	public List<ListInterview> getallbyRole(String flg,long userid);

	@Query(value = " select i.flg,s.endclient,c.consultantid as consid, c.consultantname as name, i.interview_date, i.round, i.mode, s.vendor, s.created_date,\r\n"
			+ " u.userid, u.fullname,u.pseudoname, i.interview_status, i.time_zone, i.id as intrid, s.subid\r\n"
			+ " from interview i, consultant_info c, submission s , users u where i.submissionid = s.subid and\r\n"
			+ "            s.consultantid = c.consultantid and i.addedby = u.userid", countQuery = "SELECT count(*) FROM interview ", nativeQuery = true)
	public Page<List<ListInterview>> getallbypage(Pageable pageable);

	// foreign key relation
	public List<Interview> findBySubmissionSubmissionid(long id);
	
	@Query(value="select i.id,c.consultantname as name, c.consultantemail as email, r.category,r.jobtitle,r.location,r.reqnumber,r.vendor,i.interview_date, i.interview_status,i.mode, \r\n"
			+ "i.round,i.time_zone as timezone from consultant_info c , interview i , requirment r ,submission s  where \r\n"
			+ "s.reqid = r.id and s.consultantid = c.consultantid and i.submissionid = s.subid and i.id = :id", nativeQuery = true)
	public IRecInterviewHelper getinfo(long id);

//	@Query(value="select i.id, i.interview_date,i.interview_status,i.mode, i.round,i.time_zone as timezone, c.name, c.email,s.project_location as location,s.position as jobtitle,\r\n"
//			+ "s.vendor\r\n"
//			+ "from interview i , tbl_sales_consultant c , sales_submission s\r\n"
//			+ "where c.id = s.consultant_id and s.id = i.submission_id and i.id= :id", nativeQuery = true)
//	public IRecInterviewHelper getinfo(long id);
	
}
