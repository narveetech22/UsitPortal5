package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.Visa;
import com.narvee.usit.repository.IVisaRepository;
import com.narvee.usit.service.IVisaService;

@Transactional
@Service
public class VisaServiceImpl implements IVisaService {
	public static final Logger logger = LoggerFactory.getLogger(VisaServiceImpl.class);

	@Autowired
	private IVisaRepository repository;

	@Override
	public boolean saveVisa(Visa visa) {

		logger.info("VisaServiceImpl.saveVms()");
		Optional<Visa> entity = null;
		if (visa.getVid() == null) {
			logger.info("VisaServiceImpl.saveVms() => Save Success");
			entity = repository.findByVisastatus(visa.getVisastatus());
		} else {
			logger.info("VisaServiceImpl.saveVms() => Not Saved");
			entity = repository.findByVisastatusAndVidNot(visa.getVisastatus(), visa.getVid());
		}

		if (entity.isEmpty()) {
			repository.save(visa);
			return true;
		}
		return false;
	}

	@Override
	public Visa getVisaById(long visaId) {
		logger.info("VisaServiceImpl.getVisaById()");
		return repository.findById(visaId).get();
	}

	@Override
	public List<Visa> getAllVisa() {
		logger.info("VisaServiceImpl.getAllVisa()");
		return repository.findAll(Sort.by(Sort.Direction.ASC, "visastatus"));
	}

	@Override
	public boolean deleteVisaStatus(long visaId) {
		logger.info("VisaServiceImpl.deleteVisaStatus()");
		Visa visa = getVisaById(visaId);
		if (visa != null) {
			repository.delete(visa);
			return true;
		}
		return false;
	}

	@Override
	public Page<Visa> findPaginated(int pageNo, int pageSize) {
		logger.info("VisaServiceImpl.findPaginated()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<Visa> findAll = repository.findAll(pageable);
		return findAll;
	}

	@Override
	public List<Object[]> getvisaidname() {
		return repository.getvisaidname();
	}

	@Override
	public List<Object[]> getH1visa() {
		// TODO Auto-generated method stub
		return repository.geth1Via();
	}

}
