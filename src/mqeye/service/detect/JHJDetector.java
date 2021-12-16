package mqeye.service.detect;

import java.util.List;

import mqeye.data.vo.Dsview;
import mqeye.service.Constant;
import mqeye.service.routine.SubPortRepairTask;

import org.apache.commons.lang.StringUtils;

public class JHJDetector extends CommonDetector {
	
	@Override
	protected void snmpRecord(List<SnmpResult> svs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		
		for(Dsview dsv:snmpList){
		  			String oid = convertOID(dsv);
		  			if (StringUtils.equalsIgnoreCase(Constant.SERVICE_NET_INBOUND ,dsv.getSVCode())||
		    				StringUtils.equalsIgnoreCase(Constant.SERVICE_NET_OUTBOUND ,dsv.getSVCode())){		//如果是流量监控则,数据包放入value2中
		  				value1 = "0" ;
		  				value2 = getSnmpResult(oid,svs) ;
		  				if (StringUtils.isEmpty(value2)){
		  							SubPortRepairTask rt = new SubPortRepairTask(device ,dsv);
		  							Thread rThread = new Thread(rt);
		  							rThread.start();
		  				}	
		  			}else {
		    			value1 = getSnmpResult(oid,svs) ;
		    			value2 = "";
		    		}
		  			addMonitorLog(dsv, value1 , value2);
		  			}
		}
}
