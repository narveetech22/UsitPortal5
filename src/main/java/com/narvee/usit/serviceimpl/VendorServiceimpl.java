package com.narvee.usit.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.narvee.usit.dto.VendorAssRecruiterDTO;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.GetVendor;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListVendor;
import com.narvee.usit.repository.IRecruiterRepository;
import com.narvee.usit.repository.IVendorRepository;
import com.narvee.usit.service.IRecruiterService;
import com.narvee.usit.service.IVendorService;

@Service
@Transactional
public class VendorServiceimpl implements IVendorService {
	public static final Logger logger = LoggerFactory.getLogger(VendorServiceimpl.class);

	@Autowired
	IVendorRepository repo;

	@Autowired
	IRecruiterRepository recrepo;

	@Override
	public VendorDetails save(VendorDetails vms) {
		return repo.save(vms);
	}

	@Override
	public List<VendorDetails> getall() {
		return repo.findAll();
	}

	@Override
	public boolean deleteById(long id) {
		logger.info("VmsServiceImpl.deleteById()");
		List<VendorAssRecruiterDTO> recruiterInfo = recrepo.findByVendorVmsid(id);
		if ((recruiterInfo == null || recruiterInfo.isEmpty())) {
			repo.deleteById(id);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public VendorDetails getbyId(long id) {
		return repo.findById(id).get();
	}

	@Override
	public List<GetVendor> getvendordetails(String flg) {
		if (flg.equalsIgnoreCase("all")) {
			return repo.listvendor();
		}
		return repo.listvendor(flg);
	}

	@Override
	public int approve(String stat, Long id) {
		logger.info("VmsServiceImpl.saveVms()");
		int save = repo.approveStatus(stat, id);
		if (save != 0)
			return 1;
		else
			return 0;
	}

	@Override
	public int changeStatus(String status, Long id, String remarks) {
		logger.info("VmsServiceImpl.changeStatus()");
		return repo.toggleStatus(status, id, remarks);
	}

	@Override
	public Page<VendorDetails> findPaginated(int pageNo, int pageSize) {
		logger.info("VmsServiceImpl.findPaginated()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<VendorDetails> findAll = repo.findAll(pageable);
		return findAll;
	}

	@Override
	public void saveexcel(MultipartFile file) {
//		try {
//			List<VendorDetails> products = ExcelUploads.convertExcelToListOfVendor(file.getInputStream());
//			System.out.println(products);
//			saveMail(products);
//			// this.productRepo.saveAll(products);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public void saveMail(List<VendorDetails> products) {
		// System.out.println(products);
		Users u = new Users();
		Long cityid = null;
		Long stateid = null;
		// List<VendorDetails> newvendor = new ArrayList();
		for (ListIterator<VendorDetails> it = products.listIterator(); it.hasNext();) {
			VendorDetails value = it.next();
			u.setUserid(1);
			value.setUser(u);
		}

		this.repo.saveAll(products);
	}

	@Value("${consultant-access-spadmin}")
	private String spadmin;

	@Value("${consultant-access-admin}")
	private String admin;

	@Value("${consultant-access-manager}")
	private String manager;

	@Value("${consultant-access-teamlead}")
	private String teamlead;

	@Override
	public List<ListVendor> getvendor(String role, long userid) {
//		if (!role.equalsIgnoreCase("Employee")) {
//			if (role.equalsIgnoreCase(admin) || role.equalsIgnoreCase(spadmin)) {
//				return repo.getvendor();
//			} else {
//				return repo.getvendor();
//			}
//		} else {
//			return repo.getvendor();
//		}
		if (role.equalsIgnoreCase("Employee") || role.equalsIgnoreCase("Team Lead")) {
			return repo.getvendorForExecutive(userid);
		}
//		else if (role.equalsIgnoreCase("Employee")) {
//			return repo.getvendor();
//		}
		else {
			return repo.getvendor();
		}
		/*
		 * if (flg.equalsIgnoreCase("presales")) { if
		 * (!role.equalsIgnoreCase("Employee")) { if (role.equalsIgnoreCase(admin) ||
		 * role.equalsIgnoreCase(spadmin)) { return repository.adminRoleList(flg); }
		 * else { return repository.managerRoleList(flg, userid); } } else { return
		 * repository.normalRoleList(flg, userid); } } else { return
		 * repository.adminRoleList(flg); }
		 */
	}

	@Override
	public List<VendorDetails> findByCompanyAndHeadquerter(String company, String location) {
		return repo.findByCompanyAndHeadquerter(company, location);
	}

	@Override
	public int rejectVendor(Long id, String remarks) {
		logger.info("VmsServiceImpl.saveVms()");
		int save = repo.rejectVendor(remarks, id);
		if (save != 0)
			return 1;
		else
			return 0;
	}

	@Override
	public List<VendorDetails> checkDuplicateVendor(String vendorname) {
		return repo.findByVendor(vendorname);
	}

	

}
