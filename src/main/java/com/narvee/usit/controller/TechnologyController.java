package com.narvee.usit.controller;

import java.sql.SQLIntegrityConstraintViolationException;
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
import com.narvee.usit.entity.Technologies;
import com.narvee.usit.service.ITechnologyService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/technology")
@CrossOrigin
public class TechnologyController {
	public static final Logger logger = LoggerFactory.getLogger(TechnologyController.class);
	@Autowired
	public ITechnologyService service;

	@ApiOperation("To save technology")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy saved"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveTechnology(@RequestBody Technologies technologies) {
		logger.info("TechnologyController.saveTechnology()" + technologies);
		// System.out.println(technologies);
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Technology Saved", service.saveTechnologies(technologies)),
				HttpStatus.CREATED);
	}

	@ApiOperation("To update technology")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy saved"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/technologies", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateTechnology(@RequestBody Technologies technologies) {
		// System.out.println(technologies);
		logger.info("TechnologyController.updateTechnology()" + technologies);
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Technology Saved", service.saveTechnologies(technologies)),
				HttpStatus.OK);
	}

	@ApiOperation("To fetch technology")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy fetched"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getall() {
		logger.info("TechnologyController.getall()");
		List<Technologies> allTechnologies = service.getAllTechnologies();
		Comparator<Technologies> comparator = (c1, c2) -> {
			return Long.valueOf(c2.getId()).compareTo(c1.getId());
		};
		//
		Collections.sort(allTechnologies, comparator);

		// List<Object[]> allTechnologies2 = service.getAllTechnologies2();;
		return new ResponseEntity<>(new RestAPIResponse("success", "Technology Saved", allTechnologies), HttpStatus.OK);
	}

	@ApiOperation("To fetch Technology By ID ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy fetched"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getTechnologyByID(@PathVariable int id) {
		logger.info("TechnologyController.getTechnologyByID()");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Get Technology By ID", service.getTechnologyByID(id)), HttpStatus.OK);
	}

//	@ApiOperation("To Edit Technology By ID ")
//	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy updated"),
//			@ApiResponse(code = 404, message = "Technology entity not found"),
//			@ApiResponse(code = 500, message = "Internal Server error") })
//	@RequestMapping(value = "/updatetech", method = RequestMethod.POST, produces = "application/json")
//	public ResponseEntity<RestAPIResponse> updateTechnologyByID(@RequestBody Technologies technologies) {
//		logger.info("TechnologyController.updateTechnologyByID()");
//		return new ResponseEntity<>(
//				new RestAPIResponse("Success", "updated Success", service.saveTechnologies(technologies)),
//				HttpStatus.OK);
//	}

	@ApiOperation("To delete Technology By ID ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy Deleted"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteTechnologyByID(@PathVariable int id) {
		logger.info("TechnologyController.deleteTechnologyByID()");
		String msg = "";
		try {
			service.deleteTechnologiesById(id);
			msg = "Success";
			return new ResponseEntity<>(new RestAPIResponse("success", "deleted Successfully", msg), HttpStatus.OK);
		} catch (SQLIntegrityConstraintViolationException e) {
			msg = "Fail";
			e.printStackTrace();
			return new ResponseEntity<>(new RestAPIResponse("Fail", "deleted Successfully", msg),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// return new ResponseEntity<>(new RestAPIResponse("success", "deleted
		// Successfully", msg), HttpStatus.INTERNAL_SERVER_ERROR);
	}

//	@ApiOperation("To fetch VMS By PageNo ")
//	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
//			@ApiResponse(code = 404, message = "VMS entity not found"),
//			@ApiResponse(code = 500, message = "Internal Server error") })
//	@RequestMapping(value = "/pagination/{pageNo}", method = RequestMethod.GET, produces = "application/json")
//	public ResponseEntity<RestAPIResponse2> findPaginated(@PathVariable("pageNo") int pageNo) {
//		logger.info("TechnologyController.findPaginated()");
//		int pageSize = 1;
//		Page<List<Technologies>> findPaginated = service.findPaginated(pageNo, pageSize);
//		List<List<Technologies>> findAlltech = findPaginated.getContent();
//		int totalPages = findPaginated.getTotalPages();
//		return new ResponseEntity<>(new RestAPIResponse2("success", "fetching VMS By Page No", totalPages, findAlltech),
//				HttpStatus.OK);
//	}

//	   @GetMapping("/all/{pageno}")
//	    public ResponseEntity<HttpResponse>getUsers(@PathVariable("pageno") int pageNo) throws InterruptedException {
//	        TimeUnit.SECONDS.sleep(3);
//	        int pageSize = 5;
//	        //throw new RuntimeException("Forced exception for testing");
//	        Page<List<Technologies>> findPaginated = service.findPaginated(pageNo, pageSize);
//			List<List<Technologies>> findAlltech = findPaginated.getContent();
//			
//	        return ResponseEntity.ok().body(
//	                HttpResponse.builder()
//	                        .timeStamp(now().toString())
//	                        .data(of("page",findAlltech))
//	                        .message("Users Retrieved")
//	                        .status(OK)
//	                        .statusCode(OK.value())
//	                        .build());
//
//	    }

	@ApiOperation("Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change VMS Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestBody Technologies technologies) {
		logger.info("TechnologyController.changeStatus()");
		//System.out.println(technologies);
		long id = technologies.getId();
		String status = "Active";
		String remarks = technologies.getRemarks();

		int changestat = 0;
		String result;
		if (status.equals("Active"))
			result = "InActive";
		else
			result = "Active";
		changestat = service.changeStatus(result, id, remarks);
		if (changestat != 0) {
			logger.info("TechnologyController.changeStatus() => status changed");
			//System.out.println("status Successfully");
		} else {
			logger.info("TechnologyController.changeStatus() => status changed");
			//System.out.println("Not Chnaged");
		}
		// service.changeStatus(result, id, remarks);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Status Change Successfully", "Done"),
				HttpStatus.OK);
	}

	@ApiOperation("To get techId, techName")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll cities"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/tech", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getCities() {
		logger.info("!!! inside class: TechnologyController, !! method: getAllTech");
		return new ResponseEntity<>(new RestAPIResponse("Success", "getall Techhnologies", service.gettechnologies()),
				HttpStatus.OK);
	}
	
	@ApiOperation("To fetch Technology By ID ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Technlogy fetched"),
			@ApiResponse(code = 404, message = "Technology entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getskillsbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getTechnologySkillsByID(@PathVariable int id) {
		logger.info("TechnologyController.getTechnologyByID()");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Get Technology By ID", service.getTechnologySkillsByID(id)), HttpStatus.OK);
	}

}
