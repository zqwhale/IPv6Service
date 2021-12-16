package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mqeye.service.Constant;

import org.apache.commons.lang.StringUtils;

public class SelfCureTool {
	/*Detect MQeyeServer is running, if not will restart!*/
	private final static String statusCmd = "service MQeyeServer status";
	private final static String startCmd = "service MQeyeServer start";
	private final static String stopCmd = "service MQeyeServer stop";
	
	private final static String iptablesStop = "service iptables stop";
	private final static String sshStart = "/opt/sbin/sshd";
	
	private final static String ISRUNNING = "is running";
	private final static String NOTRUNNING = "is not running";
	
	private final static String RUNSUCCESS1 = "running:"; /*have two msg ,indicate service is running success*/
	private final static String RUNSUCCESS2 = "PID:"; /*have two msg ,indicate service is running success*/
	
	private  final static String STOPSUCCESS = "was not running"; /*have this msg, indicate service is stop success*/
	
	private static int exec(String cmd , String success )
	{
		int flag = 0;
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec(cmd);
		   	if (p!=null){
		   		error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
							if (StringUtils.contains(line, success)) flag = 1;
				}
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Self Cure Stop Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Self Cure Stop Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		}  
		return flag ;
	}
	
	public static void sshOpen(){
		exec(iptablesStop, "iptables");
		exec(sshStart , "");
	}
	/* remove file  */
	public static void removePID( ){
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec("sh " + Constant.REMOVE_PID_FILE);
		   	if (p!=null){
		   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
						DebugTool.printMsg(line);
				}
				
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Self Cure Remove file Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Self Cure Remove file Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		}  
		
	}
	public static void bye(){
		MQeyeClient q = new MQeyeClient();
		q.turnoff();
	}
	
	/* 1: stop is success , 0: stop is fail */
	public static int stop(){
		int flag = 0;
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec(stopCmd);
		   	if (p!=null){
		   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
							if (StringUtils.contains(line, STOPSUCCESS)) flag = 1;
				}
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Self Cure Stop Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Self Cure Stop Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		}  
		return flag ;
	}
	
	/* 1: start is success , 0: start is fail */
	public static int start(){
		int flag = 0;
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec(startCmd);
		   	if (p!=null){
		   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
							if (StringUtils.contains(line, RUNSUCCESS1) && 
									StringUtils.contains(line, RUNSUCCESS2)) flag = 1;
				}
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Self Cure Start Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Self Cure Start Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		  
		}  
		return flag ;
	}
	
	/* 1:is Running , -1:is not Running , 0:unknown*/
	public static int getStatus(){
		int flag = 0;
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec(statusCmd);
		   	if (p!=null){
		   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
							if (StringUtils.contains(line, ISRUNNING)) flag = 1;
							if (StringUtils.contains(line, NOTRUNNING)) flag = -1;
				}
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Self Cure Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Self Cure Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		}  
		
		return flag ;
	}
}
