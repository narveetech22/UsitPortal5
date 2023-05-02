package com.narvee.usit.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.narvee.usit.entity.Visa;

public interface IVisaService {
	public boolean saveVisa(Visa visa);

	public Visa getVisaById(long visaId);

	public List<Visa> getAllVisa();

	public boolean deleteVisaStatus(long visaId);

	public Page<Visa> findPaginated(int pageNo, int pageSize);

	public List<Object[]> getvisaidname();
	public List<Object[]> getH1visa();
}