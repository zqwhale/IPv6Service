package mqeye.data.cv;

import mqeye.data.vo.Dsview;
import mqeye.service.serial.SensorReader;

import mqeye.service.tools.CrcHexUtil;

import org.apache.commons.lang.StringUtils;
// Leak Sensor Convert | Smoke Sensor Convert| Power-Support Sensor Convert

class SwitchValueReader extends SensorReader{
	public static int getSV( byte[] result ,  int bit , int isCrc)
	{	int switchValue = -1111 ;
		
		if (isCrc==1)
		{ 
			if (checkPassByCRC(result))
			{
				byte[] values = result ;
				byte vlow = values[3];
				byte vhigh = values[4];
				switchValue = getLine16Status(vlow,vhigh, bit);
			}
		}
		if (isCrc==0)
		{
			byte[] values = result ;
			byte vlow = values[3];
			byte vhigh = values[4];
			switchValue = getLine16Status(vlow,vhigh, bit);
		}
		return switchValue;
	}
}
public class ModbusLSPSensorConvert extends AbstractConvert {
	private static String result1 =  "0101020000b9fc";
	//private static String result2 =  "030101005030"; 
	private static String result2 = "01010203fe388c";
	

	//01010203fff94c

	//01010203fe388c
	public static void main(String[] args){
		byte[] result = CrcHexUtil.HexString2Bytes(result2);
		for(int bit = 1;bit<=16;bit++ )
		{
			int data  = SwitchValueReader.getSV(result, bit , 0);
			System.out.println(bit + "th = " + data);
		}
	}
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		if (value2!=null && value2.length() > 2)
		{
		byte[] result = CrcHexUtil.HexString2Bytes(value2);
			String spec = dsv.getSpecification();
			int isCrc = 0;
			if (StringUtils.contains(spec, "XW-8053")) isCrc = 1;
			if (StringUtils.contains(spec, "R-8053")) isCrc = 0;
			String subPort = dsv.getSubPort();
			int bit = 0;
			if (StringUtils.isNotEmpty(subPort)) bit = Integer.parseInt(subPort);
			int data  = SwitchValueReader.getSV(result, bit,isCrc);
			if (data!=-1111) 	
				value1 = data +"";
			else
				value1 = null;
		}
		return value1;
	}

}
