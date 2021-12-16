package mqeye.service.tools;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import mqeye.service.Constant;

import org.apache.commons.lang.StringUtils;

/* 基本的通用函数 属性文件函数 ，日期转换函数 */
public class BaseCommonFunc {
	public static final String getStrFromDateTime(Date datetime){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 String dateString = formatter.format(datetime);
		 return dateString;
	}
	
	public static final String getStrFromDate(Date date){
		 SimpleDateFormat formatter = new SimpleDateFormat("YYYYMMDD");
		 String dateString = formatter.format(date);
		 return dateString;
	}
	
	public static final Date getDateTimeFromStr(String datetimeStr){
		 SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 Date d = null;
		 try {
			d = formatter.parse(datetimeStr);
		} catch (ParseException e) {
			System.err.println(datetimeStr + "无法转换正确的日期");
		}
		return d;
	} 
	
	public static final String getProperty(String key){
		 PropertiesUtil util=new PropertiesUtil(Constant.MQEYE_FILE);
		 String value = util.getProperty(key);
		 if (value!=null) value = value.trim();
		 return value ;
	}
	
	public static final boolean isPropertyValid(String key , String valid){
		String value = getProperty(key);
		return StringUtils.equalsIgnoreCase(value, valid);
	}
}
