package com.tzq.login.entity;

import java.io.Serializable;

import com.alibaba.fastjson.annotation.JSONField;
import com.tzq.login.model.WxTzqUser;

public class WechatAuthenticationResponse implements Serializable{
	/**
	 * 序列化
	 */
	private static final long serialVersionUID = -7592043617367045314L;
	private String thirdSession;
	@JSONField(name="data")
	private WechatAuthenticationResponseData data;
	@JSONField(name="userInfo")
	private WxTzqUser wxTzqUser;
	
	public WechatAuthenticationResponse(String thirdSession) {
		this.thirdSession =  thirdSession;
	}
	public String getThirdSession() {
		return thirdSession;
	}
	public void setThirdSession(String thirdSession) {
		this.thirdSession = thirdSession;
	}
	
	public WechatAuthenticationResponseData getData() {
		return data;
	}
	public void setData(WechatAuthenticationResponseData data) {
		this.data = data;
	}
	public WxTzqUser getWxTzqUser() {
		return wxTzqUser;
	}
	public void setWxTzqUser(WxTzqUser wxTzqUser) {
		this.wxTzqUser = wxTzqUser;
	}

}

class Data {
	
	
	
}
