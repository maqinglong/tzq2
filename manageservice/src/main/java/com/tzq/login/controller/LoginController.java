package com.tzq.login.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tzq.controller.FileUploadController;

@RestController
@RequestMapping(value="/login")
public class LoginController {
	private Logger logger = LoggerFactory.getLogger(FileUploadController.class.getName());
	

	public LoginController() {
		// TODO Auto-generated constructor stub
	}
	
	@PostMapping(value="/wxauth")
	public String auth() {
		String ret = "";
		return ret;
	}

}
