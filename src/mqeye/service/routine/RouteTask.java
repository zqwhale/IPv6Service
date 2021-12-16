package mqeye.service.routine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.tools.DebugTool;

public class RouteTask implements Runnable{
	private String cmd = "sh " + Constant.ROUTE_INIT_FILE;
	
	public RouteTask(){
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

	
//	/* *
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//			new RouteExecutor();
//	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
			Runtime r = Runtime.getRuntime();   
		   BufferedReader reader = null;
		   BufferedReader error = null;
		   try { 
		   		Process p = r.exec(cmd);  
		   		if (p != null) {   
		   						p.waitFor();
		   						error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
		   						String line = null;   
					       while ((line = error.readLine()) != null) {
					    	   			DebugTool.printErr(line);
					       				}
		   						reader = new BufferedReader(new InputStreamReader(p.getInputStream()));   
		            while ((line = reader.readLine()) != null) {   
		            			DebugTool.showConsole(line);
		            					}
		            
		   		}
		   		
		   
	   } catch (IOException ex) {   
		   	
					DebugTool.printErr("Route Init Error!");
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
					   DebugTool.printErr("Route Runtime InputStream Error!");
					   DebugTool.printExc(e); 	}   
	   	 }   
}
		   	
	

}
