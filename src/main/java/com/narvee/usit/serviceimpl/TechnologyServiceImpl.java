package com.narvee.usit.serviceimpl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.repository.TechnologyRepository;
import com.narvee.usit.service.ITechnologyService;
import static org.springframework.data.domain.PageRequest.of;

@Service
@Transactional
public class TechnologyServiceImpl implements ITechnologyService {
	public static final Logger logger = LoggerFactory.getLogger(TechnologyServiceImpl.class);
	@Autowired
	public TechnologyRepository repository;

	@Override
	public boolean saveTechnologies(Technologies technologies) {
		logger.info("TechnologyServiceImpl.saveTechnologies()");
		Technologies tech = repository.save(technologies);
		if (tech != null)
			return true;
		else
			return false;
	}

	@Override
	public List<Technologies> getAllTechnologies() {
		logger.info("TechnologyServiceImpl.getAllTechnologies()");
		return repository.findAll();
	}

	@Override
	public Technologies getTechnologyByID(long id) {
		logger.info("TechnologyServiceImpl.getTechnologyByID()");
		return repository.findById(id).get();
	}

	@Override
	public boolean deleteTechnologiesById(long id) throws SQLIntegrityConstraintViolationException {
		logger.info("TechnologyServiceImpl.deleteTechnologiesById()");
		// Technologies eid = repository.findById(id).get();
		repository.deleteById(id);
		return true;
	}

	@Override
	public List<Object[]> gettechnologies() {
		logger.info("TechnologyServiceImpl.getAllTechnologies2()");
		return repository.gettechnologies();
	}

	@Override
	public Page<Technologies> getUsers(String name, int page, int size) {
		logger.info("TechnologyServiceImpl.getUsers()");
		return repository.findByTechnologyareaContaining(name, of(page, size));
	}

	@Override
	public Page<List<Technologies>> findPaginated(int pageNo, int pageSize) {
		logger.info("TechnologyServiceImpl.findPaginated()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<List<Technologies>> findAll = repository.getalltech(pageable);
		return findAll;
	}

	@Override
	public int changeStatus(String status, long id, String remarks) {
		logger.info("TechnologyServiceImpl.changeStatus()");
		return repository.toggleStatus(id, remarks);
	}

	@Override
	public String getTechnologySkillsByID(long id) {
		return repository.gettechnologySkillById(id);
	}

}
