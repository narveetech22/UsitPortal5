package com.narvee.usit.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.Users;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.entity.Visa;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.ListVendor;
import com.narvee.usit.service.IVendorService;
import com.narvee.usit.service.IVisaService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/vendor")
@CrossOrigin
public class VendorManagementController {
	public static final Logger logger = LoggerFactory.getLogger(VendorManagementController.class);

	@Autowired
	public IVendorService service;
	
	// used check duplicate contact number
	@ApiOperation("To check duplicate record with Company Name")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/duplicatecheck/{vendor}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> duplicateCheck(@PathVariable String vendor) {
		//ConsultantInfo findByContactnumber = service.findByContactnumber(number);
		List<VendorDetails> checkDuplicateVendor = service.checkDuplicateVendor(vendor);
		if (checkDuplicateVendor == null || checkDuplicateVendor.isEmpty()) {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
			return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
		} else {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
			return new ResponseEntity<>(new RestAPIResponse("duplicate"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To save Vendor Details")
	@ApiResponses({ @ApiResponse(code = 200, message = "Vendor Details saved"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveVendor(@RequestBody VendorDetails vms) {// @RequestBody VendorDetails vms
		List<VendorDetails> vendor = service.findByCompanyAndHeadquerter(vms.getCompany(), vms.getHeadquerter());
		if (vendor == null || vendor.isEmpty()) {
			logger.info("VendorManagementController.saveVendor()");
			return new ResponseEntity<>(new RestAPIResponse("success", "Vendor Added Successfully", service.save(vms)),
					HttpStatus.CREATED);
		} else {
			logger.info("VendorManagementController.saveVendor()");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Vendor Already Exists ", "Duplicate vendor"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("Approve VMS")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS Edited"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/approve", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> approveVMS(@RequestParam("stat") String stat, @RequestParam("id") long id) {
		logger.info("VMSController.updateVMS()");
		int changestat = 0;
		String msg = "";
		if (stat.equalsIgnoreCase("Approved"))
			msg = "Approved";
		else
			msg = "Rejected";
		// changestat = service.approve(stat, id, role);
		changestat = service.approve(stat, id);
		if (changestat != 0) {
			logger.info("VMSController.changeStatus() => Status changed succvessfully ");
			return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully", msg),
					HttpStatus.OK);
		} else {
			logger.info("VMSController.changeStatus() => Status not changed ");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Status not Change ", "Done"), HttpStatus.OK);
		}
	}

	@ApiOperation("To get Vendor Details")
	@ApiResponses({ @ApiResponse(code = 200, message = "Vendor Details fetched"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all/{access}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> gateall(@PathVariable String access, @PathVariable long userid) {
		// System.out.println("ooooooooooo " + service.getall());
		logger.info("VendorManagementController.gateall()");
		List<ListVendor> getvendor = service.getvendor(access, userid);
		return new ResponseEntity<>(new RestAPIResponse("success", "Vendor fetched Successfully", getvendor),
				HttpStatus.OK);
	}

	@ApiOperation("To Delete VMS")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS Deleted"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> delete(@PathVariable long id) {
		logger.info("VendorManagementController.delete()");
		boolean val = service.deleteById(id);
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Vendor Company Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("fail",
							"Vendor Company Associated with Recruiter, Please remove Associative records", ""),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To Fetch entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS Fetched"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/vendor/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getbyId(@PathVariable long id) {
		logger.info("VendorManagementController.getbyId()");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "VMS Fetched By ID Successfully ", service.getbyId(id)), HttpStatus.OK);
	}

	@ApiOperation("Update Entity")
	@ApiResponses({ @ApiResponse(code = 200, message = "Updating Vendor"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/vendor", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateVMS(@RequestBody VendorDetails vms) {
		logger.info("VendorManagementController.updateVMS()");
		return new ResponseEntity<>(new RestAPIResponse("success", "VMS Successfully Update", service.save(vms)),
				HttpStatus.OK);
	}

	@ApiOperation("Vendor Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change Vendor Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestParam("id") Long id,
			@RequestParam("status") String status, @RequestParam("remarks") String remarks) {// id="+vmsid+"status="+status+"remarks
		logger.info("VMSController.changeStatus()");
		int changestat = 0;
		String result;
		if (status.equalsIgnoreCase("Active")) {
			result = "InActive";
		} else {
			result = "Active";
		}
		changestat = service.changeStatus(result, id, remarks);

		if (changestat != 0) {
			logger.info("VMSController.changeStatus() => Status changed succvessfully ");
			return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		} else {
			logger.info("VMSController.changeStatus() => Status not changed ");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("Vendor Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change Vendor Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/reject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> rejectVendor(@RequestParam("id") Long id,
			@RequestParam("remarks") String remarks) {
		service.rejectVendor(id, remarks);
		logger.info("VMSController.changeStatus() => Status not changed ");
		return new ResponseEntity<>(new RestAPIResponse("success", "Vendor Rejected  Successfully"), HttpStatus.OK);
	}

	/*
	 * @ApiOperation("To fetch VMS By PageNo ")
	 * 
	 * @ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
	 * 
	 * @ApiResponse(code = 404, message = "VMS entity not found"),
	 * 
	 * @ApiResponse(code = 500, message = "Internal Server error") })
	 * 
	 * @RequestMapping(value = "/vms/{pageNo}", method = RequestMethod.GET, produces
	 * = "application/json") public ResponseEntity<RestAPIResponse2>
	 * findPaginated(@PathVariable("pageNo") int pageNo) {
	 * logger.info("VMSController.findPaginated()"); int pageSize = 2;
	 * 
	 * Page<VendorDetails> findPaginated = service.findPaginated(pageNo, pageSize);
	 * List<VendorDetails> findAlltech = findPaginated.getContent(); int totalPages
	 * = findPaginated.getTotalPages(); // System.out.println("kkkkk " +
	 * Integer.toString(totalPages)); return new ResponseEntity<>(new
	 * RestAPIResponse2("success", "fetche", totalPages, findAlltech),
	 * HttpStatus.OK); }
	 */

	@ApiOperation("To fetch VMS By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> uploadexcel(@RequestParam("file") MultipartFile file) {
		logger.info("VMSController.findPaginated()");
		if (ExcelUploads.checkExcelFormat(file)) {
			service.saveexcel(file);
			return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.BAD_REQUEST);
	}

}
