package com.narvee.usit.serviceimpl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.narvee.usit.dto.TechAndSupportDTO;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.TechAndSupport;
import com.narvee.usit.repository.ITechSupportRepository;
import com.narvee.usit.service.ITechSupportService;

@Transactional
@Service
public class TechAndSupportImpl implements ITechSupportService {
	public static final Logger logger = LoggerFactory.getLogger(TechAndSupportImpl.class);

	@Autowired
	private ITechSupportRepository repository;

	@Override
	public boolean saveTechSupp(TechAndSupport entity) {
		logger.info("TechAndSupportImpl.saveTechSupp()");
		TechAndSupport dupcheck = repository.findByEmail(entity.getEmail());

		if (dupcheck == null) {
			repository.save(entity);
			//System.out.println("=================");
			return true;
		} else {
			//System.out.println("------------------");
			return false;
		}
	}

	@Override
	public boolean updateTechSupp(TechAndSupport entity) {
		logger.info("TechAndSupportImpl.saveTechSupp()");
		TechAndSupport dupcheck = repository.findByEmailAndIdNot(entity.getEmail(), entity.getId());
		if (dupcheck == null) {
			repository.save(entity);
			//System.out.println("=================");
			return true;
		} else {
			//System.out.println("------------------");
			return false;
		}
	}

	@Override
	public List<TechAndSupportDTO> getAll(String search) {
		logger.info("TechAndSupportImpl.getAll()");
		List<TechAndSupportDTO> findAlln = new ArrayList();
		System.out.println(search);
//		if (!search.equals("dummysearch")) {
//			findAlln = repository.getAll(search.trim());
//		} else if (search.equals("dummysearch")) {
//			findAlln = repository.findAll();
//		} else {
//			findAlln = repository.findAll();
//		}
		return findAlln;
	}

	@Override
	public TechAndSupport getTechSupp(Long id) {
		logger.info("TechAndSupportImpl.getTechSuppByhid()");
		return repository.findById(id).get();
	}

	@Override
	public boolean deleteSupp(Long id) {
		logger.info("TechAndSupportImpl.deleteSupp()");
		repository.deleteById(id);
		return true;
	}

	@Override
	public int changeStatus(String status, Long id, String rem) {
		logger.info("TechAndSupportImpl.changeStatus()");
		return repository.toggleStatus(id, rem);
	}

	@Override
	public List<TechAndSupportDTO> all() {
		return repository.getAll();
	}

}
