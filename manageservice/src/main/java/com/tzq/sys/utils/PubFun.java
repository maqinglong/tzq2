package com.tzq.sys.utils;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.tzq.sys.dao.ExeFunctionDao;
import com.tzq.sys.model.ExeFunction;


/**
 * 公共函数
 *
 */
@Service
public class PubFun {
	
	@Autowired
	private ExeFunctionDao exeFunctionDao;
	
	private static ExeFunctionDao tExeFunctionDao;
	
	@PostConstruct
	public void init() {
		tExeFunctionDao = exeFunctionDao;
	}
	
	
//	private static ExeFunctionDao tExeFunctionDao = SpringContextHolder.getBean(ExeFunctionDao.class);
	
	public static int getAge(String birthDay) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return getAge(sdf.parse(birthDay));
		} catch (Exception  e) {
			return 0;
		}
	}
	
   //根据起保日期计算被保人年龄
	public static int getAge(Date birthDay,Date effective) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			return 0;
		}
		
		cal.setTime(effective);
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}
	//根据当前日期计算被保人年龄
	public static int getAge(Date birthDay) throws Exception {
		Calendar cal = Calendar.getInstance();
		if (cal.before(birthDay)) {
			return 0;
		}
		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH);
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
		cal.setTime(birthDay);

		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH);
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;
		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				age--;
			}
		}
		return age;
	}
	/**
	 * 功能：产生指定长度的流水号，一个号码类型一个流水
	 * 
	 * @param cNoType
	 *            String 流水号的类型
	 * @param cNoLength
	 *            int 流水号的长度
	 * @return String 返回产生的流水号码
	 */
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public synchronized static String CreateMaxNo(String cNoType, int cNoLength) {
		if ((cNoType == null) || (cNoType.trim().length() <= 0)
				|| (cNoLength <= 0)) {
			System.out.println("NoType长度错误或NoLength错误");
			return null;
		}

		cNoType = cNoType.toUpperCase();
		String cNoLimit = "SN";

		BigInteger tMaxNo = new BigInteger("0");
		try {
			// 开始事务
			ExeFunction tExeFunction = new ExeFunction();
			tExeFunction.setNoType(cNoType);
			String result = tExeFunctionDao.createMaxNo(tExeFunction);
			
			tMaxNo = new BigInteger(result);
			
		} catch (Exception Ex) {
			Ex.printStackTrace();
			System.out.println("创建最大流水号异常。。。");
		}
		return PubFun.LCh(tMaxNo.toString(), "0", cNoLength);
	}

    /**
     * 功能：产生号码类型对应的指定长度的流水号，一个号码类型一个流水，调用CreateMaxNo
     * 示例 PubFun.CreateMaxSerialNo("HRS",20)，返回HRS00000000000000000001
     * @param cNoType
     *            String 流水号的类型
     * @param cNoLength
     *            int 流水号的长度
     * @return String 返回产生的类型 + 流水号码
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public synchronized static String CreateMaxSerialNo(String cNoType, int cNoLength) {
        return cNoType + CreateMaxNo(cNoType,cNoLength);
    }

	/**
	 * 将字符串补数,将sourString的<br>前面</br>用cChar补足cLen长度的字符串,如果字符串超长，则不做处理
	 * <p><b>Example: </b><p>
	 * <p>LCh("Minim", "0", 10) returns "00000Minim"<p>
	 * @param sourString 源字符串
	 * @param cChar 补数用的字符
	 * @param cLen 字符串的目标长度
	 * @return 字符串
	 */
	public static String LCh(String sourString, String cChar, int cLen) {
		int tLen = sourString.length();
		int i, j, iMax;
		String tReturn = "";
		if (tLen >= cLen)
			return sourString;
		iMax = cLen - tLen;
		for (i = 0; i < iMax; i++) {
			tReturn = cChar + tReturn;
		}
		tReturn = tReturn.trim() + sourString.trim();
		return tReturn;
	}
	/**
	 * 得到当前时点 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getCurrDateTime() {
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String tReturn = sdf.format(date);
			return tReturn;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 得到当前系统日期 
	 * 
	 * @return 当前日期的格式字符串,日期格式为"yyyy-MM-dd"
	 */
	public static String getCurrentDate() 
	{
		GregorianCalendar tGCalendar = new GregorianCalendar();
    	StringBuffer tStringBuffer = new StringBuffer(10);
        int sYears = tGCalendar.get(Calendar.YEAR);
        tStringBuffer.append(sYears);
        tStringBuffer.append('-');
        int sMonths = tGCalendar.get(Calendar.MONTH)+1;
        if(sMonths < 10){
        	tStringBuffer.append('0');
        }
        tStringBuffer.append(sMonths);
        tStringBuffer.append('-');
        int sDays = tGCalendar.get(Calendar.DAY_OF_MONTH);
        if(sDays < 10){
        	tStringBuffer.append('0');
        }
        tStringBuffer.append(sDays);
        String tString = tStringBuffer.toString();
    	return tString;
	
	}

	/**
	 * 得到当前系统时间
	 * 
	 * @return 当前时间的格式字符串，时间格式为"HH:mm:ss"
	 */
	public static String getCurrentTime() 
	{
		GregorianCalendar tGCalendar = new GregorianCalendar();
   	    StringBuffer tStringBuffer = new StringBuffer(8);
        int sHOUR = tGCalendar.get(Calendar.HOUR_OF_DAY);
        if(sHOUR < 10){
       	 tStringBuffer.append('0');
        }
        tStringBuffer.append(sHOUR);
        tStringBuffer.append(':');
        int sMINUTE = tGCalendar.get(Calendar.MINUTE);
        if(sMINUTE < 10){
       	 tStringBuffer.append('0');
        }
        tStringBuffer.append(sMINUTE);
        tStringBuffer.append(':');
        int sSECOND = tGCalendar.get(Calendar.SECOND);
        if(sSECOND < 10){
       	 tStringBuffer.append('0');
        }
        tStringBuffer.append(sSECOND);
        String tString = tStringBuffer.toString();
        return tString;

	}
	
	/**
	 * 通过起始日期和终止日期计算以时间间隔单位为计量标准的时间间隔 author: HST
	 * <p><b>Example: </b><p>
	 * <p>参照calInterval(String  cstartDate, String  cendDate, String unit)，前两个变量改为日期型即可<p>
	 * @param startDate 起始日期，Date变量
	 * @param endDate 终止日期，Date变量
	 * @param unit 时间间隔单位，可用值("Y"--年 "M"--月 "D"--日)
	 * @return 时间间隔,整形变量int
	 */
	public static int calInterval(Date startDate, Date endDate, String unit) {
		int interval = 0;

		GregorianCalendar sCalendar = new GregorianCalendar();
		sCalendar.setTime(startDate);
		int sYears = sCalendar.get(Calendar.YEAR);
		int sMonths = sCalendar.get(Calendar.MONTH);
		int sDays = sCalendar.get(Calendar.DAY_OF_MONTH);
		int sDaysOfYear = sCalendar.get(Calendar.DAY_OF_YEAR);

		GregorianCalendar eCalendar = new GregorianCalendar();
		eCalendar.setTime(endDate);
		int eYears = eCalendar.get(Calendar.YEAR);
		int eMonths = eCalendar.get(Calendar.MONTH);
		int eDays = eCalendar.get(Calendar.DAY_OF_MONTH);
		int eDaysOfYear = eCalendar.get(Calendar.DAY_OF_YEAR);

		if (unit.equals("Y")) {
			interval = eYears - sYears;
			if (eDaysOfYear < sDaysOfYear)
				interval--;
		}
		if (unit.equals("M")) {
			interval = eYears - sYears;
			interval = interval * 12;

			interval = eMonths - sMonths + interval;
			if (eDays < sDays)
				interval--;
		}
		if (unit.equals("D")) {
			interval = eYears - sYears;
			interval = interval * 365;

			interval = eDaysOfYear - sDaysOfYear + interval;

			// 处理润年
			int n = 0;
			eYears--;
			if (eYears > sYears) {
				int i = sYears % 4;
				if (i == 0) {
					sYears++;
					n++;
				}
				int j = (eYears) % 4;
				if (j == 0) {
					eYears--;
					n++;
				}
				n += (eYears - sYears) / 4;
			}
			if (eYears == sYears) {
				int i = sYears % 4;
				if (i == 0)
					n++;
			}
			interval += n;
		}
		return interval;
	}

	/**
	 * 通过起始日期和终止日期计算以时间间隔单位为计量标准的时间间隔 author: HST
	 * <p><b>Example: </b><p>
	 * <p>calInterval("2002-10-8", "2012-10-1", "Y") returns 10<p>
	 * @param startDate 起始日期，(String,格式："YYYY-MM-DD")
	 * @param endDate 终止日期，(String,格式："YYYY-MM-DD")
	 * @param unit 时间间隔单位，可用值("Y"--年 "M"--月 "D"--日)
	 * @return 时间间隔,整形变量int
	 */
	public static int calInterval(String cstartDate, String cendDate,
			String unit) {
		FDate fDate = new FDate();
		Date startDate = fDate.getDate(cstartDate);
		Date endDate = fDate.getDate(cendDate);
//		if (fDate.mErrors.needDealError())
//			return 0;

		int interval = 0;

		GregorianCalendar sCalendar = new GregorianCalendar();
		sCalendar.setTime(startDate);
		int sYears = sCalendar.get(Calendar.YEAR);
		int sMonths = sCalendar.get(Calendar.MONTH);
		int sDays = sCalendar.get(Calendar.DAY_OF_MONTH);
		int sDaysOfYear = sCalendar.get(Calendar.DAY_OF_YEAR);
        int sday = Integer.parseInt(cstartDate.substring(8));
		
		GregorianCalendar eCalendar = new GregorianCalendar();
		eCalendar.setTime(endDate);
		int eYears = eCalendar.get(Calendar.YEAR);
		int eMonths = eCalendar.get(Calendar.MONTH);
		int eDays = eCalendar.get(Calendar.DAY_OF_MONTH);
		int eDaysOfYear = eCalendar.get(Calendar.DAY_OF_YEAR);
		int eday = Integer.parseInt(cendDate.substring(8));

		if (StrTool.cTrim(unit).equals("Y")) {
			interval = eYears - sYears;
			if(checkScanner(sYears) && !checkScanner(eYears) && 
					(sMonths == 1 && (sday == 29) || sMonths > 1))
			{
				sDaysOfYear -= 1;
			}
			else if(!checkScanner(sYears) && checkScanner(eYears) && 
					(eMonths == 1 && (eday == 29) || eMonths > 1))
			{
				eDaysOfYear -= 1;
			}
			if (eDaysOfYear < sDaysOfYear)
			{
				interval--;
		    }
		}
		if (StrTool.cTrim(unit).equals("M")) {
			interval = eYears - sYears;
			interval = interval * 12;

			interval = eMonths - sMonths + interval;
			if (eDays < sDays)
			{
			    if(checkScanner(sYears) && !checkScanner(eYears) && (sDays - eDays ) == 1 && sday == 29)
	            {
	            }
			    else if(!checkScanner(sYears) && checkScanner(eYears) && (eDays - sDays ) == 1 && eday == 29)
                {
                }
	            else 
	            {
	                interval--;
	            }
				
			}
		}
		if (StrTool.cTrim(unit).equals("D")) {
			interval = eYears - sYears;
			interval = interval * 365;

			interval = eDaysOfYear - sDaysOfYear + interval;

			// 处理润年
			int n = 0;
			eYears--;
			if (eYears > sYears) {
				int i = sYears % 4;
				if (i == 0) {
					sYears++;
					n++;
				}
				int j = (eYears) % 4;
				if (j == 0) {
					eYears--;
					n++;
				}
				n += (eYears - sYears) / 4;
			}
			if (eYears == sYears) {
				int i = sYears % 4;
				if (i == 0)
					n++;
			}
			interval += n;
		}
		return interval;
	}
	
	/**
	 * 判断是否为闰年 XinYQ added on 2006-09-25
	 */
	public static boolean isLeapYear(int nYear) {
		boolean ResultLeap = false;
		ResultLeap = (nYear % 400 == 0) | (nYear % 100 != 0) & (nYear % 4 == 0);
		return ResultLeap;
	}
	
	/**
	 * 计算日期的函数 author: HST 参照日期指当按照年月进行日期的计算的时候，参考的日期，如下例，结果返回2002-03-31
	 * <b>Example: </b>
	 * FDate tD=new FDate();
	 * Date baseDate =new Date();
	 * baseDate=tD.getDate("2000-02-29");
	 * Date comDate =new Date();
	 * comDate=tD.getDate("1999-12-31");
	 * int inteval=1;
	 * String tUnit="M";
	 * Date tDate =new Date();
	 * tDate=PubFun.calDate(baseDate,inteval,tUnit,comDate);
	 * logger.debug(tDate.toString());
	 * @param baseDate
	 *            起始日期
	 * @param interval
	 *            时间间隔
	 * @param unit
	 *            时间间隔单位
	 * @param compareDate
	 *            参照日期
	 * @return Date类型变量
	 */
	public static Date calDate(Date baseDate, int interval, String unit,
			Date compareDate) {
		Date returnDate = null;

		GregorianCalendar tBaseCalendar = new GregorianCalendar();
		tBaseCalendar.setTime(baseDate);

		if (unit.equals("Y")) {
			tBaseCalendar.add(Calendar.YEAR, interval);
		}
		if (unit.equals("M")) {
			tBaseCalendar.add(Calendar.MONTH, interval);
		}
		if (unit.equals("D")) {
			tBaseCalendar.add(Calendar.DATE, interval);
		}

		if (compareDate != null) {
			GregorianCalendar tCompCalendar = new GregorianCalendar();
			tCompCalendar.setTime(compareDate);
			int nBaseYears = tBaseCalendar.get(Calendar.YEAR);
			int nBaseMonths = tBaseCalendar.get(Calendar.MONTH);
			int nCompMonths = tCompCalendar.get(Calendar.MONTH);
			int nCompDays = tCompCalendar.get(Calendar.DATE);
			int oldDays = tCompCalendar.get(Calendar.DATE);

			if (unit.equals("Y")) {
				/*目前来看，compareDate非空的情况下unit一定是Y，这样确保新日期的月和日是不变的，但是此时还需要考虑一种情况，
                    如果旧日期是闰年2-29，例如2008-2-29，而新的年份为非闰年，例如2038，拼在一起为2038-2-29，对于此种数据，	
                  tCompCalendar.set(nBaseYears, nCompMonths, nCompDays)将会自动将其置为2038-3-1，为了避免出现此问题，
                    我们需要特殊处理nCompDays	
				*/
				//特殊处理1，如果旧日期为润年2-29，新年份nBaseYears为非闰年，日nCompDays只能为28
				if(1==nCompMonths && 29==oldDays && !isLeapYear(nBaseYears))
				{
					nCompDays=28;
				}
				tCompCalendar.set(nBaseYears, nCompMonths, nCompDays);
				if (tCompCalendar.before(tBaseCalendar)) {
					//特殊处理2，如果旧日期为润年2-29，新年份nBaseYears+1为非闰年，日nCompDays只能为28
					if(1==nCompMonths && 29==oldDays && !isLeapYear(nBaseYears+1))
					{
						nCompDays=28;
					}
					//特殊处理3，如果旧日期为润年2-29，新年份nBaseYears为非闰年，日nCompDays在特殊处理1中改为28
					//但是如果nBaseYears+1为闰年，需要还原nCompDays，实际上就是29。
					if(1==nCompMonths && 29==oldDays && isLeapYear(nBaseYears+1))
					{
						nCompDays=oldDays;
					}
					tBaseCalendar.set(nBaseYears + 1, nCompMonths, nCompDays);
					returnDate = tBaseCalendar.getTime();
				} else {
					returnDate = tCompCalendar.getTime();
				}
			}
			if (unit.equals("M")) {
				tCompCalendar.set(nBaseYears, nBaseMonths, nCompDays);
				if (tCompCalendar.before(tBaseCalendar)) {
					tBaseCalendar.set(nBaseYears, nBaseMonths + 1, nCompDays);
					returnDate = tBaseCalendar.getTime();
				} else {
					returnDate = tCompCalendar.getTime();
				}
			}
			if (unit.equals("D")) {
				returnDate = tBaseCalendar.getTime();
			}
			tCompCalendar = null;
		} else {
			returnDate = tBaseCalendar.getTime();
		}
		tBaseCalendar = null;

		return returnDate;
	}



	/**
	 * 重载计算日期，参数见楼上，add by Minim
	 * @param baseDate
	 *            String
	 * @param interval
	 *            int
	 * @param unit
	 *            String
	 * @param compareDate
	 *            String
	 * @return String
	 */
	public static String calDate(String baseDate, int interval, String unit,
			String compareDate) {
		try {
			FDate tFDate = new FDate();
			Date bDate = tFDate.getDate(baseDate);
			Date cDate = tFDate.getDate(compareDate);
			return tFDate.getString(calDate(bDate, interval, unit, cDate));
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static boolean checkScanner(int y) {
		if ((y % 4 == 0 && y % 100 != 0) || y % 400 == 0) {
			return true;
		}
		return false;
	}
	
	public static Date getSimpleDate(int year,int month,int day,int hourOfDay,int minute,int second){
		Calendar calendar =  Calendar.getInstance();//获取一个日历实例 
		calendar.set(year, month-1, day,hourOfDay,minute,second);//设定日历的日期  
		return calendar.getTime();
	}
	
	/**
	 * 获取流水号
	 * @param cNoType 前导字符串
	 * @param cRandomLegth  随机数的位数
	 * @return 返回示例：HB02015082611245604123
	 */
    public static String createSeriaNo(String cNoType,int cRandomLegth){
    	StringBuffer sbf = new StringBuffer();
    	String date = new SimpleDateFormat("yyyyMMdd").format(new Date());  
        String seconds = new SimpleDateFormat("HHmmss").format(new Date());
        String random = getRandom(cRandomLegth);
    	return sbf.append(cNoType).append(date).append(seconds).append(random).toString();
    }
    
    /**
     * 默认的5位随机数的流水号产生方法
     * @param cNoType
     * @return
     */
    public static String createSeriaNo(String cNoType){
    	return createSeriaNo(cNoType,5);
    }
    
    /** 
     * 产生N位随机数
     * @return 
     */  
    public static String getRandom(int length){  
        Random rad=new Random();  
        
        //获得10的length次幂，作为下面获取随机数的上限范围
        int nRange = (int) Math.pow(10, length);
        
        String tResult  = rad.nextInt(nRange) +"";  
        
        //不足位数用“0”左补齐
        String result = LCh(tResult, "0", length);
        
        return result;  
    }

    /**
     * 获取业务流水号
     * @param cNoType 前导字符串
     * @param cLength  随机数的位数
     * @return 返回示例：HB02015082611245604123
     */
    public static String createBusinessSeriaNo(String cNoType,int cLength){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String time = new SimpleDateFormat("HHmmss").format(new Date());
        return cNoType + date + time + CreateMaxNo(cNoType,cLength);
    }

    /**
     * 默认的5位随机数的流水号产生方法
     * @param cNoType
     * @return
     */
    public static String createBusinessSeriaNo(String cNoType){
        return createBusinessSeriaNo(cNoType,5);
    }
    
    /** 
     *  
     * @description 转换javabean ,将class2中的属性值赋值给class1，如果class1属性有值，则不覆盖 
     *              ，前提条件是有相同的属性名 
     *              </p> 
     * @param class1 
     *            基准类,被赋值对象 
     * @param class2 
     *            提供数据的对象 
     * @throws Exception 
     * @author ex_dingyongbiao 
     * @see 
     */  
    public static void converJavaBean(Object class1, Object class2) {  
        try {  
            Class<?> clazz1 = class1.getClass();  
            Class<?> clazz2 = class2.getClass();  
            // 得到method方法  
            Method[] method1 = clazz1.getMethods();  
            Method[] method2 = clazz2.getMethods();  
   
            int length1 = method1.length;  
            int length2 = method2.length;  
            if (length1 != 0 && length2 != 0) {  
                // 创建一个get方法数组，专门存放class2的get方法。  
                Method[] get = new Method[length2];  
                for (int i = 0, j = 0; i < length2; i++) {  
                    if (method2[i].getName().indexOf("get") == 0) {  
                        get[j] = method2[i];  
                        ++j;  
                    }  
                }  
   
                for (int i = 0; i < get.length; i++) {  
                    if (get[i] == null)// 数组初始化的长度多于get方法，所以数组后面的部分是null  
                        continue;  
                    // 得到get方法的值，判断时候为null，如果为null则进行下一个循环  
                    Object value = get[i].invoke(class2, new Object[] {});  
                    if (null == value)  
                        continue;  
                    // 得到get方法的名称 例如：getXxxx  
                    String getName = get[i].getName();  
                    // 得到set方法的时候传入的参数类型，就是get方法的返回类型  
                    Class<?> paramType = get[i].getReturnType();  
                    Method getMethod = null;  
                    try {  
                        // 判断在class1中时候有class2中的get方法，如果没有则抛异常继续循环  
                        getMethod = clazz1.getMethod(getName, new Class[] {});  
                    } catch (NoSuchMethodException e) {  
                        continue;  
                    }  
                    // class1的get方法不为空并且class1中get方法得到的值为空，进行赋值，如果class1属性原来有值，则跳过  
                    if (null == getMethod || null != getMethod.invoke(class1, new Object[] {}))  
                        continue;  
                    // 通过getName 例如getXxxx 截取后得到Xxxx，然后在前面加上set，就组装成set的方法名  
                    String setName = "set" + getName.substring(3);  
                    // 得到class1的set方法，并调用  
                    Method setMethod = clazz1.getMethod(setName, paramType);  
                    setMethod.invoke(class1, value);  
                }  
            }  
        } catch(Exception e) {  
        	  System.out.println(e);
        }  
    }
}
