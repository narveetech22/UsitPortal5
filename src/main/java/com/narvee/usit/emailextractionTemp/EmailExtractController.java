package com.narvee.usit.emailextractionTemp;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.UsitPortalApplication;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Email;
import com.narvee.usit.service.IEmailBackupService;
import com.narvee.usit.vo.Request;

@RestController
@RequestMapping("/email")
@CrossOrigin
public class EmailExtractController {

	private static final Logger logger = LoggerFactory.getLogger(EmailExtractController.class);

	@Autowired
	private IEmailExtract service;

	@RequestMapping(value = "/extractemails", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> extractEmail(@RequestBody Request request) {
		// System.out.println("Start Of Mail Extract");
		//System.out.println(request);
		logger.info(" extractEmail ");
		//String host = "smtp.narveetech.com";
		//String port = "993";// 995 pop3
		
		
		//fot gmail
		String host = "smtp.gmail.com";
		String port = "993";// 995 pop3
		
		final String username = request.getUserName(); // "swamy@narveetech.com";
		final String password = request.getUserPwd(); // "Narvee123$";// change accordingly
		Date fromdate = request.getFromdate();
		Date todate = request.getTodate();
		String message = "nDomain";
		HttpStatus Status = HttpStatus.OK;
		String msg = "success";
		try {
			String result = username.substring(username.indexOf("@") + 1, username.indexOf("."));
//			if (result.equalsIgnoreCase("narveetech") || result.equalsIgnoreCase("narvee")
//					|| result.equalsIgnoreCase("gmail")) {
				//message = service.mailExtraction(host, port, username, password, fromdate, todate);
				//logger.info(" Calling mail completed " + msg);
//			} else {
//				msg = "domain_not_matched";
//				logger.info(" Other Domian " + msg);
//				return new ResponseEntity<>(new RestAPIResponse(msg, "Fetched All Emails", ""), HttpStatus.OK);
//			}

		} catch (StringIndexOutOfBoundsException e3) {
		}
		message = service.mailExtraction(host, port, username, password, fromdate, todate);
		logger.info(" Calling mail completed " + msg);
		logger.info(" End Of Mail Kiran " + msg + "," + message);
		logger.info(" End Of Mail karan " + message);
		if (message.equalsIgnoreCase("error1")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "fail";
		} else if (message.equalsIgnoreCase("error2")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "fail";
		}

		else if (message.equalsIgnoreCase("Authentication Error")) {
			Status = HttpStatus.NOT_IMPLEMENTED;
			msg = "AuthError";
		} else if (message.equalsIgnoreCase("Success")) {
			Status = HttpStatus.OK;
			msg = "success";
		}
		else if (message.equalsIgnoreCase("nDomain")) {
			Status = HttpStatus.OK;
			msg = "success";
		}
		else {
			msg = "fail";
			Status = HttpStatus.NOT_IMPLEMENTED;
		}
		logger.info(" Everithing " + msg);
		logger.info(" End Of Mail " + msg + "," + message);
		return new ResponseEntity<>(new RestAPIResponse(msg, "Fetched All Emails", ""), HttpStatus.OK);
	}

	@RequestMapping(value = "/getMails22", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllMails() {
		List<ListExtractMailDTO> email = service.listAll();
		logger.info(" getAllMails ");
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", email), HttpStatus.OK);
	}
	
	//created api for fetching all data from database
	@RequestMapping(value = "/getMails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllMailWithAttachment(){
		logger.info(" getAllMailsWithAttachment ");
		List<ExtractEmail> allmails = service.findAllMailDataWithAttachment();
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Data from ExtractEmail and EmailAttachment",allmails),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMailByID(@PathVariable long id) {
		Optional<ExtractEmail> getmailbyid = service.getmailbyid(id);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", getmailbyid), HttpStatus.OK);
	}

}
