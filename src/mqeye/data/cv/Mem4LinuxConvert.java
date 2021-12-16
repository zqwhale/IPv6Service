package mqeye.data.cv;

import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.Dsview;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;

import org.apache.commons.lang.StringUtils;

public class Mem4LinuxConvert extends AbstractConvert {

	List<SubPort> sps = new ArrayList<SubPort>(){
		{
			SubPort totalRam = new SubPort("5.0","TotalRAM");
			SubPort freeRam = new SubPort("6.0","FreeRAM");
			SubPort bufferedRam = new SubPort("14.0","BufferedRAM");
			SubPort cachedRam = new SubPort("15.0","CachedRAM");
			this.add(totalRam);
			this.add(freeRam);
			this.add(bufferedRam);
			this.add(cachedRam);
		}
	}	;
	
	
	@Override
	public List<SubPort> getDynamicOID(Dsview dsv) {
		return  sps ; 	
	}

	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
	
		String cvalue = null ;
		if ( StringUtils.isNotEmpty(value2) )
		{
				String[] values = StringUtils.split(value2 , ","); 
				long sum = 0 , max = 0 , cnt = 0;
				for (String v : values)
				{
					 if (StringUtils.isNumeric(v))
					  {
						 long data = Long.parseLong(v);
						 if (max < data ) max = data;
						 sum = sum + data;
						 cnt++;
					  }
				}
				
				sum = sum - max ;
				if ( max > sum ){
						long memsize =	(long) 100*(max - sum)/max;
						cvalue = memsize + "";
				}
		}
		return cvalue;
	}

}
