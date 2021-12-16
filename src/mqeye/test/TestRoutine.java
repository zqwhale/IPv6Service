package mqeye.test;

import java.util.Calendar;

import mqeye.service.routine.DatabaseRepairTask;
import mqeye.service.routine.LogFileTask;
import mqeye.service.routine.RouteTask;
import mqeye.service.routine.TimeSynTask;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.SelfCureTool;

public class TestRoutine {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		LogFileTask task = new LogFileTask();
//		Calendar c = Calendar.getInstance();
//		int delayDate = (8 - c.get(Calendar.DAY_OF_WEEK)) % 7;
//		int delayHour = (24	-	c.get(Calendar.HOUR_OF_DAY));
//		DebugTool.setCharSet();
//		new TimeSynTask();
		
	
		
//		int cnt=0;
//		DatabaseRepairTask rd = new DatabaseRepairTask();
//		Thread t = new Thread(rd);
//		t.start();
//		while (t.isAlive() && cnt<=180)
//				try {
//					cnt++;
//					Thread.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				if (cnt>180) {
//					try {
//						t.interrupt( );
//						t.join();
//					} catch (InterruptedException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//					}
//				} 
//			System.out.println("Again !!!!!!!!!!!11");

	}

}
 