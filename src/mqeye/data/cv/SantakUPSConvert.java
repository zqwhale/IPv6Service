package mqeye.data.cv;

import mqeye.data.vo.Dsview;

import org.apache.commons.lang.StringUtils;

public class SantakUPSConvert extends AbstractConvert {
	private final static String SPLIT = " ";
	
	private static String remove( String result){
		result = StringUtils.remove(result, "(");
		result = StringUtils.remove(result, "\r");
		return result;
	}
	public static void main(String[] args){

	  //String resultStr_1= "(217.6 219.2 219.0 50.0 218.8 ---.- ---.- 50.0 001 --- --- 215.2 ---.- 26.6 34463 100 30 00000000 00000000 00\r";
		String resultStr_1= "(233.5 000.0 000.0 50.0 219.8 ---.- ---.- 50.0 003 --- --- 217.1 ---.- 40.1 99999 100 30 00000000 00000000 11\r";
		resultStr_1 = remove(resultStr_1);
		//String resultStr_2= "(000.0 ---.- ---.- 000.4 ---.- ---.- 000.0 000.4 001.1 ---.- ---.- 003 00000000\r";
		String resultStr_2= "(000.4 ---.- ---.- 000.6 ---.- ---.- 000.4 000.6 003.0 ---.- ---.- 006 00000000\r";
		resultStr_2 = remove(resultStr_2);
		String resultStr_3= "(NAK\r";
		resultStr_3 = remove(resultStr_3);
		
		String[] results_1 = resultStr_1.split(SPLIT);
		String[] results_2 = resultStr_2.split(SPLIT);


		SantakUPSResult r = new SantakUPSResult();
		System.out.println(" SVUPSZT---" + r.getUPSZT(results_1));
		System.out.println(" SVUPSGD---" + r.getUPSGD(results_1));
		System.out.println(" SVUPSWD---" + r.getUPSWD(results_1)+"C");
		System.out.println(" SVUPSDCDL---" + r.getUPSDCDL(results_1)+"%");
		System.out.println(" SVUPSGDSJ---" + r.getUPSGDSJ(results_1)+"min");
		
		System.out.println(" SVUPSIV--A " + r.getUSPIV(results_1)[0] + "V");
		System.out.println(" SVUPSIV--B " + r.getUSPIV(results_1)[1] + "V");
		System.out.println(" SVUPSIV--C " + r.getUSPIV(results_1)[2] + "V");
		
		System.out.println(" SVUPSOV--A " + r.getUPSOV(results_1)[0] + "V");
		System.out.println(" SVUPSOV--B " + r.getUPSOV(results_1)[1] + "V");
		System.out.println(" SVUPSOV--C " + r.getUPSOV(results_1)[2] + "V");
		
		System.out.println(" SVUPSOA--A " + r.getUPSOA(results_1)[0] + "A");
		System.out.println(" SVUPSOA--B " + r.getUPSOA(results_1)[1] + "A");
		System.out.println(" SVUPSOA--C " + r.getUPSOA(results_1)[2] + "A");
		
		System.out.println(" SVUPSOL--A " + r.getUPSOA(results_2)[0] + "KW");
		System.out.println(" SVUPSOL--B " + r.getUPSOA(results_2)[1] + "KW");
		System.out.println(" SVUPSOL--C " + r.getUPSOA(results_2)[2] + "KW");

	}
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && StringUtils.containsNone(value2, SantakUPSResult.NAK) && value2.length() > 2)
		{
			value2 = StringUtils.remove(value2, "(");
			value2 = StringUtils.remove(value2, "\r");
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			
			String[] result = value2.split(SPLIT);
			SantakUPSResult r = new SantakUPSResult();
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSZT))
					value1 = r.getUPSZT(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSGD))
					value1 = r.getUPSGD(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVDCDL))
				value1 = r.getUPSDCDL(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVGDSJ))
				value1 = r.getUPSGDSJ(result)+"";
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVWD))
			{	value1 = r.getUPSWD(result)+"";										}
			
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSIV))
			{	float[] iv = r.getUSPIV(result); value1 = iv[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSOV))
			{	float[] ov = r.getUPSOV(result); value1 = ov[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSOA))
			{	float[] oa = r.getUPSOA(result); value1 = oa[bit]+"";					  }
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),SantakUPSResult.SVUPSOL))
			{	float[] ol = r.getUPSOL(result); value1 = ol[bit]+"";					  }


		}
		
		return value1;
	}

}
class SantakUPSResult {
	public final static String EMPTY = "EMPTY";
	public final static String NAK = "NAK";
	
	public final static String SVUPSZT = "SVUPSZT";
	public final static String SVUPSGD = "SVUPSGD";
	public final static String SVUPSIV = "SVUPSIV";
	public final static String SVUPSOV = "SVUPSOV";
	public final static String SVUPSOA = "SVUPSOA";
	public final static String SVUPSOL = "SVUPSOL";
	public final static String SVDCDL = "SVDCDL";
	public final static String SVGDSJ = "SVGDSJ";
	public final static String SVWD = "SVWD";
	
	public final static String WITHOUT = "---";
	
	
	

	private int ups_dcdl ;	//��ص���
	private int ups_gdsj ;	//����ʱ��
	
	private String getUPSValueByIdx(String[] results , int idx){
		if (results==null || results.length<=idx)
			return EMPTY ;
		else
			return results[idx];
	}
	
	/* *
	 *  -1: UNKNOW
	 *  0: PowerOn 
	 *  1: StandBy
	 *  2: ByPass
	 *  3: Line
	 *  4: Bat
	 *  5: BatTest
	 *  6: Fault
	 *  7: Converter
	 *  8: He
	 * */
	public int getUPSZT(String[] results){
		int ups_zt ;	 //UPS״̬
		String ztStr = getUPSValueByIdx(results,16);
		if (!StringUtils.equals(ztStr , EMPTY))
		{	
			String data = StringUtils.substring(ztStr, 0, 1);
			ups_zt = Integer.parseInt(data);
		}else
			ups_zt = -1;
		
		return ups_zt;
	}
	/* *
	 *  -1: UNKNOW
	 *  0: Idle 
	 *  1: processing
	 *  2: result:no failure
	 *  3: result:failure | warning
	 *  4: Not possible|inhibite
	 *  5: Test Cancel
	 *  6: Reserved
	 *  7: Other values
	 * */
	public int getUPSGD(String[] results){
		int ups_gd ;  //UPS����
		String gdStr = getUPSValueByIdx(results,16);
		if (!StringUtils.equals(gdStr , EMPTY))
		{		
			String data = StringUtils.substring(gdStr, 1,2);
			ups_gd = Integer.parseInt(data);
		}else
			ups_gd = -1;
		
		return ups_gd;
	}
	
	//-1: UNKNOW
	public int getUPSDCDL(String[] results){
		String dcdlStr = getUPSValueByIdx(results,15);
		if (!StringUtils.equals(dcdlStr , EMPTY))
		{	
			ups_dcdl = Integer.parseInt(dcdlStr);
		} else
			ups_dcdl = -1;
		return ups_dcdl;
	}
	
	//-1: UNKNOW
	public int getUPSGDSJ(String[] results){
		String gdsjStr = getUPSValueByIdx(results,14);
		
		if (!StringUtils.equals(gdsjStr , EMPTY))
		{	ups_gdsj = Integer.parseInt(gdsjStr)/60;
		} else
			ups_gdsj = -1;
		return ups_gdsj;
	}
	
	private String replaceValue(String value, String replace, String change)
	{
		if (StringUtils.startsWith(value, replace))
		{
			value = change;
		}
		return value ;
	}
	
	
	private float getFloatValue(String data_str)
	{	float data = 0;
		try{
			data = Float.parseFloat(data_str);
		}catch(NumberFormatException exc){
			System.err.println("Santak UPS -- Data is Invalid!");
		}
		return data;
	}
	private float[] get3PhaseValue(String[] results , int startIdx)
	{	float[] ups_3p = new float[3];
		for(int i=0;i<3;i++)
		{
			String data  =  	getUPSValueByIdx(results,startIdx+i);
			data = 	replaceValue(data,WITHOUT,"000.0");
			ups_3p[i] = getFloatValue(data);
		}
		return ups_3p;
	}
	public float getUPSWD(String[] results)
	{	
		String data_str = getUPSValueByIdx(results , 13);
		float data = getFloatValue(data_str);
		return data ;
	}
	
	public float[] getUSPIV(String[] results)
	{	
		return get3PhaseValue(results,0);
	}
	public float[] getUPSOV(String[] results)
	{	
		return get3PhaseValue(results,4);
	}
	public float[] getUPSOA(String[] results)
	{	
		return get3PhaseValue(results,8);
	}
	public float[] getUPSOL(String[] results)
	{
		return  get3PhaseValue(results,0);
	}
	
	
	
}
