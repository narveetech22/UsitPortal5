package com.narvee.usit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.narvee.usit.commons.RestAPIResponse;
import com.narvee.usit.vo.Request;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashBoardCOntroller {

	@RequestMapping(value = "/index", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<RestAPIResponse> index() {
		System.out.println("========================");
		return new ResponseEntity<>(new RestAPIResponse("index"), HttpStatus.OK);
	}
}
