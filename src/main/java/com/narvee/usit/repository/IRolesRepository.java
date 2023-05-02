package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.helper.GetRoles;

public interface IRolesRepository extends JpaRepository<Roles, Long> {

	@Query("SELECT r.rolename FROM Roles r WHERE r.rolename=:roleName")
	public List<String> findRolByRolName(@Param("roleName") String roleName);

	@Query("SELECT r.roleno FROM Roles r WHERE r.roleno=:roleno")
	public List<Long> findByRoleno(@Param("roleno") long roleno);

	public Optional<Roles> findByRolenameAndRoleidNot(String rolename, Long id);

	@Modifying
	@Query("UPDATE Roles c SET c.status = :status,c.remarks = :rem  WHERE c.roleid = :id")
	public int toggleStatus(@Param("status") String status, @Param("id") Long id, @Param("rem") String rem);

	@Query(value = "SELECT roleid, rolename from roles where status!='InActive' ", nativeQuery = true)
	public List<GetRoles> getAllRoles();

}
