/**
 * Copyright (c) 2002 sinosoft  Co. Ltd.
 * All right reserved.
 */
package com.tzq.sys.utils;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * **************************************************************** Program
 * NAME: 字符串处理工具类 programmer: Ouyangsheng (Modify) Create DATE: 2002.04.17
 * Create address: Beijing Modify DATE: Modify address:
 * ****************************************************************
 * 
 * 对字符串操作的工具类。
 * 
 * ****************************************************************
 */
public class StrTool {
private static Logger logger = LoggerFactory.getLogger(StrTool.class);
	public static final String EQUAL = "=";
	public static final String GREATER = ">";
	public static final String GREATGE_EQUAL = ">=";
	public static final String LESS = "<";
	public static final String LESS_EQUAL = "<=";
	public static final String NOT_EQUAL = "!=";
	public static final String CONTAIN = "*";
	public static final String BETWEEN = ":";
	public static final String DATEDELIMITER = "/";
	public static final String VISADATEDELIMITER = "-";
	public static final String TIMEDELIMITER = ":";
	public static final String ADDRESSDELIMITER = "$$";
	public static final String DELIMITER = "^";
	public static final String PACKAGESPILTER = "|";
	public static final String OR = "~!";
	public static final int LENGTH_OR = 2;
	public static final String BETWEEN_AND = ":";
	public static final String BLANK = "?";
	public static final String DIRECTMODE = "Direct Mode!" + DELIMITER
			+ DELIMITER;

	/**
	 * 将字符串按照指定的分隔字符进行拆分,返回从指定序号的分隔符到前一个分隔符之间的字符串
	 * 
	 * @param strMain
	 *            String 主字符串
	 * @param strDelimiters
	 *            String 分隔符
	 * @param intSerialNo
	 *            int 分隔符序号
	 * @return String 指定序号的分隔符到前一个分隔符之间的字符串,如果没有找到则返回"" 例如：值赋串类似于 值1|值2|值3|值4|
	 *         则intSerialNo=0 return "" intSerialNo=1 return "值1" intSerialNo=5
	 *         return ""
	 */
	public static String decodeStr(String strMain, String strDelimiters,
			int intSerialNo) {
		int intIndex = 0; /* 分隔符出现在主字符串中的起始位置 */
		int intCount = 0; /* 在扫描主字符串的过程中,第几次遇到分隔符字符串 */
		String strReturn = ""; /* 作为返回值的字符串 */

		if (strMain.length() < strDelimiters.length()) {
			return ""; /* 若主字符串比分隔符串还要短的话,则返回空字符串 */
		}

		intIndex = strMain.indexOf(strDelimiters);
		if (intIndex == -1) {
			return ""; /* 若主字符串中不存在分隔符,则返回空字符串 */
		}

		while (intIndex != -1) /* 未找到分隔符时退出循环,并返回空字符串 */
		{
			strReturn = strMain.substring(0, intIndex);
			intCount++;
			if (intCount == intSerialNo) {
				if (intIndex == 0) {
					return "";
				} else {
					return strReturn.trim();
				}
			}
			strMain = strMain.substring(intIndex + 1);
			intIndex = strMain.indexOf(strDelimiters);
		}
		return "";
	}

	/**
	 * 获取子串在主串中出现第 n 次的位置
	 * 
	 * @param strMain
	 *            String 主字符串
	 * @param strSub
	 *            String 子字符串
	 * @param intTimes
	 *            int 出现次数
	 * @return int 位置值,如果子串在主串中没有出现指定次数,则返回-1
	 */
	public static int getPos(String strMain, String strSub, int intTimes) {
		int intCounter = 0; // 循环记数
		int intPosition = 0; // 位置记录
		int intLength = strSub.length(); // 子串长度

		if (intTimes <= 0) {
			return -1;
		}
		while (intCounter < intTimes) {
			intPosition = strMain.indexOf(strSub, intPosition);
			if (intPosition == -1) {
				return -1;
			}
			intCounter++;
			intPosition += intLength;
		}
		return intPosition - intLength;
	}

	/**
	 * 获取从指定位置开始子串在主串中出现第 n 次的位置
	 * 
	 * @param strMain
	 *            String 主字符串
	 * @param strSub
	 *            String 子字符串
	 * @param intStartIndex
	 *            int 起始位置
	 * @param intTimes
	 *            int 出现次数
	 * @return int 位置值,如果从起始位置起子串在主串中没有出现指定次数,则返回-1
	 */
	public static int getPos(String strMain, String strSub, int intStartIndex,
			int intTimes) {
		if (strMain.length() - 1 < intStartIndex) {
			return -1;
		}
		int intPosition = getPos(strMain.substring(intStartIndex), strSub,
				intTimes);
		if (intPosition != -1) {
			intPosition += intStartIndex;
		}
		return intPosition;
	}

	/**
	 * 转换字符串（主要是SQL语句的条件串）
	 * 
	 * @param strMessage
	 *            String 待转换的字符串 (形如:<I>字段名^操作符^字段值^</I>) 新格式为：{[(]字段名 操作符 字段值
	 *            [)]连接符号}版本格式串^
	 * @return String 返回字符串(SQL语句中的 WHERE 条件子句,但不包括 'where'关键字)
	 */
	public static String makeCondition(String strMessage) {
		String strSegment = "";
		String strField = "";
		String strOperator = "";
		String strValue = "";
		String strRemain = "";
		String strReturn = "1=1"; // 恒真条件
		int intPosition = 0;

		if (strMessage.indexOf(StrTool.DELIMITER) < 0) {
			return strMessage;
		}
		strRemain = strMessage;

		if (!strRemain.endsWith(StrTool.DIRECTMODE + StrTool.DELIMITER)) {
			do {
				intPosition = getPos(strRemain, StrTool.DELIMITER, 3);
				if (intPosition < 0) // 分解完毕
				{
					return strReturn;
				}
				strSegment = strRemain.substring(0, intPosition + 1).trim();
				strRemain = strRemain.substring(intPosition + 1);
				if (strSegment.length() < 1) // 分段结束
				{
					break;
				}
				strField = decodeStr(strSegment, StrTool.DELIMITER, 1);
				strOperator = decodeStr(strSegment, StrTool.DELIMITER, 2);
				strValue = decodeStr(strSegment, StrTool.DELIMITER, 3);
				if (strValue.equals(BLANK)) {
					strValue = " ";
				}
				strReturn = strReturn + " AND " + "(";
				// 判断操作符
				if (strOperator.equals(BETWEEN)) {
					intPosition = strValue.indexOf(BETWEEN_AND);
					strReturn = strReturn + strField + " BETWEEN '"
							+ strValue.substring(0, intPosition).trim() + "'"
							+ " AND  '"
							+ strValue.substring(intPosition + 1).trim() + "' ";
				} else if (strOperator.equals(CONTAIN)) {
					strReturn = strReturn + strField + " matches '" + strValue
							+ "*' ";
				} else {
					strSegment = "";
					while (true) {
						intPosition = strValue.indexOf(OR);
						if (intPosition < 0) {
							if (strSegment.equals("")) {
								strReturn = strReturn + strField + " "
										+ strOperator + " '" + strValue + "' ";
							} else {
								strReturn = strReturn + strSegment + " OR "
										+ strField + " " + strOperator + " '"
										+ strValue.trim() + "' ";
							}
							break;
						}
						if (!strSegment.equals("")) {
							strSegment += " OR ";
						}

						strSegment = strSegment + strField + " " + strOperator
								+ " '"
								+ strValue.substring(0, intPosition).trim()
								+ "' ";
						strValue = strValue.substring(intPosition + LENGTH_OR);
					}
				}
				strReturn += ") ";
			} while (true);
		} else {
			strRemain = strRemain.substring(0, strRemain.length()
					- StrTool.DIRECTMODE.length() - StrTool.DELIMITER.length());
			if (strRemain.trim().equals("")) {
				strRemain = "1=1";
			}
			strReturn = strRemain;
		}
		return strReturn;
	}

	/**
	 * 将字符串转换为GBK字符串
	 * 
	 * @param strOriginal
	 *            String 原串
	 * @return String 将原串由ISO8859_1(Unicode)编码转换为GBK编码
	 */
	public static String unicodeToGBK(String strOriginal) {
		if (strOriginal != null) {
			try {
				// 如果在这里不作任何处理，全部直接返回的话，会是什么现象？
				if (isGBKString(strOriginal)) {
					// logger.debug("It's GBK: " + strOriginal);
					return strOriginal;
				} else {
					// logger.debug("It's ISO8859_1: " + strOriginal);
					return new String(strOriginal.getBytes("ISO8859_1"), "GBK");
				}

			} catch (Exception exception) {
				logger.debug(exception.getMessage());
				return strOriginal;
			}
		} else {
			return "";
		}
	}

//	/**
//	 * 将字符串转换为Unicode字符串
//	 * 
//	 * @param strOriginal
//	 *            String 原串
//	 * @return String 将原串由GBK编码转换为ISO8859_1(Unicode)编码
//	 */
//	public static String GBKToUnicode(String strOriginal) {
////		logger.debug("StrSql---"+strOriginal);
//		if (SysConst.CHANGECHARSET) {
//			if (strOriginal != null) {
//				try {
//					if (isGBKString(strOriginal)) {
//						return new String(strOriginal.getBytes("GBK"),
//								"ISO8859_1");
//					} else {
//						return strOriginal;
//					}
//				} catch (Exception exception) {
//					return strOriginal;
//				}
//			} else {
//				return null;
//			}
//		} else {
//			// 不可以随意修改这里的返回，否则导致系统存入的数据有问题
//			// return unicodeToGBK(strOriginal);
//			// logger.debug("Don't unicodeToGBK ......");
//			if (strOriginal == null) {
//				return "";
//			} else {
//				return strOriginal;
//			}
//		}
//	}

	/**
	 * 将字符串转换为Unicode字符串
	 * 
	 * @param strOriginal
	 *            String 原串
	 * @param realConvert
	 *            boolean 是否确认转换
	 * @return String 将原串由GBK编码转换为ISO8859_1(Unicode)编码
	 */
	public static String GBKToUnicode(String strOriginal, boolean realConvert) {
		if (!realConvert) {
			return unicodeToGBK(strOriginal);
		}
		if (strOriginal != null) {
			try {
				if (isGBKString(strOriginal)) {
					return new String(strOriginal.getBytes("GBK"), "ISO8859_1");
				} else {
					return strOriginal;
				}
			} catch (Exception exception) {
				return strOriginal;
			}
		} else {
			return null;
		}
	}

	/**
	 * 判断是否是GBK编码
	 * 
	 * @param tStr
	 *            String
	 * @return boolean
	 */
	public static boolean isGBKString(String tStr) {
		int tlength = tStr.length();
		// Integer t = new Integer(0);
		int t1 = 0;
		for (int i = 0; i < tlength; i++) {
			t1 = Integer.parseInt(Integer.toOctalString(tStr.charAt(i)));
			if (t1 > 511) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否是Unicode编码
	 * 
	 * @param tStr
	 *            String
	 * @return boolean
	 */
	public static boolean isUnicodeString(String tStr) {
		int tlength = tStr.length();
		Integer t = new Integer(0);
		int t1 = 0;
		for (int i = 0; i < tlength; i++) {
			t1 = Integer.parseInt(Integer.toOctalString(tStr.charAt(i)));
			if (t1 > 511) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 使用指定类中的decode()方法解包给定字符串
	 * 
	 * @param strMessage
	 *            String 字符串
	 * @param intCount
	 *            int 解包次数
	 * @param cl
	 *            Class 包含decode()方法的类,并且此类中含有FIELDNUM属性
	 * @return Vector 将每个解包数据生成对应的类实例,并将这些实例作为返回Vector的元素
	 * @throws Exception
	 *             如果查找decode方法出错、或者查找FIELDNUM字段出错、或者解包出错，方法抛出异常
	 */
	public static Vector stringToVector(String strMessage, int intCount,
			Class cl) throws Exception {
		int intFieldNum = 0;
		Object object = null;
		Vector vec = new Vector();
		int intPosition = 0;
		Class[] parameters = { String.class };
		Method method = null;
		Field field = null;
		String[] therecord = { new String() };
		try {
			object = cl.newInstance();
			method = cl.getMethod("decode", parameters);
			field = cl.getField("FIELDNUM");
			intFieldNum = field.getInt(object);

			for (int i = 0; i < intCount; i++) {
				object = cl.newInstance();
				intPosition = StrTool.getPos(strMessage,
						StrTool.PACKAGESPILTER, intFieldNum);

				if (intPosition == strMessage.length() - 1) {
					therecord[0] = strMessage;
					method.invoke(object, therecord);
					vec.addElement(object);
					break;
				} else {
					therecord[0] = strMessage.substring(0, intPosition + 1);

					method.invoke(object, therecord);
					vec.addElement(object);
					strMessage = strMessage.substring(intPosition + 1);
				}
			}
		} catch (Exception exception) {
			throw exception;
		} finally {
		}
		return vec;
	}

	/**
	 * 字符串替换函数
	 * 
	 * @param strMain
	 *            String 原串
	 * @param strFind
	 *            String 查找字符串
	 * @param strReplaceWith
	 *            String 替换字符串
	 * @return String 替换后的字符串，如果原串为空或者为""，则返回""
	 */
	public static String replace(String strMain, String strFind,
			String strReplaceWith) {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intStartIndex = 0;
		int intEndIndex = 0;

		if (strMain == null || strMain.equals("")) {
			return "";
		}

		while ((intEndIndex = strMain.indexOf(strFind, intStartIndex)) > -1) {
			// strReturn = strReturn +
			// strMain.substring(intStartIndex, intEndIndex) +
			// strReplaceWith;
			tSBql.append(strMain.substring(intStartIndex, intEndIndex));
			tSBql.append(strReplaceWith);

			intStartIndex = intEndIndex + strFind.length();
		}
		// strReturn += strMain.substring(intStartIndex, strMain.length());
		tSBql.append(strMain.substring(intStartIndex, strMain.length()));

		return tSBql.toString();
	}

	/**
	 * 由给定日期字符串获取格式日期(年/月/日)
	 * 
	 * @param strYear
	 *            String 年
	 * @param strMonth
	 *            String 月
	 * @param strDay
	 *            String 日
	 * @return String 格式日期：年/月/日
	 */
	public static String getDate(String strYear, String strMonth, String strDay) {
		String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intYear = 0;
		int intMonth = 0;
		int intDay = 0;
		if ((strYear != null) && (strMonth != null) && (strDay != null)
				&& (strYear.trim().length() > 0)
				&& (strMonth.trim().length() > 0)
				&& (strDay.trim().length() > 0)) {
			try {
				intYear = new Integer(strYear).intValue();
				intMonth = new Integer(strMonth).intValue();
				intDay = new Integer(strDay).intValue();
			} catch (Exception exception) {
				return strReturn;
			}

			if ((intYear <= 0) || (intMonth <= 0) || (intMonth > 12)
					|| (intDay <= 0) || (intDay > 31)) {
				strReturn = "";
			} else {
				tSBql.append(intYear);
				// strReturn = "" + intYear;
				if (intMonth < 10) {
					tSBql.append(StrTool.DATEDELIMITER);
					tSBql.append("0");
					tSBql.append(intMonth);
					// strReturn += StrTool.DATEDELIMITER + "0" + intMonth;
				} else {
					tSBql.append(StrTool.DATEDELIMITER);
					tSBql.append(intMonth);
					// strReturn += StrTool.DATEDELIMITER + intMonth;
				}

				if (intDay < 10) {
					tSBql.append(StrTool.DATEDELIMITER);
					tSBql.append("0");
					tSBql.append(intDay);
					// strReturn += StrTool.DATEDELIMITER + "0" + intDay;
				} else {
					tSBql.append(StrTool.DATEDELIMITER);
					tSBql.append(intDay);
					// strReturn += StrTool.DATEDELIMITER + intDay;
				}
				strReturn = tSBql.toString();
			}
		}
		return strReturn;
	}

	/**
	 * 获得系统日期(年/月/日)
	 * 
	 * @return String 格式日期：年/月/日
	 */
	public static String getDate() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intYear = Calendar.getInstance().get(Calendar.YEAR);
		int intMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		int intDate = Calendar.getInstance().get(Calendar.DATE);

		tSBql.append(intYear);
		// strReturn = "" + intYear;

		if (intMonth < 10) {
			tSBql.append(StrTool.DATEDELIMITER);
			tSBql.append("0");
			tSBql.append(intMonth);
			// strReturn += StrTool.DATEDELIMITER + "0" + intMonth;
		} else {
			tSBql.append(StrTool.DATEDELIMITER);
			tSBql.append(intMonth);
			// strReturn += StrTool.DATEDELIMITER + intMonth;
		}

		if (intDate < 10) {
			tSBql.append(StrTool.DATEDELIMITER);
			tSBql.append("0");
			tSBql.append(intDate);
			// strReturn += StrTool.DATEDELIMITER + "0" + intDate;
		} else {
			tSBql.append(StrTool.DATEDELIMITER);
			tSBql.append(intDate);
			// strReturn += StrTool.DATEDELIMITER + intDate;
		}
		return tSBql.toString();
	}

	/**
	 * 由给定日期字符串获取格式日期(年/月)
	 * 
	 * @param strYear
	 *            String 年
	 * @param strMonth
	 *            String 月
	 * @return String 格式日期：年/月
	 */
	public static String getDate(String strYear, String strMonth) {
		String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intYear = 0;
		int intMonth = 0;
		if ((strYear != null) && (strMonth != null)
				&& (strYear.trim().length() > 0)
				&& (strMonth.trim().length() > 0)) {
			intYear = new Integer(strYear).intValue();
			intMonth = new Integer(strMonth).intValue();
			if ((intYear <= 0) || (intMonth <= 0) || (intMonth > 12)) {
				strReturn = "";
			} else {
				tSBql.append(intYear);
				tSBql.append(StrTool.DATEDELIMITER);
				tSBql.append(intMonth);
				// strReturn = "" + intYear + StrTool.DATEDELIMITER + intMonth;
				strReturn = tSBql.toString();
			}
		}
		return strReturn;
	}

	/**
	 * 获取日期值中的年份
	 * 
	 * @param strDate
	 *            String 日期（年/月/日）
	 * @return String 年
	 */
	public static String getYear(String strDate) {
		int intPosition = 0;
		String strReturn = "";
		int intYear = 0;

		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 1);
			if (intPosition > 0) {
				strReturn = strDate.substring(0, intPosition);
				intYear = new Integer(strReturn).intValue();
				if ((intYear <= 0)) {
					strReturn = "";
				} else {
					strReturn = "" + intYear;
				}

				if ((intYear < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 获取系统日期中的年份
	 * 
	 * @return String 年
	 */
	public static String getYear() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intYear = Calendar.getInstance().get(Calendar.YEAR);
		tSBql.append(intYear);
		// strReturn = "" + intYear;
		return tSBql.toString();
	}

	/**
	 * 获取日期值中的月份
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String 月
	 */
	public static String getMonth(String strDate) {
		int intPosition1 = 0, intPosition2 = 0;
		String strReturn = "";
		int intMonth = 0;
		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition1 = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 1);
			intPosition2 = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 2);
			if ((intPosition1 > 0) && intPosition2 > intPosition1) {

				strReturn = strDate.substring(intPosition1 + 1, intPosition2);

				intMonth = new Integer(strReturn).intValue();
				if ((intMonth <= 0) || (intMonth > 12)) {
					strReturn = "";
				} else {
					strReturn = "" + intMonth;
				}

				if ((intMonth < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 获取系统日期中的月份
	 * 
	 * @return String 月
	 */
	public static String getMonth() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intMonth = Calendar.getInstance().get(Calendar.MONTH) + 1;
		if (intMonth < 10) {
			tSBql.append("0");
			tSBql.append(intMonth);
			// strReturn = "0" + intMonth;
		} else {
			tSBql.append(intMonth);
			// strReturn = "" + intMonth;
		}
		return tSBql.toString();
	}

	/**
	 * 获取给定日期值中的天
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String 天
	 */
	public static String getDay(String strDate) {
		int intPosition = 0;
		String strReturn = "";
		int intDay = 0;
		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition = StrTool.getPos(strDate, StrTool.DATEDELIMITER, 2);
			if (intPosition > 0) {

				strReturn = strDate.substring(intPosition + 1);

				intDay = new Integer(strReturn).intValue();

				if ((intDay <= 0) || (intDay > 31)) {
					strReturn = "";
				} else {
					strReturn = "" + intDay;
				}

				if ((intDay < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 获取系统日期值中的天
	 * 
	 * @return String 天
	 */
	public static String getDay() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intDate = Calendar.getInstance().get(Calendar.DATE);
		if (intDate < 10) {
			tSBql.append("0");
			tSBql.append(intDate);
			// strReturn = "0" + intDate;
		} else {
			tSBql.append(intDate);
			// strReturn = "" + intDate;
		}
		return tSBql.toString();
	}

	/**
	 * 获取系统时间值
	 * 
	 * @return String 小时: 分钟：秒
	 */
	public static String getTime() {
		StringBuffer tSBql = new StringBuffer();
		tSBql.append(StrTool.getHour());
		tSBql.append(":");
		tSBql.append(StrTool.getMinute());
		tSBql.append(":");
		tSBql.append(StrTool.getSecond());

		// return StrTool.getHour() + ":" + StrTool.getMinute() + ":" +
		// StrTool.getSecond();
		return tSBql.toString();
	}

	/**
	 * 获取系统时间值中的小时
	 * 
	 * @return String 小时
	 */
	public static String getHour() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intHour = Calendar.getInstance().get(Calendar.HOUR)
				+ (Calendar.HOUR_OF_DAY + 1)
				* Calendar.getInstance().get(Calendar.AM_PM);
		if (intHour < 10) {
			tSBql.append("0");
			tSBql.append(intHour);
			// strReturn = "0" + intHour;
		} else {
			tSBql.append(intHour);
			// strReturn = "" + intHour;
		}

		return tSBql.toString();
	}

	/**
	 * 获取系统时间值中的分钟
	 * 
	 * @return String 分钟
	 */
	public static String getMinute() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intMinute = Calendar.getInstance().get(Calendar.MINUTE);
		if (intMinute < 10) {
			tSBql.append("0");
			tSBql.append(intMinute);
			// strReturn = "0" + intMinute;
		} else {
			tSBql.append(intMinute);
			// strReturn = "" + intMinute;
		}

		return tSBql.toString();
	}

	/**
	 * 获取系统时间值中秒数
	 * 
	 * @return String 秒数
	 */
	public static String getSecond() {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		int intSecond = Calendar.getInstance().get(Calendar.SECOND);
		if (intSecond < 10) {
			tSBql.append("0");
			tSBql.append(intSecond);
			// strReturn = "0" + intSecond;
		} else {
			tSBql.append(intSecond);
			// strReturn = "" + intSecond;
		}

		return tSBql.toString();
	}

	/**
	 * 获取以"-"分隔日期值中的年份
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String 年
	 */
	public static String getVisaYear(String strDate) {
		int intPosition = 0;
		String strReturn = "";
		int intYear = 0;

		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 1);
			if (intPosition > 0) {
				strReturn = strDate.substring(0, intPosition);
				intYear = new Integer(strReturn).intValue();
				if ((intYear <= 0)) {
					strReturn = "";
				} else {
					strReturn = "" + intYear;
				}

				if ((intYear < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 获取以"-"分隔日期值的中文表示
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String YYYY年MM月DD日
	 */
	public static String getChnDate(String strDate) {
		String strReturn = getVisaYear(strDate) + "年" + getVisaMonth(strDate)
				+ "月" + getVisaDay(strDate) + "日";
		return strReturn;
	}

	/**
	 * 获取以"-"分隔日期值中的月份
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String 月
	 */
	public static String getVisaMonth(String strDate) {
		int intPosition1 = 0, intPosition2 = 0;
		String strReturn = "";
		int intMonth = 0;
		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition1 = StrTool
					.getPos(strDate, StrTool.VISADATEDELIMITER, 1);
			intPosition2 = StrTool
					.getPos(strDate, StrTool.VISADATEDELIMITER, 2);
			if ((intPosition1 > 0) && intPosition2 > intPosition1) {

				strReturn = strDate.substring(intPosition1 + 1, intPosition2);

				intMonth = new Integer(strReturn).intValue();
				if ((intMonth <= 0) || (intMonth > 12)) {
					strReturn = "";
				} else {
					strReturn = "" + intMonth;
				}

				if ((intMonth < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 获取以"-"分隔日期值中的天
	 * 
	 * @param strDate
	 *            String 日期
	 * @return String 天
	 */
	public static String getVisaDay(String strDate) {
		int intPosition = 0;
		String strReturn = "";
		int intDay = 0;
		if ((strDate != null) && (strDate.trim().length() > 0)) {
			intPosition = StrTool.getPos(strDate, StrTool.VISADATEDELIMITER, 2);
			if (intPosition > 0) {

				strReturn = strDate.substring(intPosition + 1);

				intDay = new Integer(strReturn).intValue();

				if ((intDay <= 0) || (intDay > 31)) {
					strReturn = "";
				} else {
					strReturn = "" + intDay;
				}

				if ((intDay < 10) && (!strReturn.equals(""))) {
					strReturn = "0" + strReturn;
				}
			}
		}
		return strReturn;
	}

	/**
	 * 字符串转换成HTML格式
	 * 
	 * @param strInValue
	 *            String 传入字符串
	 * @return String 转换后返回
	 */
	public static String toHTMLFormat(String strInValue) {
		// String strOutValue = "";
		StringBuffer tSBql = new StringBuffer();
		char c;

		for (int i = 0; i < strInValue.length(); i++) {
			c = strInValue.charAt(i);
			switch (c) {
			case '<':
				tSBql.append("&lt;");

				// strOutValue += "&lt;";
				break;
			case '>':
				tSBql.append("&gt;");

				// strOutValue += "&gt;";
				break;
			case '\n':
				tSBql.append("<br>");

				// strOutValue += "<br>";
				break;
			case '\r':
				break;
			case ' ':
				tSBql.append("&nbsp;");

				// strOutValue += "&nbsp;";
				break;
			default:
				tSBql.append(c);

				// strOutValue += c;
				break;
			}
		}
		return tSBql.toString();
	}

	/**
	 * 字符串打包
	 * 
	 * @param strInValue
	 *            String
	 * @return String
	 */
	public static String encode(String strInValue) {
		// String strOutValue = "";
		StringBuffer tSBql = new StringBuffer();
		char c;

		for (int i = 0; i < strInValue.length(); i++) {
			c = strInValue.charAt(i);
			switch (c) {
			case ':':

				// hardcode 同Common.js中 NAMEVALUEDELIMITER //域名与域值的分隔符
				tSBql.append("：");

				// strOutValue += "：";
				break;
			case '|':

				// hardcode 同Common.js中 FIELDDELIMITER //域之间的分隔符
				tSBql.append("┃");

				// strOutValue += "┃";
				break;
			case '\n':
				tSBql.append("\\n");

				// strOutValue += "\\n";
				break;
			case '\r':
				tSBql.append("\\r");

				// strOutValue += "\\r";
				break;
			case '\"':
				tSBql.append("\\\"");

				// strOutValue += "\\\"";
				break;
			case '\'':
				tSBql.append("\\\'");

				// strOutValue += "\\\'";
				break;
			case '\b':
				tSBql.append("\\b");

				// strOutValue += "\\b";
				break;
			case '\t':
				tSBql.append("\\t");

				// strOutValue += "\\t";
				break;
			case '\f':
				tSBql.append("\\f");

				// strOutValue += "\\f";
				break;
			case '\\':
				tSBql.append("\\\\");

				// strOutValue += "\\\\";
				break;
			case '<':
				tSBql.append("\\<");

				// strOutValue += "\\<";
				break;
			case '>':
				tSBql.append("\\>");

				// strOutValue += "\\>";
				break;
			default:
				tSBql.append(c);

				// strOutValue += c;
				break;
			}
		}
		return tSBql.toString();
	}



	/**
	 * 生成给定长度的字符串
	 * 
	 * @param intLength
	 *            int 字符串长度
	 * @return String
	 */
	public static String space(int intLength) {
		StringBuffer strReturn = new StringBuffer();
		for (int i = 0; i < intLength; i++) {
			strReturn.append(" ");
		}
		return strReturn.toString();
	}

	/**
	 * 以指定内容生成给定长度的字符串,不足以空格补齐,超长截去
	 * 
	 * @param strValue
	 *            String 指定内容
	 * @param intLength
	 *            int 字符串长度
	 * @return String
	 */
	public static String space(String strValue, int intLength) {
		int strLen = strValue.length();

		StringBuffer strReturn = new StringBuffer();
		if (strLen > intLength) {
			strReturn.append(strValue.substring(0, intLength));
		} else {
			if (strLen == 0) {
				strReturn.append(" ");
			} else {
				strReturn.append(strValue);
			}

			for (int i = strLen; i < intLength; i++) {
				strReturn.append(" ");
			}
		}
		return strReturn.toString();
	}

	/**
	 * 以指定内容生成给定长度的字符串,不足以指定字符按指定方式补齐,超长截去
	 * 
	 * @param strValue
	 *            String 指定内容
	 * @param intLength
	 *            int 字符串长度
	 * @param appendchar
	 *            char 指定字符
	 * @param LRApp
	 *            char 指定方式 L:左补齐 R:右补齐
	 * @return String
	 */
	public static String getStringWith(String strValue, int intLength,
			char appendchar, char LRApp) {
		int strLen = strValue.length();

		StringBuffer strReturn = new StringBuffer();
		if (strLen > intLength) {
			strReturn.append(strValue.substring(0, intLength));
		} else {
			if (strLen == 0) {
				strReturn.append(appendchar);
			} else {
				if (LRApp == 'R') {
					strReturn.append(strValue);// 右补齐
				}
			}

			for (int i = strLen; i < intLength; i++) {
				strReturn.append(appendchar);
			}
			if (strLen > 0) {
				if (LRApp == 'L') {
					strReturn.append(strValue);// 左补齐
				}
			}
		}
		return strReturn.toString();
	}

	/**
	 * 得到转化等价字节数组的长度
	 * 
	 * @param strSource
	 *            String
	 * @return int
	 */
	public static int getLength(String strSource) {
		return strSource.getBytes().length;
	}

	/**
	 * 复制文件
	 * 
	 * @param fromFile
	 *            String
	 * @param toFile
	 *            String
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void copyFile(String fromFile, String toFile)
			throws FileNotFoundException, IOException, Exception {
		FileInputStream in = new FileInputStream(fromFile);
		FileOutputStream out = new FileOutputStream(toFile);

		byte b[] = new byte[1024];
		int len;

		while ((len = in.read(b)) != -1) {
			out.write(b, 0, len);
		}
		out.close();
		in.close();
	}

	/**
	 * 数字大写
	 * 
	 * @param intValue
	 *            int
	 * @return String
	 */
	public static String toUpper(int intValue) {
		String strOutValue = "";
		String[] strTemp = { "零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖" };
		try {
			strOutValue = strTemp[intValue];
		} catch (Exception exception) {
			// Log.printException(exception.toString());
			strOutValue = "";
		}
		return strOutValue;
	}

	/**
	 * 得到单位
	 * 
	 * @param intValue
	 *            int
	 * @return String
	 */
	public static String getUnit(int intValue) {
		String strOutValue = "";
		String[] strTemp = { "仟", "佰", "拾", "亿", "仟", "佰", "拾", "万", "仟", "佰",
				"拾", "", "", "" };

		try {
			strOutValue = strTemp[intValue];
		} catch (Exception exception) {
			// Log.printException(exception.toString());
			strOutValue = "";
		}
		return strOutValue;
	}

	/**
	 * 数字转换为大写(数据不能超过12位)
	 * 
	 * @param dblInValue
	 *            double
	 * @return String
	 */
	public static String toChinese(double dblInValue) {
		String strOutValue = "";
		String strValue = new DecimalFormat("0").format(dblInValue * 100);
		String strTemp = "                 ";
		String strThat = "";
		int i = 0;
		int j = 0;
		int k = 0;

		k = strValue.length();
		if (k > 14) {
			// Log.printException("数字太大");
			return "";
		}

		strValue = strTemp.substring(0, 14 - k) + strValue;

		for (i = 14 - k; i < 14; i++) {

			j = new Integer(strValue.substring(i, i + 1)).intValue();
			if (j > 0) {
				strOutValue = strOutValue + strThat + toUpper(j) + getUnit(i);
				strThat = "";
			} else {
				if (i == 11) {
					strOutValue += getUnit(i);
				}

				if (i == 7 && !strValue.substring(4, 8).equals("0000")) {
					strOutValue += getUnit(i);
				}

				if (i == 3 && !strValue.substring(0, 4).equals("0000")) {
					strOutValue += getUnit(i);
				}

				if (i < 11) {
					strThat = toUpper(0);
				}

				if (i == 12) {
					strThat = toUpper(0);
				}
			}
		}
		return strOutValue;
	}

	/**
	 * 数字转换为大写
	 * 
	 * @param intInValue
	 *            int
	 * @return String
	 */
	public static String toChinese(int intInValue) {
		return toChinese((double) intInValue);
	}

	/**
	 * 数字转换为大写
	 * 
	 * @param longInValue
	 *            long
	 * @return String
	 */
	public static String toChinese(long longInValue) {
		return toChinese((double) longInValue);
	}

	/**
	 * 将输入的字符串进行转换，如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 * 
	 * @param tStr
	 *            输入字符串
	 * @return 如果为空，则返回""，如果不空，则返回该字符串去掉前后空格
	 */
	public static String cTrim(String tStr) {
		String ttStr = "";
		if (tStr == null) {
			ttStr = "";
		} else {
			ttStr = tStr.trim();
		}
		return ttStr;
	}

	/**
	 * 将字符串补数 将sourString的前面用cChar补足cLen长度的字符串
	 * 
	 * @param sourString
	 *            String
	 * @param cChar
	 *            String
	 * @param cLen
	 *            int
	 * @return String 字符串,如果字符串超长，则不做处理
	 */
	public static String LCh(String sourString, String cChar, int cLen) {
		int tLen = sourString.length();
		int i, iMax;
		String tReturn = "";
		if (tLen >= cLen) {
			return sourString;
		}
		iMax = cLen - tLen;
		for (i = 0; i < iMax; i++) {
			tReturn += cChar;
		}
		tReturn = tReturn.trim() + sourString.trim();
		return tReturn;
	}

	/**
	 * 将字符串补数 将sourString的前面用cChar补足cLen长度的字符串
	 * 
	 * @param sourString
	 *            String
	 * @param cChar
	 *            String
	 * @param cLen
	 *            int
	 * @return String 字符串,如果字符串超长，则不做处理
	 */
	public static String RCh(String sourString, String cChar, int cLen) {
		int tLen = sourString.length();
		int i, iMax;
		String tReturn = "";
		if (tLen >= cLen) {
			return sourString;
		}
		iMax = cLen - tLen;
		for (i = 0; i < iMax; i++) {
			tReturn += cChar;
		}
		tReturn = tReturn.trim() + sourString.trim();
		return tReturn;
	}

	/**
	 * 该函数得到c_Str中的第c_i个以c_Split分割的字符串
	 * 
	 * @param c_Str
	 *            目标字符串
	 * @param c_i
	 *            位置
	 * @param c_Split
	 *            分割符
	 * @return 如果发生异常，则返回空
	 */
	public static String getStr(String c_Str, int c_i, String c_Split) {
		String t_Str1 = "", t_Str2 = "", t_strOld = "";
		int i = 0, i_Start = 0, j_End = 0;
		t_Str1 = c_Str;
		t_Str2 = c_Split;
		i = 0;
		try {
			while (i < c_i) {
				i_Start = t_Str1.indexOf(t_Str2, 0);
				if (i_Start >= 0) {
					i += 1;
					t_strOld = t_Str1;
					t_Str1 = t_Str1.substring(i_Start + t_Str2.length(), t_Str1
							.length());
				} else {
					if (i != c_i - 1) {
						t_Str1 = "";
					}
					break;
				}
			}

			if (i_Start >= 0) {
				t_Str1 = t_strOld.substring(0, i_Start);
			}
		} catch (Exception ex) {
			t_Str1 = "";
		}
		return t_Str1;
	}

	/**
	 * 字符串替换函数
	 * 
	 * @param strMain
	 *            String 原串
	 * @param strFind
	 *            String 查找字符串
	 * @param strReplaceWith
	 *            String 替换字符串,在替换时不区分大小写
	 * @return String 替换后的字符串，如果原串为空或者为""，则返回""
	 */
	public static String replaceEx(String strMain, String strFind,
			String strReplaceWith) {
		// String strReturn = "";
		StringBuffer tSBql = new StringBuffer();
		String tStrMain = strMain.toLowerCase();
		String tStrFind = strFind.toLowerCase();
		int intStartIndex = 0;
		int intEndIndex = 0;

		if (strMain == null || strMain.equals("")) {
			return "";
		}

		while ((intEndIndex = tStrMain.indexOf(tStrFind, intStartIndex)) > -1) {
			// strReturn = strReturn +
			// strMain.substring(intStartIndex, intEndIndex) +
			// strReplaceWith;
			tSBql.append(strMain.substring(intStartIndex, intEndIndex));
			tSBql.append(strReplaceWith);

			intStartIndex = intEndIndex + strFind.length();
		}
		// strReturn += strMain.substring(intStartIndex, strMain.length());
		tSBql.append(strMain.substring(intStartIndex, strMain.length()));

		return tSBql.toString();
	}

	/**
	 * 字符串比较函数(处理空值的情况,视null与空字符串相等) 比较字符串a,b
	 * 
	 * @param a
	 *            String
	 * @param b
	 *            String
	 * @return boolean 如果相同返回true,否则返回fasle
	 */
	public static boolean compareString(String a, String b) {
		// 这个地方是否需要修改，还需要进一步考虑，如果修改的话，仍需要保证null=""的情况
		if (StrTool.unicodeToGBK(StrTool.cTrim(a)).equals(
				StrTool.unicodeToGBK(StrTool.cTrim(b)))) {
			return true;
		} else {
			return false;
		}
	}

	private static NumberFormat format = new DecimalFormat("0.00");

	/**
	 * 将数字格式化为金额形式 0.00
	 * 
	 * @param value
	 * @return String
	 */
	public static String formatDec(double value) {
		try {
			return format.format(value);
		} catch (Exception ex) {
			return String.valueOf(value);
		}
	}

	private static FDate fDate = new FDate();

	/**
	 * 格式化日期的为一般形式的简便用法.
	 * 
	 * @param date
	 *            Date the Date object you want to format.
	 * @return String format (yyyy-mm-dd) String
	 * @Date 12/21/05
	 */
	public static String formatDate(Date date) {
		return fDate.getString(date);
	}

	/**
	 * 测试函数
	 * 
	 * @param args
	 *            String[]
	 */
	public static void main(String[] args) {
		String r;
		// long a = System.currentTimeMillis();
		// logger.debug(a);
		// r = StrTool.replace("select * from ld where ABCDERewrABCDSFASf",
		// "ABC", "xXx");
		r = StrTool.getStringWith("2341", 14, '0', 'L');
		logger.debug(r);
		r = StrTool.getStringWith("2341", 14, '0', 'R');
		logger.debug(r);
		// logger.debug(System.currentTimeMillis() - a);
	}
}
