package mqeye.data.cv;

import java.math.BigDecimal;

import mqeye.data.vo.Dsview;
import mqeye.service.Constant;

import org.apache.commons.lang.StringUtils;

public class ZigbeeTHSensorConvert extends AbstractConvert {
	
	private String data = "fb0d7c5658033356168f173a30040723e2a4";
	
	private void test()
	{
		String sd = data.substring(22, 24);
		String wdz = data.substring(16, 18);
		String wdx = data.substring(18, 20);
		
		int humidity = Integer.parseInt(sd,16);
		double temperature = Integer.parseInt(wdz,16) + Integer.parseInt(wdx,16)/256.0;
		System.out.println(humidity);
		System.out.println(temperature);
	}
	
	 public static void main(String args[]) {
		new ZigbeeTHSensorConvert().test();
	 }
	// Based-ZigBee Temperature & Humidity Sensor Convert
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		String data = value2 ;
		if (data!=null && data.length() > 0)
		{
			
			String sd = data.substring(22, 24);
			String wdz = data.substring(16, 18);
			String wdx = data.substring(18, 20);
			int humidity = Integer.parseInt(sd,16);
			double temperature = Integer.parseInt(wdz,16) + Integer.parseInt(wdx,16)/256.0;
			BigDecimal b = new BigDecimal(temperature);
    		temperature = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    		
    		
    		if (StringUtils.equalsIgnoreCase(Constant.SERVICE_SERSOR_WD, dsv.getSVCode()))
				value1 = temperature +"";
    		if (StringUtils.equalsIgnoreCase(Constant.SERVICE_SERSOR_SD, dsv.getSVCode()))
				value1 = humidity +"";
    		
		}
		
		
		
		return value1;
	}

}
