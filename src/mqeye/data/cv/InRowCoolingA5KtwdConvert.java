package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

/* ÷�����ܿյ� InRow Cooling_A5 �¶Ȳ���ת����*/
public class InRowCoolingA5KtwdConvert extends AbstractConvert {
	
	@Override
	public String convert(Dsview dsv, String value1 ,String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;
		if (StringUtils.isNotEmpty(value1)){
			int tvalue = Integer.parseInt(value1)/10;
			cvalue = String.valueOf(tvalue);
		}
		return cvalue;
	}

}
