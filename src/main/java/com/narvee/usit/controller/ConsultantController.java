package com.narvee.usit.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.dto.ConsultantDTO;
import com.narvee.usit.dto.ConsultantReportDTO;
import com.narvee.usit.dto.DateSearcherDto;
import com.narvee.usit.entity.ConsultantInfo;
import com.narvee.usit.helper.ExcelUploads;
import com.narvee.usit.service.IConsultantService;
import com.narvee.usit.service.IfileStorageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/consultant")
@CrossOrigin
public class ConsultantController {
	private static final Logger logger = LoggerFactory.getLogger(ConsultantController.class);

	@Autowired
	private IConsultantService service;

	@Autowired
	private IfileStorageService fileStorageService;

	// used check duplicate contact number
	@ApiOperation("To check duplicate record with Number")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/duplicatecheck/{number}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> duplicateCheck(@PathVariable String number) {
		ConsultantInfo findByContactnumber = service.findByContactnumber(number);
		if (findByContactnumber == null) {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
			return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
		} else {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Consultant already exists with Phone Number"),
					HttpStatus.OK);
		}
	}

	// used check duplicate contact number
	@ApiOperation("To check duplicate record with Email")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/duplicateMail/{email}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> emailDuplicateCheck(@PathVariable String email) {
		ConsultantInfo findByEmail = service.findByEmail(email);
		if (findByEmail == null) {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with email");
			return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
		} else {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with email");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Consultant already exists with Email"),
					HttpStatus.OK);
		}
	}

	// used at submission as drop down
	@ApiOperation("To check duplicate record with Email update record")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/duplicateMailEdit/{email}/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> oneditDuplicateEmailChecking(@PathVariable String email,
			@PathVariable long id) {
		List<ConsultantInfo> findByContactnumber = service.findByConsultantemailAndConsultantidNot(email, id);
		if (findByContactnumber == null || findByContactnumber.isEmpty()) {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with email");
			return new ResponseEntity<>(new RestAPIResponse("success", "No records found"), HttpStatus.OK);
		} else {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with email");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Consultant already exists with mail Address"),
					HttpStatus.OK);
		}
	}

	// used at submission as drop down
	@ApiOperation("check duplicate with number with id")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/duplicatecheck/{number}/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> oneditDuplicateChecking(@PathVariable String number, @PathVariable long id) {
		ConsultantInfo findByContactnumber = service.findByContactnumberAndConsultantidNot(number, id);
		if (findByContactnumber == null) {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number with id");
			return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
		} else {
			logger.info("!!! inside class: Consultant , !! method: check duplicate with number with id");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Consultant already exists with Phone Number"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To save Consultant")
	@ApiResponses({ @ApiResponse(code = 200, message = "consultance saved"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "saveconsultant", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> savingConsultant(@RequestBody ConsultantInfo consultant) {
		logger.info("Consultant Controller Saving");
		if (consultant.getConsultantid() == null) {
			consultant.setConsultantname(consultant.getFirstname() + " " + consultant.getLastname());
			ConsultantInfo consultantInfo = service.saveConsultant(consultant);
			return new ResponseEntity<>(new RestAPIResponse("success", "Consultant added Successfully", consultantInfo),
					HttpStatus.CREATED);
		} else {
			consultant.setConsultantname(consultant.getFirstname() + " " + consultant.getLastname());
			ConsultantInfo consultantInfo = service.updateConsultant(consultant);
			return new ResponseEntity<>(new RestAPIResponse("success", "Consultant added Successfully", consultantInfo),
					HttpStatus.CREATED);
		}
	}

	// uploading sales consultant files
	@RequestMapping(value = "/uploadMultiple/{id}", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<RestAPIResponse> uploadMultipleFile2(
			@RequestParam(required = false, value = "resume") MultipartFile resume,
			@RequestParam(required = false, value = "h1b") MultipartFile h1b,
			@RequestParam(required = false, value = "dl") MultipartFile dl, @PathVariable Long id,
			@RequestParam(required = false, value = "files") MultipartFile[] files) throws IOException {
		
		ConsultantInfo entity = service.getconsulatntbyId(id);
		String nresumne = entity.getResume();
		String nh1b = entity.getH1bcopy();
		String ndl = entity.getDlcopy();
		String mobileNumber = entity.getContactnumber();
		try {
			nresumne = fileStorageService.storeFile(resume, mobileNumber, "Resume");
			entity.setResume(nresumne);
		} catch (NullPointerException e) {
		}

		try {
			nh1b = fileStorageService.storeFile(h1b, mobileNumber, "Visa");
			// nh1b = StringUtils.cleanPath(h1b.getOriginalFilename());
			entity.setH1bcopy(nh1b);
		} catch (NullPointerException e) {
		}

		try {
			ndl = fileStorageService.storeFile(dl, mobileNumber, "DL");
			// ndl = StringUtils.cleanPath(dl.getOriginalFilename());
			entity.setDlcopy(ndl);
		} catch (NullPointerException e) {
		}

		try {
			List<String> fileNames = new ArrayList<>();
			if (files.length != 0) {
				Arrays.asList(files).stream().forEach(file -> {
					String filename = fileStorageService.storemultiplefiles(file);
					service.uploadfiles(filename, id);
					fileNames.add(filename);
				});
			}
		} catch (Exception e) {
		}
		service.update(nresumne, nh1b, ndl, id);
		return new ResponseEntity(new RestAPIResponse("success", "Uploaded files"), HttpStatus.OK);
	}

	// recruiting file upload
	@PostMapping("/upload/{id}")
	public ResponseEntity<RestAPIResponse> uploadFiles(
			@RequestParam(required = false, value = "resume") MultipartFile resume,
			@RequestParam(required = false, value = "files") MultipartFile[] files, @PathVariable long id) {
		String message = "";
		String resumen = "";
		String number = "";
		logger.info("Consultant Controller Saving FIle Upload");
		try {
			String resumename = fileStorageService.storeFile(resume, number, "kk");
			service.update(resumename, id);
		} catch (NullPointerException e) {
		}
		try {
			List<String> fileNames = new ArrayList<>();
			if (files.length != 0) {
				Arrays.asList(files).stream().forEach(file -> {
					String filename = fileStorageService.storemultiplefiles(file);
					service.uploadfiles(filename, id);
					fileNames.add(filename);
				});
			}
			message = "Uploaded the files successfully: ";
			return new ResponseEntity(new RestAPIResponse("success", "Uploaded files"), HttpStatus.OK);
		} catch (Exception e) {
			message = "Fail to upload files!";
			return new ResponseEntity(new RestAPIResponse("fail", "Uploaded files"), HttpStatus.OK);
		}
	}

	@ApiOperation("To getAll Consultance")
	@ApiResponses({ @ApiResponse(code = 200, message = "get All Consultance"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "all/{flg}/{access}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllConsultant(@PathVariable String flg, @PathVariable String access,
			@PathVariable long userid) {
		logger.info("Consultant Controller Get All");
		List<ConsultantDTO> findAllConsultants = service.findallconsultant(flg, access, userid);
		logger.info("!!! inside class: ConsultantController, !!method: getAllConsultant");
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Successfuuly fetched consultant details", findAllConsultants),
				HttpStatus.OK);
	}

	@ApiOperation("To Delete  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Deleted"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getConsultantByID(@PathVariable long id) {
		logger.info("!!! inside class: ConsultantController, method : deleteConsultantByID");
		ConsultantInfo consulatntby = service.getconsulatntbyId(id);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant Deleted", consulatntby), HttpStatus.OK);
	}

	@ApiOperation("Consultant Download Resume ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Resume"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/download/{id}/{flg}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadresume(@PathVariable long id, @PathVariable String flg) throws IOException {
		Resource file = service.downloadfile(id, flg);
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
		service.removeFile(id, flg);
		return new ResponseEntity<>(
				new RestAPIResponse("success", "Consultant Submitted  for the requirement ,Consultant Not Deleted"),
				HttpStatus.OK);
	}

	@ApiOperation("Consultant Download Resume ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Resume"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/removefiles/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> removemultipleFile(@PathVariable long id) throws IOException {
		logger.info("!!! inside class: ConsultantController, method : Consultant Download Resume");
		int count = service.removeMultipleFile(id);
		if (count == 0) {
			return new ResponseEntity<>(
					new RestAPIResponse("fail", "Consultant Submitted  for the requirement ,Consultant Not Deleted"),
					HttpStatus.OK);
		} else {
			return new ResponseEntity<>(
					new RestAPIResponse("success", "Consultant Submitted  for the requirement ,Consultant Not Deleted"),
					HttpStatus.OK);
		}

	}

	@ApiOperation("Consultant Download Files")
	@ApiResponses({ @ApiResponse(code = 200, message = "Consultant Download Files"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/downloadfiles/{id}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ResponseEntity<Resource> downloadFile(@PathVariable long id) throws IOException {
		Resource file = service.download(id);
		Path path = file.getFile().toPath();
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(path))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
				.body(file);
	}

	@ApiOperation("To Delete  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Deleted"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteConsultantByID(@PathVariable long id) {
		logger.info("!!! inside class: ConsultantController, method : deleteConsultantByID");
		boolean val = service.deleteconsultant(id);
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Consultant Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("fail",
					"Consultant Submitted  for the requirement ,Consultant Not Deleted", ""), HttpStatus.OK);
		}
	}

	// used at submission as drop down
	@ApiOperation("To get Consultant detail for drop down field")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/consultantinfo/{flg}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getConsultat(@PathVariable String flg) {
		logger.info("!!! inside class: Consultant , !! method: get consultant drop down");
		return new ResponseEntity<>(new RestAPIResponse("Success", "getall Consultant", service.getconsultInfo(flg)),
				HttpStatus.OK);
	}

	// used at submission as drop down
	@ApiOperation("To get Consultant detail for drop down field")
	@ApiResponses({ @ApiResponse(code = 200, message = "get consultant info"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/consultantTrack/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getConsultatTrack(@PathVariable long id) {
		logger.info("!!! inside class: Consultant , !! method: get consultant drop down");
		return new ResponseEntity<>(new RestAPIResponse("Success", "getall Consultant", service.consultantTracker(id)),
				HttpStatus.OK);
	}

	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/movedtosales/{id}/{flg}/{remarks}/{userid}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> moveConsult(@PathVariable long id, @PathVariable String flg,
			@PathVariable String remarks, @PathVariable long userid) {
		logger.info("!!! inside class: ConsultantController, method : deleteConsultantByID");
		String result;
		if (flg.equalsIgnoreCase("sales")) {
			result = "presales";
		} else {
			result = "sales";
		}
		int consulatntby = service.movedtoconsultant(id, result, remarks, userid);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant Deleted"), HttpStatus.OK);
	}

	@ApiOperation("Vendor Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change Vendor Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestParam("id") Long id,
			@RequestParam("status") String status, @RequestParam("remarks") String remarks) {// id="+vmsid+"status="+status+"remarks
		logger.info("VMSController.changeStatus()");
		int changestat;
		String result;
		String flg;
		if (status.equalsIgnoreCase("Active")) {
			result = "InActive";
			flg = "presales";
		} else {
			result = "Active";
			flg = "sales";
		}
		changestat = service.activeinactiveConsultant(result, remarks, id, flg);

		if (changestat != 0) {
			logger.info("VMSController.changeStatus() => Status changed succvessfully ");
			return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		} else {
			// System.out.println("Not Chnaged");
			logger.info("VMSController.changeStatus() => Status not changed ");
			return new ResponseEntity<>(new RestAPIResponse("fail", "Status Change Successfully", "Done"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/creport", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> consultantReport(@RequestBody DateSearcherDto dateSearcherDto) {
		logger.info("!!! inside class: ConsultantController, method : deleteConsultantByID");
		List<ConsultantReportDTO> consutantReport = service.consutantReport(dateSearcherDto);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant Deleted", consutantReport),
				HttpStatus.OK);
	}

	@ApiOperation("To Move  Consultant entity By ID")
	@ApiResponses({ @ApiResponse(code = 200, message = " Consultant Move"),
			@ApiResponse(code = 404, message = " Consultant entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/consultantreport/{startDate}/{status}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> consultantDrillDownReport(
			@PathVariable("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
			@PathVariable("status") String status) {
		logger.info("!!! inside class: ConsultantController, method : deleteConsultantByID");
		List<ConsultantDTO> consutantReport = service.reportDrillDown(status, startDate);
		return new ResponseEntity<>(new RestAPIResponse("success", "Consultant Deleted", consutantReport),
				HttpStatus.OK);
	}

	@ApiOperation("To fetch VMS By PageNo ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched VMS"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> uploadexcel(@RequestParam("file") MultipartFile file) {
		logger.info("VMSController.findPaginated()");
		logger.info("VMSController.findPaginated()");
		if (ExcelUploads.checkExcelFormat(file)) {
			service.uploadExcel(file);
			return new ResponseEntity<>(new RestAPIResponse("success"), HttpStatus.OK);
		}
		return new ResponseEntity<>(new RestAPIResponse("fail"), HttpStatus.BAD_REQUEST);
	}
}
