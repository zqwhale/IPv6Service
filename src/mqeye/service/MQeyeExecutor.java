package mqeye.service;

import java.util.Calendar;
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
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.routine.LogFileTask;
import mqeye.service.routine.SerialPortGuardTask;
import mqeye.service.routine.SerialServerGuardTask;
import mqeye.service.routine.TunnelBuildTask;
import mqeye.service.routine.VideoManageTask;
import mqeye.service.routine.WatchTask;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;

public class MQeyeExecutor {
	private int coreCpuNum = 1; 						/* 记录服务器上的CPU核数*/
	private ScheduledExecutorService  executor = null;  			/* 线程池服务器 */
	private boolean isRunning = false ;
	private List<Device>  dlist = null ;
	private ScheduledFuture<?>	logFutures = null;		/*  Log File Backup thread */ 
	private ScheduledFuture<?>	watchFutures = null;		/*  Watch detect is normal thread */
	private ScheduledFuture<?>	tunnelFutures	= null; 	/* Build tunnel thread */
	private ScheduledFuture<?>	videoFutures	= null; 	/* Build push video thread */
	private ScheduledFuture<?>	serialFutures	= null; 	/* Single thread get serial device data */
	private ScheduledFuture<?>	stcpFutures	= null; 	/* Single thread get serial server data */
	private Map<String ,ScheduledFuture<?>> futures = new HashMap<String , ScheduledFuture<?>>();  /* 管理所有future,String 对应 Device的主键 */
	private static MQeyeExecutor instance = null ;
	
	private MQeyeExecutor(){
		/* 初始化 计算机环境中的 线程池 */
		coreCpuNum = Runtime.getRuntime().availableProcessors();
		executor = Executors.newScheduledThreadPool(coreCpuNum * Constant.POOL_SIZE);
		/* 初始化 监控对象列表, 并始终保存在内存中 */
		init();
		
	}
	
	private boolean isNeedSupport(Device d , String type)
	{
		boolean flag = false;
		DsviewDAO dao = new DsviewDAO();
		List<Dsview>	dslist = dao.getBeanByDCode(d.getDCode());
		if (dslist!=null)
		{
			for(Dsview ds:dslist){
				flag = StringUtils.equals(ds.getMethod().trim(), type);
				break;
			}
		}
		return flag;
	}
	
	private boolean init(){
		boolean  flag  = true ;
		LogFileTask lfTask = new LogFileTask();
		Calendar c = Calendar.getInstance();
		int delayDate = (8 - c.get(Calendar.DAY_OF_WEEK)) % 7;
		int delayHour = (24	-	c.get(Calendar.HOUR_OF_DAY));
		logFutures = executor.scheduleAtFixedRate(lfTask, delayDate*24+delayHour ,7*24 ,TimeUnit.HOURS); 
		WatchTask wTask = new WatchTask();
		watchFutures = executor.scheduleAtFixedRate(wTask, WatchTask.delay,WatchTask.loop,TimeUnit.SECONDS);
		
		if (StringUtils.equals(BaseCommonFunc.getProperty("buildTunnel"),"yes"))
		{
			TunnelBuildTask tbTask = new TunnelBuildTask();
			tunnelFutures = executor.scheduleAtFixedRate(tbTask, TunnelBuildTask.delay,TunnelBuildTask.loop,TimeUnit.SECONDS);
		}
		if (StringUtils.equals(BaseCommonFunc.getProperty("pushVideo"),"yes"))
		{
			VideoManageTask vmTask = VideoManageTask.getInstance();
			videoFutures = executor.scheduleAtFixedRate(vmTask, VideoManageTask.delay,VideoManageTask.loop,TimeUnit.SECONDS);
			vmTask.stopAllPush();
			vmTask.startAllPush();
		}
		
		if (StringUtils.equals(BaseCommonFunc.getProperty("isSuppertSerialPort"),"yes")){
			SerialPortGuardTask spgtask = SerialPortGuardTask.getInstance();
			serialFutures = executor.scheduleAtFixedRate(spgtask, SerialPortGuardTask.delay,SerialPortGuardTask.loop,TimeUnit.SECONDS);
		}
		
		if (StringUtils.equals(BaseCommonFunc.getProperty("isSupportSerialServer"),"yes")){
			SerialServerGuardTask ssgtask = SerialServerGuardTask.getInstance();
			stcpFutures = executor.scheduleAtFixedRate(ssgtask, SerialServerGuardTask.delay,SerialServerGuardTask.loop,TimeUnit.SECONDS);
		}
		DeviceDAO dao = new DeviceDAO();
			
			for(Device dv:dao.getAll()){
				if (!StringUtils.equals(BaseCommonFunc.getProperty("isSupportVMWare"),"yes"))
				{
					if (isNeedSupport(dv,Constant.VMC)) 	dao.markValid(dv,0);
				}else{
					if (isNeedSupport(dv,Constant.VMC)) 	dao.markValid(dv,1);
				}
				if (!StringUtils.equals(BaseCommonFunc.getProperty("isSuppertSerialPort"),"yes"))
				{
					if (isNeedSupport(dv,Constant.SERI)) 	dao.markValid(dv,0);
				}else{
					if (isNeedSupport(dv,Constant.SERI)) 	dao.markValid(dv,1);
				}
				if (!StringUtils.equals(BaseCommonFunc.getProperty("isSupportSerialServer"),"yes"))
				{
					if (isNeedSupport(dv,Constant.STCP)) 	dao.markValid(dv,0);
				}else{
					if (isNeedSupport(dv,Constant.STCP)) 	dao.markValid(dv,1);
				}
			}
		
		
		/* 判断是否 成功 获取了 监控对象信息 */
		dlist = dao.getAllValid();
		if (dlist == null) 
				flag = false ;
		else if ( dlist.size() == 0 ) 
				flag = false ;
		
		
		return flag ;
	}
	
	public static MQeyeExecutor getIntance(){
		if ( instance == null){
			instance = new MQeyeExecutor();
		}
		return instance ;
	}
	
	public List<Device> getDeviceList(){
				return dlist;
	}
	
	public void shutdown(){
		Runnable r = new Runnable(){
				private void waitForCancelled(int time){
					try {
						Thread.sleep(time);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				@Override
				public void run() {
					// TODO Auto-generated method stub
						do{
							executor.shutdown();
							waitForCancelled(100);
							}while(!executor.isTerminated());
							DebugTool.showConsole("MQeye Service shutdown success!");
				}
				
			};
			new Thread(r).run();
	}
	public boolean isRunning(){
		isRunning = (!futures.isEmpty()) ;
		return isRunning;
	}
	
		private boolean isNormal(String dcode){
				boolean normalFlag = true ;
				boolean flag1 = false ;
				boolean flag2 = false ;
				DeviceDAO ddao = new DeviceDAO();
				Device d = ddao.getValidByPK(dcode);
				flag1 = ( d.getIsValid() == 1 );
				if  ( flag1 ) {
		    		DsviewDAO dvdao = new DsviewDAO();
		    		List<Dsview> dsvlist = dvdao.getBeanByDCode(dcode);
		    		for(Dsview dsv:dsvlist){
		    						flag2 = ( dsv.getOnOff()==1 );
		    						break;
		    			}
				}
				
				if (flag1 && flag2){
					MonitorlogDAO mdao = new MonitorlogDAO();
					normalFlag = mdao.hasRecentMonitorlog(dcode);
				}
				return normalFlag ;
	}

	public boolean isRunning(String dcode){
		boolean flag = false ;
		
		if (StringUtils.isNotBlank(dcode) && futures.containsKey(dcode) ){
			ScheduledFuture<?> f = futures.get(dcode);
			flag = !f.isCancelled(); 
			DebugTool.showConsole("＠＠＠isDone:" + f.isDone()+ "＠＠＠ isCancelled:" + f.isCancelled()+" ＠＠＠ isNormal:" + isNormal(dcode) + " ＠＠＠ " );
		}else if ( StringUtils.equals(dcode, "LOG")){
			flag = !logFutures.isCancelled();
			DebugTool.showConsole("＠＠＠isDone:" + logFutures.isDone()+ "＠＠＠ isCancelled:" + logFutures.isCancelled() + " ＠＠＠ " );
		}else if ( StringUtils.equals(dcode, "WATCH")){
			flag = !watchFutures.isCancelled();
			DebugTool.showConsole("＠＠＠isDone:" + watchFutures.isDone()+ "＠＠＠ isCancelled:" + watchFutures.isCancelled() + " ＠＠＠ " );
		}else if ( StringUtils.equals(dcode, "TUNNEL")){
			flag = !tunnelFutures.isCancelled();
			DebugTool.showConsole("＠＠＠isDone:" + tunnelFutures.isDone()+ "＠＠＠ isCancelled:" + tunnelFutures.isCancelled() + " ＠＠＠ " );
		}
		
		return flag ;
	}
	
	public String restart(){
		String runMsg = "";
		if (isRunning) runMsg = runMsg + stop();
		init();
		if (!isRunning) runMsg = runMsg + start();
		return runMsg;
	}

    public String stop(){  
    	/* 执行取消所有任务的执行 的代码*/
    	String runMsg = "";		/* 记录每个线程是否被成功停止*/
    	if (isRunning && dlist!=null){
    		
			for(Device d:dlist){
				if (futures.containsKey(d.getDCode())){
					futures.get(d.getDCode()).cancel(true);
					futures.remove(d.getDCode());
					runMsg =  runMsg + "|" + d.getDName()+"("+ d.getDCode()+")" + "停止成功" ;
					changeState(d,Constant.SERVICE_STOP);		//设备监控标记为停止
				}else{
					runMsg =  runMsg + "|" + d.getDName()+"("+ d.getDCode()+")" + "停止失败，未发现指定进程";
				}
			}
			isRunning = false ;
		}	
    	return runMsg ;
    }  
    
    private void changeState(Device d , int state){
    							DeviceDAO dao1 = new DeviceDAO();
    							dao1.changeState(d, state);
    								
    							DsviewDAO dao2 = new DsviewDAO();
    							dao2.resetRunStop(d.getDCode(), state);
    }

    public String start(){
    	String runMsg = "";			/* 	返回概要信息	*/
    	int sCnt = 0 , fCnt = 0; 	/*		记录启动成功个数与失败个数	*/
    	String runLog = "";			/* 	记录每个线程是否被成功运行	*/
    	
    	if ( dlist!=null){
    				int delay = 0 ;
    				for(Device d:dlist){
    							if (isRunning(d.getDCode())) continue;
    							MQeyeDetectTask mtask = new MQeyeDetectTask(d);
    							String err = mtask.getErrMsg();
    							if (StringUtils.isEmpty(err)){
    									sCnt++; 
    									ScheduledFuture<?> f =   executor.scheduleAtFixedRate(mtask,delay++,mtask.Loop,TimeUnit.SECONDS);  // Delay错开启动线程
    									futures.put(d.getDCode(), f);
    									changeState(d,Constant.SERVICE_RUN);		//设备监控标记为运行
    									runLog = runLog + "|" + d.getDName()+"("+ d.getDCode()+")" + "运行成功"  ;
    							}else{
    									fCnt++;
    									runLog = runLog + "|" + err;
    								}
    				}
    		}
    	isRunning = true ;
    	runMsg = "启动成功:" +sCnt + "个,失败:" + fCnt+"个.";
    	DebugTool.showConsole(runMsg);
    	return runLog ;
    }

    //Device start control
    private String startDevice(String dcode){
    	String runMsg = "监控对象未知，服务启动失败";
    	
		Device sd = null ;
		
		for(Device d:dlist){
			if (StringUtils.equals(d.getDCode(),dcode)) 
			{dlist.remove(d); break;}
		}
		DeviceDAO dao = new DeviceDAO();
		sd = dao.getValidByPK(dcode);
		
		if (futures.containsKey(dcode)){
			futures.get(dcode).cancel(true);
			futures.remove(dcode);
			}
		if (sd!=null ){
			dlist.add(sd);
			MQeyeDetectTask mtask = new MQeyeDetectTask(sd);
			String err = mtask.getErrMsg();
			if (StringUtils.isEmpty(err)){
					
				ScheduledFuture<?> f =   executor.scheduleAtFixedRate(mtask,0,mtask.Loop,TimeUnit.SECONDS);
				futures.put(sd.getDCode(), f);
					
				changeState(sd,Constant.SERVICE_RUN);		//设备监控标记为运行
				isRunning = true;
				runMsg = sd.getDName()+"("+ sd.getDCode()+")"+"运行成功";
			}else{
				runMsg = err ;
				}
		}
		
		return runMsg;
    }
    
    //watch Thread 
    private String startWatch( ){
		String runMsg = "监控对象未知，服务启动失败";
		watchFutures.cancel(true);
		WatchTask wTask = new WatchTask();
		watchFutures = executor.scheduleAtFixedRate(wTask, WatchTask.delay,WatchTask.loop,TimeUnit.SECONDS);
		runMsg = "值守线程运行成功";
		return runMsg ;
     }	
    
    
    public String start(String dcode){
    	String runMsg = "";
    	if (StringUtils.isNotBlank(dcode) && dlist!=null)
    	{
    		if (dcode.equals("watch"))
    			runMsg = startWatch( );
    		else
    			runMsg = startDevice(dcode);
    	}
    	
    	DebugTool.showConsole(runMsg);
    	return runMsg;
    }

    private String stopDevice( String dcode )
     {
    	String runMsg = "监控对象未知，服务停止失败";
    	Device sd = null ;
    	for(Device d:dlist){
			if (StringUtils.equals(d.getDCode(),dcode)) 
			{	sd = d ;	dlist.remove(d); break;}
			}
			
		if (sd !=null){
    		if (futures.containsKey(dcode)){
    			futures.get(dcode).cancel(true);
    			futures.remove(dcode);
    			runMsg = sd.getDName()+"("+ sd.getDCode()+")" + "停止成功" ;
    			changeState(sd,Constant.SERVICE_STOP);		//设备监控标记为停止
    			}else{
    				runMsg = sd.getDName()+"("+ sd.getDCode()+")" + "停止失败，未发现指定进程";
    			}
			}
		if (futures.size()==0) 
				isRunning = false ;
		
		return runMsg ;
     }
    
    
    
    public String pushVideo(String dcode)
    {
    	VideoManageTask vmTask = VideoManageTask.getInstance();
		String result = vmTask.startOnePush(dcode);
		return result;
    }
    
    public String disconVideo(String dcode)
    {
    	VideoManageTask vmTask = VideoManageTask.getInstance();
		String result = vmTask.stopOnePush(dcode);
		return result;
    }
    public String isLiveVideo(String dcode)
    {
    	VideoManageTask vmTask = VideoManageTask.getInstance();
		int r = vmTask.isLive(dcode);
		String s = "unknow";
		if (r==0) 	s = "boot";
		if (r==1) 	s = "online";
		if (r==-1) s= "offline";
		return s;
    }
    private String stopSupport(String support){
    	String runMsg = "";
    	if (support.equals("watch")) watchFutures.cancel(true);
    	if (support.equals("tunnel")) tunnelFutures.cancel(true);
    	if (support.equals("log")) logFutures.cancel(true);
    	if (support.equals("video")) {
    		videoFutures.cancel(true);
    		VideoManageTask vmTask = VideoManageTask.getInstance();
			vmTask.stopAllPush();
    	}
    	runMsg = support + "线程停止成功" ;
    	return runMsg;
    }
    
    public String stopAllSupport()
    {	String runMsg = "";
    	runMsg = runMsg  + "|" + stopSupport("watch");
    	runMsg = runMsg  + "|" + stopSupport("log");
    	runMsg = runMsg  + "|" + stopSupport("tunnel");
    	runMsg = runMsg  + "|" + stopSupport("video");
    	return runMsg;
    }
    
   public String stop(String dcode){
    	String runMsg = "";
    	
    	if (StringUtils.isNotBlank(dcode)){
    			runMsg = stopDevice(dcode);
    	}
    	
    	DebugTool.showConsole(runMsg);
    	return runMsg;
    }

    public String restart(String dcode){
    	String runMsg = "";
    	runMsg = runMsg + stop(dcode);
    	runMsg = runMsg + start(dcode);
    	return runMsg;
    }
    
    public String refresh(String dcode){
    	String runMsg = "监控对象未知，无刷新服务";
    	if (StringUtils.isNotBlank(dcode)){
    		if (futures.containsKey(dcode)) {
    					Reset r = Reset.getInstance();
    					
    					r.reset(dcode, true);
    					runMsg = "设备:"+ dcode + "监控服务刷新成功";
    		 }else{
    			 	runMsg = start(dcode);	
    		 		}
    	}
    	return runMsg;
    }
}
