package mqeye.service.routine;

import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.Device;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.SnmpV3Device;
import mqeye.data.vo.SnmpV3DeviceDAO;
import mqeye.service.MQeyeExecutor;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.SnmpWalk;

import org.apache.commons.lang.StringUtils;

public class SubPortRepairTask implements Runnable{
	private Dsview dsv = null;
	private Device device = null;
	
	public SubPortRepairTask(Device d,Dsview dsv){
		this.device = d ;
		this.dsv = dsv ;
	} 
	private List<SubPort> getSubPortFromDevice(String ip ,  String paramStr ,  String targetOid){
        List<SnmpResult> svs = SnmpWalk.snmpWalk(ip, paramStr, targetOid);
        List<SubPort> sps = new ArrayList<SubPort>();
        for(SnmpResult sv:svs){
        	sps.add(new SubPort(sv,targetOid));
           }
		return sps;
	}
	
	private String getSubPortFromList(List<SubPort> sps ,String spName){
		String subport = "UNKNOW" ;
		if (sps!=null)
			for(SubPort sp : sps){
				if (StringUtils.equalsIgnoreCase(sp.getSubPortName(), spName)){
					subport = sp.getSubPort();break;
				} 
			}
		return subport ;
	}
	
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		DsviewDAO dvdao = new DsviewDAO();
		
		if (device != null && dsv!=null)
		{
			String version = device.getReserve1();
			String paramStr = " -v 2c -c public ";
			if (StringUtils.isNotEmpty(version) && StringUtils.equals( version,"v3"))
			{	String dcode = device.getDCode();
				SnmpV3DeviceDAO dao = new SnmpV3DeviceDAO();
				SnmpV3Device s3d = dao.getSnmpV3DeviceByDCode(dcode);
				paramStr = s3d.toString();
			}else{
				String commity = dsv.getSnmpCommity();
				commity = (StringUtils.isNotEmpty(commity)?commity:"public");
				paramStr =  " -v 2c -c " + commity;
			}
			
			String ip = dsv.getIPAddr();
			
			String targetOid = dvdao.getSubPortOID(dsv.getBSCode(),
										dsv.getSVCode(), dsv.getSubModule());
			
			
			if (StringUtils.isNotBlank(ip) && StringUtils.isNotBlank(targetOid)){		
						List<SubPort> sps = getSubPortFromDevice(ip,paramStr,targetOid);
						String deviSubport = getSubPortFromList(sps,dsv.getSubPortName());
						String indbSubport =dsv.getSubPort();
						if (!StringUtils.equals(deviSubport,"UNKNOW") && !StringUtils.equalsIgnoreCase(deviSubport, indbSubport))
						{  /* Get deviSubport from device, Get indbSubport from database
						 	 * if deviSubport is different indbSubport, you will sync 
						 	 * the subport value in database and refresh process! 
						 	 */
							dvdao.syncSubPort(dsv, deviSubport);
							dvdao.free();
							
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							
							MQeyeExecutor exe = MQeyeExecutor.getIntance();
							exe.refresh(dsv.getDCode());
							
							DebugTool.showConsole("@@@SubPort Repair Task: Device--" + dsv.getDName() + " ' DBSubPort(" + indbSubport + ") has refresh to newSubPort(" + deviSubport + ")");
							
						}
			}
		}
		
		
	}
	
	
	
}
