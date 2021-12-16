package mqeye.data.cv;

import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.Dsview;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;

import org.apache.commons.lang.StringUtils;

public class Cpu4WinConvert extends AbstractConvert {
	
	public final static String CPU_TYPE = "1.3.6.1.2.1.25.3.1.3";
	public final static String CPU_DESC = "Unknown Processor Type";
	public final static String DEFAULT_SUBPORTOID = ".1.3.6.1.2.1.25.3.2.1.2";
	

	@Override
	public List<SubPort> getDynamicOID(Dsview dsv){
	
		String targetOid =  dsv.getSubPortOID();
		if (StringUtils.isEmpty(targetOid)){
					targetOid = DEFAULT_SUBPORTOID;
					dsv.setSubPortOID(DEFAULT_SUBPORTOID);
		}			
		List<SnmpResult> srs = snmpwalk(dsv);
		
		List<SubPort> sps = new ArrayList<SubPort>();
		for(SnmpResult sr:srs){
			if (StringUtils.equals(sr.getValue(),CPU_TYPE))
				sps.add(new SubPort(sr,targetOid));
		}
		return  sps ; 	
		
	}
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;
		if (value2 !=null && StringUtils.isNotEmpty(value2))
		{
				String[] values = StringUtils.split(value2 , ",");
				
				int sum = 0 , cnt = 0;
				for (String v : values)
				{
					 if (StringUtils.isNumeric(v))
					  {
						 int data = Integer.parseInt(v);
						 sum = sum + data;
						 cnt++;
					  }
				}
				if (cnt > 0) sum = sum / cnt;
				cvalue = sum +"" ;
		}
		
		return cvalue;
	}

}
