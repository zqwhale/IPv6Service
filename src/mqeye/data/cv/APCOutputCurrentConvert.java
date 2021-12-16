package mqeye.data.cv;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Dsview;

public class APCOutputCurrentConvert extends AbstractConvert {

	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;
		if (StringUtils.isNotEmpty(value1)){
			double tvalue = Double.parseDouble(value1)/10.0;
			cvalue = String.valueOf(tvalue);
		}
		return cvalue;
	}

}
