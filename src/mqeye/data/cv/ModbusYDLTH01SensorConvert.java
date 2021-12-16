package mqeye.data.cv;

import mqeye.data.vo.Dsview;
import mqeye.service.Constant;
import mqeye.service.serial.SensorReader;
import mqeye.service.tools.CrcHexUtil;

import org.apache.commons.lang.StringUtils;




class ModbusYDLTH01SensorReader extends SensorReader{
	public static TemperatureHumidityResult getTH( byte[] result )
	{
		return getTH("unknow" ,result);
	}
	
	public static TemperatureHumidityResult getTH(String shortAddr , byte[] result)
	{
		TemperatureHumidityResult thr = null;
		
		if (checkPassByCRC(result))
		{ 
			byte[] values = result ;
			byte[] temperature_b = new byte[]{values[3],values[4]};
			byte[] humidity_b = new byte[]{values[5],values[6]};
			
			double temperature = ((short)CrcHexUtil.Bytes2Integer(temperature_b))/10.0 ;
			double humidity = ((short)CrcHexUtil.Bytes2Integer(humidity_b))/10.0 ;
			System.out.println("YDLTH01---" + temperature + ":" + humidity);
			thr = new TemperatureHumidityResult();
			thr.setTemperature(temperature);
			thr.setHumidity(humidity);
		}
		return thr;
	}
}

// Based-ModBus Temperature & Humidity Sensor Convert
public class ModbusYDLTH01SensorConvert extends AbstractConvert {
	
	
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		
		if ( (value2!=null)	&&	(value2.length()>=2)) 
		{		byte[] result = CrcHexUtil.HexString2Bytes(value2);
			
				TemperatureHumidityResult thr = ModbusYDLTH01SensorReader.getTH(result);
				if (StringUtils.equals(dsv.getSVCode(),Constant.SERVICE_SERSOR_WD)) value1 = thr.getTemperature() + "";
				if (StringUtils.equals(dsv.getSVCode(),Constant.SERVICE_SERSOR_SD)) value1 = thr.getHumidity() + "";
		}
		return value1;
	}

}
