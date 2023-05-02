package com.narvee.usit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.ApiResponses;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Qualification;
import com.narvee.usit.service.IQualificationService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
@RestController
@RequestMapping("/qualification")
@CrossOrigin
public class QualificationController {

	private static final Logger logger = LoggerFactory.getLogger(QualificationController.class);

	@Autowired
	private IQualificationService qualificationService;

	@ApiOperation("To save Qualification")
	@ApiResponses({ @ApiResponse(code = 200, message = "qualification saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "saveQualification", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveCity(@RequestBody Qualification qualification) {
		logger.info("!!! inside class: QualificationController, !! method: saveQualification");
		boolean saveQualification = qualificationService.saveQualification(qualification);
		if (saveQualification) {
			return new ResponseEntity<>(new RestAPIResponse("success", "saved Qualification Entity", "Entity Saved"),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("duplicate", "Record Already Exists", "Not Saved"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To get one Qualification")
	@ApiResponses({ @ApiResponse(code = 200, message = "get one Qualification"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "getQualificationById/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getQualificationById(@PathVariable Long id) {
		logger.info("!!! inside class: QualificationController, !! method: getQualification");

		Qualification qualification = qualificationService.getQualificationById(id);
		return new ResponseEntity<RestAPIResponse>(new RestAPIResponse("Success", "Get Qualification", qualification),
				HttpStatus.OK);
	}

	@ApiOperation("To getAll Qualifications")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll Qualifications"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllQualifications() {
		logger.info("!!! inside class: QualificationController, !! method: getAllQualifications");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "getall Cities", qualificationService.getAllQualifications()),
				HttpStatus.OK);
	}

	@ApiOperation("To delete Qualification")
	@ApiResponses({ @ApiResponse(code = 200, message = "delete Qualifications"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteQualificationByID(@PathVariable long id) {
		logger.info("!!! inside class: QualificationController, !! method: deleteQualificationByID");
		try {
			qualificationService.deleteQualificationById(id);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new RestAPIResponse("fail", "Cannot perform operation"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "delete Qualification"), HttpStatus.OK);
	}
}
