package com.narvee.usit.serviceimpl;

import java.io.UnsupportedEncodingException;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.narvee.usit.entity.Users;
import com.narvee.usit.repository.IUsersRepository;
import com.narvee.usit.service.ILoginService;

@Service
public class LoginServiceImpl implements ILoginService {
	public static final Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);
	@Autowired
	private IUsersRepository userRepo;

	@Autowired
	private EmailService emailService;

	// signin check LoginController
	@Override
	public Users findByEmail(String email) {
		logger.info("LoginServiceImpl.findByEmail()");
		// Users fingbyEmail = userRepo.findByEmail(email);
		return userRepo.findByEmail(email);
	}

	@Override
	public Users getByuserCredentials(String email, String password) {
		logger.info("LoginServiceImpl.getByuserCredentials()");
		Users userDetails = userRepo.finByEmailAndPassword(email, password);
		if (userDetails == null) {
			logger.info("VisaServiceImpl.saveVms()");
			throw new NoSuchElementException("No value present");
		}
		return userDetails;
	}

	@Override
	@Modifying
	public void updatePassword(Users user, String newPsw) {
		logger.info("LoginServiceImpl.updatePassword()");
		// user.setPassword(pwd);
		user.getEmail();
		userRepo.save(user);
		/* kiran commented
		try {
			userRepo.save(user);
			String username = user.getFullname();
			if (user.getDepartment().equalsIgnoreCase("Bench Sales")) {
				username = user.getPseudoname();
			}
			emailService.resetpasswordmail(user.getEmail(), newPsw, username);
		} catch (UnsupportedEncodingException | MessagingException e) {
			e.printStackTrace();
		}
		*/

	}

	@Override
	public Users findbyuserid(long userid) {
		logger.info("LoginServiceImpl.findbyuserid()");
		return userRepo.findByUserid(userid);
	}

	@Override
	public void updateResetPasswordToken(String token, String email) {
		logger.info("LoginServiceImpl.updateResetPasswordToken()");
		Users customer = userRepo.findByEmail(email);
		if (customer!=null) {
			//customer.setResetPasswordToken(token);
			userRepo.save(customer);
		}
	}

	@Override
	public Users findBytoken(String token) {
		logger.info("LoginServiceImpl.findBytoken()");
		return userRepo.findByResetPasswordToken(token);
	}

}
