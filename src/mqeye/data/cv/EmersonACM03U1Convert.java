package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class EmersonACM03U1Convert extends AbstractConvert{
	
	
	
	public static void main(String[] args){
		String resultStr_1 = "~21016000103C00A002BF087808780878000101F400AFFE6601FFFE660004000400040004F0F1\r";
		String resultStr_2 = "~21016000802600010000000100000000000000000000010000F683\r";
		String resultStr_3 = "?21016000103C00AC022808BF08BF08BF000101F500BCFE66031BFE66000\r";
		String resultStr_4 = "?1016000103C00AB022908BF08BF08BF000101F400BAFE660319FE660004\r";
		String resultStr_5 = "?21016000802600010001000100000100000000000000010100F680\r";
		
		EmersonACM03U1Result r = new EmersonACM03U1Result();
		System.out.println(r.getRTN(resultStr_1));
		System.out.println(r.getWD(resultStr_1));
		System.out.println(r.getSD(resultStr_1));
		System.out.println(r.getZT(resultStr_2));
		
		System.out.println(r.getRTN(resultStr_3));
		System.out.println(r.getWD(resultStr_3));
		System.out.println(r.getSD(resultStr_3));
		System.out.println(r.getZT(resultStr_5));
		
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2 )
		{
			if (!value2.startsWith("~"))
			{
				String temp = StringUtils.right(value2, value2.length() -1);
				if ( !StringUtils.startsWith(temp,"2") )	temp = "2" + temp;
				value2 = "~" + temp;
			}
			EmersonACM03U1Result r = new EmersonACM03U1Result();
			String result = value2;
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EmersonACM03U1Result.SVACZT))
				value1 = r.getZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EmersonACM03U1Result.SVWD))
				value1 = r.getWD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EmersonACM03U1Result.SVSD))
				value1 = r.getSD(result)+"";
		}

		return value1;
	}

}

class EmersonACM03U1Result {
	public final static String SVACZT = "SVACZT"; //0
	public final static String SVWD = "SVWD"; //2
	public final static String SVSD = "SVSD"; //1
	
	public String getDataStr(String result , int begin , int len){
		String data = StringUtils.substring(result, begin, begin+len);
		return data;
	}
	
	public int getRTN(String result){
		int flag = 1;
		String rtn = getDataStr(result,7,2);
		if (StringUtils.equals(rtn, "00")) flag = 0;
		
		return flag;
	}
	public float getWD(String result){
		String wdstr = getDataStr(result,13,4);
		int wdInt = Integer.parseInt(wdstr,16);
		float wd = (float) wdInt/10;
		return wd;
	}
	
	public float getSD(String result){
		String sdstr = getDataStr(result,17,4);
		int sdInt = Integer.parseInt(sdstr,16);
		float sd = (float) sdInt/10;
		return sd;
	}
	
	public int getZT(String result){
		int flag = -1 ;
		String ktzt = getDataStr(result,13,2);
		String fjzt = getDataStr(result,15,2);
		if (StringUtils.equals(ktzt, "00")) 
			flag = 0;
		else if (StringUtils.equals(ktzt, "01"))
			flag = 1;
		else if (StringUtils.equals(ktzt, "02"))
			flag = 2;
		else if (StringUtils.equals(ktzt, "03"))
			flag = 3;
		System.out.println(ktzt + "---" + fjzt);
		return flag;
	}

}