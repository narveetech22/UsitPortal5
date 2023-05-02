package com.narvee.usit.controller;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.helper.GetRoles;
import com.narvee.usit.entity.Requirements;
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.Users;
import com.narvee.usit.service.IRequirmentService;
import com.narvee.usit.service.IRoleService;
import com.narvee.usit.service.IUserService;
import com.narvee.usit.serviceimpl.UserAuthService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xddf.usermodel.chart.DisplayBlanks;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping(value = "/users")
public class UsersController {
	public static final Logger logger = LoggerFactory.getLogger(UsersController.class);
	@Autowired
	private IUserService iUserService;

	@Autowired
	private IRoleService roleService;

	@Autowired
	private IRequirmentService service;

	// @PostConstruct
	private void init() {
		Roles roles = new Roles();
		roles.setRolename("Super Admin");
		roles.setAddedby(1);
		roles.setRoleno(1);
		roles.setStatus("Active");
		roleService.saveRole(roles);

		Roles role = new Roles();
		role.setRoleid(1L);
		Users user = new Users();
		user.setDepartment("Administration");
		user.setEmail("saikiran@narveetech.com");
		user.setFullname("Super Admin");
		user.setRole(role);
		iUserService.saveUser(user);

		Users u = new Users();
		LocalDate date = LocalDate.now();
		Requirements req = new Requirements();
		req.setCategory("Java Developer");
		req.setCreateddate(date);
		req.setUpdatedby(0);
		req.setPostedon(date);
		req.setJobtitle("Java Developer");
		req.setLocation("Hyderabad");
		req.setVendor("Narvee Technologies");
		req.setEmploymenttype("Full Time");
		req.setMaxnumber(1L);
		req.setReqnumber("Intreq-0001");
		req.setStatus("Active");
		u.setUserid(1);
		req.setUsers(u);
		service.initmethod(req);
	}

	@ApiOperation("To save Employee")
	@ApiResponses({ @ApiResponse(code = 200, message = "Employee saved"),
			@ApiResponse(code = 404, message = " URL not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> addUsers(@RequestBody Users users)
			throws JsonMappingException, JsonProcessingException {
		logger.info("UsersController.addUsers()");
		boolean saveUser = iUserService.saveUser(users);
		if (saveUser) {
			logger.info("UsersController.addUsers() saving user");
			return new ResponseEntity<>(new RestAPIResponse("Success", "User Saved", "User Saved"), HttpStatus.CREATED);
		} else {
			logger.info("UsersController.addUsers() => user not saved Already exists");
			return new ResponseEntity<>(new RestAPIResponse("Fail", "User Already Exist", "User Email Already Exist"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To get recruiters")
	@ApiResponses({ @ApiResponse(code = 200, message = "recruiters fetch"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/recruiterlist", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> listrecruiter() {
		logger.info("!!! inside class : UsersController, !! method : listrecruiter");
		return new ResponseEntity<>(new RestAPIResponse("Success", "fetch All recruiters", iUserService.getRecruiter()),
				HttpStatus.OK);
	}

	@ApiOperation("To get recruiters")
	@ApiResponses({ @ApiResponse(code = 200, message = "recruiters fetch"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getrecruiter", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getrecruiter() {
		logger.info("!!! inside class : UsersController, !! method : getAllRequirments");
		return new ResponseEntity<>(new RestAPIResponse("Success", "fetch All recruiters", iUserService.getUser()),
				HttpStatus.OK);
	}

	@ApiOperation("To get all Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getroles", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles() {
		logger.info("UsersController.getAllRoles()");
		List<GetRoles> getrole = roleService.getRoles();
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", getrole), HttpStatus.OK);

	}

	@ApiOperation("To Update Users")
	@ApiResponses({ @ApiResponse(code = 200, message = "User saved"), // c3roqYbE
			@ApiResponse(code = 404, message = " entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> updateUsers(@RequestBody Users users) {
		// System.out.println("=========== " + users);
		logger.info("UsersController.updateUsers()");
		boolean flg = iUserService.updateUser(users);
		if (flg) {
			logger.info("UsersController.updateUsers() User updated");
			return new ResponseEntity<>(new RestAPIResponse("Success", "User Updated", ""), HttpStatus.CREATED);
		} else {
			logger.info("UsersController.updateUsers() User not updated");
			return new ResponseEntity<>(new RestAPIResponse("Fail", "User Already Exist", "User Email Already Exist"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To Fetch All Users")
	@ApiResponses({ @ApiResponse(code = 200, message = "Users Fetched"),
			@ApiResponse(code = 404, message = " Users not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getUsers() {
		logger.info("UsersController.getUsers()");
		List<Users> listall = iUserService.getAllUsers();

		Comparator<Users> comparator = (c1, c2) -> {
			return Long.valueOf(c2.getUserid()).compareTo(c1.getUserid());
		};

		Collections.sort(listall, comparator);
		return new ResponseEntity<>(new RestAPIResponse("Fail", "User Already Exist", listall), HttpStatus.OK);
	}

	@ApiOperation("To Fetch user By Id")
	@ApiResponses({ @ApiResponse(code = 200, message = "Users Fetched"),
			@ApiResponse(code = 404, message = " Users not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/userbyid/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getUserByid(@PathVariable Long id) {
		logger.info("UsersController.getUserByid()");
		System.out.println(iUserService.finduserInfoById(id));
		return new ResponseEntity<>(new RestAPIResponse("success", "User Details", iUserService.finduserById(id)),
				HttpStatus.OK);
	}
	
	@ApiOperation("To Fetch user By Id")
	@ApiResponses({ @ApiResponse(code = 200, message = "Users Fetched"),
			@ApiResponse(code = 404, message = " Users not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/userinfo/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getUserInfoByid(@PathVariable Long id) {
		logger.info("UsersController.getUserByid()");
		//System.out.println(iUserService.finduserInfoById(id));
		return new ResponseEntity<>(new RestAPIResponse("success", "User Details", iUserService.finduserInfoById(id)),
				HttpStatus.OK);
	}
	
	

	// to check authorization
	@ApiOperation("To delete Users")
	@ApiResponses({ @ApiResponse(code = 200, message = "delete records"),
			@ApiResponse(code = 404, message = " entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> editUser(@PathVariable Long id) {
		logger.info("UsersController.editUser()");
		return new ResponseEntity<>(new RestAPIResponse("Success", "User Deleted", iUserService.deleteUsers(id)),
				HttpStatus.OK);
	}

	@ApiOperation("user Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change user Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestBody Users users) {
		logger.info("UsersController.changeStatus()");
		// System.out.println(users);
		Long id = users.getUserid();
		String status = users.getStatus();
		String remarks = users.getRemarks();

		int changestat = 0;
		String result;
		if (status.equals("Active"))
			result = "InActive";
		else
			result = "Active";
		changestat = iUserService.changeStatus(result, id, remarks);
		if (changestat != 0) {
			logger.info("UsersController.getAllRoles() status changed Successfully");
		} else {
			logger.info("UsersController.getAllRoles() status not changed ");
		}
		return new ResponseEntity<>(new RestAPIResponse("Success", "Status Change Successfully", "Done"),
				HttpStatus.OK);
	}

	@ApiOperation("To Fetch Employee By filter")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS Fetched Records"),
			@ApiResponse(code = 404, message = "VMS entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/search2/{keyword}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> filterVMS(@PathVariable String keyword) {
		// System.out.println(keyword + " =========================");
		/// List<GetRoles> getrole = roleService.getRoles();
		return new ResponseEntity<>(
				new RestAPIResponse("Success", "Employee Fetched Records", iUserService.filterEmployee2(keyword)),
				HttpStatus.OK);
	}

	// manager tl drop down
	@ApiOperation("To get all Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/manageDropDown", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> managerDropdown() {
		logger.info("UsersController.getAllRoles()");
		List<Object[]> getrole = iUserService.managerDropDown();
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", getrole), HttpStatus.OK);
	}

	// manager tl drop down
	@ApiOperation("To get all Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/TlDropDown/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> TLDropdown(@PathVariable long id) {
		logger.info("UsersController.getAllRoles()");
		List<Object[]> getrole = iUserService.TLDropDown(id);
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", getrole), HttpStatus.OK);

	}
	

	@ApiOperation("user unlock  ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change user Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/unlock", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity<RestAPIResponse> unlockUser(@RequestBody Users users) {
		logger.info("UsersController.changeStatus()");
		String remarks = users.getRemarks();

		int changestat = 0;
		String result;
		changestat = iUserService.unlockUser(users);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Status Change Successfully", "Done"),
				HttpStatus.OK);
	}

}
