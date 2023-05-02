package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.narvee.usit.entity.Todo;
import com.narvee.usit.helper.GetRecruiter;

public interface ITaskRepository extends JpaRepository<Todo, Serializable> {
	
	
	@Query(value = "select u.userid, u.fullname,u.pseudoname from users u where u.status = 'Active' ", nativeQuery = true)
	public List<GetRecruiter> getEmployees();

}
