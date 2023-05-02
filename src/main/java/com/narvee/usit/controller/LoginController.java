package com.narvee.usit.controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.service.ILoginService;
import com.narvee.usit.service.IUserService;
import com.narvee.usit.serviceimpl.EmailService;
import com.narvee.usit.serviceimpl.LoginServiceImpl;
import com.narvee.usit.serviceimpl.UserAuthService;
import com.narvee.usit.util.JwtUtil;
import com.narvee.usit.vo.Response;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Users;
import com.narvee.usit.exception.DisabledUserException;
import com.narvee.usit.exception.InvalidUserCredentialsException;
import com.narvee.usit.helper.ResetPassword;
import com.narvee.usit.helper.Utility;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.bytebuddy.utility.RandomString;

@RestController
@RequestMapping("/login/")
@CrossOrigin
public class LoginController {
	public static final Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	public ILoginService loginService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private JwtUtil jwtUtil;
	
	@Value("${unlock.sadmin}")
	private String sadmin;
	
	@Value("${unlock.admin}")
	private String admin;

	@Autowired
	private UserAuthService userAuthService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private IUserService userService;

	@PostMapping("/signin")
	public ResponseEntity<RestAPIResponse> generateJwtToken(@RequestBody Users request,
			HttpServletRequest httprequest) {
		logger.info("!!! inside class: LoginController, !! method: generateJwtToken ");
		Authentication authentication = null;
		try {
			authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

		} catch (DisabledException e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => User Inactive");
			throw new DisabledUserException("User Inactive");
		} catch (BadCredentialsException e) {
			logger.info("!!! inside class: LoginController, !! method: generateJwtToken => Invalid Credentials");
			throw new InvalidUserCredentialsException("Invalid Credentials");
		}

		// getting logged in user's system ip & system name
		String ipAddress = httprequest.getHeader("X-Forwarded-For");
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httprequest.getHeader("Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httprequest.getHeader("WL-Proxy-Client-IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httprequest.getHeader("HTTP_CLIENT_IP");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httprequest.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
			ipAddress = httprequest.getRemoteAddr();
		}

		String systemIp = null;
		String systemName = null;
		InetAddress inetAddress;

		try {
			inetAddress = InetAddress.getLocalHost();
			System.out.println("Logged-in user system ip address:" + inetAddress);
			String str1 = String.valueOf(inetAddress);
			int val = str1.indexOf("/");
			systemName = str1.substring(0, val);
			systemIp = str1.substring(val + 1);
		} catch (UnknownHostException e) {

		}

		User user = (User) authentication.getPrincipal();
		Set<String> roles = user.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet());
		String token = jwtUtil.generateToken(authentication);
		Response response = new Response();
		response.setToken(token);
		roles.forEach(rl -> {
			response.setRoles(rl);
		});
		response.setFullname(user.getUsername());
		if (user != null) {
			String email = request.getEmail();
			// Optional<Users> userDetails = loginService.findByEmail(email).get();
			Users userDetails2 = loginService.findByEmail(email);
			Users userDetails = userDetails2;
			// saving user log in ip address
			userDetails.setSystemip(systemIp);
			userDetails.setSystemname(systemName);
			userService.saveUser(userDetails);

			LocalDateTime logoutTime = userDetails.getLastLogout();
			if(logoutTime==null) {
				logoutTime = LocalDateTime.now();
			}
			LocalDateTime currenetTime = LocalDateTime.now();
			long minutesSinceLastLogin = ChronoUnit.DAYS.between(logoutTime, currenetTime);
			if(userDetails.getRole().getRolename().equalsIgnoreCase(admin) || userDetails.getRole().getRolename().equalsIgnoreCase(sadmin)){
			}
			else {
				if (minutesSinceLastLogin > 3) {
					userDetails.setLocked(true);
					userService.saveUser(userDetails);
					return new ResponseEntity<>(
							new RestAPIResponse("locked", "User in-active for for 5 Minutes", "User Account Locked"),
							HttpStatus.OK);
				}
			}
			
			String name = userDetails.getFullname();
			String dept = userDetails.getDepartment();
			if (dept.equalsIgnoreCase("Bench Sales")) {
				name = userDetails.getPseudoname();
			}
			response.setFullname(name);
			response.setDesignation(userDetails.getDesignation());
			response.setUserid(userDetails.getUserid());
			// response.setRoleno(userDetails.getRole().getRoleno());
			response.setDepartment(dept);
			logger.info("!!! inside class: LoginController, !! method: UserDetails => ");
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "User logged in successful", response),
				HttpStatus.OK);
	}

	@SuppressWarnings("unused")
	@RequestMapping(value = "/sendlink", method = RequestMethod.POST, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "User login Successfull"),
			@ApiResponse(code = 404, message = "login page not found"),
			@ApiResponse(code = 500, message = "Internal Server babu") })
	public ResponseEntity<RestAPIResponse> processForgotPassword(HttpServletRequest request,
			@RequestBody ResetPassword user) {
		logger.info("!!! inside class: LoginController, !! method: processForgotPassword => ");

		String token = RandomString.make(30);
		String email = user.getEmail();
		Users validateuser = loginService.findByEmail(email);
		if (validateuser != null) {
			try {
				logger.info("!!! inside class: LoginController, !! method: processForgotPassword => ");
				loginService.updateResetPasswordToken(token, email);// http://localhost:4200/#/forgot-password
				String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
				// Kiran Commented
				emailService.resetlinkmail(validateuser.getFullname(), email, resetPasswordLink);
			} catch (Exception e) {
			}
			return new ResponseEntity<>(new RestAPIResponse("success", "Your Password has been changed", ""),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "Please enter registered email", "InValid Credentials"), HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/change_password", method = RequestMethod.POST, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "User login Successfull"),
			@ApiResponse(code = 404, message = "login page not found"),
			@ApiResponse(code = 500, message = "Internal Server babu") })
	public ResponseEntity<RestAPIResponse> changePasswordExternalLink(@RequestBody ResetPassword restpswEntity) {
		Users validateuser = loginService.findByEmail(restpswEntity.getEmail());
		if (validateuser == null) {
			return new ResponseEntity<>(new RestAPIResponse("mailerror", "Email not registered with Narvee Portal"),
					HttpStatus.OK);
		} else {
			Users validuser = validateuser;
			validuser.setPassword(passwordEncoder.encode(restpswEntity.getNewpassword()));
			loginService.updatePassword(validuser, restpswEntity.getNewpassword());
			return new ResponseEntity<>(new RestAPIResponse("success", "Your Password has been changed"),
					HttpStatus.OK);
		}

	}

	// reset password from my user rofile
	@SuppressWarnings("unused")
	@RequestMapping(value = "/reset_password", method = RequestMethod.POST, produces = "application/json")
	@ApiResponses({ @ApiResponse(code = 200, message = "User login Successfull"),
			@ApiResponse(code = 404, message = "login page not found"),
			@ApiResponse(code = 500, message = "Internal Server babu") })
	public ResponseEntity<RestAPIResponse> loginCheck(@RequestBody ResetPassword restpswEntity) {
		System.out.println(restpswEntity);
		String reOldpsw = restpswEntity.getPassword();
		String newPassword = restpswEntity.getNewpassword();
		Users users = loginService.findbyuserid(restpswEntity.getUserid());
		String dboldpassword = users.getPassword();
		if (!passwordEncoder.matches(reOldpsw, dboldpassword)) {
			return new ResponseEntity<>(new RestAPIResponse("fail", "password is incorrect", ""), HttpStatus.OK);
		} else {

			if (newPassword.equals(reOldpsw)) {
				return new ResponseEntity<>(
						new RestAPIResponse("samepassword", "New Password And OldPassword are Same"), HttpStatus.OK);
			} else {
				users.setPassword(passwordEncoder.encode(newPassword));
				loginService.updatePassword(users, restpswEntity.getNewpassword());
				return new ResponseEntity<>(new RestAPIResponse("success", "Your Password has been changed"),
						HttpStatus.OK);
			}
		}

	}

	@GetMapping("/logout/{id}")
	public ResponseEntity<?> signOut(@PathVariable("id") Long id) {
		Users optionalUser = userService.finduserById(id);
		if (optionalUser != null) {
			LocalDateTime local = LocalDateTime.now();
			optionalUser.setLastLogout(local);
			userService.saveUser(optionalUser);
			return ResponseEntity.ok().build();
		} else {
			return ResponseEntity.notFound().build();
		}
	}

}
