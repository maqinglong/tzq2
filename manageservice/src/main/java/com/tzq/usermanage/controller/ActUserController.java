package com.tzq.usermanage.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.tzq.contanst.Contansts;
import com.tzq.usermanage.model.ActUser;
import com.tzq.usermanage.service.ActUserService;
import com.tzq.utils.FileNameUtils;

@RestController
@RequestMapping("/usermanage")
public class ActUserController {
	
	private Logger logger = LoggerFactory.getLogger(ActUserController.class.getName());
	@Autowired
	ActUserService actUserService ;
	private final ResourceLoader resourceLoader;
	
	@Value("${fileupload.path:/app/upload/}")
	private String fileUploadPath ;
	
	@Autowired
    public ActUserController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
	
	@PostMapping(value = "/addUser",produces="application/json;charset=utf-8")
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
			
			int ret = actUserService.saveActUser(actuser);
			if ( ret >0 ) {
				result = makeResJson("00","add user Success!","");
			}
		}		

		return result;
	}
	
	private ActUser  getRequestUserInfo(HttpServletRequest request) {
		ActUser actuser = new ActUser();
		actuser.setUserName(request.getParameter("userName"));
	    actuser.setWechatOpenid(request.getParameter("wechatId"));
   	
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
    	logger.info("wechatId:" + actUser.getWechatOpenid());
    	
    	if (file != null && !file.isEmpty()) {
    		
            String saveFileName =  FileNameUtils.getFileName(file.getOriginalFilename()) ;
            
            logger.info("文件名"+ saveFileName);
            File saveFile = new File(fileUploadPath + saveFileName);
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
    /**
     * 通过微信号取得微信信息
     * @param userInfo
     * @param request
     * @return
     */
    @PostMapping(value = "/showUserInfo",produces="application/json;charset=utf-8")
    @ResponseBody
    public String showActUserInfo(@RequestBody String userInfo, HttpServletRequest request) {
    	String ret="";
    	logger.info("请求的用户查询jason :{} " , userInfo);
		if (userInfo != null && !"".equals(userInfo)) {
			JSONObject jsonObj = JSONObject.parseObject(userInfo);
			String wechatOpenid = jsonObj.getString("wechatOpenid");
			ActUser actuser = actUserService.getActUserInfo(wechatOpenid);
			if (actuser != null ) {
				List<ActUser> list = new ArrayList<ActUser>(1);
				list.add(actuser);
				ret = JSONObject.toJSONString(list);
			}			
		}
		logger.info("查询到的用户信息{}",ret);
    	return ret;
    }
    /**
     * 显示单张图片
     * @return
     */
    @RequestMapping("show")
    public ResponseEntity showPhotos(String fileName){

        try {
        	logger.info("显示文件名：" + fileName);
            // 由于是读取本机的文件，file是一定要加上的， path是在application配置文件中的路径
            return ResponseEntity.ok(resourceLoader.getResource("file:" + fileUploadPath + fileName));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
