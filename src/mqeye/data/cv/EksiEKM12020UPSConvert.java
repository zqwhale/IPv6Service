package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class EksiEKM12020UPSConvert  extends AbstractConvert{
	private final static String resultStr1 = "(234.9 235.6 236.7 49.9 220.0 220.0 219.6 49.9 0024.1 0024.2 0015.0 026 026 016 275.2 275.0 041.9 100000000000";
	private final static String resultStr2 = "(L ";
	public static void main(String[] args){
		EksiEKM12020UPSResult r = new EksiEKM12020UPSResult();
		System.out.println(r.getUSPIV(resultStr1)[0] + ":" + r.getUSPIV(resultStr1)[1] + ":" + r.getUSPIV(resultStr1)[2]);
		System.out.println(r.getUSPOV(resultStr1)[0] + ":" + r.getUSPOV(resultStr1)[1] + ":" + r.getUSPOV(resultStr1)[2]);
		System.out.println(r.getUSPOL(resultStr1)[0] + ":" + r.getUSPOL(resultStr1)[1] + ":" + r.getUSPOL(resultStr1)[2]);
		System.out.println(r.getUSPOA(resultStr1)[0] + ":" + r.getUSPOA(resultStr1)[1] + ":" + r.getUSPOA(resultStr1)[2]);
		
		System.out.println( r.getUPSZT(resultStr2) );
		System.out.println( r.getUPSPL(resultStr1) );
		System.out.println( r.getWD(resultStr1) );
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		if (value2!=null && value2.length() > 0)
		{
					
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			String result = value2;
			EksiEKM12020UPSResult r = new EksiEKM12020UPSResult();
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSZT))
				value1 = r.getUPSZT(result);
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSPL))
				value1 = r.getUPSPL(result);
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVWD))
				value1 = r.getWD(result);
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSIV))
			{	float[] iv = r.getUSPIV(result); value1 = iv[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSOV))
			{	float[] ov = r.getUSPOV(result); value1 = ov[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSOL))
			{	float[] ol = r.getUSPOL(result); value1 = ol[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiEKM12020UPSResult.SVUPSOA))
			{	float[] oa = r.getUSPOA(result); value1 = oa[bit]+"";					  }
		}
		return value1;
	}
}
class EksiEKM12020UPSResult {
	
	public final static String PREFIX = "(";
	public final static String SPLIT = " ";
	
	public final static String EMPTY = "EMPTY";
	
	public final static String SVUPSZT = "SVUPSZT"; //0
	public final static String SVUPSPL = "SVUPSPL"; //2
//	public final static String SVUPSGD = "SVUPSGD"; //1
	
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVUPSOA = "SVUPSOA";
	public final static String SVUPSOL = "SVUPSOL";
	public final static String SVWD = "SVWD";
	
//	public final static String SVGDSJ = "SVGDSJ";
	
	private String ups_zt ;	 //UPS状态
	private String ups_pl ;  //UPS旁路
	
	private String ups_wd ; //电池温度
	
	private float[] ups_iv ;	 //三项输入电压 A B C
	private float[] ups_ov;		 //三项输出电压 A B C
	private float[] ups_ol;		 //三项输出负载 A B C
	private float[] ups_oa;		 //三项输出负载 A B C
	
	private String[] getSplitData(String result)
	{
		result = StringUtils.removeStart(result, PREFIX) ;
		String[] datas = StringUtils.split(result, SPLIT);
		return datas ;
	}
	
	private String getData(String result,int idx)
	{
		String[] datas =  getSplitData( result );
		String data = "";
		if ( idx < datas.length ){
			data = datas[idx];
		}
		return data;
	}
	private float[] getDatas(String result,int idx)
	{
		String[] datas =  getSplitData( result );
		String[] data = {"0","0","0"};
		float[] ups_datas = new float[3];
		if ( idx < datas.length )
		{
			data[0] = datas[idx+0];
			data[1] = datas[idx+1];
			data[2] = datas[idx+2];
		}
		ups_datas[0] = Float.parseFloat(data[0]);
		ups_datas[1] = Float.parseFloat(data[1]);
		ups_datas[2] = Float.parseFloat(data[2]);
		
		return ups_datas ;
	}
	
	
	public String getWD(String result){
		ups_wd = getData(result,16);
		if (StringUtils.isNotEmpty(ups_wd) && ups_wd.length() > 0)
		{
			try{
				ups_wd = Float.parseFloat(ups_wd) + "";
			}catch(NumberFormatException exc){
				System.err.println("Eksi UPS --" + ups_wd + "-- Data is Invalid!");
			}
		}
		return ups_wd;
	}
	public String getUPSPL(String result){
		ups_zt = getData(result,0);
		if (StringUtils.isNotEmpty(ups_zt) && ups_zt.length() > 0)
			ups_zt = StringUtils.left(ups_zt, 1);
		return ups_zt;
	}
	
	public String getUPSZT(String result){
		ups_pl = getData(result,17);
		if (StringUtils.isNotEmpty(ups_pl) && ups_pl.length() > 2)
			ups_pl = StringUtils.left(ups_pl, 2);
		return ups_pl ;
	}
	
	public float[] getUSPIV(String result)
	{	
		ups_iv = getDatas(result,0);
		return ups_iv;
	}
	
	public float[] getUSPOV(String result)
	{
		ups_ov = getDatas(result,4);
		return ups_ov;
	}

	public float[] getUSPOA(String result)
	{
		ups_oa = getDatas(result,8);
		return ups_oa;
	}
	
	public float[] getUSPOL(String result)
	{
		ups_ol =  getDatas(result,11);
		return ups_ol;
	}
	
	
	
}