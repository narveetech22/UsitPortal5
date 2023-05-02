package com.narvee.usit.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.entity.Todo;
import com.narvee.usit.service.ITodoService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/task")
@CrossOrigin
public class TaskController {

	private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

	@Autowired
	private ITodoService service;

	@ApiOperation("To Save Task")
	@ApiResponses({ @ApiResponse(code = 200, message = "Task Saved"), @ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<RestAPIResponse> save(@RequestBody Todo todo) {
		logger.info("!!! inside class : RequirmentsController, !! method : saveRequirement");
		Todo obj = service.save(todo);
		return new ResponseEntity<>(new RestAPIResponse("success", "Save Requirments Successfully", obj),
				HttpStatus.CREATED);

	}
	
	@ApiOperation("To get Employee")
	@ApiResponses({ @ApiResponse(code = 200, message = "recruiters fetch"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/employeelist", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> listrecruiter() {
		logger.info("!!! inside class : UsersController, !! method : listrecruiter");
		return new ResponseEntity<>(new RestAPIResponse("Success", "fetch All recruiters", service.getEMployees()),
				HttpStatus.OK);
	}
	
	@ApiOperation("To get All")
	@ApiResponses({ @ApiResponse(code = 200, message = "recruiters fetch"),
			@ApiResponse(code = 404, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server error") })
	@RequestMapping(value = "/all", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> listAll() {
		logger.info("!!! inside class : UsersController, !! method : listrecruiter");
		return new ResponseEntity<>(new RestAPIResponse("Success", "fetch All recruiters", service.getAllTasks()),
				HttpStatus.OK);
	}

}
