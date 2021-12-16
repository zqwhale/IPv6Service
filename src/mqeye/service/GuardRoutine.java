package mqeye.service;

import org.apache.commons.lang.StringUtils;

import mqeye.service.local.config.CpuUsageBean;
import mqeye.service.local.config.PerformanceCollector;
import mqeye.service.tools.LCDScreenTool;
import mqeye.service.tools.MQeyeClient;
import mqeye.service.tools.SelfCureTool;
 
public class GuardRoutine {
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try{	
		String lcdMsg = "";
		CpuUsageBean cpu = PerformanceCollector.getCpuUsage();
		int usage = (int) (cpu.getCupUsage()*100);
		MQeyeClient q = new MQeyeClient();
		
		if (StringUtils.equals("TESTOK", q.test()))
			//lcdMsg =  "'Status:Running' 'CPU:" +usage +"%'";
			lcdMsg =  "Status:Running CPU:" +usage +"%";
		else
		{
			//lcdMsg =  "'Status:Unknow' 'CPU:" +usage +"%'";
			lcdMsg =  "Status:Unknow CPU:" +usage +"%";
			int cnt = 0;
			while (SelfCureTool.getStatus()==-1 && cnt<=5 ){
				 if (cnt == 5) {
					 q.turnoff();
					 SelfCureTool.stop();
					 SelfCureTool.removePID();
					 System.out.println(SelfCureTool.start()==1? "SelfCureTool: MQeyeServer start OK!":"SelfCureTool: MQeyeServer start FAIL!");
					 Thread.sleep(10*1000);
				 }
				 Thread.sleep(500);
				 cnt++;
			}
		}
		LCDScreenTool.lcmPrint(lcdMsg);
		}catch(Exception ex){
			System.out.println(ex);
		}
	}

}
