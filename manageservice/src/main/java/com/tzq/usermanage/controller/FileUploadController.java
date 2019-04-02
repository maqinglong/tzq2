package com.tzq.usermanage.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.tzq.ManageserviceApplication;
import com.tzq.contanst.Contansts;

@RestController
//@RequestMapping("/file")
public class FileUploadController {
	
	private Logger logger = LoggerFactory.getLogger(FileUploadController.class.getName());
	
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        return new MultipartConfigElement("");
    }
	
	
	@PostMapping(value="/testupload")
	public String testUpload() {
		String ret = "";
		logger.info("This is testupload!!");
		return ret;
	}

//    @Bean
//    public MultipartResolver multipartResolver() {
//        org.springframework.web.multipart.commons.CommonsMultipartResolver multipartResolver = new org.springframework.web.multipart.commons.CommonsMultipartResolver();
//        multipartResolver.setMaxUploadSize(1000000);
//        return multipartResolver;
//    }
    /**
     * 单文件上传
     *
     * @param file
     * @param request
     * @return
     */
    @PostMapping(value="/fileupload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    	logger.info("上传文件开始");
    	String ret = "";
    	if (file != null && !file.isEmpty()) {
    		
            String saveFileName = file.getOriginalFilename();
            logger.info("文件名"+ saveFileName);
//            File saveFile = new File(request.getSession().getServletContext().getRealPath("/upload/") + saveFileName);
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
    /**
     * 多文件上传
     *
     * @param request
     * @return
     */
    @PostMapping("/uploadFiles")
    @ResponseBody
    public String uploadFiles(HttpServletRequest request) throws IOException {
        File savePath = new File(request.getSession().getServletContext().getRealPath("/upload/"));
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    File saveFile = new File(savePath, file.getOriginalFilename());
                    stream = new BufferedOutputStream(new FileOutputStream(saveFile));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    if (stream != null) {
                        stream.close();
                        stream = null;
                    }
                    return "第 " + i + " 个文件上传有错误" + e.getMessage();
                }
            } else {
                return "第 " + i + " 个文件为空";
            }
        }
        return "所有文件上传成功";
    }
}
