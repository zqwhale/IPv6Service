package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class ChadiUPSConvert extends AbstractConvert {
	private final static String SPLIT = " ";
	public static void main(String[] args){
		
		String resultStr = "(239.1 239.1 221.4 024 50.0 2.26 25.9 00000000\r";
		resultStr = StringUtils.remove(resultStr, "(");
		resultStr = StringUtils.remove(resultStr, "\r");
		
		String[] results = resultStr.split(SPLIT);
		ChadiUPSResult r = new ChadiUPSResult();
		float iv = r.getUPSIV(results);
		System.out.println("ÊäÈëµçÑ¹-UPSIV:" + iv);
		float ov = r.getUPSOV(results);
		System.out.println("Êä³öµçÑ¹-UPSIV:" + ov); 
		float wd = r.getUPSWD(results);
		System.out.println("ÎÂ¶È-UPSWD:" + wd); 
		System.out.println("UPS×´Ì¬-UPSZT:" + r.getUPSZT(results));
		System.out.println("UPSÅÔÂ·-UPSPL:" + r.getUPSPL(results));
		System.out.println("µç³Ø×´Ì¬-UPSGD:" + r.getUPSGD(results));
	
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
			
			value2 = StringUtils.remove(value2, "(");
			value2 = StringUtils.remove(value2, "\r");
			
			String[] result = value2.split(SPLIT);
			ChadiUPSResult r = new ChadiUPSResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVUPSZT))
				value1 = r.getUPSZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVUPSGD))
				value1 = r.getUPSGD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVUPSPL))
				value1 = r.getUPSPL(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVUPSIV))
			{	float iv = r.getUPSIV(result); value1 = iv+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVUPSOV))
			{	float ov = r.getUPSOV(result); value1 = ov+"";				  		}
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),ChadiUPSResult.SVWD))
			{	value1 = r.getUPSWD(result)+"";										}
		}
		return value1;
	}

}
class ChadiUPSResult {
public final static String EMPTY = "EMPTY";
	
	public final static String SVUPSZT = "SVUPSZT"; //0
	public final static String SVUPSPL = "SVUPSPL"; //2
	public final static String SVUPSGD = "SVUPSGD"; //1
	
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVWD = "SVWD";
	
	private String getUPSValueByIdx(String[] results , int idx){
		if (results.length<=idx)
			return EMPTY ;
		else
			return results[idx];
	}
	
	private float getFloatValue(String data_str)
	{	float data = 0;
		try{
			data = Float.parseFloat(data_str);
		}catch(NumberFormatException exc){
			System.err.println("Chadi UPS -- Data is Invalid!");
		}
		return data;
	}
	public float getUPSIV(String[] result)
	{	
		String data_str = getUPSValueByIdx(result , 0);
		float data = getFloatValue(data_str);
		return data ;
		
	}
	
	public float getUPSOV(String[] result)
	{	
		String data_str = getUPSValueByIdx(result , 2);
		float data = getFloatValue(data_str);
		return data ;
	}

	public float getUPSWD(String[] result)
	{	
		String data_str = getUPSValueByIdx(result , 6);
		float data = getFloatValue(data_str);
		return data ;
	}

	public String getUPSZT(String[] result)
	{
		String data_str = getUPSValueByIdx(result , 7);
		String data = StringUtils.substring(data_str, 3, 4);
		return data ;
	}
	
	public String getUPSPL(String[] result)
	{
		String data_str = getUPSValueByIdx(result , 7);
		String data = StringUtils.substring(data_str, 2, 3);
		return data ;
	}
	
	public String getUPSGD(String[] result)
	{
		String data_str = getUPSValueByIdx(result , 7);
		String data = StringUtils.substring(data_str, 1, 2);
		return data ;
	}
	
	
}