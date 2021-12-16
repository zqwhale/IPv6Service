package mqeye.data.cv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.lang.StringUtils;
import org.snmp4j.mp.SnmpConstants;

public class HDisk4WinConvert extends AbstractConvert {
	
	public final static String HD_TYPE = "1.3.6.1.2.1.25.2.1.4";
	public final static String hrStorageDescrOid = ".1.3.6.1.2.1.25.2.3.1.3";
	public final static String DEFAULT_SUBPORTOID = ".1.3.6.1.2.1.25.3.2.1.2";
	
	private String getConvertValue(List<SnmpResult> svsdesc , String oid){
		String cvalue = null ;
		for(SnmpResult sv : svsdesc)
		{
			String svoid = (StringUtils.startsWith(sv.getOid(),".")?sv.getOid():"."+sv.getOid());
			if (StringUtils.equals(svoid , oid))
			{	cvalue =sv.getValue();
				if (StringUtils.contains(cvalue, "3a:5c"))
				{
					String subv = StringUtils.left(cvalue, 2);
					int diskVolumn = Integer.valueOf(subv, 16);
					cvalue = (char)diskVolumn+":"; 
				}else
					cvalue = StringUtils.left(cvalue, 2);
				
				break;
			}
		}
		return cvalue;
	}
	private String getSubValue(String svoid , String toid){
		String oid = (StringUtils.startsWith(svoid,".")?svoid:"."+svoid);
		String subv = StringUtils.remove(oid, toid+".");
		return subv;
	}
	
	
	@Override
	public List<SubPort> getDynamicOID( Dsview dsv ) {
		// TODO Auto-generated method stub
		String targetOid =  dsv.getSubPortOID();
		if (StringUtils.isEmpty(targetOid)){
					targetOid = DEFAULT_SUBPORTOID;
					dsv.setSubPortOID(DEFAULT_SUBPORTOID);
		}	
		List<SnmpResult> svs = snmpwalk(dsv);
		dsv.setSubPortOID(hrStorageDescrOid);
		List<SnmpResult> svsdesc = snmpwalk(dsv);
		dsv.setSubPortOID(targetOid);
		
		List<SubPort> sps = new ArrayList<SubPort>();
		for(SnmpResult sv:svs){
			if (StringUtils.equals(sv.getValue(),HD_TYPE))
			{	String svalue = getSubValue(sv.getOid(),targetOid);
				String cvalue = getConvertValue(svsdesc , hrStorageDescrOid + "." + svalue );
				sv.setValue(cvalue);
				sps.add(new SubPort(sv,targetOid));
			}
		}
		return  sps ; 
	}
	
	
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
		// TODO Auto-generated method stub
			String cvalue = null ;
			
			if (StringUtils.isNotEmpty(value2) && StringUtils.isNumeric(value2) )
				{	
					String dcode = dsv.getDCode();
					int snmpVersion = SnmpConstants.version2c ;
					DeviceDAO dao = new DeviceDAO();
					Device device = dao.getValidByPK(dcode);

					String community = "public";
					String ip = dsv.getIPAddr();
					String subport = dsv.getSubPort();
	  				List<String> oidList = new ArrayList<String>();
	  				String oidforStorageSize = ".1.3.6.1.2.1.25.2.3.1.5." + subport;
	  				String oidforStorageUnit = ".1.3.6.1.2.1.25.2.3.1.4."+subport;
	  				oidList.add(oidforStorageSize);
	  				oidList.add(oidforStorageUnit);
	  				community = (StringUtils.isNotBlank(dsv.getSnmpCommity())? StringUtils.trim(dsv.getSnmpCommity()):"public");
	  				String version = (StringUtils.isNotBlank(device.getReserve1())? StringUtils.trim(device.getReserve1()):"v2");
	  				if		("v1".equals(version)) 	        	  snmpVersion = SnmpConstants.version1 ;   
			         else if("v2".equals(version))				  snmpVersion = SnmpConstants.version2c ;
			         else if ("v3".equals(version))				  snmpVersion = SnmpConstants.version3 ;
			         
			     		List<SnmpResult> srs = new ArrayList<SnmpResult>(); 
			         if (snmpVersion==SnmpConstants.version2c)
						{	
			        	 	
			        	 		SnmpGetList sgl = new SnmpGetList();
			        	 		sgl.setSnmpVersion(snmpVersion);
			        	 		srs = sgl.snmpv2Get(ip,community,oidList);
						}
			         
			         if (snmpVersion==SnmpConstants.version3)
						{	
							SnmpV3DeviceDAO v3dao = new SnmpV3DeviceDAO();
							SnmpV3Device sdv = v3dao.getSnmpV3DeviceByDCode(dcode);
							if (sdv !=null ){
								Map<String,String> v3param = sdv.toMap();
								srs =  SnmpV3GetList.snmpv3Get(ip,v3param,oidList);
							}else
								DebugTool.printErr("SNMP v3 Without Param record!");
						}
			       long storageUsed = Long.parseLong(value2);
			       long storageSize = Long.parseLong(getSnmpResult(oidforStorageSize,srs));
			       String storageUnit = getSnmpResult(oidforStorageUnit,srs);
			       long pecent = (storageUsed * 100 )/ storageSize ;
			       cvalue = pecent+"";
				}

		return cvalue;
	}
	
	private String getSnmpResult(String oid , List<SnmpResult> svs){
		String value = null ;
		if (svs!=null && svs.size()>0){
			for(SnmpResult sv:svs){
				String svoid = sv.getOid();
				if (StringUtils.startsWith(oid, ".") && !StringUtils.startsWith(svoid, ".")) 
						svoid = "." + svoid;				
					{value = sv.getValue();break;}
			}
		}
		return value ;
	} 

}
