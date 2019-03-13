package com.tzq.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value="/user")
public class UserManageController {

	
	public UserManageController() {
		// TODO Auto-generated constructor stub
	}
	
	@RequestMapping(value="/usertest")
	@ResponseBody
	public String getUserList() {
		return "user service test OK!";
	}

}
