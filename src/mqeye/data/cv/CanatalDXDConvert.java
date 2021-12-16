package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class CanatalDXDConvert extends AbstractConvert {
	static String resultStr_wsd1 = "01030400DB01914A34";
	static String resultStr_wsd2 = "01030400DB018FCA3C";
	static String resultStr_ztd1 = "01030600000000800040B5";
	

	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
			String result = value2 ;
			CanatalDXDResult r = new CanatalDXDResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),CanatalDXDResult.SVACZT))
				value1 = r.getZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),CanatalDXDResult.SVWD))
				value1 = r.getWD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),CanatalDXDResult.SVSD))
				value1 = r.getSD(result)+"";
		}
		return value1;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CanatalDXDResult result = new CanatalDXDResult();
		System.out.println(result.getWD(resultStr_wsd1));
		System.out.println(result.getWD(resultStr_wsd2));
		System.out.println(result.getSD(resultStr_wsd1));
		System.out.println(result.getSD(resultStr_wsd2));
		System.out.println(result.getZT(resultStr_ztd1));
	}

}

class  CanatalDXDResult {
	public final static String SVACZT = "SVACZT"; //0
	public final static String SVWD = "SVWD"; //2
	public final static String SVSD = "SVSD"; //1
	
	public String getDataStr(String result , int begin , int len){
		String data = StringUtils.substring(result, begin, begin+len);
		return data;
	}
	
	
	public float getWD(String result){
		String wdstr = getDataStr(result , 6,4);
		int wdInt = Integer.parseInt(wdstr,16);
		float wd = (float) wdInt/10;
		return wd;
	}
	
	public float getSD(String result){
		String sdstr = getDataStr(result , 10,4);
		int sdInt = Integer.parseInt(sdstr,16);
		float sd = (float) sdInt/10;
		return sd;
	}
	
	public int getZT(String result){
		int flag = -1 ;
		String ztstr = getDataStr(result,6,6);
		if (ztstr.equals("000000"))
			flag = 1;
		else
			flag = 0;
		
		return flag;
	}

}
