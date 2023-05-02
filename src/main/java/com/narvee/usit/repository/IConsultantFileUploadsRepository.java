package com.narvee.usit.repository;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.narvee.usit.entity.ConsultantFileUploads;
import com.narvee.usit.helper.ListFiles;

public interface IConsultantFileUploadsRepository extends JpaRepository<ConsultantFileUploads, Serializable> {

	@Modifying
	@Query("delete from ConsultantFileUploads where consultantid=:id")
	public void queryfordelete(@Param("id") long id);

	@Query(value = "select  f.consultant_id, f.docid, f.filename, c.resume from consultant_file_uploads f, recruiting_consultant c where c. id = f.consultant_id and f.consultant_id = :id", nativeQuery = true)
	public List<ListFiles> getfiles();
}
