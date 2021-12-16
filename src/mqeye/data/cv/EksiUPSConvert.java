package mqeye.data.cv;

import mqeye.data.vo.Dsview;

import org.apache.commons.lang.StringUtils;

public class EksiUPSConvert  extends AbstractConvert {
	private final static String SPLIT = "\r\n";
	
	public static void main(String[] args){
		String resultStr = "SOK0\r\nSUF0\r\nSBP0\r\nSBM0\r\nSBL0";
		String[] results = resultStr.split(SPLIT);
		EksiUPSResult r = new EksiUPSResult();
		System.out.println("电池状态-UPSZT:" + r.getUPSZT(results));
		System.out.println("旁路状态-UPSPL:" + r.getUPSPL(results));
		System.out.println("供电状态-UPSGD:" + r.getUPSGD(results));
		
		resultStr = "DVIA0239.30\r\nDVIB0235.10\r\nDVIC0236.90\r\n" +
					"DVOA0221.10\r\nDVOB0221.70\r\nDVOC0222.50\r\n"	+
					"DLOA0052.81\r\nDLOB0074.30\r\nDLOC0053.48\r\n"	+
					"DVBT0439.20\r\nDVBN0432.50\r\nDVBL0100.00\r\n"	+
					"DHZF0049.90\r\nDTMB0120.00\r\nDTMP0035.00\r\n";
		results = resultStr.split(SPLIT);
		float[] data =  r.getUSPIV(results);
		System.out.println("输入电压-UPSIV:" + data[0] + ":" + data[1]+":" + data[2]);
		data =  r.getUSPOV(results);
		System.out.println("输出电压-UPSOV:" + data[0] + ":" + data[1]+":" + data[2]);
		data =  r.getUSPOL(results);
		System.out.println("输出负载-UPSOL:" + data[0] + ":" + data[1]+":" + data[2]);
		System.out.println("电池温度-UPSWD:" + r.getUPSWD(results));
		
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
					
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			String[] result = value2.split(SPLIT);
			EksiUPSResult r = new EksiUPSResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSZT))
				value1 = r.getUPSZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSGD))
				value1 = r.getUPSGD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSPL))
				value1 = r.getUPSPL(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSIV))
			{	float[] iv = r.getUSPIV(result); value1 = iv[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSOV))
			{	float[] ov = r.getUSPOV(result); value1 = ov[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVUPSOL))
			{	float[] ol = r.getUSPOL(result); value1 = ol[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiUPSResult.SVWD))
				value1 = r.getUPSWD(result)+"";
		}
		return value1;
	}

}
class EksiUPSResult {
	public final static String EMPTY = "EMPTY";
	
	public final static String SVUPSZT = "SVUPSZT"; //0
	public final static String SVUPSPL = "SVUPSPL"; //2
	public final static String SVUPSGD = "SVUPSGD"; //1
	
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVUPSOL = "SVUPSOL";
	public final static String SVWD = "SVWD";
	
	public final static String SVGDSJ = "SVGDSJ";
	
	
	
	private String ups_zt ;	 //UPS状态
	private String ups_pl ;  //UPS旁路
	private String ups_gd ;  //UPS供电
	private float[] ups_iv ;	 //三项输入电压 A B C
	private float[] ups_ov;		 //三项输出电压 A B C
	private float[] ups_ol;		 //三项输出负载 A B C
	
	private float ups_wd ; //电池温度
	
	private String getUPSValueByIdx(String[] results , int idx){
		if (results.length<=idx)
			return EMPTY ;
		else
			return results[idx];
	}
	
	private String getUPSValueByKey(String[] results , String key){
		String rs = EMPTY;
		for(String r:results)
		{
			if (StringUtils.startsWith(r, key))
			{
				rs = r; break;
			}
		}
		return rs;
	}
	
	private float getFloatData(String[] result , String key)
	{	String data_str = getUPSValueByKey(result,key);
		if (StringUtils.equalsIgnoreCase(data_str, EMPTY))	data_str = "0";
		data_str = StringUtils.remove(data_str, key);
		float data = 0;
		try{
			data = Float.parseFloat(data_str);
		}catch(NumberFormatException exc){
			System.err.println("Eksi UPS --" + key + "-- Data is Invalid!");
		}
		return data;
	}
	public String getUPSZT(String[] results){
		ups_zt = getUPSValueByIdx(results,0);
		return ups_zt;
	}
	public String getUPSPL(String[] results){
		ups_pl = getUPSValueByIdx(results,2);
		return ups_pl;
	}
	public String getUPSGD(String[] results){
		ups_gd = getUPSValueByIdx(results,1);
		return ups_gd;
	}
	
	
	public float[] getUSPIV(String[] result)
	{		
		ups_iv = new float[3];
		ups_iv[0] = getFloatData(result,"DVIA");
		ups_iv[1] = getFloatData(result,"DVIB");
		ups_iv[2] = getFloatData(result,"DVIC");
		
		return ups_iv;
	}
	
	public float[] getUSPOV(String[] result)
	{
		ups_ov = new float[3];
		ups_ov[0] =getFloatData(result,"DVOA");
		ups_ov[1] =getFloatData(result,"DVOB");
		ups_ov[2] =getFloatData(result,"DVOC");
		
		return ups_ov;
	}

	public float[] getUSPOL(String[] result)
	{
		ups_ol = new float[3];
		ups_ol[0] = getFloatData(result,"DLOA");
		ups_ol[1] = getFloatData(result,"DLOB");
		ups_ol[2] = getFloatData(result,"DLOC");
		
		return ups_ol;
	}
	
	
	public float getUPSWD(String[] result)
	{
		ups_wd = getFloatData(result,"DTMP");
		return ups_wd;
	}
}