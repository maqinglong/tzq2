package com.tzq.usermanage.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.tzq.usermanage.model.ActUser;

/**
 * 发送rabbit消息
 * @author 马庆龙
 *
 */
@Service
@EnableBinding(Source.class)
public class RabbitMqProviderImpl implements IRabbitMqProvider{
	private Logger logger = LoggerFactory.getLogger(RabbitMqProviderImpl.class.getName());
	@Autowired
	private MessageChannel output;

	@Override
	public void sendImgcompressMsg(ActUser actUser) {	
		logger.info("The message has been send! {}",JSONObject.toJSONString(actUser));
		output.send(MessageBuilder.withPayload(actUser).build());
		
	}

}
