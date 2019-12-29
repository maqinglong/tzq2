package com.tzq.usermanage.service;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.stereotype.Service;

import com.tzq.usermanage.model.ActUser;


/**
 * rabbitmq定义
 * @author 马庆龙
 *
 */
@Service
@EnableBinding(Source.class)
public interface IRabbitMqProvider {
	public void sendImgcompressMsg(ActUser actUser);
}
