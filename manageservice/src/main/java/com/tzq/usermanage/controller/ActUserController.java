package com.tzq.usermanage.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.tzq.contanst.Contansts;
import com.tzq.sys.utils.PubFun;
import com.tzq.usermanage.model.ActUser;
import com.tzq.usermanage.service.ActUserService;

@RestController
@RequestMapping("/usermanage")
public class ActUserController {
	
	private Logger logger = LoggerFactory.getLogger(ActUserController.class.getName());
	@Autowired
	ActUserService actUserService ;
	
	
	@PostMapping(value = "/addUser")
	@ResponseBody
	public String addUser(@RequestBody String userInfo, HttpServletRequest request) {
		logger.info("开始用户信息新建处理");
		String result = "";
		ActUser actuser = null;
		JSONObject jsonObj = null;

		logger.info("请求的用户信息jason : " + userInfo);
		if (userInfo != null && !"".equals(userInfo)) {
			jsonObj = JSONObject.parseObject(userInfo);
			actuser = JSONObject.toJavaObject(jsonObj, ActUser.class);
			String userNo = PubFun.CreateMaxSerialNo("AU", 5);
			actuser.setUserNo(userNo);
			int ret = actUserService.saveActUser(actuser);
		}
		

		return result;
	}
	
	private ActUser  getRequestUserInfo(HttpServletRequest request) {
		ActUser actuser = new ActUser();
		actuser.setUserName(request.getParameter("userName"));
	    actuser.setWechatId(request.getParameter("wechatId"));
//	    actuser.setPhone(phone);
//    	String age = request.getParameter("age");
//    	String wechatId = request.getParameter("wechatId");
    	
    	
		return actuser;
	}
	
	/**
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	@PostMapping(value="/addUserAndFileupload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    	logger.info("上传文件开始");
    	String ret = "";
    	
    	ActUser actUser =  getRequestUserInfo(request);
    	logger.info("userName:" + actUser.getUserName());
//    	logger.info("age:" + actUser.get);
    	logger.info("wechatId:" + actUser.getWechatId());
//    	logger.info("userName:" + userName);
    	
    	if (file != null && !file.isEmpty()) {
    		
            String saveFileName = file.getOriginalFilename();
            logger.info("文件名"+ saveFileName);
//	            File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
            File saveFile = new File("/app/upload/" + saveFileName);
            if (!saveFile.getParentFile().exists()) {
                saveFile.getParentFile().mkdirs();
            }
            try {
            	logger.info("上传文件写入磁盘");            	
                BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(saveFile));
                out.write(file.getBytes());
                out.flush();
                out.close();
                logger.info("上传文件成功");
                ret = makeResJson("00",Contansts.FIILUPLOAD_SUCCES_MESSAGE,saveFileName);                
            } catch (FileNotFoundException e) {
            	ret = makeResJson("02",Contansts.FIILUPLOAD_FAIL_MESSAGE,saveFileName);                
            } catch (IOException e) {
                e.printStackTrace();
                ret = makeResJson("03",Contansts.FIILUPLOAD_FAIL_MESSAGE,saveFileName);                
            }
            
        } else {
        	ret = makeResJson("01",Contansts.FIILUPLOAD_FAIL_MESSAGE,"");            
        }
    	
    	return ret;
    	
    }
    /**
     * 做成返回json
     * @param returnCode
     * @param errMsg
     * @return
     */
    private String makeResJson(String returnCode ,String errMsg ,String saveFileName) {
    	String ret = "";
    	
    	Map<String,String> retMap = new HashMap<String,String>(2);
    	retMap.put("returnCode", returnCode);
    	retMap.put("errMsg", errMsg);
    	retMap.put("saveFileName", saveFileName);
    	ret =  JSONObject.toJSONString(retMap);
    	return ret;  	
    	
    }
}
