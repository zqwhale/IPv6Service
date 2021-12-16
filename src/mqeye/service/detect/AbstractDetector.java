package mqeye.service.detect;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import mqeye.data.cv.AbstractConvert;
import mqeye.data.vo.Device;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.SelfDAO;
import mqeye.data.vo.SerialDevice;
import mqeye.data.vo.SerialDeviceDAO;
import mqeye.data.vo.SnmpV3Device;
import mqeye.data.vo.SnmpV3DeviceDAO;
import mqeye.data.vo.Warmlog;
import mqeye.service.Constant;
import mqeye.service.routine.SerialPortGuardTask;
import mqeye.service.routine.SerialServerGuardTask;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.CmdBean;
import mqeye.service.serial.ProtocolCode;
import mqeye.service.serial.ProtocolParser;
import mqeye.service.serial.SerialPort;
import mqeye.service.serial.SerialPortDetect;
import mqeye.service.serial.SerialPortReader;
import mqeye.service.serial.SerialPortTool;
import mqeye.service.serial.SerialUsbCfgTool;
import mqeye.service.serial.ShareObject;
import mqeye.service.serial.TCPSocketTool;
import mqeye.service.serial.TaskLock;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.CrcHexUtil;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.HttpConnect;
import mqeye.service.tools.HttpsConnect;
import mqeye.service.tools.PingTool;
import mqeye.service.tools.PortConnect;
import mqeye.service.tools.SnmpGetList;
import mqeye.service.tools.SnmpV3GetList;
import mqeye.service.vmware.VCenterCfg;
import mqeye.service.vmware.VMConnResult;
import mqeye.service.vmware.VMWareAlarm;
import mqeye.service.vmware.VMWareConnection;
import mqeye.service.vmware.VMWareDAO;
import mqeye.service.vmware.VMWareEntityType;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;
import org.snmp4j.mp.SnmpConstants;




public abstract class AbstractDetector {
		private List<Monitorlog> mls = new ArrayList<Monitorlog>();		//监控数据存放列表
		private List<Warmlog> wls = new ArrayList<Warmlog>();		//监控告警存放列表
		private Date detectTime ;
		protected Device device = null ;
		protected List<Dsview> dslist = null ;
		protected List<Dsview> snmpList = new ArrayList<Dsview>();
		protected List<Dsview> pingList = new ArrayList<Dsview>();
		protected List<Dsview> httpList = new ArrayList<Dsview>();
		protected List<Dsview> portList = new ArrayList<Dsview>();
		protected List<Dsview> seriList = new ArrayList<Dsview>();
		protected List<Dsview> stcpList = new ArrayList<Dsview>();
		protected List<Dsview> vmcList 	= new ArrayList<Dsview>();
		protected List<Dsview> selfList = new ArrayList<Dsview>();
		
		public void init(Device device,List<Dsview> dslist){
					this.device = device ;
					this.dslist = dslist ;
		}
		
		//For SNMP Detect
		private String convertOID(Dsview dsv){		//SNMP  转换端口包含了问号的OID
			String oid = dsv.getSnmpOID() ;
			if (StringUtils.isNotBlank(oid)){
				oid = StringUtils.remove(oid, " ");
				if (StringUtils.contains(oid, "?")) {		//SNMP 此处需要考虑不是子端口的情况
					String subport = (StringUtils.isNotBlank(dsv.getSubPort())?StringUtils.trim(dsv.getSubPort()):"0");
					oid = StringUtils.replace(oid, "?",subport);
				}
			}
			return oid ;
		}
		
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
						String oid = convertOID(dsv);
						oidList.add(oid);
			}
			
			List<SnmpResult> svs = new ArrayList<SnmpResult>(); 
			if (oidList.size()>0){
				if (snmpVersion==SnmpConstants.version2c)
				{	
					String community = (StringUtils.isNotBlank(device.getSnmpCommity())? 
										StringUtils.trim(device.getSnmpCommity()):"public");
					/* 根据 OID值 ，获取设备监控值*/
				    
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
			
		    
		    /*	如果SNMP监控有数据 	*/
		  if ( svs.size()>=0)
		  {		DebugTool.printMsg("Read:"  + svs.size() + " SNMP  Record!");
			  		snmpRecord(svs);
		  	} else{
  					snmpRecord(svs);
		  	}
		}
		
		//For SNMP Detect
		
		protected  void pingDetect(){
			String ip = StringUtils.trim(device.getIPAddr());
			PingResult pr = new PingTool().ping(ip,10,10) ;
			if (pr!=null){
								DebugTool.printMsg("Ping detect:"  +ip + "10 times!");
								pingRecord(pr);
			}else{
								pingRecord(new PingResult());
			}
			
		}
		
		protected  void httpDetect(){
			String ip = StringUtils.trim(device.getIPAddr());
			List<String>	urls = new ArrayList<String>();
			List<HttpResult>	hrs = new ArrayList<HttpResult>(); 
			for(Dsview dsv:dslist){
				String url = dsv.getUrl();
				if (StringUtils.isBlank(url)){
					String http = "http://";
					String port = dsv.getSubPort();
					port = (StringUtils.isNotBlank(port)?":" + port:"");
					url = http+ip+port ;
				}
				if (!urls.contains(url)){
					urls.add(url);
					HttpResult hr = null;
					if (StringUtils.startsWith(url.toLowerCase(), "https"))
					{
						HttpsConnect hConn = new HttpsConnect();
						hr =  hConn.connect(url);
					}
					else
					{
						HttpConnect hConn = new HttpConnect();
						hr =  hConn.connect(url);
					}
					hrs.add(hr);
				}
			}
			if (hrs.size()>0){
				DebugTool.printMsg("Detect:"  +hrs.size() + " HTTP Link!");
				httpRecord(hrs);
			}else{
				httpRecord(hrs);
			}			
		}
		
		protected	 void portDetect(){
			String ip = StringUtils.trim(device.getIPAddr());
			List<PortResult> prs = new ArrayList<PortResult>();
			for(Dsview dsv:portList){
										String port = dsv.getSubPort();
										port = (StringUtils.isNotBlank(port)?port:"80");
										PortConnect pConn = new PortConnect();
										PortResult pr = pConn.connect(ip, port);
										if (pr!=null) 			prs.add(pr);
			}
			if (prs.size()>0){
						DebugTool.printMsg("Detect :"  +prs.size() + " PORT！");
						portRecord(prs);
			}else{
						portRecord(prs);
			}
			
		}
		
		protected abstract void snmpRecord(List<SnmpResult> svs );
		protected abstract void pingRecord(PingResult pr);
		protected abstract void portRecord(List<PortResult> prs);
		protected abstract void httpRecord(List<HttpResult> hrs);
		//protected abstract void seriRecord(List<AbstractSensorResult> thsrs);
		protected abstract void seriRecord(Map<String,AbstractSensorResult> asrs);
		protected abstract void stcpRecord(List<AbstractSensorResult> thsrs); //useless
		protected abstract void stcpRecord(Map<String,AbstractSensorResult> asrs);
		protected abstract void vmcRecord(List<VMConnResult> vmcrs);
		protected abstract void selfRecord(List<SelfResult> sfrs);
		
		protected String convertCMD(Dsview dsv)
		{
			String dicode = dsv.getDICode();	// Short Address 
			if (StringUtils.isEmpty(dicode)) dicode = "01";
			String cmdstr = dsv.getSnmpOID();
			if (StringUtils.isNotBlank(cmdstr))
			{
				cmdstr = cmdstr.trim().toUpperCase();
				if (StringUtils.contains(cmdstr, "?"))
					cmdstr = StringUtils.replace( cmdstr, "?" , dicode);
				if (StringUtils.contains(cmdstr, "#CRC#"))
				{
					String dataStr = StringUtils.remove(cmdstr, "#CRC#");
					byte[] databyt = HexUtils.convert(dataStr);
					String crc =  CrcHexUtil.CRC16(databyt);
					cmdstr = StringUtils.replace(cmdstr, "#CRC#", crc); 
				}
					
			}
			return cmdstr ;
			
		}
		
		
		/* STCP Socket detect datas  new*/
		private void stcpDetect(){
			SerialServerGuardTask task = SerialServerGuardTask.getInstance();
			if (task.getRunFlag()==1){
				Map<String,AbstractSensorResult> asrs = task.getResult();
				stcpRecord(asrs);
			}
		}
		/* STCP Socket detect datas  old*/
		private void stcpDetect2(){
			List<AbstractSensorResult> thsrs = new ArrayList<AbstractSensorResult>();
			String ip = StringUtils.trim(device.getIPAddr());
			String tcpport = StringUtils.trim(device.getSnmpCommity());
			List<CmdBean> cmdList = new ArrayList<CmdBean>();
			String pattern = "RTU";
			for(Dsview dsv:stcpList)
			{
				ProtocolParser parser = covertParser(dsv);
				String cmds = dsv.getSnmpOID();
				int validlen = dsv.getDelay();
				String cmdStr = parser.convertCmdStr(cmds);
				byte[] hexcmd =   parser.convertCmd(cmds);
				boolean find = false;
				for(CmdBean cmdTmp:cmdList)
					if (StringUtils.equalsIgnoreCase(cmdTmp.getCmdStr(), cmdStr))
						{find = true ;break;}
				if (!find) {CmdBean bean = new CmdBean(cmdStr,hexcmd,validlen); cmdList.add(bean);
				}
				pattern = parser.getPattern();
			}
			
			if (pattern.equals(ProtocolCode.RTU))
			{
				int port = Integer.parseInt(tcpport);
				SerialPort sp = new SerialPort(ip+":"+port,ip,port);
				SerialPortDetect spd = new SerialPortDetect(sp);
				String shortAddr = StringUtils.trim(device.getDICode());
				if(spd.getSocket()!=null && spd.isOpen( ))
				{	
					for( CmdBean cmdTmp:cmdList)
					{
							String cmdStr = cmdTmp.getCmdStr();
							try {
								Thread.sleep(cmdTmp.t_detectspan1);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							byte[] result = spd.sendCmdTmp(spd.getSocket(), cmdTmp);
							
							try {
								Thread.sleep(cmdTmp.t_detectspan2);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							if (result!=null)
							{
								AbstractSensorResult asr = new AbstractSensorResult();
								asr.setShortAddress(shortAddr);
								asr.setCmd(cmdStr);
								asr.setResult(result);
								thsrs.add(asr);
							}
							try {
									Thread.sleep(1000);
							} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							}
						}
				}else{
					spd.initSocket(sp);
				}
			}
			
			if (pattern.equals(ProtocolCode.ASCII))
			{
				int port = Integer.parseInt(tcpport);
				String key = ip + ":" + port;
				ShareObject share = ShareObject.getInstance();
				TaskLock lock = share.getLock(key);
				TCPSocketTool t = new TCPSocketTool(lock);
				int cnt=0;
				while(!t.isOpen(ip, port) && cnt <= 10){
					try { 
						DebugTool.showConsole("Waiting for " + ip + ":" + port);
						Thread.sleep(100); 
					} catch (InterruptedException e) {
								e.printStackTrace();
					}
					cnt++;
				}
				if (t.isOpen(ip, port))
				{
					String shortAddr = StringUtils.trim(device.getDICode());
					for( CmdBean cmdTmp:cmdList)
					{
						byte[] cmd = cmdTmp.getHexcmd();
						byte[] result = null;
						String cmdStr = cmdTmp.getCmdStr();
						int len = cmdTmp.getValidLen();
						cnt=0;
						do{	
								try { Thread.sleep(100); 
								} catch (InterruptedException e) {
											e.printStackTrace();
								}
							cnt++;
						}while(lock.islock()&& cnt<100);
						
						
						do{
							result = t.session(ip, port, cmdTmp);
							if (result !=null && result.length > cmdTmp.getValidLen() )
							{
								System.out.println("------------------" + new String(result));
								break;
							}
							try {
								Thread.sleep(50);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							}
							cnt++;
						}while(cnt<10);
						

						if (result!=null && result.length > cmdTmp.getValidLen())
						{
							AbstractSensorResult asr = new AbstractSensorResult();
							asr.setShortAddress(shortAddr);
							asr.setCmd(cmdStr);
							asr.setResult(result);
							thsrs.add(asr);
						}
						try {
								Thread.sleep(1000);
						} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
					}
					
				}
			}
			stcpRecord(thsrs);
		}
		
		
		protected ProtocolParser covertParser( Dsview dsv )
		{
			String params = dsv.getSnmpParam();
			String shortAddr = dsv.getDICode();
			ProtocolParser parser = new ProtocolParser();
			if (StringUtils.isEmpty(params))
				params = ProtocolCode.DEFAULT_PARAM;
			parser.initParser(params);
			if (StringUtils.isNotEmpty(shortAddr))
				parser.initParser(ProtocolCode.PARAMS_SHORTADDR, shortAddr);
			return parser;
		}
		private void seriDetect(){
			SerialPortGuardTask task = SerialPortGuardTask.getInstance();
			if (task.getRunFlag()==1){
				Map<String,AbstractSensorResult> asrs = task.getResult();
				seriRecord(asrs);
			}
		}
		
		/* 方法未考虑多个传感器的情况 */
//		private void seriDetect(){
//			String dcode = device.getDCode();
//			SerialDeviceDAO dao = new SerialDeviceDAO();
//			SerialDevice sd = dao.getSerialDeviceByDCode(dcode);
//			if (sd==null){ // OLD Version SeriDetect
//				DebugTool.printMsg("This Serial Device without SerialDevice Params");
//			}else{
//				String sysPort = dao.getCurrSysPort(dcode);
//				List<AbstractSensorResult> thsrs = new ArrayList<AbstractSensorResult>();
//				if (sysPort!=null && !StringUtils.equals(sysPort,SerialUsbCfgTool.UNKNOW_PORT))
//				{
//					/*------------------------------------------------*/
//					ShareObject share = ShareObject.getInstance();
//					SerialPortReader sr = share.getReader(sysPort);
//					int cnt = 0;
//					while(sr.isOpen() && cnt<=10)
//					{	try { 
//							DebugTool.showConsole("Waiting for the '" + sysPort + "' port closed....");
//							Thread.sleep(100); 
//						} catch (InterruptedException e) {
//									e.printStackTrace();
//						}
//						cnt++;
//					}
//	
//					List<String> cmdList = new ArrayList<String>();
//					for(Dsview dsv:seriList)
//					{
//						Map<String,String> params = sd.toParamMap(); 
//						SerialPortTool tool = new SerialPortTool(sr);
//						tool.initParams(params);
//						
//						ProtocolParser parser = covertParser(dsv);
//						String cmds = dsv.getSnmpOID();
//						String cmdStr = parser.convertCmdStr(cmds);
//						byte[] hexcmd =   parser.convertCmd(cmds);
//					
//						byte[] result = null;
//						boolean find = false;
//						for(String cmdTmp:cmdList)
//								if (StringUtils.equalsIgnoreCase(cmdTmp, cmdStr))
//									{find = true ;break;}
//						if (!find)
//						{	
//							cmdList.add(cmdStr);
//							if (StringUtils.isEmpty(cmdStr))
//								result = tool.read(null);
//							else
//								result = tool.read(hexcmd);
//							
//							AbstractSensorResult r = new AbstractSensorResult();
//							r.setShortAddress(dsv.getDICode());
//							r.setCmd(cmdStr);
//							r.setResult(result);
//							
//							thsrs.add(r);
//							
//							try {
//								Thread.sleep(1000);
//							} catch (InterruptedException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}
//				}
//				seriRecord(thsrs);
//			}
//		}
		
			
		private void  vmcDetect(){
			List<VMConnResult> vmcrs = new ArrayList<VMConnResult>();
		
			String vmCode = device.getDCode();
			VMConnResult vmcr = new VMConnResult();
			vmcr.setVmCode(vmCode);
			vmcr.setState(VMConnResult.EXCEPT);
			
			VMWareDAO dao = new VMWareDAO();
			VCenterCfg cfg = dao.getVCenterCfg(vmCode);
			int err = 0;
			if (cfg!=null)
			{
				List<VMWareAlarm> list = null;
				String vmcode = cfg.getVmCode( ) ;
				try {
					VMWareConnection conn = new VMWareConnection(cfg);
					err = 0;
					
					DebugTool.showConsole("Detect VCenter Begin .................");
					conn.retrieveEntitiesAlarm(VMWareEntityType.DATASTORE);
					conn.retrieveEntitiesAlarm(VMWareEntityType.HOST_SYSTEM);
					conn.retrieveEntitiesAlarm(VMWareEntityType.NETWORK);
					conn.retrieveEntitiesAlarm(VMWareEntityType.VIRTUAL_MACHINE);
					list = conn.getCurrAlarmBean();
					if (list!=null && list.size()>0)
					{
						
						/*if overall status have 'red'*/
						String state = VMConnResult.WARM;
						for(VMWareAlarm alarm:list){
							if (StringUtils.endsWithIgnoreCase(alarm.getOverallStatus(),"red"))
							{
								state = VMConnResult.EXCEPT; break ;
							}
						}
						vmcr.setState(state);
						/*if overall status have 'red'*/
						dao.refreshCurrAlarm(list,vmcode);
						DebugTool.showConsole("VCenter Alarm num :" + list.size());
					}
					else
					{
						vmcr.setState(VMConnResult.NORMAL);
						dao.clearCurrAlarm( vmcode );
					}
					conn.logout();
	
					DebugTool.showConsole("Detect VCenter End .................");
		
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					DebugTool.printErr("VMWareDetectTask RemoteException........");
					err = 1 ;
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					DebugTool.printErr("VMWareDetectTask MalformedURLException........");
					err = 1 ;
				}
				if (err==1){
					VMWareAlarm vCenterAlarm = new VMWareAlarm();
					list = new ArrayList<VMWareAlarm> () ;
					vCenterAlarm.setVmCode(cfg.getVmCode());
					vCenterAlarm.setAlarmKey(cfg.getVmCode());
					vCenterAlarm.setAlarmName("Connect Fail!");
					vCenterAlarm.setAlarmDescription("VCenter Service Cannt connect, please check configuration or network!");
					vCenterAlarm.setEntityName(cfg.getVmCenterName());
					vCenterAlarm.setEntityType("VCenter");
					vCenterAlarm.setOverallStatus("red");
					vCenterAlarm.setTriggerTime(BaseCommonFunc.getStrFromDate(new Date()));
					vCenterAlarm.setAcknowledged(false);
					list.add(vCenterAlarm);
					dao.refreshCurrAlarm(list,vmcode);
					DebugTool.printErr("VCenter Connect Fail!");
				}
			}
			
			vmcrs.add(vmcr);
			vmcRecord(vmcrs);
		}
		
		private void  selfDetect(){
			List<SelfResult> sfrs = new ArrayList<SelfResult>();
			for(Dsview dsv:selfList){
				SelfDAO dao = new SelfDAO();
				SelfResult sr = new SelfResult();
				sr.setSrPK(dsv.getPK());
				sr.setSrValues(dao.getValue(dsv));
				sfrs.add(sr);
			}
			selfRecord(sfrs);
		}
		
		public List<Monitorlog> getMonitorlog(){  	return mls;  }
		public List<Warmlog> getWarmLog(){				return wls ;	}
		
		
		/* 读取探测方法种类*/
		public void detect( ){
				/* 将服务信息分别放在不同的List中*/
				for(Dsview ds:dslist){
									if (StringUtils.equals(ds.getMethod().trim(), Constant.SNMP)) 		snmpList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.PING)) 		pingList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.HTTP)) 		httpList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.PORT)) 		portList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.SERI)) 		seriList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.STCP)) 		stcpList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.VMC)) 		vmcList.add(ds);
									if (StringUtils.equals(ds.getMethod().trim(), Constant.SELF)) 		
																							selfList.add(ds);
				}
				
				detectTime = new Date();	//为所有Detect方法设置相同的采集开始时间
				
				if (snmpList.size()>0)  snmpDetect();
				if (pingList.size()>0)  pingDetect();			
				if (httpList.size()>0)  httpDetect();
				if (portList.size()>0)  portDetect();
				if (seriList.size()>0 ) seriDetect();
				if (stcpList.size()>0 ) stcpDetect(); 
				if (vmcList.size()>0 )  vmcDetect(); 
				if (selfList.size()>0 ) selfDetect();
		}
		
		/*  添加监控记录 */
		protected  void addMonitorLog(Dsview dsv , String value1 ,String value2 ){
			Monitorlog  ml = new Monitorlog() ;
			ml.setMLDateTime(BaseCommonFunc.getStrFromDateTime(detectTime));
			ml.setDCode(dsv.getDCode());
			ml.setSVCode(dsv.getSVCode());
			ml.setSubModule(dsv.getSubModule());
			ml.setSubPort(dsv.getSubPort());
			
			/*如果Value值需要转化，调用相关转化类*/
			String cvName = dsv.getValueConvert();
			AbstractConvert convert = null ;
		
			if (StringUtils.isNotEmpty(cvName)){
					try {
						convert = (AbstractConvert) Class.forName(cvName).newInstance();
						value1 = convert.convert(dsv, value1 ,value2);
						
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
			
			// In Database, the Monitorlog table have two column value1 and value2 , 
			//	this column's length is max char is 60 .
			if (value1!=null && value1.length() > 60) value1 = StringUtils.substring(value1, 0,60);
			if (value2!=null && value2.length() > 60) value2 = StringUtils.substring(value2, 0,60);
			
			ml.setValue1(value1);
			ml.setValue2(value2);
			
			String threshold = dsv.getThreshold();
			String valueType = dsv.getValueType();
			String express = dsv.getWarmExpress();
			
			if (isWarming(value1,threshold,valueType,express))	// 如果是告警值，则在告警列表里添加一条信息
			{
						addWarmlog(dsv,value1);
						ml.setIsWarming(1);
			}else{
					ml.setIsWarming(0);
			}
			DebugTool.showConsole("Device:" + device.getDName() + " | Service: " + dsv.getSVName()
					+		ml.toString());
			mls.add(ml);
			
		}
		
		//Add warm data input "warmlog" table
		private void addWarmlog(Dsview dsv , String value){
			Warmlog wl = new Warmlog();
			wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(detectTime));
			wl.setWMCode(dsv.getWMCode());
			wl.setWMLevel(dsv.getWMLevel());
			wl.setDCode(dsv.getDCode());
			wl.setSVCode(dsv.getSVCode());
			
			if (StringUtils.isEmpty(value))	value = "空值";
			String content = dsv.getDName()+"[" + dsv.getIPAddr() + ":" + (StringUtils.isEmpty(dsv.getSubPort())?"": dsv.getSubPort())+ "-" + 
			(StringUtils.isEmpty(dsv.getSubPortName())?"": dsv.getSubPortName()) +"]:出现" + dsv.getWMName()+",当前采集值为:" + value ;
	 		wl.setWMContent(content);
	 		wl.setClosed(0);
	 		DebugTool.showConsole("Device:" + device.getDName() + " | Service: " + dsv.getSVName()
					+		wl.toString());
	 		wls.add(wl);
	 		
		}
		
		//This adjust value is need warm!
		private boolean isWarming( String value , String threshold , String valueType , String express){
			
			boolean flag = false;
			if (StringUtils.isEmpty(value)){
				flag = true;
			}else if (StringUtils.isEmpty(threshold) || StringUtils.isEmpty(express) || StringUtils.isEmpty(valueType)){
				flag = false ;
			}else if (StringUtils.equals(valueType, Constant.NUMBER_TYPE) && StringUtils.containsNone(threshold,",")){
				try{
				double dbValue = Double.parseDouble((StringUtils.isEmpty(value)?"0":value));
				double dbThreshold = Double.parseDouble(threshold);
				if (StringUtils.equals(express, Constant.LT))	flag = (dbValue < dbThreshold );
				if (StringUtils.equals(express, Constant.GT))	flag = (dbValue > dbThreshold );
				if (StringUtils.equals(express, Constant.LTEQ))	flag = (dbValue <= dbThreshold );
				if (StringUtils.equals(express, Constant.GTEQ))	flag = (dbValue >= dbThreshold );
				if (StringUtils.equals(express, Constant.EQ))	flag = (dbValue == dbThreshold );
				if (StringUtils.equals(express, Constant.NEQ))	flag = (dbValue != dbThreshold );
				}catch(NumberFormatException err){
					flag=false;
					DebugTool.printErr("********Warming Express dataType error! Please check config!");
				}
			}else if (StringUtils.equals(valueType, Constant.STRING_TYPE) && StringUtils.containsNone(threshold,",")){
				String strValue = (StringUtils.isEmpty(value)?"":value);
				String strThreshold = threshold ;
				if (StringUtils.equals(express, Constant.LT))	flag = (strValue.compareToIgnoreCase(strThreshold)<0);
				if (StringUtils.equals(express, Constant.GT))	flag = (strValue.compareToIgnoreCase(strThreshold)>0);
				if (StringUtils.equals(express, Constant.LTEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)<=0);
				if (StringUtils.equals(express, Constant.GTEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)>=0);
				if (StringUtils.equals(express, Constant.EQ))	flag = (strValue.compareToIgnoreCase(strThreshold)==0);
				if (StringUtils.equals(express, Constant.NEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)!=0);
				
			}else if (StringUtils.equals(valueType, Constant.NUMBER_TYPE) && StringUtils.contains(threshold,","))	{
				double dbValue = Double.parseDouble((StringUtils.isEmpty(value)?"0":value));
				String[] thresholds = StringUtils.split(threshold, ",");
				if (StringUtils.equals(express, Constant.JY)&& thresholds.length>=2) 
				{	double threshold1 = Double.parseDouble(thresholds[0]);
					double threshold2 = Double.parseDouble(thresholds[1]);
					flag = (threshold1<= dbValue && dbValue <= threshold2);
				}
				if (StringUtils.equals(express, Constant.NJY)&& thresholds.length>=2) 
				{	double threshold1 = Double.parseDouble(thresholds[0]);
					double threshold2 = Double.parseDouble(thresholds[1]);
					flag = !(threshold1<= dbValue && dbValue <= threshold2);
				}
				if (StringUtils.equals(express, Constant.SY)) 
				{
					flag = ( StringUtils.contains(threshold, value));
				}
				if (StringUtils.equals(express, Constant.NSY)) 
				{
					flag = (!StringUtils.contains(threshold, value));
				}
			}else if (StringUtils.equals(valueType, Constant.STRING_TYPE) && StringUtils.contains(threshold,","))	{
				String strValue = (StringUtils.isEmpty(value)?"":value);
				String[] thresholds = StringUtils.split(threshold, ",");
				if (StringUtils.equals(express, Constant.JY)&& thresholds.length>=2) 
				{	
					boolean c1 = (strValue.compareToIgnoreCase(thresholds[0])>=0);
					boolean c2 = (strValue.compareToIgnoreCase(thresholds[1])<=0);
					flag = ( c1 && c2);
				}
				if (StringUtils.equals(express, Constant.NJY)&& thresholds.length>=2) 
				{						
					boolean c1 = (strValue.compareToIgnoreCase(thresholds[0])>=0);
					boolean c2 = (strValue.compareToIgnoreCase(thresholds[1])<=0);
					flag = !( c1 && c2);
				}
				if (StringUtils.equals(express, Constant.SY)) 
				{
					flag = ( StringUtils.contains(threshold, value));
				}
				if (StringUtils.equals(express, Constant.NSY)) 
				{
					flag = (!StringUtils.contains(threshold, value));
				}
			}	
			
			return flag ;
		}
}
