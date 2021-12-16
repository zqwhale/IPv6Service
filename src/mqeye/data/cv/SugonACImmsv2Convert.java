package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

import mqeye.data.vo.Dsview;

public class SugonACImmsv2Convert extends AbstractConvert {
	
	
	public static void main(String[] args){
	String hexresult = "b5d001000a910004050607151413141414171614140005000500050" +
				"0050909000b0b0b0b28260000292f000012282800001e1e0000000000000000000" +
				"0000000001d1900000000000000000000000000000000000000000000000000000" +
				"000000000000000000000000000000000000000000000000000000000000000000" +
				"000000000000000000000000000000000000000000000078f";
	
	byte[] results = HexUtils.convert(hexresult);
	SugonACResult r = new SugonACResult();
	float wd = r.getSVWD(results);
	System.out.println("AC -SVWD:" + wd);
	String zt = r.getACZT(results);
	System.out.println("Statu-SVACZT:" + zt);
		
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{	byte[] results = HexUtils.convert(value2);
			SugonACResult r = new SugonACResult();
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SugonACResult.SVACZT))
					value1 = r.getACZT(results);
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SugonACResult.SVWD))	
			{		float  t = r.getSVWD(results);
					if (t<-100) value1 = null;
					else value1 = t + "";
			}
		}
		return value1;
	}

}

class SugonACResult{
	public final static String SVACZT = "SVACZT"; //0
	public final static String SVWD = "SVWD"; //2
	
	int inner_status = 31;
	int[] outer_status = {68,69,70,71};
	
	int[] infans = {17,18};	// Input fan up and down  WD
	
	public String getResultData(byte[] result , int idx){
		String data = null ;
		if (idx < result.length)
			data = Integer.toString(result[idx]);
		return data ;
	}
	
	public String getACZT(byte[] result ){
		String inst = getResultData(result  , inner_status);
		if ( inst ==null ) inst = "-1";
		
		String oust = "0";
		for(int i:outer_status)
		{
			String t = getResultData(result  , i);
			if ( t ==null ) {	oust = "-1";break; }
			if (t.equals("1") || t.equals("-1")) {		oust = t;break; }
		}
		String data = inst + ":" + oust ;
		return data ;
	}
		
	
	public float getSVWD(byte[] result ){
		int flag = 0;
		int sum_wd = 0;
		for(int i:infans)
		{	String t = getResultData(result  , i);
			if ( t != null ) 
			{
				sum_wd = sum_wd + Integer.parseInt(t);
			}
			else
			{
				flag = -1; break;
			}
		}
		
		float wd = -1000;
		if (flag!=-1)
			wd = 	(float)sum_wd / infans.length ;
		return wd;
	}
}