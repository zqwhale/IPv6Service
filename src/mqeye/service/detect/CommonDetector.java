package mqeye.service.detect;

import java.util.List;
import java.util.Map;

import mqeye.data.vo.BrandSpec;
import mqeye.data.vo.BrandSpecDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.SerialDevice;
import mqeye.data.vo.SerialDeviceDAO;
import mqeye.service.Constant;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.ProtocolParser;
import mqeye.service.tools.DebugTool;
import mqeye.service.vmware.VMConnResult;

import org.apache.commons.lang.StringUtils;


/* 设备通用探测类，实现主要方法*/
public class CommonDetector extends AbstractDetector {
	
	
	protected String convertOID(Dsview dsv){		// 转换端口包含了问号的OID
		String oid = dsv.getSnmpOID() ;
		if (StringUtils.isNotBlank(oid)){
			oid = StringUtils.remove(oid, " ");
			if (StringUtils.contains(oid, "?")) {		// 此处需要考虑不是子端口的情况
				String subport = (StringUtils.isNotBlank(dsv.getSubPort())?StringUtils.trim(dsv.getSubPort()):"0");
				oid = StringUtils.replace(oid, "?",subport);
			}
		}
		return oid ;
	}

	protected String getSnmpResult(String oid , List<SnmpResult> svs){
		String value = null ;
		if (svs!=null && svs.size()>0){
			for(SnmpResult sv:svs){
				String svoid = sv.getOid();
				if (StringUtils.startsWith(oid, ".") && !StringUtils.startsWith(svoid, ".")) 
						svoid = "." + svoid;				//如果从数据库中取出的oid 是以点开头，则 svoid 也要加上前缀“.”
				if (StringUtils.equals(oid, svoid))
					{value = sv.getValue();break;}
			}
		}
		return value ;
	} 
	
	protected PortResult getPortResult(String port,List<PortResult> prs){
					PortResult r = null;
					for(PortResult pr : prs){
								if (StringUtils.equalsIgnoreCase(port, pr.getPort())){
										r = pr;break;
								}
					}
					return r;
	}
	
	protected HttpResult getHttpResult(String url,List<HttpResult> hrs){
		HttpResult r = null;
		for(HttpResult hr : hrs){
					if (StringUtils.equalsIgnoreCase(url, hr.getUrl())){
							r = hr;break;
					}
		}
		return r;
}

	protected AbstractSensorResult getSTcpResult(String cmdStr,List<AbstractSensorResult> thsrs){
		//传感器的短地址没有标识的作用
		AbstractSensorResult r = null;
		
		for(AbstractSensorResult thr:thsrs){
				String cmd = thr.getCmd();
				if (StringUtils.equalsIgnoreCase(cmdStr,cmd)){
						r = thr;
						break;
				}
		}
		return r;
	}
	protected AbstractSensorResult getZigBeeSensorResult(String shortAdd,List<AbstractSensorResult> thsrs){
		//传感器的短地址没有标识的作用
		AbstractSensorResult r = null;
		for(AbstractSensorResult thr:thsrs){
				String sa = thr.getShortAddress();
				//if (StringUtils.equalsIgnoreCase(shortAdd,sa)){
						r = thr;
						//System.out.println("##########Old:" + shortAdd + ":"+ r.getShortAddress() + ":" + r.getHumidity() + ":" + r.getTemperature());
						break;
				//}
		}
		return r;
	}
	protected AbstractSensorResult getModbusSensorResult(String shortAdd,String cmdStr,List<AbstractSensorResult> thsrs){
		//传感器的短地址没有标识的作用
		AbstractSensorResult r = null;
		for(AbstractSensorResult thr:thsrs){
				String sa = thr.getShortAddress();
				String cmd = thr.getCmd();
				if (StringUtils.equalsIgnoreCase(shortAdd,sa) && StringUtils.equalsIgnoreCase(cmdStr,cmd)){
						r = thr;
						break;
				}
		}
		return r;
	}
	
	protected AbstractSensorResult getModbusAsciiResult(String cmdStr,List<AbstractSensorResult> thsrs){
		//传感器的短地址没有标识的作用
		AbstractSensorResult r = null;
		for(AbstractSensorResult thr:thsrs){
				String cmd = thr.getCmd();
				if (StringUtils.equalsIgnoreCase(cmdStr,cmd)){
						r = thr;
						break;
				}
		}
		return r;
	}

	protected AbstractSensorResult getSensorResult(String cmdStr,List<AbstractSensorResult> thsrs){
		//传感器的短地址没有标识的作用
		AbstractSensorResult r = null;
		for(AbstractSensorResult thr:thsrs){
				String cmd = thr.getCmd();
				if (StringUtils.isEmpty(cmdStr))
				{
					r = thr;
					break;
				}else	if (StringUtils.isNotEmpty(cmdStr) && StringUtils.equalsIgnoreCase(cmdStr,cmd)){
						r = thr;
						break;
				}
		}
		return r;
	}
	@Override
	protected void snmpRecord(List<SnmpResult> svs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		
		for(Dsview dsv:snmpList){
		  			String oid = convertOID(dsv);
		  			value1 = getSnmpResult(oid,svs);
		  			addMonitorLog(dsv, value1 , value2);
		  			
		  			}
		}
	
	@Override
	protected void pingRecord(PingResult pr) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		for(Dsview dsv:pingList){
						if (pr!=null){
								if (StringUtils.equalsIgnoreCase(Constant.SERVICE_PING_TD ,dsv.getSVCode()))
								{			value1 = pr.getFlag();		value2 = pr.getStatus();														}
								if (StringUtils.equalsIgnoreCase(Constant.SERVICE_PING_DB ,dsv.getSVCode()))
								{			value1 = pr.getLosePacket()+"";  value2 = pr.getLosePacketRate() +"";	}	
								if (StringUtils.equalsIgnoreCase(Constant.SERVICE_PING_SY ,dsv.getSVCode()))
								{				value1 = pr.getTimeConsum()+"";																									}	
						}
						addMonitorLog(dsv, value1 , value2);
					
		}
		
	}

	@Override
	protected void portRecord(List<PortResult> prs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		for(Dsview dsv:portList){
				String port = (StringUtils.isBlank(dsv.getSubPort())?"80":dsv.getSubPort());
				PortResult pr = getPortResult(port,prs);
				if(pr!=null){
						value1 = pr.getStatus();
						value2 = new Long(pr.getTimeConsum()).toString() ;
				}
				addMonitorLog(dsv, value1 , value2);
		}
	}

	@Override
	protected void httpRecord(List<HttpResult> hrs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		for(Dsview dsv:httpList){
				String url = dsv.getUrl();
				if (StringUtils.isBlank(url)){
					String http = "http://";
					String ip = StringUtils.trim(device.getIPAddr());
					String port = dsv.getSubPort();
					port = (StringUtils.isNotBlank(port)?":" + port:"");
					url = http+ip+port ;
				}
				HttpResult hr = getHttpResult(url,hrs);
				if (StringUtils.equalsIgnoreCase(Constant.SERVICE_HTTP_STATUS, dsv.getSVCode()) && hr!=null)
							value1 = hr.getStatus();
				if (StringUtils.equalsIgnoreCase(Constant.SERVICE_HTTP_LOADTIME, dsv.getSVCode()) && hr!=null)
						 value1 = new Long(hr.getTimeConsum()).toString();
				addMonitorLog(dsv, value1 , value2);
		}
	}
	
	@Override
	protected void stcpRecord(List<AbstractSensorResult> thsrs) {
		// TODO Auto-generated method stub
		String value1 = null;
		String value2 = null;
		for(Dsview dsv:stcpList){
			ProtocolParser parser = covertParser(dsv);
			String cmds = dsv.getSnmpOID();
			String cmdStr = parser.convertCmdStr(cmds);
			
			AbstractSensorResult r = getSTcpResult(cmdStr,thsrs);
			if (r!=null )
			{
				value1 = "";
				value2 = parser.convertResult(r.getResult());
			}
			
			addMonitorLog(dsv, value1 , value2);
		}
	}
	@Override
	protected void stcpRecord(Map<String,AbstractSensorResult> asrs){
		String value1 = null ;
		String value2 = null;
		for(Dsview dsv:stcpList){
			ProtocolParser parser = covertParser(dsv);
			String key = dsv.getDCode() + "_" + dsv.getSVCode() + "" + 
			(StringUtils.isEmpty(dsv.getSubPort())?"":dsv.getSubPort());
			AbstractSensorResult r = asrs.get(key);
			if (	r!=null	)
			{
				value1 = "";
				value2 = parser.convertResult(r.getResult());
			}
			addMonitorLog(dsv, value1 , value2);
		}
	}
	@Override
	protected void seriRecord(Map<String,AbstractSensorResult> asrs)
	{
		String value1 = null ;
		String value2 = null;
		for(Dsview dsv:seriList){
			ProtocolParser parser = covertParser(dsv);
			String key = dsv.getDCode() + "_" + dsv.getSVCode() + "" + 
			(StringUtils.isEmpty(dsv.getSubPort())?"":dsv.getSubPort());
			AbstractSensorResult r = asrs.get(key);
			if (	r!=null	)
			{
				value1 = "";
				value2 = parser.convertResult(r.getResult());
			}
			addMonitorLog(dsv, value1 , value2);
		}
	}
	
	protected void seriRecord2(List<AbstractSensorResult> thsrs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		
		String dcode = device.getDCode();
		
		SerialDeviceDAO dao = new SerialDeviceDAO();
		SerialDevice sd = dao.getSerialDeviceByDCode(dcode);
		if (sd==null){ // OLD Version SeriDetect
			DebugTool.printMsg("This Serial Device without SerialDevice Params");
			seriRecord1(thsrs);
		}else
		{
			for(Dsview dsv:seriList){
				ProtocolParser parser = covertParser(dsv);
				String cmds = dsv.getSnmpOID();
				String cmdStr = parser.convertCmdStr(cmds);
				
				AbstractSensorResult r = getSensorResult(cmdStr,thsrs);
				if (r!=null)
				{	value1 = "";
					value2 = parser.convertResult(r.getResult());
				}
				
				addMonitorLog(dsv, value1 , value2);
			}
		}
	}
	
	protected void seriRecord1(List<AbstractSensorResult> thsrs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null;
		
				for(Dsview dsv:seriList){
					String shortAdd = dsv.getDICode();
					String bscode = device.getBSCode().trim();
					BrandSpecDAO bsdao = new BrandSpecDAO(); 
					BrandSpec bs = bsdao.getByBSCode(bscode);
					String type = bs.getCtrlType();
					AbstractSensorResult r = null ;
					int asc_flag = 0;
					if ( StringUtils.equals(type,Constant.ZIGBEE_SERIAL_TYPE)){
							r = getZigBeeSensorResult(shortAdd,thsrs); asc_flag = 0;
					}
					if ( StringUtils.equals(type,Constant.MODBUS_SERIAL_TYPE)){
							String cmdStr = convertCMD(dsv);
							r = getModbusSensorResult(shortAdd,cmdStr ,thsrs); asc_flag = 0;
					}
					if ( StringUtils.equals(type,Constant.MODBUS_ASCII_SERIAL_TYPE)){
						String cmdStr = dsv.getSnmpOID();
						r = getModbusAsciiResult(cmdStr , thsrs);
						asc_flag = 1;
					}
					if (r!=null)
					{
						value1 = "";
						if (asc_flag==1)
							value2 = r.getResultAscii();
						else
							value2 = r.getResultRTU();
							
					}
					
					addMonitorLog(dsv, value1 , value2);
				}
	}


	protected VMConnResult getVMConnResult(String vmCode , List<VMConnResult> vmcrs)
	{
		VMConnResult r = null ;
		for(VMConnResult vmcr:vmcrs){
			if (StringUtils.equals(vmcr.getVmCode(),vmCode))
			{
				r = vmcr ; break;
			}
		}
		return r;
	}
	
	@Override
	protected void vmcRecord(List<VMConnResult> vmcrs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null ;
		String vmCode = device.getDCode();
		for(Dsview dsv:vmcList){
			VMConnResult r= getVMConnResult(vmCode ,vmcrs );
			if (r!=null)
			{
				value1 = r.getState();
			}
			addMonitorLog(dsv, value1 , value2);	
		}
	}

	protected SelfResult getSelfResult(String pk , List<SelfResult> sfrs){
		SelfResult r = null ;
		for(SelfResult sr:sfrs){
			if (StringUtils.equals(sr.getSrPK(),pk))
			{
				r = sr ; break;
			}
		}
		return r ;
	}
	@Override
	protected void selfRecord(List<SelfResult> sfrs) {
		// TODO Auto-generated method stub
		String value1 = null ;
		String value2 = null ;
		for(Dsview dsv:selfList){
			String pk = dsv.getPK();
			SelfResult sr = getSelfResult(pk,sfrs);
			if (sr!=null)
			{
				value1 = sr.getSrValues();
			}
			addMonitorLog(dsv, value1 , value2);	
		}
	}

}
