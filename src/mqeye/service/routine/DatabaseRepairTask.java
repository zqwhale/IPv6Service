package mqeye.service.routine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.EncryDecryUtil;
import mqeye.service.tools.PropertiesUtil;

public class DatabaseRepairTask implements Runnable{
			private String cmd = "sh " + Constant.REPAIR_DB_FILE;
			public final static String BEGIN = "begin";
			public final static String RUNNING = "running";
			public final static String FAIL = "fail";
			public final static String END = "end";
			private String flag = BEGIN;
			
			private final static String PASSWD_WARNING = "Warning: Using a password on the command line interface can be insecure.";
			//±ê¼Ç
			public String getFlag() {
				return flag;
			}
			
//			/* *
//			 * @param args
//			 */
			public static void main(String[] args) {
				// TODO Auto-generated method stub
					new DatabaseRepairTask();
			}
			
			public DatabaseRepairTask(){
				  int cnt=0;
					Thread t = new Thread(this);
					t.start();
					while (t.isAlive() && cnt<=180)
							try {
								cnt++;
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						if (cnt>180) {
							try {
								t.interrupt( );
								t.join();
							} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							}
						} 

			    }
			
			public void run() {
				// TODO Auto-generated method stub
				   Runtime r = Runtime.getRuntime();   
				   BufferedReader reader = null;
				   BufferedReader error = null;
				   PropertiesUtil util = new  PropertiesUtil( Constant.MQEYE_DB_FILE );
				   String dbname 		= "mqeye";
				   String user  		= util.getProperty(dbname + ".user");
				   String password 	= util.getProperty(dbname + ".password");
				   password = EncryDecryUtil.MyDecry(password);
				   cmd = cmd + " " + user  + " " + password  + " " + dbname ; 
				   try { 
					   		Process p = r.exec(cmd);   flag = RUNNING;
					   		if (p != null) {   
					   						p.waitFor();
					   						error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
					   						String line = null;   
								       while ((line = error.readLine()) != null) {
								    	   						flag = FAIL;
								    	   						if (!StringUtils.contains(line,PASSWD_WARNING)) 	DebugTool.printErr(line);
								    	   
								       				}
					   						reader = new BufferedReader(new InputStreamReader(p.getInputStream()));   
					            while ((line = reader.readLine()) != null) {   
					            					 flag = RUNNING;  DebugTool.showConsole(line);
					            					}
					            
					   		}else{
					   												flag = FAIL; 		DebugTool.printErr("RepairDatabase Runtime Error!");
					   				}
					   		
					   
				   } catch (IOException ex) {   
					   	flag = FAIL;
	 							DebugTool.printErr("RepairDatabase Runtime Error!");
	 							DebugTool.printExc(ex);
				   } catch (InterruptedException ex) {
					// TODO Auto-generated catch block
					   flag = FAIL;
							DebugTool.printErr("WaitFor Error");
							DebugTool.printExc(ex);
				} finally {   
							   try {   
								   error.close();
		            reader.close();   
		            flag = END;
		            					
							   } catch (IOException e) {
								   flag = FAIL;
								   DebugTool.printErr("RepairDatabase Runtime InputStream Error!");
								   DebugTool.printExc(e); 	}   
				   	 }   
			}
}
