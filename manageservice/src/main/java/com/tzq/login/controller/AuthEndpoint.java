package com.tzq.login.controller;

import org.apache.http.auth.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.tzq.login.entity.AccountDto;
import com.tzq.login.entity.Consumer;
import com.tzq.login.entity.WechatAuthenticationResponse;
import com.tzq.login.model.WxTzqUser;
import com.tzq.login.service.WechatService;
import com.tzq.rabbitmq.HelloSender;

@RestController
public class AuthEndpoint {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthEndpoint.class);
    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private WechatService wechatService;
    @Autowired
    private HelloSender helloSender;
    
    @GetMapping("/test")
    public String test() {
    	helloSender.send();
        return "test_success";
    }

    @GetMapping("/testAuth")
    public String testAuth() {
        return "testAuth_success";
    }

    @PostMapping("/auth")
    public ResponseEntity<WechatAuthenticationResponse> createAuthenticationToken(@RequestBody AccountDto accountDto)
            throws AuthenticationException {
    	LOGGER.info("开始执行认证");
    	LOGGER.info("得到用户的OpenId{}",accountDto.getCode());
        WechatAuthenticationResponse jwtResponse = wechatService.wechatLogin(accountDto.getCode());
       
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/updateConsumerInfo")
    public void updateConsumerInfo(@RequestBody WxTzqUser consumer) {
    	LOGGER.info("保存微信用户信息 wechatOpneId" + consumer.getWechatOpenid() );
        wechatService.updateConsumerInfo(consumer);
    }

}