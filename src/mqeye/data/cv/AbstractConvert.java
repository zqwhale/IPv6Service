package mqeye.data.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.snmp4j.mp.SnmpConstants;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.SnmpV3Device;
import mqeye.data.vo.SnmpV3DeviceDAO;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.SnmpGetList;
import mqeye.service.tools.SnmpV3GetList;
import mqeye.service.tools.SnmpValue;
import mqeye.service.tools.SnmpWalk;

public abstract class AbstractConvert {
	private String split = " ";
	String[] getParams(String paramStr){
		return StringUtils.split(paramStr, split);
	}
	
	private Device getDevice(Dsview dsv){
		String dcode = dsv.getDCode();
		DeviceDAO dao = new DeviceDAO();
		Device device = dao.getValidByPK(dcode);
		return device;
	}
	
	List<SnmpResult> snmpget( Dsview dsv ,List<String> oidList) {
		String dcode = dsv.getDCode();
		Device device =  getDevice(dsv);
		String ipAddr = dsv.getIPAddr();
		int snmpVersion = SnmpConstants.version2c ;
		String version = (StringUtils.isNotBlank(device.getReserve1())? StringUtils.trim(device.getReserve1()):"v2");
	 if		("v1".equals(version)) 	        	  snmpVersion = SnmpConstants.version1 ;   
	 else if("v2".equals(version))				  snmpVersion = SnmpConstants.version2c ;
	 else if ("v3".equals(version))				  snmpVersion = SnmpConstants.version3 ;
	  
	 List<SnmpResult> srs = new ArrayList<SnmpResult>(); 
	 if (snmpVersion==SnmpConstants.version2c)
				{	
		 							String community = (StringUtils.isNotBlank(device.getSnmpCommity())? 
		 										StringUtils.trim(device.getSnmpCommity()):"public");
	        	 		SnmpGetList sgl = new SnmpGetList();
	        	 		sgl.setSnmpVersion(snmpVersion);
	        	 		srs = sgl.snmpv2Get(ipAddr,community,oidList);
				}
	         
	 if (snmpVersion==SnmpConstants.version3)
		{	
					SnmpV3DeviceDAO v3dao = new SnmpV3DeviceDAO();
					SnmpV3Device sdv = v3dao.getSnmpV3DeviceByDCode(dcode);
					if (sdv !=null ){
						Map<String,String> v3param = sdv.toMap();
						srs =  SnmpV3GetList.snmpv3Get(ipAddr,v3param,oidList);
					}else
						DebugTool.printErr("SNMP v3 Without Param record!");
		}
		
		return srs ;
		
	}
	List<SnmpResult> snmpwalk( Dsview dsv ){
		Device device =  getDevice(dsv);
		String ipAddr = dsv.getIPAddr();
		String targetOid =  dsv.getSubPortOID();
		int snmpVersion = SnmpConstants.version2c ;
		// SNMP v3 Modify
		String version = (StringUtils.isNotBlank(device.getReserve1())? StringUtils.trim(device.getReserve1()):"v2");
		if		("v1".equals(version)) 	        	  snmpVersion = SnmpConstants.version1 ;   
         else if("v2".equals(version))				  snmpVersion = SnmpConstants.version2c ;
         else if ("v3".equals(version))				  snmpVersion = SnmpConstants.version3 ;
		
		String paramStr = "";
		if (snmpVersion==SnmpConstants.version2c)
		{	
			String community = (StringUtils.isNotBlank(device.getSnmpCommity())? 
					StringUtils.trim(device.getSnmpCommity()):"public");
			paramStr = " -v 2c -c " + community;
		}
		if (snmpVersion==SnmpConstants.version3)
		{	
			SnmpV3DeviceDAO dao = new SnmpV3DeviceDAO();
			SnmpV3Device sdv = dao.getSnmpV3DeviceByDCode(device.getDCode());
			if (sdv !=null ){
				paramStr = sdv.toString();
			}else
				DebugTool.printErr("SNMP v3 Without Param record!");
		}
		List<SnmpResult> srs = snmpwalk(ipAddr,paramStr,targetOid);
		return srs;
	}
	
	private List<SnmpResult> snmpwalk(String ipAddr , String paramStr , String targetOid){
		/* snmp v3 support*/
		SnmpWalk tester = new SnmpWalk();
		String walkStr = " walk "+ ipAddr + " " + targetOid;
		paramStr = paramStr + walkStr ;
		String[] params = getParams(paramStr);
		List<SnmpResult> srs = tester.snmpWalk(params);
		/* snmp v3 support*/
		return srs;
	}
	
	public List<SubPort> getDynamicOID(Dsview dsv){
		return null;
	}
	
	/* 该方法用于转换采集的数值,当采集数值无法直接使用时，需转换后返回
	 * @Param  	dsv 	监控设备基本信息 
	 * @Param	value1 	采集的value1值
	 * @Param	value2     采集的value2值*/
	public abstract String convert(Dsview dsv , String value1 ,String value2);
	
	
	
}
