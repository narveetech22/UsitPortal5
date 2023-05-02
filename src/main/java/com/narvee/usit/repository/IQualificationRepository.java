package com.narvee.usit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.narvee.usit.entity.Qualification;

public interface IQualificationRepository extends JpaRepository<Qualification, Long> {
	public Optional<Qualification> findByName(String name);
	public Optional<Qualification> findByNameAndIdNot(String name, Long id);
}
