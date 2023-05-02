package com.narvee.usit.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.narvee.usit.serviceimpl.EmailService;
import com.narvee.usit.dto.UserInfoDTO;
import com.narvee.usit.entity.Users;
import com.narvee.usit.helper.GetRecruiter;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.service.IUserService;

@Transactional
@Service
public class UserServiceImpl implements IUserService {
	public static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private IUsersRepository iUsersRepo;

	@Autowired
	private EmailService emailService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Override
	public boolean saveUser(Users users) {
		logger.info("!!! inside class : UserAuthService, !! method : saveUser");
		Users user = this.findUserByEmail(users.getEmail());
		if (user == null) {
			users.setPassword(passwordEncoder.encode("Narvee123$"));
//			iUsersRepo.save(users);
//			return true;
			try {
				iUsersRepo.save(users);
				String username = users.getFullname();
				if (users.getDepartment().equalsIgnoreCase("Bench Sales")) {
					username = users.getPseudoname();
				}
				emailService.employeeRegistarionMail(users.getEmail(), username);
				return true;
			} catch (UnsupportedEncodingException | MessagingException e) {
				e.printStackTrace();
				return false;
			}
		} else {
			return false;
		}

	}

	@Override
	public int changeStatus(String status, Long id, String rem) {
		logger.info("UserServiceImpl.changeStatus()");
		return iUsersRepo.toggleStatus(status, id, rem);
	}

	@Override
	public Users findUserByEmail(String email) {
		logger.info("UserServiceImpl.findUserByEmail()");
		Users user = iUsersRepo.findByEmail(email);
		return user;
	}

	@Override
	public Users findUserByEmailandUid(String email, Long id) {
		logger.info("UserServiceImpl.findUserByEmailandUid()");
		return iUsersRepo.findByEmailAndUseridNot(email, id);
	}

	@Override
	public List<Users> getAllUsers() {
		logger.info("UserServiceImpl.getAllUsers()");
		return iUsersRepo.findAll();
	}

	@Override
	public Users finduserById(Long id) {
		logger.info("UserServiceImpl.finduserById()");
		return iUsersRepo.findById(id).get();
	}

	@Override
	public List<UserInfoDTO> finduserInfoById(Long id) {
		return iUsersRepo.getUserInfo(id);
	}

	@Override
	public boolean deleteUsers(Long id) {
		logger.info("UserServiceImpl.deleteUsers()");
		iUsersRepo.deleteById(id);
		return true;
	}

	@Override
	public List<Users> filterEmployee(String search) {
		logger.info("UserServiceImpl.filterEmployee()");
		List<Users> findAlln = new ArrayList();
		System.out.println(search);
		if (!search.equals("dummysearch")) {
			findAlln = iUsersRepo.getAll(search.trim());
		} else if (search.equals("dummysearch")) {
			findAlln = iUsersRepo.findAll();
		} else {
			findAlln = iUsersRepo.findAll();
		}
		return findAlln;
	}

	@Override
	public List<String> filterEmployee2(String search) {
		logger.info("UserServiceImpl.filterEmployee()");
		List<String> findAlln = new ArrayList();
		System.out.println(search);
		findAlln = iUsersRepo.search(search.trim());
		return findAlln;
	}

	@Override
	public Page<List<Users>> findPaginated(int pageNo, int pageSize) {
		logger.info("UserServiceImpl.findPaginated()");
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
		Page<List<Users>> findAll = iUsersRepo.getallbypagination(pageable);
		return findAll;
	}

	@Override
	public boolean updateUser(Users users) {
		Users user = iUsersRepo.findByEmailAndUseridNot(users.getEmail(), users.getUserid());
		if (user == null) {
			// try {
			iUsersRepo.save(users);
			// emailService.employeeRegistarionMail(users.getEmail(), users.getFullname());
			return true;
			// } catch (UnsupportedEncodingException | MessagingException e) {
			// e.printStackTrace();
			// return false;
			// }
		} else {
			return false;
		}
	}

	@Override
	public List<Object[]> getUser() {
		// TODO Auto-generated method stub
		return iUsersRepo.getUser();
	}

	@Override
	public List<GetRecruiter> getRecruiter() {
		return iUsersRepo.getRecruiter();
	}

	@Override
	public List<Object[]> managerDropDown() {
		return iUsersRepo.managerDropdown();
	}

	@Override
	public List<Object[]> TLDropDown(long id) {
		return iUsersRepo.TlDropdown(id);
	}

	@Override
	public int unlockUser(Users users) {
		try {
			emailService.unlockUserMail(users);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
		Users lockedUser = iUsersRepo.findById(users.getUserid()).get();
		lockedUser.setLocked(false);
		lockedUser.setRemarks(users.getRemarks());
		LocalDateTime logoutTime =  LocalDateTime.now();
		lockedUser.setLastLogout(logoutTime);
		iUsersRepo.save(lockedUser);
		return 0;
	}

}
