package com.narvee.usit.controller;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;

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
import com.narvee.usit.entity.Requirements;
import com.narvee.usit.entity.Users;
import com.narvee.usit.service.IRequirmentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/requirement")
@CrossOrigin
public class RequirmentsController {

	private static final Logger logger = LoggerFactory.getLogger(RequirmentsController.class);

	@Autowired
	private IRequirmentService service;

	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRequirments() {
		List<Requirements> allRequirments = service.getAllRequirments();
		Comparator<Requirements> comparator = (c1, c2) -> {
			return Long.valueOf(c2.getRequirementid()).compareTo(c1.getRequirementid());
		};
		Collections.sort(allRequirments, comparator);
		logger.info("!!! inside class : RecRequirmentsController, !! method : getAllRequirments");
		return new ResponseEntity<>(new RestAPIResponse("success", "fetch All Requirments", allRequirments),
				HttpStatus.OK);
	}

	@ApiOperation("To Delete Requirements")
	@ApiResponses({ @ApiResponse(code = 200, message = "Role Deleted"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteRequirmentByID(@PathVariable Long id) {
		logger.info("inside RequirmentsController.deleteRole()=> delete single role by id");
		boolean val = service.deleteRequirmentsByID(id);
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Requirement Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "Requirement Assigned to profile,Requirement Not Deleted", ""),
					HttpStatus.OK);
		}

	}

	@ApiOperation("To Save Requirment")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveRequirement(@RequestBody Requirements requirements) {
		logger.info("!!! inside class : RequirmentsController, !! method : saveRequirement");
		Object obj = service.saveRequirments(requirements);
		return new ResponseEntity<>(new RestAPIResponse("success", "Save Requirments Successfully", obj),
				HttpStatus.CREATED);

	}

	@ApiOperation("To update Requirment")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment updates"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateRequirement(@RequestBody Requirements requirements) {
		// System.out.println(requirements);
		logger.info("!!! inside class : RequirmentsController, !! method : saveRequirement");
		Object obj = service.updateRequirment(requirements);
		return new ResponseEntity<>(new RestAPIResponse("success", "Save Requirments Successfully", obj),
				HttpStatus.OK);

	}

	@ApiOperation("To get Requirment Number")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getmaxnumber", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getrequirementNumber() {
		logger.info("!!! inside class : RequirmentsController, !! method : getrequirementNumber");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Max requirement Number", service.findmaxReqNumber()), HttpStatus.OK);
	}

	@ApiOperation("To Save Requirment")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getrequirements", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getrequirements() {
		logger.info("!!! inside class : RequirmentsController, !! method : getrequirements");
		return new ResponseEntity<>(new RestAPIResponse("success", "fetch All Requirments", service.getrequirements()),
				HttpStatus.OK);
	}

	@ApiOperation("To Save Requirment")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment Saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getempl/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAssignedEmployees(@PathVariable Long id) {
		logger.info("!!! inside class : RequirmentsController, !! method : getAssignedEmployees");
		return new ResponseEntity<>(new RestAPIResponse("success", "fetch All Requirments Assigned EMployees",
				service.getAssignedemploy(id)), HttpStatus.OK);
	}

	// findmaxReqNumber

	@ApiOperation("To Fetch Requirment")
	@ApiResponses({ @ApiResponse(code = 200, message = "Requirment Fetched"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRequirmentByID(@PathVariable long id) {
		logger.info("!!! inside class : RequirmentsController, !! method : getRequirmentByID");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Requirment Fetch Success Fully", service.getRequrimentByID(id)),
				HttpStatus.OK);
	}

	@RequestMapping(value = "/editrequirments", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> editRequirmentByID(@RequestBody Requirements requirements) {
		logger.info("!!! inside class : RequirmentsController, !! method : editRequirmentByID");
		return new ResponseEntity<>(new RestAPIResponse("success", "updated Requirments Successfully",
				service.updateRequirments(requirements)), HttpStatus.ACCEPTED);
	}

	/*
	 * @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE,
	 * produces = "application/json") public ResponseEntity<RestAPIResponse>
	 * deleteRequirmentByID(@PathVariable long id) { logger.
	 * info("!!! inside class : RecRequirmentsController, !! method : deleteRequirmentByID"
	 * ); return new ResponseEntity<>( new RestAPIResponse("Success",
	 * "Deleted Requirment Successfully", service.deleteRequirmentsByID(id)),
	 * HttpStatus.OK); }
	 */

	@RequestMapping(value = "/pagerequirment/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRequirmentsByPageNo(@PathVariable("pageNo") int pageNo) {
		int pageSize = 2;
		logger.info("!!! inside class : RequirmentsController, !! method : getAllRequirmentsByPageNo");
		Page<Requirements> findPaginated = service.findPaginated(pageNo, pageSize);
		List<Requirements> findAlltech = findPaginated.getContent();
		return new ResponseEntity<>(new RestAPIResponse("success", "fetching Requirments By Page No", findAlltech),
				HttpStatus.OK);
	}

//	@RequestMapping(value = "/searchrequiments/{keyword}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<RestAPIResponse> getAllRequirmentsByFilter(@PathVariable String keyword) {
//		return new ResponseEntity<>(new RestAPIResponse("Success","fetch Requirments By search", service.getRequirmentByFiletr(keyword)),HttpStatus.OK);
//	}
}
