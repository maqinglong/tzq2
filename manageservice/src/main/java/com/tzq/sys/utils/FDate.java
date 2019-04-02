/**
 * Copyright (c) 2002 sinosoft  Co. Ltd.
 * All right reserved.
 */
package com.tzq.sys.utils;


import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * <p>Title: 保费计算类 </p> <p>Description: 通过传入的保单信息和责任信息构建出保费信息和领取信息 </p> <p>Copyright:
 * Copyright (c) 2002</p> <p>Company: sinosoft</p> @author HST
 * 
 * @version 1.0 @date 2002-07-01
 */
public class FDate implements Cloneable ,java.io.Serializable{
private static Logger logger = LoggerFactory.getLogger(FDate.class);

	private final String pattern = "yyyy-MM-dd";
	private final String pattern1 = "yyyyMMdd";
	private SimpleDateFormat df;
	private SimpleDateFormat df1;

	// @Constructor
	public FDate() {
		df = new SimpleDateFormat(pattern);
		df1 = new SimpleDateFormat(pattern1);
	}

	/**
	 * 克隆FDate对象 2005－04－15 朱向峰添加
	 * 
	 * @return Object
	 * @throws CloneNotSupportedException
	 */
	public Object clone() throws CloneNotSupportedException {
		FDate cloned = (FDate) super.clone();
		// clone the mutable fields of this class
		return cloned;
	}

	// @Method
	/**
	 * 输入符合格式要求的日期字符串，返回日期类型变量
	 * <p>
	 * <b>Example: </b>
	 * <p>
	 * <p>
	 * getDate("2002-10-8") returns "Tue Oct 08 00:00:00 CST 2002"
	 * <p>
	 * 
	 * @param dateString
	 *            日期字符串
	 * @return 日期类型变量
	 */
	public Date getDate(String dateString) {
		Date tDate = null;
		try {
			if (dateString.indexOf("-") != -1) {
				tDate = df.parse(dateString);
			} else {
				tDate = df1.parse(dateString);
			}
		} catch (Exception e) {
		}

		return tDate;
	}

	/**
	 * 输入日期类型变量，返回日期字符串
	 * <p>
	 * <b>Example: </b>
	 * <p>
	 * <p>
	 * getString("Tue Oct 08 00:00:00 CST 2002") returns "2002-10-8"
	 * <p>
	 * 
	 * @param mDate
	 *            日期类型变量
	 * @return 日期字符串
	 */
	public String getString(Date mDate) {
		String tString = null;
		if (mDate != null) {
			tString = df.format(mDate);
		}
		return tString;
	}

	/**
	 * 输入字符串类型变量，返回日期字符串
	 * <p>
	 * <b>Example: </b>
	 * <p>
	 * <p>
	 * getString("20021008") returns "2002-10-8"
	 * <p>
	 * 
	 * @param tDateStr
	 *         日期字符串“yyyyMMdd”
	 * @return 日期字符串“yyyy-MM-dd”
	 */
	public String getDateString(String tDateStr){
		String tString = null;
		 Date tDate = getDate(tDateStr);
		 tString = getString(tDate);
		return tString;
	}
	/**
	 * MAIN方法，测试用
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		// FDate tFDate = new FDate();
		// logger.debug(tFDate.getDateString("20021008"));
	}
}
