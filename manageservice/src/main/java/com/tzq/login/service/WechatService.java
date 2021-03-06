package com.tzq.login.service;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.tzq.login.config.WechatAuthProperties;
import com.tzq.login.dao.WxTzqUserMapper;
import com.tzq.login.entity.Consumer;
import com.tzq.login.entity.WechatAuthCodeResponse;
import com.tzq.login.entity.WechatAuthenticationResponse;
import com.tzq.login.entity.WechatAuthenticationResponseData;
import com.tzq.login.filter.AppContext;
import com.tzq.login.model.WxTzqUser;

@Service
public class WechatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(WechatService.class);

    @Autowired
    private WxTzqUserMapper wxTzqUserMapper;

    /**
     * 服务器第三方session有效时间，单位秒, 默认1天
     */
    private static final Long EXPIRES = 86400L;

    private RestTemplate wxAuthRestTemplate = new RestTemplate();

    @Autowired
    private WechatAuthProperties wechatAuthProperties;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public WechatAuthenticationResponse wechatLogin(String code) {
        WechatAuthCodeResponse response = getWxSession(code);

        String wxOpenId = response.getOpenid();
        String wxSessionKey = response.getSession_key();
        LOGGER.info("取得用户的OPID{}sessionKey{}",wxOpenId,wxSessionKey);
        WxTzqUser consumer = new WxTzqUser();
        consumer.setWechatOpenid(wxOpenId);
        loginOrRegisterConsumer(consumer);

        Long expires = response.getExpiresIn();
        String thirdSession = create3rdSession(wxOpenId, wxSessionKey, expires);
        WechatAuthenticationResponse wresp =new WechatAuthenticationResponse(thirdSession);
        WechatAuthenticationResponseData wrespData = new WechatAuthenticationResponseData();
        wrespData.setAccessToken(thirdSession);
        wresp.setData(wrespData);
        wresp.setWxTzqUser(consumer);
        String retjson = JSONObject.toJSONString(wresp);
        LOGGER.info("返回用户认证的信息:" + retjson);
        return wresp;
    }

    public WechatAuthCodeResponse getWxSession(String code) {
        LOGGER.info(code);
        String urlString = "?appid={appid}&secret={srcret}&js_code={code}&grant_type={grantType}";
        String response = wxAuthRestTemplate.getForObject(
                wechatAuthProperties.getSessionHost() + urlString, String.class,
                wechatAuthProperties.getAppId(),
                wechatAuthProperties.getSecret(),
                code,
                wechatAuthProperties.getGrantType());
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerFor(WechatAuthCodeResponse.class);
        WechatAuthCodeResponse res;
        try {
            res = reader.readValue(response);
        } catch (IOException e) {
            res = null;
            LOGGER.error("反序列化失败", e);
        }
        LOGGER.info(response);
        if (null == res) {
            throw new RuntimeException("调用微信接口失败");
        }
        if (res.getErrcode() != null) {
            throw new RuntimeException(res.getErrmsg());
        }
        res.setExpiresIn(res.getExpiresIn() != null ? res.getExpiresIn() : EXPIRES);
        return res;
    }

    public String create3rdSession(String wxOpenId, String wxSessionKey, Long expires) {
    	LOGGER.info("开始创建第三方令牌");
        String thirdSessionKey = RandomStringUtils.randomAlphanumeric(64);
        StringBuffer sb = new StringBuffer();
        sb.append(wxSessionKey).append("#").append(wxOpenId);

        stringRedisTemplate.opsForValue().set(thirdSessionKey, sb.toString(), expires, TimeUnit.SECONDS);
        return thirdSessionKey;
    }

    private void loginOrRegisterConsumer(WxTzqUser wxTzqUser) {
        WxTzqUser wxTzqUser1 = wxTzqUserMapper.selectByPrimaryKey(wxTzqUser.getWechatOpenid());
        if (null == wxTzqUser1) {
        	Date now = new Date();
        	wxTzqUser.setCreateTime(now);
        	wxTzqUser.setUpdateTime(now);
        	wxTzqUserMapper.insertSelective(wxTzqUser);
        }
    }

    /**
     * 跟新微信用户信息
     * @param wxTzqUser
     */
    public void updateConsumerInfo(WxTzqUser wxTzqUser) {
    	WxTzqUser wxTzqUser1 = wxTzqUserMapper.selectByPrimaryKey(wxTzqUser.getWechatOpenid());
    	if ( wxTzqUser1 != null ) {
    		wxTzqUser1.setUpdateTime(new Date());
        	wxTzqUserMapper.updateByPrimaryKeySelective(wxTzqUser1);
    	}else {
    		Date now = new Date();
        	wxTzqUser.setCreateTime(now);
        	wxTzqUser.setUpdateTime(now);
        	wxTzqUserMapper.insertSelective(wxTzqUser);   		
    	}
    	
    }

}