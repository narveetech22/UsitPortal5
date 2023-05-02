package com.narvee.usit.controller; /* created By Swamy   			*/

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Interview;
import com.narvee.usit.helper.ListInterview;
import com.narvee.usit.helper.ListRecruiter;
import com.narvee.usit.service.IinterviewService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/sales/interview")
@CrossOrigin
public class InterviewController {

	private static final Logger logger = LoggerFactory.getLogger(InterviewController.class);

	@Autowired
	public IinterviewService service;

	@ApiOperation("To Save Interview")
	@ApiResponses({ @ApiResponse(code = 200, message = "Interview Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveSalesInterview(@RequestBody Interview interview) {
		logger.info("!!! inside class : saveInterview, !! method : saveInterview");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Interview Save Succesfully", service.saveIterview(interview)),
				HttpStatus.CREATED);
	}

	@ApiOperation("To Delete Interview")
	@ApiResponses({ @ApiResponse(code = 200, message = "Interview Deleted"),
			@ApiResponse(code = 404, message = "Interview entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteSalesInterview(@PathVariable int id) {
		logger.info("!!! inside class : saveInterview, !! method : deleteInterview");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Interview Deleted Successfully", service.deleteInterviewById(id)),
				HttpStatus.OK);
	}

	@ApiOperation("To getAll Interview")
	@ApiResponses({ @ApiResponse(code = 200, message = " getAllInterview"),
			@ApiResponse(code = 404, message = "Interview entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all/{flg}/{access}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllInterview(@PathVariable String flg, @PathVariable String access,
			@PathVariable long userid) {
		logger.info("!!! inside class : getAllInterview, !! method : getAllInterview");
		List<ListInterview> alldata = service.getAllDetailsInterview(flg,access,userid);
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Interview fetched Successfully", alldata),
				HttpStatus.OK);
	}

	@ApiOperation("To Fetch Interview By InterviewID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Interview Fetched"),
			@ApiResponse(code = 404, message = "Interview entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getinterview/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getSalesInterviewByID(@PathVariable int id) {
		logger.info("!!! inside class : saveInterview, !! method : getSalesInterviewByID");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Interview Fetched Successfully", service.getInterviewByID(id)),
				HttpStatus.OK);
	}

	@ApiOperation("To fetch Interview By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched Interviews"),
			@ApiResponse(code = 404, message = "Interviews entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getpageinterview/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> findPaginated(@PathVariable("pageNo") int pageNo) {
		int pageSize = 1;
		logger.info("!!! inside class : saveInterview, !! method : findPaginated");
		Page<Interview> findPaginated = service.findPaginated(pageNo, pageSize);
		List<Interview> findAlltech = findPaginated.getContent();
		return new ResponseEntity<>(
				new RestAPIResponse("success", "fetching Interview By Page No Successfully", findAlltech),
				HttpStatus.OK);
	}

	@ApiOperation("To fetch Interview By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched Interviews"),
			@ApiResponse(code = 404, message = "Interviews entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/pagination/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> findPaginatednative(@PathVariable("pageNo") int pageNo) {
		int pageSize = 1;
		// Page<List<ListInterview>> findPaginateded(int pageNo, int pageSize);
		logger.info("!!! inside class : saveInterview, !! method : findPaginated");
		Page<List<ListInterview>> findPaginated = service.findPaginateded(pageNo, pageSize);// //service.findPaginateded(pageNo,
																							// pageSize);
		List<List<ListInterview>> findAlltech = findPaginated.getContent();

		// List<ListInterview> findAlltech = findPaginated.getContent();
		return new ResponseEntity<>(
				new RestAPIResponse("success", "fetching Interview By Page No Successfully", findAlltech),
				HttpStatus.OK);
	}

	@ApiOperation("To Edit/Update Interview")
	@ApiResponses({ @ApiResponse(code = 200, message = "Interview updated"),
			@ApiResponse(code = 404, message = "Interview entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/editinterview", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> editSalesInterview(@RequestBody Interview interview) {
		logger.info("!!! inside class : Interview, !! method : editInterview");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Interview Edited Succesdfully", service.saveIterview(interview)),
				HttpStatus.ACCEPTED);
	}

	@ApiOperation("To fetch consultant By id drop down  ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched salesconsultant"),
			@ApiResponse(code = 404, message = "salesconsultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getconsultant/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> consultantdropdown(@PathVariable long id) {
		logger.info("!!! inside class : interviewController, !! method : consultantdropdown");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Cons By id success", service.getSalesConsById(id)), HttpStatus.OK);
	}

	@ApiOperation("To fetch submission details drop down By id  ")
	@ApiResponses({ @ApiResponse(code = 200, message = "submission details drop down "),
			@ApiResponse(code = 404, message = " entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/submissiondetails/{flg}/{id}/{role}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getsubmissiondropdown(@PathVariable String flg,@PathVariable long id,@PathVariable String role) {
		logger.info("!!! inside class : interviewController, !! method : submission details drop down ");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "submission details drop down By id success", service.submissiondetails(flg,id,role)),
				HttpStatus.OK);
	}

}
