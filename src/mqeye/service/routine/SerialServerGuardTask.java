package mqeye.service.routine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import mqeye.comm.serial.server.SocketSendTool;
import mqeye.data.cv.ModbusElectricConvert;
import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.service.Constant;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.CmdBean;
import mqeye.service.serial.ProtocolCode;
import mqeye.service.serial.ProtocolParser;

import org.apache.commons.lang.StringUtils;

public class SerialServerGuardTask  implements Runnable{
	public static int delay = 0 ;
	public static int loop = 5 ;
	
	
	private static SerialServerGuardTask instance = null;
	private SerialServerGuardTask( ){
		
	}
	public static synchronized SerialServerGuardTask getInstance(){
		if (	instance == null	) 
				instance = new SerialServerGuardTask();
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
		List<Device> dlist = new DeviceDAO().getSerialServerDevice();		//串口服务器设备清单列表
		Map<String,AbstractSensorResult> temp_result = new HashMap<String,AbstractSensorResult>();
		
		for(Device d:dlist)
		{
			
			String dcode = d.getDCode();
			String ip = StringUtils.trim(d.getIPAddr());
			String tcpport = StringUtils.trim(d.getSnmpCommity());
			System.out.println("Hello---------------------------------------------------------" +
					dcode + "--------" + d.getDName());
			//串口设备服务信息
		  	DsviewDAO dvdao = new DsviewDAO();
	  		List<Dsview> dsvList = dvdao.getBeanByDCode(dcode);
	  		List<Dsview> stcpList = new ArrayList<Dsview>();
	  		for(Dsview dsv:dsvList)
	  		{		if (StringUtils.equals(dsv.getMethod().trim(), Constant.STCP)) stcpList.add(dsv);
	  		}
	  		
	  	//构造本设备命令列表和返回值列表
	  		Map<String,AbstractSensorResult> crmap = new HashMap<String,AbstractSensorResult>();
	  		
		  	for(Dsview dsv:stcpList)
		  	{
		  		
		  		ProtocolParser parser = covertParser(dsv);
				String cmds = dsv.getSnmpOID();
				int validlen = dsv.getDelay();
				String cmdStr = parser.convertCmdStr(cmds);
				byte[] hexcmd =   parser.convertCmd(cmds);
				String pattern =  parser.getPattern();
				System.out.println("pattern" + pattern + "cmdStr" + cmdStr);
				
				if (StringUtils.isEmpty(cmdStr))	cmdStr = "#Empty#";
				AbstractSensorResult asr =  new AbstractSensorResult();
				if (crmap.containsKey(cmdStr)) 
				{
					asr = crmap.get(cmdStr);
				}else
				{
					int port = Integer.parseInt(tcpport);
					String shortAddr = StringUtils.trim(d.getDICode());
					SocketSendTool tool = new SocketSendTool();
					int empty_times = 0;
					byte[] result = null;
					
					do{
						result = tool.sendCmd(ip, port , hexcmd, validlen,1000);
						if (result==null || result.length < validlen) {
							empty_times++;
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						
						}
					}while( empty_times>0 && empty_times<3 );
					
					if (result!=null && result.length > validlen)
					{
						
						asr.setShortAddress(shortAddr);
						asr.setCmd(cmdStr);
						asr.setResult(result);
						
						crmap.put(cmdStr, asr);
						
					}
				}
				String key = dsv.getDCode() + "_" + dsv.getSVCode() + "" + 
				(StringUtils.isEmpty(dsv.getSubPort())?"":dsv.getSubPort());
				System.out.println(key + "-------"  +asr.getCmd() + "-------" + asr.getResultStr(pattern) );
				temp_result.put(key, asr);
				
				
		  	
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		 int coreCpuNum = Runtime.getRuntime().availableProcessors();
		 ScheduledExecutorService  executor = Executors.newScheduledThreadPool(coreCpuNum * Constant.POOL_SIZE);  			/* 线程池服务器 */
		 
		 SerialServerGuardTask ssgtask = SerialServerGuardTask.getInstance();
		 ssgtask.run();
		 ScheduledFuture<?> serialFutures = executor.scheduleAtFixedRate(ssgtask, SerialServerGuardTask.delay,SerialServerGuardTask.loop,TimeUnit.SECONDS);
		 
	}
}
