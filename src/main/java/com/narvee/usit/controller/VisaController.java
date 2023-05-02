package com.narvee.usit.controller;

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
import com.narvee.usit.entity.Visa;
import com.narvee.usit.service.IVisaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/visa")
@CrossOrigin
public class VisaController {
	public static final Logger logger = LoggerFactory.getLogger(VisaController.class);

	@Autowired
	public IVisaService service;

	@ApiOperation("To save Visa")
	@ApiResponses({ @ApiResponse(code = 200, message = "Visa saved"),
			@ApiResponse(code = 404, message = "Visa entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveVisa(@RequestBody Visa visa) {
		logger.info("VisaController.saveVisa()");
		boolean saveStates = service.saveVisa(visa);
		if (saveStates) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Visa Added Successfully", "Entity Saved"),
					HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("duplicate", "Record Already Exists", "Not Saved"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To Delete Visa By VisaID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Delete Visa By ID"),
			@ApiResponse(code = 404, message = " Visa ID not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteVisaByID(@PathVariable long id) {
		logger.info("VisaController.deleteVisaByID()");
		try {
			boolean flg = service.deleteVisaStatus(id);
		} catch (org.springframework.dao.DataIntegrityViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<>(new RestAPIResponse("fail", "Cannot perform operation"), HttpStatus.OK);
		}
		// if (flg) {
		return new ResponseEntity<>(new RestAPIResponse("success", " Visa Deleted Sucessfully", ""), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(new RestAPIResponse("fail", " Visa not Deleted ", ""), HttpStatus.OK);
//		}

	}

	@ApiOperation("To fetch Visa")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetch All Visa"),
			@ApiResponse(code = 404, message = " Visa not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllVisa() {
		logger.info("VisaController.getAllVisa()");
		return new ResponseEntity<>(new RestAPIResponse("success", "Visa Fetched Successfully", service.getAllVisa()),
				HttpStatus.OK);
	}

	@ApiOperation("To fetch Visa By VisaID")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetch Visa By ID"),
			@ApiResponse(code = 404, message = " Visa ID not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getVisaByID(@PathVariable long id) {
		logger.info("VisaController.getVisaByID()");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Visa Fetched Successfully", service.getVisaById(id)), HttpStatus.OK);
	}

	@ApiOperation("To Update Visa")
	@ApiResponses({ @ApiResponse(code = 200, message = "To Update Visa"),
			@ApiResponse(code = 404, message = " Visa  not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/visa", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateVisa(@RequestBody Visa visa) {
		logger.info("VisaController.updateVisa()");
		return new ResponseEntity<>(new RestAPIResponse("success", "Update Visa Successfully", service.saveVisa(visa)),
				HttpStatus.ACCEPTED);
	}

	@ApiOperation("To fetch Visa By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched Visa"),
			@ApiResponse(code = 404, message = "Visa entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/pagination/{pageNo}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> findPaginated(@PathVariable("pageNo") int pageNo) {
		logger.info("VisaController.findPaginated()");
		int pageSize = 2;
		Page<Visa> findPaginated = service.findPaginated(pageNo, pageSize);
		List<Visa> findAlltech = findPaginated.getContent();
		/*
		 * int totalpages = findPaginated.getTotalPages(); List<Technologies>
		 * findAllTechnologies = service.findAllTechnologies(); int size=
		 * findAllTechnologies.size();
		 */
		return new ResponseEntity<>(new RestAPIResponse("success", "fetching Visa By Page No", findAlltech),
				HttpStatus.OK);
	}

	@ApiOperation("To get visaId, visaName")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll cities"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/visas", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getVisa() {
		logger.info("!!! inside class: CityController, !! method: getAllCities");
		return new ResponseEntity<>(new RestAPIResponse("success", "getall Cities", service.getvisaidname()),
				HttpStatus.OK);
	}
	
	
	@ApiOperation("To get visaId, visaName")
	@ApiResponses({ @ApiResponse(code = 200, message = "getAll cities"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/h1visas", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> geth1Visa() {
		logger.info("!!! inside class: CityController, !! method: getAllCities");
		return new ResponseEntity<>(new RestAPIResponse("success", "getall Cities", service.getH1visa()),
				HttpStatus.OK);
	}
}
