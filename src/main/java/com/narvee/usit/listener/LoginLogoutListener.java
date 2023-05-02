package com.narvee.usit.listener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AbstractAuthenticationEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.narvee.usit.entity.Users;
import com.narvee.usit.exception.UserLockedException;
import com.narvee.usit.exception.UserNotFoundException;
import com.narvee.usit.repository.IUsersRepository;

@Component
public class LoginLogoutListener implements ApplicationListener<AbstractAuthenticationEvent> {

	@Autowired
	IUsersRepository userRepo;

	private final Logger logger = LoggerFactory.getLogger(LoginLogoutListener.class);

	@Override
	public void onApplicationEvent(AbstractAuthenticationEvent event) {
		if (event instanceof AuthenticationSuccessEvent) {
			UserDetails userDetails = (UserDetails) event.getAuthentication().getPrincipal();
			logger.info("User logged in: {}", userDetails);
			System.out.println("User details:" + userDetails);
			// Get current date/time
			LocalDateTime loginTime = LocalDateTime.now();
			logger.info("Login time: {}", loginTime);
			System.out.println("User Logged In Time:" + loginTime);

			// Save loginTime to database or any other storage
			Users user = userRepo.findByEmail(userDetails.getUsername());
			if (user != null) {
				String email = user.getEmail();
				user.setLastLogin(loginTime);
				userRepo.save(user);
			} // end of inner if

		} // end of outer if
		else if (event instanceof LogoutSuccessEvent) {
			Authentication authentication = event.getAuthentication();
			if (authentication != null && authentication.getPrincipal() != null) {
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				logger.info("User logged out: {}", userDetails);
				// Get current date/time
				LocalDateTime logoutTime = LocalDateTime.now();
				System.out.println("User Logout Time:" + logoutTime);
				logger.info("Logout time: {}", logoutTime);
				// Update the logoutTime in database or any other storage
				Users user = userRepo.findByEmail(userDetails.getUsername());
				if (user != null) {
					user.setLastLogout(logoutTime);
					userRepo.save(user);
				} // end of inner if
			} // end of outer if
		} // end of else if

	} // end of method

}
// end of class
