package com.tzq.usermanage.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tzq.usermanage.model.ActUser;
import com.tzq.utils.ImgCompress;



/**
 * 监听消息队列
 * @author 马庆龙
 *
 */
@Service
@EnableBinding(Sink.class)
public class RabbitMqMsgListenner {
	private Logger logger = LoggerFactory.getLogger(RabbitMqMsgListenner.class.getName());
	@Value("${fileupload.path:/app/upload/}")
	private String fileUploadPath ;
	
//	public RabbitMqMsgListenner(Message<ActUser> message) {
//		logger.info("Message is reseived! {}" ,message.getPayload());
//	}
	@StreamListener(Sink.INPUT)
	public void input(Message<ActUser> message) {
		ActUser actUser = message.getPayload();
		ImgCompress imgCompress = null;
		String content = "";
		if ( actUser != null ) {
			content = JSONObject.toJSONString(actUser);
			try {
				imgCompress= new ImgCompress(fileUploadPath + actUser.getPicFileName());
				imgCompress.resizeFix(100, 100);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				logger.error("压缩图片出错{}",e.getMessage());
			}
			
		}
		logger.info("Message is reseived! {}" ,content);
	}

}
