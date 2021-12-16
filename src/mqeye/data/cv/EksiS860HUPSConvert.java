package mqeye.data.cv;

import mqeye.data.vo.Dsview;

import org.apache.commons.lang.StringUtils;

public class EksiS860HUPSConvert  extends AbstractConvert {
	private final static String SPLIT = "/";
	private final static String resultStr1 = "!222.0/221.0/223.0 222.0/221.0/223.0 218.0/219.0/219.0 063.4/069.3/042.6";
	private final static String resultStr2 = "!00000001 00000111 00000000";
	
	public static void main(String[] args){
		EksiS860HUPSResult r = new EksiS860HUPSResult();
		System.out.println(r.getUSPIV(resultStr1)[0] +"+" + r.getUSPIV(resultStr1)[1] +"+" +r.getUSPIV(resultStr1)[2]);
		System.out.println(r.getUSPOV(resultStr1)[0] +"+" + r.getUSPOV(resultStr1)[1] +"+" +r.getUSPOV(resultStr1)[2]);
		System.out.println(r.getUSPOL(resultStr1)[0] +"+" + r.getUSPOL(resultStr1)[1] +"+" +r.getUSPOL(resultStr1)[2]);
		System.out.println(r.getUPSZT(resultStr2));
		System.out.println(r.getUPSPL(resultStr2));
		System.out.println(r.getUPSGD(resultStr2));
	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
					
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			String result = value2;
			EksiS860HUPSResult r = new EksiS860HUPSResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSZT))
				value1 = r.getUPSZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSGD))
				value1 = r.getUPSGD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSPL))
				value1 = r.getUPSPL(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSIV))
			{	float[] iv = r.getUSPIV(result); value1 = iv[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSOV))
			{	float[] ov = r.getUSPOV(result); value1 = ov[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),EksiS860HUPSResult.SVUPSOL))
			{	float[] ol = r.getUSPOL(result); value1 = ol[bit]+"";					  }
			
		}
		return value1;
	}

}
class EksiS860HUPSResult {
	public final static String EMPTY = "EMPTY";
	
	public final static String SVUPSZT = "SVUPSZT"; //0
	public final static String SVUPSPL = "SVUPSPL"; //2
	public final static String SVUPSGD = "SVUPSGD"; //1
	
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVUPSOL = "SVUPSOL";
	
	public final static String SVGDSJ = "SVGDSJ";
	
	private String ups_zt ;	 //UPS状态
	private String ups_pl ;  //UPS旁路
	private String ups_gd ;  //UPS供电
	private float[] ups_iv ;	 //三项输入电压 A B C
	private float[] ups_ov;		 //三项输出电压 A B C
	private float[] ups_ol;		 //三项输出负载 A B C
	
	private float[] getSplitData(String datas)
	{	String[] ups_dstr = null;
		float[] ups_datas = new float[3];
		int flag = 0;
		if (datas!=null)
		{
			ups_dstr = StringUtils.split(datas,"/");
			if (ups_dstr!=null && ups_dstr.length>=3)
			{	flag = 1;
				ups_datas[0] = Float.parseFloat(ups_dstr[0]);
				ups_datas[1] = Float.parseFloat(ups_dstr[1]);
				ups_datas[2] = Float.parseFloat(ups_dstr[2]);
			}
		}
		
		if (flag==0){
			ups_datas[0] = 0;
			ups_datas[1] = 0;
			ups_datas[2] = 0;
		}
		return ups_datas;
	}
	private String getData(String result , int idx)
	{	result = StringUtils.removeStart(result, "!");
		String datas[] = StringUtils.split(result, ' ');
		if (datas!=null && idx<datas.length)
			return datas[idx];
		else
			return EMPTY;
	}
	
	public String getUPSZT(String result){
		String datas = getData(result,2);
		
		if (StringUtils.equals(datas,"00000000"))
			ups_zt = "0";//Normal
		else if (StringUtils.equals(datas,EMPTY))
			ups_zt = "-1";//Empty
		else
			ups_zt = "1";//Error
		return ups_zt;
	}
	public String getUPSPL(String result){
		String datas = getData(result,2);
		String subDatas = StringUtils.substring(datas, 3, 6);
		if (StringUtils.equals(datas,EMPTY))
			ups_pl = "-1";//Empty
		else if (StringUtils.equals(subDatas,"000"))
			ups_pl = "0";//Normal
		else
			ups_pl = "1";//Error
		return ups_pl;
	}
	public String getUPSGD(String result){
		String datas = getData(result,1);
		String subDatas = StringUtils.substring(datas, 4, 7);
		
		if (StringUtils.equals(datas,EMPTY))
			ups_gd = "-1";//Empty
		else if (StringUtils.equals(subDatas,"000"))
			ups_gd = "0";//Normal
		else
			ups_gd = "1";//Error
		return ups_gd;
	}
	
	
	public float[] getUSPIV(String result)
	{	
		String datas = getData(result,0);
		ups_iv = getSplitData(datas);
		return ups_iv;
	}
	
	public float[] getUSPOV(String result)
	{
		String datas = getData(result,2);
		ups_ov = getSplitData(datas);
		return ups_ov;
	}

	public float[] getUSPOL(String result)
	{
		
		String datas = getData(result,3);
		ups_ol =  getSplitData(datas);
		return ups_ol;
	}
	
	
	
}