package com.narvee.usit.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Email;
import com.narvee.usit.service.IEmailBackupService;
import com.narvee.usit.vo.RequestForArrayType;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/mail")
@CrossOrigin
public class EmailBackupController {

	private static final String GET_EMPLOYEES_ENDPOINT_URL = "http://localhost:9070/kiran/one";

	private static final String GET_EMPLOYEES_ENDPOINT_URL1 = "http://localhost:9070/kiran/two";

	@Autowired
	private IEmailBackupService service;

	// insert into db
	@RequestMapping(value = "/insertmails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> insertmails() {
		// List<Email> squareSet =
	//	System.out.println("started ========");
		String host = "smtp.narveetech.com";
		String port = "993";// 995 pop3
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		service.downloadEmailAttachments(host, port, username, password);
		// service.deleteMessages2(host, port, username, password, "subject");
		// service.deleteMessages(host,"993", username, password);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", ""), HttpStatus.OK);
	}

	@RequestMapping(value = "/deleteMailByIds", method = RequestMethod.POST, produces = "application/json", consumes = "application/json")
	public ResponseEntity<RestAPIResponse> deleteMailByIds(@RequestBody RequestForArrayType req) {
	//	service.deleteAllByIdInBatch(req.getIds());
		return new ResponseEntity<>(new RestAPIResponse("Success"), HttpStatus.OK);
	}

	@RequestMapping(value = "/getMails", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllMails() {
		List<Email> email = service.getAllEmails();
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", email), HttpStatus.OK);
	}

	// delete from inbox and move to drafts completed
	@RequestMapping(value = "/deleteAndMove", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteAndMove() {
		// List<Email> squareSet =
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		// service.downloadEmailAttachments(host, port, username, password);
		// service.deleteMessages2(host, port, username, password, "subject");
		service.deleteMessages(host, "993", username, password);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", ""), HttpStatus.OK);
	}

	private static RestTemplate restTemplate = new RestTemplate();

	@RequestMapping(value = "/restend", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> restendPointCalling() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> allcity = restTemplate.exchange(GET_EMPLOYEES_ENDPOINT_URL, HttpMethod.GET, entity,
				String.class);
		System.out.println(allcity);

		ResponseEntity<Object[]> responseEntity = restTemplate.getForEntity(GET_EMPLOYEES_ENDPOINT_URL1,
				Object[].class);
		Object[] objects = responseEntity.getBody();
		//System.out.println(objects);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", allcity), HttpStatus.OK);
	}

	@RequestMapping(value = "/mail", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getmails() {
		// List<Email> squareSet =
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		// service.downloadEmailAttachments(host, port, username, password);
		List<Email> downloadEmailAttachments = service.downloadEmailAttachments(host, port, username, password);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", downloadEmailAttachments),
				HttpStatus.OK);
	}

//	@ApiOperation("To save Previleges")
//	@ApiResponses({ @ApiResponse(code = 200, message = "Previleges saved"),
//			@ApiResponse(code = 404, message = "entity not found"),
//			@ApiResponse(code = 500, message = "Internal Server error") })
//	@RequestMapping(value = "/delete1", method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<RestAPIResponse> savePrevg(@RequestBody Request1 previleges) {
//		return new ResponseEntity<>(new RestAPIResponse("Success", "Previleges Saved", ""), HttpStatus.CREATED);
//	}

	@RequestMapping(value = "/delete2/{subject}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteMessages(@PathVariable String subject) {
		// List<Email> squareSet =
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		service.downloadEmailAttachments(host, port, username, password);
		/// service.deleteMessages2(host, port, username, password, subject);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", ""), HttpStatus.OK);
	}

	@RequestMapping(value = "/move", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> movemail() {
		// List<Email> squareSet =
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		// service.downloadEmailAttachments(host, port, username, password);
		String subject = "kkk";
		// service.deleteMessages2(host, port, username, password, subject);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", ""), HttpStatus.OK);
	}

	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMailByID(@PathVariable long id) {
		// List<Email> squareSet =
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		// service.downloadEmailAttachments(host, port, username, password);
		Optional<Email> getmailbyid = service.getmailbyid(id);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fetched All Emails", getmailbyid), HttpStatus.OK);
	}

	@GetMapping("/mail11")
	public void save() {
		System.out.println("hello kiran");
//		String host = "smtp.narveetech.com";
//        String port = "995";
//        final String username= "saikiran@narveetech.com";  
//		final String password= "Narvee123$";//change accordingly  
//		
		String host = "smtp.narveetech.com";
		String port = "995";// 995 110
		final String username = "swamy@narveetech.com";
		final String password = "Narvee123$";// change accordingly
		// service.saveBackup(host, port, username, password);
		// try {
		List<Email> downloadEmailAttachments = service.downloadEmailAttachments(host, port, username, password);
//		} catch (MessagingException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	@RequestMapping(value = "/getMails/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMail(@PathVariable long id) {
//service.getEmail(id)
		return new ResponseEntity<>(new RestAPIResponse("Success", "Fethed Emails By ID", ""), HttpStatus.OK);
	}

	@RequestMapping(value = "/getMailsK/{keyword}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getMailByKeyord(@PathVariable String keyword) {
//service.findEmailByFilter(keyword)
		return new ResponseEntity<>(new RestAPIResponse("Success", "Successfully Fetched By Keyword", ""),
				HttpStatus.OK);
	}

}
