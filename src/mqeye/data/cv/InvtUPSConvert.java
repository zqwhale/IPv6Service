package mqeye.data.cv;

import mqeye.data.vo.Dsview;
import mqeye.service.tools.CrcHexUtil;

import org.apache.commons.lang.StringUtils;


public class InvtUPSConvert extends AbstractConvert {
	
	public static void main(String[] args){
		String resultStr = "010402000178F0";
		byte[] result = CrcHexUtil.HexString2Bytes(resultStr);
		InvtUPSResult r = new InvtUPSResult();
		System.out.println("电池状态-UPSZT:" + r.getUPSZT(result));
		
		resultStr = "010402000178F0";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		System.out.println("供电方式-UPSGD:" + r.getUPSGD(result));
		
		resultStr = "010306091F0945094561C4";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		float[] data = r.getUSPIV(result);
		System.out.println("输入电压-UPSIV:" + data[0] + ":" + data[1]+":" + data[2] );
		
		resultStr = "01030608B308A108A071DD";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		data = r.getUSPOV(result);
		System.out.println("输出电压-UPSOV:" + data[0] + ":" + data[1]+":" + data[2] );
		
		resultStr = "010306007E007D002D597A";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		data = r.getUSPOA(result);
		System.out.println("输出电流-UPSOA:" + data[0] + ":" + data[1]+":" + data[2] );
		
		resultStr = "0103060070009F0026D14A";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		data = r.getUSPOL(result);
		System.out.println("输出负载-UPSOL:" + data[0] + ":" + data[1]+":" + data[2] );
		
		resultStr = "01030403E800007A43";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		System.out.println("电池电量-UPSDCDL:" + r.getUPSDCDL(result) );
		
		resultStr = "0103021E78B1C6";
		result = CrcHexUtil.HexString2Bytes(resultStr);
		System.out.println("供电时间-UPSGDSJ:" + r.getUPSGDSJ(result) );
	}
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
			byte[] result = CrcHexUtil.HexString2Bytes(value2);
		
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			InvtUPSResult r = new InvtUPSResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSZT))
					value1 = r.getUPSZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSGD))
				value1 = r.getUPSGD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSIV))
			{	float[] iv = r.getUSPIV(result); value1 = iv[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSOV))
			{	float[] ov = r.getUSPOV(result); value1 = ov[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSOA))
			{	float[] oa = r.getUSPOA(result); value1 = oa[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVUPSOL))
			{	float[] ol = r.getUSPOL(result); value1 = ol[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVDCDL))
				value1 = r.getUPSDCDL(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),InvtUPSResult.SVGDSJ))
				value1 = r.getUPSGDSJ(result)+"";
		}
		return value1;
	}

}
class InvtUPSResult {
	
	public final static String SVUPSZT = "SVUPSZT";
	public final static String SVUPSGD = "SVUPSGD";
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVUPSOA = "SVUPSOA";
	public final static String SVUPSOL = "SVUPSOL";
	public final static String SVDCDL = "SVDCDL";
	public final static String SVGDSJ = "SVGDSJ";
	
	
	
	private int ups_zt ;	 //UPS状态
	private int ups_gd ;  //UPS供电
	private float[] ups_iv ;	 //三项输入电压 A B C
	private float[] ups_ov;		 //三项输出电压 A B C
	private float[] ups_oa;		 //三项输出电流 A B C
	private float[] ups_ol;		 //三项输出负载 A B C
	private float ups_dcdl ;	//电池电量
	private int ups_gdsj ;	//供电时间
	
	private int getUPSValue(byte[] result){
		byte[] ups_value = new byte[]{result[3],result[4]};
		int value = CrcHexUtil.Bytes2Integer(ups_value);
		return value;
	}
	
	public int getUPSZT(byte[] result){
		ups_zt = getUPSValue(result);
		return ups_zt;
	}
	public int getUPSGD(byte[] result){
		ups_gd = getUPSValue(result);
		return ups_gd;
	}

	public float getUPSDCDL(byte[] result){
		ups_dcdl = getUPSValue(result) * 0.1f;
		return ups_dcdl;
	}
	
	public int getUPSGDSJ(byte[] result){
		ups_gdsj = getUPSValue(result) / 10;
		return ups_gdsj;
	}
	private float[] getUSP3Phase(byte[] result , float unit){
		float[] ups_3p = new float[3];
		byte[] data0 = new byte[]{result[3],result[4]};
		byte[] data1 = new byte[]{result[5],result[6]};
		byte[] data2 = new byte[]{result[7],result[8]};
	
		ups_3p[0] = CrcHexUtil.Bytes2Integer(data0)*unit;
		ups_3p[1] = CrcHexUtil.Bytes2Integer(data1)*unit;
		ups_3p[2] = CrcHexUtil.Bytes2Integer(data2)*unit;
		
		ups_3p[0] = (Math.round(ups_3p[0]*10))/10.0f;
		ups_3p[1] = (Math.round(ups_3p[1]*10))/10.0f;
		ups_3p[2] = (Math.round(ups_3p[2]*10))/10.0f;
		
		return ups_3p;
	}
	public float[] getUSPIV(byte[] result)
	{
		ups_iv = getUSP3Phase(result,0.1f);
		return ups_iv;
	}
	public float[] getUSPOV(byte[] result)
	{
		ups_ov = getUSP3Phase(result,0.1f);
		return ups_ov;
	}
	public float[] getUSPOA(byte[] result)
	{
		ups_oa = getUSP3Phase(result,0.1f);
		return ups_oa;
	}
	public float[] getUSPOL(byte[] result)
	{
		ups_ol = getUSP3Phase(result,0.1f);
		return ups_ol;
	}
	
}