package com.tzq.controller;

import org.springframework.web.bind.annotation.RestController;

import com.tzq.model.ActUser;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

@RestController
@RequestMapping("/actUser")
public class ActUserController {
	
@PostMapping(value="/addUser")
@ResponseBody
public String addUser(@RequestBody String userInfo,HttpServletRequest request) {
	String result = "";
	ActUser actuser = null ;
	JSONObject jsonObj = null ;
	
	
	
	if (userInfo != null && !"".equals(userInfo)) {
		jsonObj = JSONObject.parseObject(userInfo);
		actuser = JSONObject.toJavaObject(jsonObj, ActUser.class);
	}
	
	return result;
}
}
