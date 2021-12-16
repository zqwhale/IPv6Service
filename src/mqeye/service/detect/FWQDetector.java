package mqeye.service.detect;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mqeye.data.cv.AbstractConvert;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.SnmpV3Device;
import mqeye.data.vo.SnmpV3DeviceDAO;
import mqeye.service.Constant;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.SnmpGetList;
import mqeye.service.tools.SnmpV3GetList;

import org.apache.commons.lang.StringUtils;
import org.snmp4j.mp.SnmpConstants;

public class FWQDetector extends CommonDetector {
	
	private String convertOID(Dsview dsv, String subport){		
		String oid = dsv.getSnmpOID() ;
		if (StringUtils.isNotBlank(oid)){
			oid = StringUtils.remove(oid, " ");
			if (StringUtils.contains(oid, "?")) {		
				oid = StringUtils.replace(oid, "?",subport);
			}
		}
		return oid ;
	}
	private List<SubPort> getSubPort(Dsview dsv)
	{
		String cvName = dsv.getValueConvert();
		AbstractConvert convert = null ;
		List<SubPort> list = null;
		if (StringUtils.isNotEmpty(cvName)){
			try {
				convert = (AbstractConvert) Class.forName(cvName).newInstance();
				list = convert.getDynamicOID(dsv);
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			return list;
	}
	
	private List<String> convertOIDS(Dsview dsv){
		//Convert class got a method  
		List<String> oids = new ArrayList<String>();
		List<SubPort> list = getSubPort(dsv);
		if (list!=null)
		{
				for(SubPort sp:list){
					String subport = (StringUtils.isNotBlank(sp.getSubPort())?StringUtils.trim(sp.getSubPort()):"0");
					String oid = convertOID(dsv,subport);
					oids.add(oid);
				}
		}
	
		return oids;
	}
	
	private void refreshSubPort( Dsview dsv ){
		List<SubPort> list = getSubPort(dsv);
		if (list!=null)
		{
				for(SubPort sp:list){
								String newSubPortName = sp.getSubPortName();
								String newSubPort = sp.getSubPort();
								if (StringUtils.equals( newSubPortName, dsv.getSubPortName()) && 
										!StringUtils.equals( newSubPort, dsv.getSubPort())) 
								{
												DsviewDAO dao = new DsviewDAO();
												dao.syncSubPort(dsv, newSubPort);
												dsv.setSubPort(newSubPort);

								}
								
				}
		}	
	}
	@Override
	protected  void snmpDetect( ){
		String ip = StringUtils.trim(device.getIPAddr());
		List<String> oidList = new ArrayList<String>();
		int snmpVersion = SnmpConstants.version2c ;
		
		// SNMP v3 Modify
		String version = (StringUtils.isNotBlank(device.getReserve1())? StringUtils.trim(device.getReserve1()):"v2");
		if		("v1".equals(version)) 	        	  snmpVersion = SnmpConstants.version1 ;   
         else if("v2".equals(version))				  snmpVersion = SnmpConstants.version2c ;
         else if ("v3".equals(version))				  snmpVersion = SnmpConstants.version3 ;
		
		for(Dsview dsv:snmpList){
			if ((StringUtils.equals( dsv.getSVCode(), Constant.FWQ_CPU) || StringUtils.equals( dsv.getSVCode(), Constant.FWQ_MEM)	)
					&& StringUtils.equals( dsv.getBSCode(),"BFW001")) //Windows OS
			{
					List<String> oids = convertOIDS(dsv);
					oidList.addAll(oids);
			}else if(StringUtils.equals( dsv.getSVCode(), Constant.FWQ_HD)){
					refreshSubPort(dsv );		// Refresh HD information , Windows OS
					String oid = convertOID(dsv);
					oidList.add(oid);
			}else if(StringUtils.equals( dsv.getSVCode(), Constant.FWQ_MEM)
				&& StringUtils.equals( dsv.getBSCode(),"BFW002")) //Linux Mem  
			{
				List<String> oids = convertOIDS(dsv);
				oidList.addAll(oids);
			}
			else{
					String oid = convertOID(dsv);
					oidList.add(oid);
			}
		}
		
		List<SnmpResult> svs = new ArrayList<SnmpResult>(); 
		if (oidList.size()>0){
			if (snmpVersion==SnmpConstants.version2c)
			{	
				String community = (StringUtils.isNotBlank(device.getSnmpCommity())? 
									StringUtils.trim(device.getSnmpCommity()):"public");
				
			    
						SnmpGetList sgl = new SnmpGetList();
						sgl.setSnmpVersion(snmpVersion);
						svs = sgl.snmpv2Get(ip,community,oidList);
				
			}
			
			if (snmpVersion==SnmpConstants.version3)
			{	
				SnmpV3DeviceDAO dao = new SnmpV3DeviceDAO();
				SnmpV3Device sdv = dao.getSnmpV3DeviceByDCode(device.getDCode());
				if (sdv !=null ){
					Map<String,String> v3param = sdv.toMap();
					svs =  SnmpV3GetList.snmpv3Get(ip,v3param,oidList);
				}else
					DebugTool.printErr("SNMP v3 Without Param record!");
			}
		}
		
	    
	   
	  if ( svs.size()>=0)
	  {		DebugTool.printMsg("Read:"  + svs.size() + " SNMP  Record!");
		  		snmpRecord(svs);
	  	} else{
				snmpRecord(svs);
	  	}
	}
	
	
	protected String getAllSnmpResult(String oid , List<SnmpResult> svs , List<SubPort> sps){
		String value = null ;
		if (svs!=null && svs.size()>0){
			oid = StringUtils.remove(oid, "?");
			value = "";
			for(SnmpResult sv:svs){
				String svoid = sv.getOid();
				if (StringUtils.startsWith(oid, ".") && !StringUtils.startsWith(svoid, ".")) 
						svoid = "." + svoid;				
				boolean find = false;
				for(SubPort sp:sps)
				{
					if (StringUtils.endsWith(svoid, sp.getSubPort()))
					{ find = true; break;}
				}
				if (StringUtils.startsWith(svoid, oid) && find)
				{
					value = value + sv.getValue() + ","; 
				}
			}
			if (StringUtils.isEmpty(value)) value = null;
			if (StringUtils.endsWith(value, ",")) value = StringUtils.removeEnd(value, ",");
		}
		return value ;
	} 
	
	@Override
	protected void snmpRecord(List<SnmpResult> svs) {
		
		// TODO Auto-generated method stub
		String value1 = null ; 
		String value2 = null;

		for(Dsview dsv:snmpList){
			if ((StringUtils.equals( dsv.getSVCode(), Constant.FWQ_CPU) || StringUtils.equals( dsv.getSVCode(), Constant.FWQ_MEM)	)
					&& StringUtils.equals( dsv.getBSCode(),"BFW001")) //Windows OS
			{
  			String oid = dsv.getSnmpOID();
  			List<SubPort> sps = getSubPort(dsv);
  			value1 = "" ;
				value2 = getAllSnmpResult(oid,svs,sps) ;
			}else if(StringUtils.equals( dsv.getSVCode(), Constant.FWQ_MEM)
					&& StringUtils.equals( dsv.getBSCode(),"BFW002")) //Linux Mem
			{
					String oid = dsv.getSnmpOID();
	  			List<SubPort> sps = getSubPort(dsv);
	  			value1 = "" ;
					value2 = getAllSnmpResult(oid,svs,sps) ;
			}
				else{
				String oid = convertOID(dsv);
				value1 = "";
				value2 = getSnmpResult(oid,svs);
			}
  			addMonitorLog(dsv, value1 , value2);
		}
	}

}
