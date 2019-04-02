package com.tzq.sys.model;

import java.io.Serializable;

public class ExeFunction implements Serializable  {

	/**
	 * 序列化
	 */
	private static final long serialVersionUID = 3343845958433022607L;

	private String noType;
	private String noLength;
	public String getNoType() {
		return noType;
	}
	public void setNoType(String noType) {
		this.noType = noType;
	}
	public String getNoLength() {
		return noLength;
	}
	public void setNoLength(String noLength) {
		this.noLength = noLength;
	}
	
}
