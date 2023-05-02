package com.narvee.usit.serviceimpl;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import com.narvee.usit.entity.Qualification;
import com.narvee.usit.repository.IQualificationRepository;
import com.narvee.usit.service.IQualificationService;

@Service
public class QualificationServiceImpl implements IQualificationService {

	private static final Logger logger = LoggerFactory.getLogger(QualificationServiceImpl.class);

	@Autowired
	private IQualificationRepository qualificationRepo;

	@Override
	public boolean saveQualification(Qualification qualification) {
		logger.info("!!! inside class: QualificationServiceImpl, !! method: saveQualification");
		Optional<Qualification> entity = null;
		if (qualification.getId() == null) {
			entity = qualificationRepo.findByName(qualification.getName());
		} else {
			entity = qualificationRepo.findByNameAndIdNot(qualification.getName(), qualification.getId());
		}
		if (entity.isEmpty()) {
			qualificationRepo.save(qualification);
			return true;
		}
		return false;
	}

	@Override
	public Qualification getQualificationById(Long id) {
		logger.info("!!! inside class: QualificationServiceImpl, !! method: getQualificationById");
		Optional<Qualification> qualification = qualificationRepo.findById(id);
		return qualification.get();
	}

	@Override
	public List<Qualification> getAllQualifications() {
		logger.info("!!! inside class: QualificationServiceImpl, !! method: getAllQualification");
		List<Qualification> qualificationList = qualificationRepo.findAll(Sort.by(Sort.Direction.ASC, "name"));
		return qualificationList;
	}

	@Override
	public void deleteQualificationById(Long id) {
		logger.info("!!! inside class: QualificationServiceImpl, !! method: deleteQualificationByID");
		qualificationRepo.deleteById(id);
	}

	@Override
	public boolean update(Qualification qualification) {
		logger.info("!!! inside class: QualificationServiceImpl, !! method: Update");
		Optional<Qualification> qualificatio = qualificationRepo.findByNameAndIdNot(qualification.getName(),
				qualification.getId());
		if (qualificatio.isEmpty()) {
			logger.info("Role saved after checking duplicate records available or not");
			qualificationRepo.save(qualification);
			return true;
		} else {
			return false;
		}
	}

}
