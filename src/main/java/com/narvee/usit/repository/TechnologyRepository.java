package com.narvee.usit.repository;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Pageable;
import com.narvee.usit.entity.Technologies;
import org.springframework.data.domain.Pageable;

public interface TechnologyRepository extends JpaRepository<Technologies, Serializable> {

	@Query(value = "select s.id, s.technologyarea,s.listofkeyword from technologies s  order by s.technologyarea", nativeQuery = true)
	List<Object[]> gettechnologies();
	
	@Query(value = "select listofkeyword from technologies s where s.id = :id ", nativeQuery = true)
	public String gettechnologySkillById(long id);
	
	public Optional<Technologies> findByTechnologyarea(String technology);

	Page<Technologies> findByTechnologyareaContaining(String name, Pageable pageable);

	@Query("select new com.narvee.usit.entity.Technologies(t.id,t.technologyarea,t.listofkeyword,t.comments,t.createddate,t.remarks)   from Technologies t")
	Page<List<Technologies>> getalltech(Pageable pageable);

	@Modifying
	@Query("UPDATE Technologies c SET c.remarks = :rem  WHERE c.id = :id")
	public int toggleStatus( @Param("id") long id, @Param("rem") String rem);
}
