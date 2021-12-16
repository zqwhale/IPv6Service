package mqeye.service.routine;

import java.util.Calendar;
import java.util.List;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.MQeyeClient;

public class WatchTask implements Runnable {
		public final static int delay = 30;
		public final static int loop = 5*60;
		
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
		
		
		private int watchAll(){
		int cnt = 0;
		
		MQeyeClient q = new MQeyeClient();		
		DeviceDAO ddao = new DeviceDAO();
		List<Device>  dlist = ddao.getAllRunState();
				
		String startMsg = "" ;
		
		Calendar c = Calendar.getInstance();
		String timeMsg = BaseCommonFunc.getStrFromDateTime(c.getTime());
		
		
						if (dlist!=null && dlist.size()>0){ 
						for(Device d:dlist){
											String dcode = d.getDCode();
											if (!isNormal(dcode)){
												cnt++;
												try {
													Thread.sleep(5*1000);
												} catch (InterruptedException e) {
													// TODO Auto-generated catch block
													e.printStackTrace();
												}
												startMsg = q.restart(dcode);
												DebugTool.showConsole(timeMsg + "@WATCH@" + d.toString() );
												DebugTool.showConsole("@WATCH@" + startMsg );
											}

									}
						}
		DebugTool.showConsole("Find:" +cnt+" detect threads ...");
		return cnt;
}
	
	@Override
	public void run() {
	// TODO Auto-generated method stub
					DebugTool.showConsole("WATCH THREAD START ...........");
					watchAll();
					
		 
	}

	public static void main(String[] args)   
	  {  	
				WatchTask task  = new WatchTask();
				Thread watch = new Thread(task);
				watch.start();
	  }

}
