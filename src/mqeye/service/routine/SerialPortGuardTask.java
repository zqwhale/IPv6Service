package mqeye.service.routine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.SerialDevice;
import mqeye.data.vo.SerialDeviceDAO;
import mqeye.service.Constant;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.ProtocolCode;
import mqeye.service.serial.ProtocolParser;
import mqeye.service.serial.SerialPortReader;
import mqeye.service.serial.SerialPortTool;

import org.apache.commons.lang.StringUtils;

/*--- 串口值守任务	---*/
public class SerialPortGuardTask implements Runnable{
	public static int delay = 0 ;
	public static int loop = 20 ;
	private static SerialPortGuardTask instance = null;
	private SerialPortGuardTask( ){
		
	}
	public static synchronized SerialPortGuardTask getInstance(){
		if (	instance == null	) 
				instance = new SerialPortGuardTask();
		return instance;
	}
	
	public Map<String,AbstractSensorResult> getResult()
	{
		return key_result;
	}
	private Map<String,AbstractSensorResult> key_result = new HashMap<String,AbstractSensorResult>();
	private int run_flag = 0;
	private int run_state = 0;
	
	public int getRunState(){
		return run_state ;
	}
	public int getRunFlag(){
		return run_flag;
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
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		  run_flag = 1;
		  
		  List<SerialDevice> sdlist = new SerialDeviceDAO().getSerialDeviceAll();		//串口设备清单列表
		  Map<String,AbstractSensorResult> temp_result = new HashMap<String,AbstractSensorResult>();
		  for( SerialDevice sd:sdlist )
		  {		
			  	//串口设备
			  	String dcode = sd.getDcode();
			  	
			  	DeviceDAO ddao = new DeviceDAO();
			  	Device device = ddao.getValidByPK(dcode);
			  	if (	device == null ) 	  		continue;
		  		System.out.println("----" + device.getDName());
		  		
		  		//串口设备服务信息
			  	DsviewDAO dvdao = new DsviewDAO();
		  		List<Dsview> dsvList = dvdao.getBeanByDCode(dcode);
		  		List<Dsview> seriList = new ArrayList<Dsview>();
		  		for(Dsview dsv:dsvList)
		  		{		if (StringUtils.equals(dsv.getMethod().trim(), Constant.SERI)) seriList.add(dsv);
		  		}
		  		
		  		//构造物理串口连接
		  		SerialDeviceDAO dao = new SerialDeviceDAO();
			  	String sysPort = dao.getCurrSysPort(dcode);
			  	SerialPortReader sr = new SerialPortReader(sysPort);
			  	Map<String,String> params = sd.toParamMap(); 
			  	SerialPortTool tool = new SerialPortTool(sr);
			  	tool.initParams(params);
		  		tool.openSerialPort();
		  		
			  	//构造本设备命令列表和返回值列表
		  		Map<String,AbstractSensorResult> crmap = new HashMap<String,AbstractSensorResult>();
		  		
			  	for(Dsview dsv:seriList)
			  	{
			  		
					ProtocolParser parser = covertParser(dsv);
					String cmds = dsv.getSnmpOID();
					String cmdStr = parser.convertCmdStr(cmds);
					byte[] hexcmd =   parser.convertCmd(cmds);
					String pattern = parser.getPattern();
					byte[] result = null;
					
					if (StringUtils.isEmpty(cmdStr))	cmdStr = "#Empty#";
					AbstractSensorResult r = null;
					if (crmap.containsKey(cmdStr)) 
					{
						r = crmap.get(cmdStr);
					}else
					{
						int empty_times = 0;
						do{
							if (StringUtils.equals(cmdStr, "#Empty#"))
								result = tool.read(null);
							else
								result = tool.read(hexcmd);
							if (result==null || result.length == 0) empty_times++;
							System.out.println(	device.getDCode() + "----DO----Empty:"  + empty_times);
							
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
							// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}while( empty_times>0 && empty_times<3 );
						
						r = new AbstractSensorResult();
						long timeStamp = System.currentTimeMillis();
						r.setShortAddress(dsv.getDICode());
						r.setCmd(cmdStr);
						r.setResult(result);
						r.setTimeStamp(timeStamp);
						crmap.put(cmdStr, r);
					}
					
					String key = dsv.getDCode() + "_" + dsv.getSVCode() + "" + 
								(StringUtils.isEmpty(dsv.getSubPort())?"":dsv.getSubPort());
					temp_result.put(key, r);
					System.out.println(key+ "-------" + r.getResultStr(pattern));
			  	}
			  	if (sr.isDamage())
				{
					System.out.println("@@@@@!!!!!------- is Rebuild serial port------>>>");
					tool.closeSerialPort();
					
					run_state = -1;
					break;
				}else{
					tool.closeSerialPort();
					run_state = 0;
				}
		  }
		  try {
			Thread.sleep(5*1000);
		  } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		  }
		  
		  key_result.clear();
		  key_result = temp_result;
		
	}
	
	 public static void main(String args[]) {
		 int coreCpuNum = Runtime.getRuntime().availableProcessors();
		 ScheduledExecutorService  executor = Executors.newScheduledThreadPool(coreCpuNum * Constant.POOL_SIZE);  			/* 线程池服务器 */
		 
		 SerialPortGuardTask spgtask = SerialPortGuardTask.getInstance();
		 spgtask.run();
		 
		
		 ScheduledFuture<?> serialFutures = executor.scheduleAtFixedRate(spgtask, SerialPortGuardTask.delay,SerialPortGuardTask.loop,TimeUnit.SECONDS);
		 
		 
	 }

}

