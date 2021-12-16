package mqeye.service;

import java.util.Date;
import java.util.List;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.data.vo.Warmlog;
import mqeye.data.vo.WarmlogDAO;
import mqeye.service.detect.AbstractDetector;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;




/*  核心探测类 ，可以加载不同的探测器*/
public class MQeyeDetectTask implements Runnable {
	
	private Device device = null ;
	private List<Dsview> dslist = null;
	
	private String errMsg = "";
	public int Loop = 300;
	private boolean isReady = false ;		//存在合格的服务对象, 可以启动探测任务
	
	
	private List<Monitorlog> mls = null;		//监控数据存放列表
	private List<Warmlog> wls = null;		//监控告警存放列表
	
	public String getErrMsg(){ return errMsg; } 
	/*标记启动时间，设置最小轮询周期 */
	private void getLoopAndMarkStartTime(){
		if (device!=null ) 
					Loop = (device.getDefaultLoop()>0?device.getDefaultLoop():300); 
					DebugTool.printMsg(Loop + "is Loop Time");
	}					

	/*根据品牌服务表中的记录, 查找不合理服务,将其删除 */
	
	
	private void refreshDevice( ){
		if (device != null){
			String dcode = device.getDCode() ;
			DeviceDAO ddao = new DeviceDAO();
			device = ddao.getValidByPK(dcode);
		}
	}
	/*  刷新监控列表 确保能够启动服务 */
	private void refreshDsList( ){
				if (device != null){
						if (StringUtils.isBlank(device.getIPAddr())) errMsg =  device.getDName()+"(" + device.getDCode() + ")" + "监控对象IP地址为空";		
						String dcode = device.getDCode() ;
						DsviewDAO dao = new DsviewDAO();
						if (dao.isNotConsist(dcode)) 
										dao.refreshService(dcode);  /*刷新数据库中服务的运行状态*/
						dslist = dao.getBeanByDCode(dcode);
						if (dslist == null || dslist.size()==0 ) errMsg =  device.getDName()+"(" + device.getDCode() + ")" + "无监控服务"; 
						if (dslist.size() > 0) {  isReady=true; getLoopAndMarkStartTime(); }			// 选择轮询周期最小的配置 		 
				} else 
						errMsg = "监控对象为空,DEVICE IS NULL!"; 
	}
	
	/* 构造探测任务类*/
	public MQeyeDetectTask(Device d){
			this.device  = d ;
			refreshDsList();
	}
	
	/* 根据类别名给出隶属大类编号*/
	private String getMatchTypeKey(String tpcode){
			String key = null ;
			for(String t:Constant.typeNames){
						if (StringUtils.startsWith(tpcode, t)) {key = t; break;}
			}
			return key;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*---------------准备启动探测器---------------------*/
		String detectTime = BaseCommonFunc.getStrFromDateTime(new Date());
		DebugTool.showConsole("PREPARE START DETECT :" + device.getDName() + "....." + detectTime);
		Reset r = Reset.getInstance() ;
		String dcode = device.getDCode() ;
		if (r.getFlag(dcode)){
					DebugTool.showConsole("BEGIN REFRESH :" + device.getDName() + "SERVICE");
					refreshDevice();
					refreshDsList();
					r.reset(dcode, false);
		}
		/*---------------准备启动探测器---------------------*/
		
		/*---------------启动探测器---------------------*/
		if (isReady){
				try {
					String tpcode = device.getTPCode();
					String tpHeadChar = getMatchTypeKey(tpcode);
					String clazz = Constant.detectMap.get(tpHeadChar);
					if (StringUtils.isNotEmpty(clazz)){
							AbstractDetector detector = (AbstractDetector)Class.forName(clazz).newInstance();
							detector.init(device, dslist);
							try{
							detector.detect();
							}catch(Exception err){
								DebugTool.printErr("****************设备探测异常......");
								DebugTool.printExc(err);
							}
							mls = detector.getMonitorlog();
							wls = detector.getWarmLog();
					}else
						DebugTool.printMsg("没有支持:" + device.getDName() + "的探测类..........");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("设备:" + device.getDName() + "的探测类名有误");
						DebugTool.printExc(e);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("设备:" + device.getDName() + "的探测类名有误");
						DebugTool.printExc(e);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("设备:" + device.getDName() + "的探测类名有误");
						DebugTool.printExc(e);
				} 
		}else{
			DebugTool.printErr("监控对象数据有误，未能启动监控!");
			DebugTool.printErr(errMsg);
		}
		/*---------------启动探测器---------------------*/
		
		
		/*---------------记录探测器监控数据，探测结束---------------------*/
				
			 if (mls!=null && mls.size()	>	0){
				 MonitorlogDAO mdao = new MonitorlogDAO();
			 		mdao.recordToDB(mls);
			 		for(Monitorlog ml:mls)
				    	DebugTool.printMsg("*****"+ml.getMLDateTime() + ":" + ml.getDCode()+ ":" + ml.getSVCode() + ":" + ml.getValue1() + ":" + StringUtils.trimToEmpty(ml.getValue2()));
				   
			 		mls.clear();
			 }
			 if (wls!=null && wls.size() > 0){
	    WarmlogDAO wdao = new WarmlogDAO();
	    wdao.recordToDB(wls);
	    for(Warmlog wl:wls)
	    		DebugTool.printErr("*****"+wl.getWMDateTime() + ":" + wl.getDCode()+ ":" + wl.getSVCode() + ":" + wl.getWMLevel() + ":" + wl.getWMContent());
	    	wls.clear();
			 }
			
		/*---------------记录探测器监控数据，探测结束---------------------*/
		
	}
	

	

}
