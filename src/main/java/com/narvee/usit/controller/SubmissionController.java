package com.narvee.usit.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

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
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.dto.SubmissionDTO;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Submissions;
import com.narvee.usit.service.ISubmissionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/submission")
@CrossOrigin
public class SubmissionController {

	private static final Logger logger = LoggerFactory.getLogger(SubmissionController.class);
	@Autowired
	ISubmissionService service;

	@ApiOperation("To save Submission")
	@ApiResponses({ @ApiResponse(code = 200, message = "Submission saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "savesubmission", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> savingsubmission(@RequestBody Submissions submission) {
		logger.info("!!! inside class : SubmissionController, !! method: savingsubmission ");
		if (submission.getSubmissionid() == null) {
			Optional<Submissions> entity = service
					.findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPosition(
							submission.getConsultant().getConsultantid(), submission.getProjectlocation(),
							submission.getVendor().getVmsid(), submission.getPosition());
			if (entity.isEmpty()) {
				logger.info("!!! inside class : SubmissionController, !! method: savingsubmission ");
				return new ResponseEntity<>(new RestAPIResponse("success", "Submission added Successfully",
						service.saveSubmission(submission)), HttpStatus.CREATED);
			} else {
				logger.info("!!! inside class : SubmissionController, !! method: savingsubmission ");
				return new ResponseEntity<>(new RestAPIResponse("duplicate"), HttpStatus.OK);
			}
		} else {
			Optional<Submissions> entity = service
					.findByConsultantConsultantidAndProjectlocationAndVendorVmsidAndPositionAndSubmissionidNot(
							submission.getConsultant().getConsultantid(), submission.getProjectlocation(),
							submission.getVendor().getVmsid(), submission.getPosition(),submission.getSubmissionid());
			if (entity.isEmpty()) {
				logger.info("!!! inside class : SubmissionController, !! method: savingsubmission ");
				return new ResponseEntity<>(new RestAPIResponse("success", "Submission added Successfully",
						service.saveSubmission(submission)), HttpStatus.CREATED);
			} else {
				logger.info("!!! inside class : SubmissionController, !! method: savingsubmission ");
				return new ResponseEntity<>(new RestAPIResponse("duplicate"), HttpStatus.OK);
			}
			
		}
	}

	@ApiOperation("Get all Entities")
	@RequestMapping(value = "/all/{flg}/{access}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllSubmission(@PathVariable String flg, @PathVariable String access,
			@PathVariable long userid) {
		logger.info("!!! inside class : SubmissionController, !! method: getAllSubmission ");
		List<SubmissionDTO> getall = service.getAllSubmission(flg, access, userid);
		return new ResponseEntity<>(new RestAPIResponse("Success", "fetch All Submissions", getall), HttpStatus.OK);
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteSubmissionById(@PathVariable long id) {
		// logger.info("!!! inside class : SalesSubmissionController, !! method :
		logger.info("!!! inside class : SubmissionController, !! method: deleteSubmissionById ");
		boolean val = service.deleteSubmission(id);
		if (val == true) {
			logger.info("!!! inside class : SubmissionController, !! method: Submission Deleted ");
			return new ResponseEntity<>(new RestAPIResponse("Success", "Submission Deleted", ""), HttpStatus.OK);
		} else {
			logger.info(
					"!!! inside class : SubmissionController, !! method: Interview Schedule for Submission, Submission Not Deleted ");
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "Interview Schedule for Submission, Submission Not Deleted", ""),
					HttpStatus.OK);
		}

	}

	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getSalesSubmissionByID(@PathVariable long id) {
		Optional<Submissions> submission = service.getSubmissionByID(id);
		System.out.println(submission);
		return new ResponseEntity<>(new RestAPIResponse("Success", "SuccesfullyFetched ", submission), HttpStatus.OK);
	}

}
