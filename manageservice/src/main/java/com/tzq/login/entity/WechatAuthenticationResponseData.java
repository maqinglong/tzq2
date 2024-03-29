package com.tzq.login.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;

public class WechatAuthenticationResponseData implements Serializable {

	/**
	 * 序列化号码
	 */
	private static final long serialVersionUID = -3666824080626462528L;

	@JSONField(name="accessToken")
	private String accessToken;

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}


}
