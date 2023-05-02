package com.narvee.usit.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.helper.GetVendor;
import com.narvee.usit.entity.Recruiter;
import com.narvee.usit.entity.VendorDetails;
import com.narvee.usit.helper.ListRecruiter;
import com.narvee.usit.service.IRecruiterService;
import com.narvee.usit.service.IVendorService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/recruiter")
@CrossOrigin
public class RecruiterController {
	public static final Logger logger = LoggerFactory.getLogger(RecruiterController.class);

	@Autowired
	public IRecruiterService service;

	@Autowired
	IVendorService vservice;
	
	// used check duplicate contact number
		@ApiOperation("To check duplicate record with Company Name")
		@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
				@ApiResponse(code = 404, message = "Bad Request"),
				@ApiResponse(code = 500, message = "Internal Server error") })
		@RequestMapping(value = "/duplicateMail/{email}", method = RequestMethod.GET, produces = "application/json")
		public ResponseEntity<RestAPIResponse> duplicateCheck(@PathVariable String email) {
			//ConsultantInfo findByContactnumber = service.findByContactnumber(number);
			Recruiter checkDuplicateVendor = service.duprecruiter(email);
			if (checkDuplicateVendor == null) {
				logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
				return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
			} else {
				logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
				return new ResponseEntity<>(new RestAPIResponse("duplicate"),
						HttpStatus.OK);
			}
		}

	@ApiOperation("To get all Recruiters")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/venodorCompanies/{flg}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles(@PathVariable String flg) {
		logger.info("To get all Recruiters");
		List<GetVendor> getall = vservice.getvendordetails(flg);
		return new ResponseEntity<>(new RestAPIResponse("success", "All Recruiters Fetched", getall), HttpStatus.OK);
	}

	@ApiOperation("To Fetch Recruiter Info By Company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Details Fetched"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/recrbycompany/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getrecruiterinfobycompanyid(@PathVariable long id) {
		// System.out.println(service.getbyId(id)+" ===================");
		logger.info("getrecruiterinfobycompanyid.getbyId()");
		return new ResponseEntity<>(new RestAPIResponse("success", "Recruiter Info Fetched By ID Successfully ",
				service.recruiterinfobyVmsid(id)), HttpStatus.OK);
	}

	@ApiOperation("To save Details")
	@ApiResponses({ @ApiResponse(code = 200, message = "Vendor Details saved"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> saveRecruiter(@RequestBody Recruiter ent) {
		Recruiter vm = service.duprecruiter(ent.getEmail());// service.dupvendor(ent.getEmail());
		logger.info("RecruiterController.saveRecruiter()");
		if (vm == null) {
			logger.info("RecruiterController.saveRecruiter() => Record Inserted");
			Recruiter save = service.save(ent);
			if (save != null) {
				return new ResponseEntity<>(new RestAPIResponse("success", "Recruiter Saved", save), HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(new RestAPIResponse("fail", "Recruiter not Saved", ""), HttpStatus.OK);
			}

		} else {
			logger.info("RecruiterController.saveRecruiter() => Record Not Inserted");
			return new ResponseEntity<>(
					new RestAPIResponse("Fail", "Recruiter Already Exist", ""), HttpStatus.OK);
		}

	}

	@ApiOperation("To get Details")
	@ApiResponses({ @ApiResponse(code = 200, message = " Details fetched"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all/{access}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> gateall(@PathVariable String access, @PathVariable long userid) {
		logger.info("RecruiterController.getall()");
		// System.out.println(service.getall());
		List<ListRecruiter> getallrecruiter = service.getallrecruiter(access,userid);
//		Comparator<ListRecruiter> comparator = (c1, c2) -> {
//			return Long.valueOf(c2.getId()).compareTo(c1.getId());
//		};
		//Collections.sort(getallrecruiter, comparator);
		return new ResponseEntity<>(new RestAPIResponse("success", "Recruiter fetched Successfully", getallrecruiter),
				HttpStatus.OK);
	}

	// @CrossOrigin(origins = "http://localhost:4200")
	@ApiOperation("To Delete entity")
	@ApiResponses({ @ApiResponse(code = 200, message = " Deleted"),
			@ApiResponse(code = 404, message = " entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> delete(@PathVariable long id) {
		// System.out.println("hello");
		logger.info("RecruiterController.delete()");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Record Deleted Successfully", service.deleteById(id)), HttpStatus.OK);
	}

	@ApiOperation("To Fetch entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS Fetched"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/recruiter/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getbyId(@PathVariable long id) {
		// System.out.println(service.getbyId(id));
		logger.info("RecruiterController.getbyId()");
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Recruiter Fetched By ID Successfully ", service.getbyId(id)), HttpStatus.OK);
	}

	@ApiOperation("Update Entity")
	@ApiResponses({ @ApiResponse(code = 200, message = "Updating Vendor"),
			@ApiResponse(code = 404, message = "url not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/recruiter", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateRecruiter(@RequestBody Recruiter vms) {
		// System.out.println(vms);
		logger.info("RecruiterController.updateRecruiter()");
		return new ResponseEntity<>(new RestAPIResponse("success", "Recruiter Successfully Updated", service.save(vms)),
				HttpStatus.OK);
	}

	@ApiOperation("Recruiter Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change Recruiter Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestParam("id") Long id,
			@RequestParam("status") String status, @RequestParam("remarks") String remarks) {
		logger.info("RecruiterController.changeStatus()");
		int changestat = 0;
		String result;
		if (status.equalsIgnoreCase("Active")) {
			result = "InActive";
		} else {
			result = "Active";
		}

		changestat = service.changeStatus(result, id, remarks);
		if (changestat != 0) {
			// System.out.println("status Successfully");
			logger.info("RecruiterController.changeStatus() => Status changed succvessfully ");
			return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		} else {
			// System.out.println("Not Chnaged");
			logger.info("RecruiterController.changeStatus() => Status not changed ");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("Approve Recruiter")
	@ApiResponses({ @ApiResponse(code = 200, message = "RecruiterController Approved"),
			@ApiResponse(code = 404, message = "RecruiterController entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/approve", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> approveVMS(@RequestParam("stat") String stat, @RequestParam("id") long id) {
		logger.info("RecruiterController.updateVMS()");
		int changestat = 0;
		changestat = service.approve(stat, id);
		if (changestat != 0) {
			// System.out.println("status Successfully"); @RequestParam("role") long role,
			logger.info("RecruiterController.changeStatus() => Status changed succvessfully ");
			return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully",stat),
					HttpStatus.OK);
		} else {
			// System.out.println("Not Chnaged");
			logger.info("RecruiterController.changeStatus() => Status not changed ");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Status not Change ", "Done"), HttpStatus.OK);
		}
	}

	@ApiOperation("To fetch Recruiter By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
			@ApiResponse(code = 404, message = "Recruiter entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> uploadexcel(@RequestParam("file") MultipartFile file) {
		/*logger.info("VMSController.findPaginated()");
		if (Helper.checkExcelFormat(file)) {
			// this.productService.save(file);
			service.saveexcel(file);
			return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.BAD_REQUEST);
		*/
		return null;
	}
	
	@ApiOperation("Vendor Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change Vendor Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/reject", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> rejectRecruiter(@RequestParam("id") Long id,
			@RequestParam("remarks") String remarks) {
		service.rejectRecruiter(id,remarks);
		logger.info("VMSController.changeStatus() => Status not changed ");
		return new ResponseEntity<>(new RestAPIResponse("success", "Vendor Rejected  Successfully"), HttpStatus.OK);
	}

	@ApiOperation("To fetch VMS By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/excelUpload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> uploadexcel2(@RequestParam("file") MultipartFile file) {
		logger.info("VMSController.findPaginated()");
		if (ExcelUploads.checkExcelFormat(file)) {
			// this.productService.save(file);
			service.uploadExcel(file);
			return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("success", "fetche", ""), HttpStatus.BAD_REQUEST);
	}
	
}
