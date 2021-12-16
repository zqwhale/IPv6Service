package mqeye.data.cv;

import mqeye.data.vo.Dsview;

import org.apache.commons.lang.StringUtils;

public class EmersonRDUAG2Convert extends AbstractConvert{
	private final static String SVACFJZS = "SVACFJZS"; 

	@Override
	public String convert(Dsview dsv, String value1 ,String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;
		if (StringUtils.isNotEmpty(value1))
		{
			cvalue = value1;
			if (!StringUtils.equalsIgnoreCase(dsv.getSVCode(), SVACFJZS))
			{
				float tvalue = Integer.parseInt(value1)/100.00f;
				cvalue = String.valueOf(tvalue);
			}
		}
		return cvalue;
	}
}
