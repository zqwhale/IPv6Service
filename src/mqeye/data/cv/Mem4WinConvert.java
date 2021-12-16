package mqeye.data.cv;

import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.Dsview;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;

import org.apache.commons.lang.StringUtils;

public class Mem4WinConvert extends AbstractConvert {

	

	public final static boolean AUTO_ADD = true;
	public final static String MEM_TYPE = "1.3.6.1.2.1.25.2.1.2";
	public final static String MEM_DESC = "Physical Memory";
	public final static String DEFAULT_SUBPORTOID = ".1.3.6.1.2.1.25.2.3.1.2" ;
	
	
	@Override
	public List<SubPort> getDynamicOID(Dsview dsv) {
		
		String targetOid =  dsv.getSubPortOID();
		if (StringUtils.isEmpty(targetOid)){
			targetOid = DEFAULT_SUBPORTOID;
			dsv.setSubPortOID(DEFAULT_SUBPORTOID);
}			
		List<SnmpResult> srs = snmpwalk(dsv);
		List<SubPort> sps = new ArrayList<SubPort>();
		
		for(SnmpResult sr:srs){
			if (StringUtils.equals(sr.getValue(),MEM_TYPE))
				sps.add(new SubPort(sr,targetOid));
		}
		return  sps ; 	
	}

	public final static String MEM_SIZE_DETAIL_OID = ".1.3.6.1.2.1.25.2.3.1.5.?";
	
	
	private int getMemSize( List<SnmpResult> srs )
	{
		int memsize = 0;
		for(SnmpResult sr : srs)
		{
			String ms = sr.getValue();
			if (StringUtils.isNotEmpty(ms) && StringUtils.isNumeric(ms))
									memsize = memsize + Integer.parseInt(ms);
		}
		return memsize;
	}
	
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
		String cvalue = null ;
		if ( StringUtils.isNotEmpty(value2) )
		{	
				List<String> oidList = new ArrayList<String>();
				List<SubPort> splist = getDynamicOID(dsv);
				for( SubPort sp:splist){
											String oid = StringUtils.replace(MEM_SIZE_DETAIL_OID, "?", sp.getSubPort());
											oidList.add(oid);
				}
				List<SnmpResult>	srs =  snmpget(  dsv , oidList) ;
				int	memsize = getMemSize(srs);
				if( memsize > 0 )
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
						// Add all mem use size
						int memuse = sum ;
						int pecent = (memuse * 100 )/ memsize ;
						cvalue = pecent + "";
				}
		}
		return cvalue;
	}


}
