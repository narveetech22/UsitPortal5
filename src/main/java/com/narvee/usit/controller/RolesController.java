package com.narvee.usit.controller;

import java.util.List;

import javax.annotation.PostConstruct;

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
import com.narvee.usit.entity.Roles;
import com.narvee.usit.entity.Users;
import com.narvee.usit.service.IRoleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/roles")
@CrossOrigin
public class RolesController {

	public static final Logger logger = LoggerFactory.getLogger(RolesController.class);

	@Autowired
	private IRoleService Service;

	@ApiOperation("To save Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS saved"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> AddRoles(@RequestBody Roles roles) {
		logger.info("inside RolesController.AddRoles()" + roles);
		boolean flg = Service.saveRole(roles);
		if (flg) {
			logger.info("Role saved after checking duplicate records available or not");
			return new ResponseEntity<>(new RestAPIResponse("Success", "Role Saved", "Success"), HttpStatus.CREATED);
		} else {
			logger.info("Role not saved => Role Already Exist");
			return new ResponseEntity<>(new RestAPIResponse("Fail", "Role Already Exist", "Data not Saved"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To Update Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "VMS saved"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/updaterole", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<RestAPIResponse> UpdateRoles(@RequestBody Roles roles) {
		logger.info("inside RolesController.UpdateRoles()" + roles);
		// System.out.println(roles);
		boolean flg = Service.updateRole(roles);
		if (flg) {
			logger.info("Role Updated after checking duplicate records available or not");
			return new ResponseEntity<>(new RestAPIResponse("Success", "Role Updated", "Updated"), HttpStatus.CREATED);
		} else {
			logger.info("Role not Updated => Role Already Exist");
			return new ResponseEntity<>(new RestAPIResponse("Fail", "Role Already Exist", "Data not Saved"),
					HttpStatus.OK);
		}
	}

	@ApiOperation("To get all Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Fetched all roles"),
			@ApiResponse(code = 404, message = "Entities not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getAllRoles() {
		logger.info("inside RolesController.getAllRoles()=> fetching all roles");
		List<Roles> saveroles = Service.getAllRoles();
		return new ResponseEntity<>(new RestAPIResponse("Success", "All Roles Fetched", saveroles), HttpStatus.OK);

	}

	@ApiOperation("To get single role")
	@ApiResponses({ @ApiResponse(code = 200, message = "fetched single role"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/getrole/{id}", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> getRole(@PathVariable Long id) {
		logger.info("inside RolesController.getRole()=> fetching single role by id");
		Roles saveroles = Service.getRole(id);
		return new ResponseEntity<>(new RestAPIResponse("Success", "Role Feteched By ID", saveroles), HttpStatus.OK);

	}

	@ApiOperation("To Delete Roles")
	@ApiResponses({ @ApiResponse(code = 200, message = "Role Deleted"),
			@ApiResponse(code = 404, message = "entity not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE, produces = "application/json")
	public ResponseEntity<RestAPIResponse> deleteRole(@PathVariable Long id) {
		logger.info("inside RolesController.deleteRole()=> delete single role by id");
		boolean val = Service.deleteRole(id);
		if (val == true) {
			return new ResponseEntity<>(new RestAPIResponse("success", "Role Deleted", ""), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(new RestAPIResponse("fail", "Role Assigned to User, Role Not Deleted", ""),
					HttpStatus.OK);
		}

	}

	@ApiOperation("Status Change ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Change VMS Status"),
			@ApiResponse(code = 404, message = "Status not found"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/status", method = RequestMethod.PATCH, produces = "application/json")
	public ResponseEntity<RestAPIResponse> changeStatus(@RequestBody Roles roles) {
		logger.info("inside RolesController.changeStatus()");
		Long id = roles.getRoleid();
		String status = roles.getStatus();
		String remarks = roles.getRemarks();

		int changestat = 0;
		String result;

		if (status.equals("Active") || status == null) {
			result = "InActive";
			// logger.info("inside RolesController.changeStatus() ");
		} else {
			result = "Active";
			// logger.info("inside RolesController.changeStatus()");
		}

		changestat = Service.changeStatus(result, id, remarks);
		if (changestat != 0) {
			// System.out.println("status Successfully");
			logger.info("inside RolesController.changeStatus() => Status changed successfully");
		} else {
			// System.out.println("Not Chnaged");
			logger.info("inside RolesController.changeStatus()=> Status not changed ");
		}
		Service.changeStatus(result, id, remarks);
		return new ResponseEntity<>(new RestAPIResponse("success", "Status Change Successfully", "Done"),
				HttpStatus.OK);
	}

}
