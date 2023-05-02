package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.dto.UserInfoDTO;
import com.narvee.usit.entity.Users;
import com.narvee.usit.helper.GetRecruiter;

public interface IUsersRepository extends JpaRepository<Users, Long> {

	//public Optional<Users> findByEmail(String email);
	
	public Users findByEmail(String email);

	public Users findByUserid(long id);

	public Users findByEmailAndUseridNot(String email, Long id);

	public Users findByRoleRoleid(long roleid);

	public Users findByResetPasswordToken(String token);

	@Query(value = "select u.status,u.userid, u.fullname,u.pseudoname,u.department,u.designation,u.email,p.international_number as personalcontactnumber,(select  m.pseudoname from users m where m.userid=u.manager) as manager ,\r\n"
			+ "	(select  t.pseudoname from users t where t.userid=u.teamlead) as teamlead \r\n"
			+ "	from users u , phonenumber p where  p.id=u.personalcontactnumber and u.userid=:userid", nativeQuery = true)
	public List<UserInfoDTO> getUserInfo(@Param("userid") long userid);

	@Query("SELECT u.email FROM Users u WHERE u.userid in(:ids) ")
	public String[] findrequirementemail(@Param("ids") List<Long> ids);

	@Query("SELECT u.email FROM Users u WHERE u.department='Recruiting'")
	public String[] findrecruiterMail();

//	@Query("SELECT u.email FROM Users u WHERE u.department in ('Bench Sales')")
//	public String[] findsalesrecruiterMail();
	
	/// mails for executive sent to team, tl, manager
	@Query("SELECT u.email FROM Users u WHERE u.teamlead=:teamleadid and u.manager=:managerid or userid in (:teamleadid,:managerid)")
	public String[] executivemail(@Param("teamleadid") Long teamleadid,@Param("managerid")  long managerid);
	
	/// mails for teamlead sent to team, tl, manager
	@Query("SELECT u.email FROM Users u WHERE  u.manager=:managerid or userid in (:managerid)")
	public String[] teamleadmail(@Param("managerid")  long managerid);

//	@Query(value = "select email from users where department in ('Administration', 'Bench Sales')", nativeQuery = true)
//	public String[] salesTeamEmails();

	@Modifying
	@Query("UPDATE Users c SET c.status = :status,c.remarks = :rem  WHERE c.userid = :id")
	public int toggleStatus(@Param("status") String status, @Param("id") Long id, @Param("rem") String rem);

	@Query("SELECT u FROM Users u WHERE u.email=:email AND u.password=:password ")
	public Users finByEmailAndPassword(@Param("email") String email, @Param("password") String password);

	@Query(" from  Users t where CONCAT(t.fullname, t.email, t.personalcontactnumber, t.designation,  t.status) like %?1%")
	public List<Users> getAll(String search);

	@Query("SELECT u FROM Users u")
	Page<List<Users>> getallbypagination(Pageable pageable);

	public Users findByRole(long role);

	@Query("SELECT firstname FROM Users where firstname like %:keyword%")
	public List<String> search(@Param("keyword") String keyword);

	// @Query(value="select u.userid, u.fullname from users u where u.role_roleid =
	// 9 and u.status = 'Active'", nativeQuery = true)
	@Query(value = "select u.userid, u.fullname from users u where u.status = 'Active' and u.department='Recruiting' ", nativeQuery = true)
	public List<Object[]> getUser();

	// GetRecruiter
	@Query(value = "select u.userid, u.fullname,u.pseudoname from users u where u.status = 'Active' and u.department='Recruiting' ", nativeQuery = true)
	public List<GetRecruiter> getRecruiter();

	@Query(value = "select u.userid, u.fullname,u.pseudoname from users u,roles r where u.status = 'Active' and u.role_roleid=r.roleid and r.rolename='Manager'", nativeQuery = true)
	public List<Object[]> managerDropdown();

	@Query(value = "select u.userid, u.fullname,u.pseudoname from users u,roles r where u.status = 'Active' and u.role_roleid=r.roleid and r.rolename='Team Lead' and u.manager=:id", nativeQuery = true)
	public List<Object[]> TlDropdown(long id);

}
