package com.narvee.usit.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.H1BApplicants;
import com.narvee.usit.service.IH1BApplicantsService;
import com.narvee.usit.service.IfileStorageService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/H1BApplicants")
@CrossOrigin
public class H1BApplicantsController {

	private static final Logger logger = LoggerFactory.getLogger(H1BApplicantsController.class);

	@Autowired
	private IH1BApplicantsService h1bApplicantsService;

	@Autowired
	private IfileStorageService fileStorageService;

	@ApiOperation("To save H1BApplicant")
	@ApiResponses({ @ApiResponse(code = 200, message = "H1BApplicant saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveH1BApplicant(@RequestBody H1BApplicants h1bApplicant) {
		logger.info("!!! inside class: H1BApplicantController, !! method: save H1BApplicant");
		return new ResponseEntity<>(new RestAPIResponse("success", "saved H1BApplicants Entity",
				h1bApplicantsService.saveH1BApplicants(h1bApplicant)), HttpStatus.CREATED);
	}

	// passportdoc everifydoc i9doc I797doc i94doc ssndoc
	@RequestMapping(value = "/h1docs/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> uploadMultipleFile2(
			@RequestParam(required = false, value = "passportdoc") MultipartFile passportdoc,
			@RequestParam(required = false, value = "everifydoc") MultipartFile everifydoc,
			@RequestParam(required = false, value = "i9doc") MultipartFile i9doc, @PathVariable Long id,
			@RequestParam(required = false, value = "I797doc") MultipartFile I797doc,
			@RequestParam(required = false, value = "i94doc") MultipartFile i94doc,
			@RequestParam(required = false, value = "ssndoc") MultipartFile ssndoc) throws IOException {
		
		H1BApplicants entity = h1bApplicantsService.getH1BApplicantsById(id);
		String passportdocrf = entity.getPassportdoc();
		String everifydocrf = entity.getEverifydoc();
		String i9docrf = entity.getI9doc();
		String I797docrf = entity.getI797doc();
		String i94docrf = entity.getI94doc();
		String ssndocrf = entity.getSsndoc();
		String mobileNumber = entity.getEmployeename();
		try {
			passportdocrf = fileStorageService.storeFile(passportdoc, mobileNumber, "PassPort");
			entity.setPassportdoc(passportdocrf);
		} catch (NullPointerException e) {
		}

		try {
			everifydocrf = fileStorageService.storeFile(everifydoc, mobileNumber, "Everify");
			entity.setEverifydoc(everifydocrf);
		} catch (NullPointerException e) {
		}

		try {
			i9docrf = fileStorageService.storeFile(i9doc, mobileNumber, "I9");
			entity.setI9doc(i9docrf);
		} catch (NullPointerException e) {
		}
		try {
			I797docrf = fileStorageService.storeFile(I797doc, mobileNumber, "I797");
			entity.setI797doc(I797docrf);
		} catch (NullPointerException e) {
		}

		try {
			i94docrf = fileStorageService.storeFile(i94doc, mobileNumber, "I94DOC");
			entity.setI94doc(i94docrf);
		} catch (NullPointerException e) {
		}

		try {
			ssndocrf = fileStorageService.storeFile(ssndoc, mobileNumber, "SSN");
			entity.setSsndoc(ssndocrf);
		} catch (NullPointerException e) {
		}
		h1bApplicantsService.saveH1BApplicants(entity);
		//h1bApplicantsService.update(passportdocrf, everifydocrf, i9docrf, I797docrf, i94docrf, ssndocrf, id);
		return new ResponseEntity(new RestAPIResponse("success", "Uploaded files"), HttpStatus.OK);
	}

	@ApiOperation("To getAll H1BApplicants")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll H1BApplicants"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllH1BApplicants() {
		logger.info("!!! inside class: H1BApplicantsController, !! method: getAllH1BApplicants");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "getall Cities", h1bApplicantsService.getAllH1BApplicants()),
				HttpStatus.OK);
	}

	@ApiOperation("To delete H1BApplicant")
	@ApiResponses({ @ApiResponse(code = 200, message = "delete H1BApplicant"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteH1BApplicantByID(@PathVariable long id) {
		logger.info("!!! inside class: H1BApplicantController, !! method: deleteH1BApplicantByID");
		try {
			h1bApplicantsService.deleteH1BApplicantsById(id);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new RestAPIResponse("fail", "Cannot delete perform operation"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "delete H1BApplicant"), HttpStatus.OK);
	}

	@ApiOperation("To get H1BApplicant")
	@ApiResponses({ @ApiResponse(code = 200, message = "get H1BApplicant"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "getH1BApplicant/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getH1BApplicantById(@PathVariable long id) {
		logger.info("!!! inside class: H1BApplicantController, !! method: getH1BApplicantById");
		System.out.println(h1bApplicantsService.getH1BApplicantsById(id));
		return new ResponseEntity<>(
				new RestAPIResponse("success", "get H1BApplicant", h1bApplicantsService.getH1BApplicantsById(id)),
				HttpStatus.OK);
	}

	@ApiOperation("H1 Download files ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Resume"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/download/{id}/{flg}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadresume(@PathVariable long id, @PathVariable String flg) throws IOException {
		Resource file = h1bApplicantsService.downloadfile(id, flg);
		Path path = file.getFile().toPath();
		logger.info("!!! inside class: ConsultantController, method : Consultant Download Resume");
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ApiOperation("Consultant Download Resume ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Resume"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/removefile/{id}/{flg}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> removeFile(@PathVariable long id, @PathVariable String flg)
			throws IOException {
		logger.info("!!! inside class: ConsultantController, method : Consultant Download Resume");
		h1bApplicantsService.removeFile(id, flg);
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Consultant Submitted  for the requirement ,Consultant Not Deleted"),
				HttpStatus.OK);
	}

}
