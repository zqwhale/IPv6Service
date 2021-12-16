package mqeye.service.routine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;
 
public class TimeSynTask implements Runnable{
		
	private String cmd = "ntpdate " ;
	private final String SERVER_FIND = "time server";
	//private final String SERVER_UNFIND = "no server suitable";
	
	public TimeSynTask(){
		int cnt=0;
		Thread t = new Thread(this);
		t.start();
		while (t.isAlive() && cnt<=5)
				try {
					cnt++;
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (cnt>5) {
				try {
					t.interrupt( );
					t.join();
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
				}
			} 
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		boolean tsfind = false;
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		   try { 
			   	Process p = null;
			   		String timeServer = BaseCommonFunc.getProperty("timeServer");
			   		p = r.exec(cmd + timeServer );  
			   		if (p != null && !tsfind) {   
		   						error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
		   						String line = null;   
		   						while ((line = error.readLine()) != null) {
					    	   			DebugTool.printErr(line);
					       			}
		   						reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
		   						while ((line = reader.readLine()) != null) {   
		   									DebugTool.showConsole(line);
		   									tsfind = StringUtils.contains(line,SERVER_FIND);
		            				}
		   						p.waitFor();
		   				
			   		}
	   } catch (IOException ex) {   
		   	
					DebugTool.printErr("Time Syn Error!");
					DebugTool.printExc(ex);
	   } catch (InterruptedException ex) {
		// TODO Auto-generated catch block
		   	DebugTool.printErr("Wait For Error!");
				DebugTool.printExc(ex);
	   } finally {   
				   try {   
					   error.close();
					   reader.close();   
				   } catch (IOException e) {
					   DebugTool.printErr("Time Syn Runtime InputStream Error!");
					   DebugTool.printExc(e); 	}   
	   	 }   

	}
}
