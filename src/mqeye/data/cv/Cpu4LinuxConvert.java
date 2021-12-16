package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class Cpu4LinuxConvert extends AbstractConvert {
	
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;  
	   
		if(StringUtils.isNotEmpty(value2)){
			  	float load = Float.parseFloat(value2);
			  	cvalue = (int)(load * 100) + "";
		  }
		return cvalue;
	}

}
