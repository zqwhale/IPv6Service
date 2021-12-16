package mqeye.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.DsviewDAO;
import mqeye.service.routine.DatabaseRepairTask;
import mqeye.service.routine.RouteTask;
import mqeye.service.routine.TimeSynTask;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;
import mqeye.service.tools.LocalDBController;
import mqeye.service.tools.SelfCureTool;
import mqeye.service.tools.UnicodeConverter;



import org.apache.commons.lang.StringUtils;
  
public class MQeyeServer   
{  
    private ServerSocket ss;  
    private Socket socket;  
    private BufferedReader in;  
    private PrintWriter out;  
    private MQeyeExecutor exe = MQeyeExecutor.getIntance();
   
    
    private String parser(String cmd){
    	String result = null ;
    	cmd = StringUtils.trim(cmd);
    	if (StringUtils.isBlank(cmd)){
    		DebugTool.printErr("NULL COMMAND String!");
    	}else if (StringUtils.startsWithIgnoreCase(cmd,ICMD.START)){
    		if (StringUtils.equalsIgnoreCase(cmd,ICMD.START)) 
    			result =  exe.start();
    		else{
    			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.START));
    			result = exe.start(dcode);
    		}
    			
    	}else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.STOP)){
    		if (StringUtils.equalsIgnoreCase(cmd,ICMD.STOP)) 
    			result = exe.stop();
    		else{
    			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.STOP));
    			result = exe.stop(dcode);
    		}
    	}else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.PUSH)){
    			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.PUSH));
    			result = exe.pushVideo(dcode);
    	}
    	else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.DISCON)){
				String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.DISCON));
				result = exe.disconVideo(dcode);
    	}
    	else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.ISLIVE)){
			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.ISLIVE));
			result = exe.isLiveVideo(dcode);
			System.out.println(result);
    	}
    	else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.RESTART)){
    		if (StringUtils.equalsIgnoreCase(cmd,ICMD.RESTART)) 
    			result = exe.restart();
    		else{
    			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.RESTART));
    			result = exe.restart(dcode);
    		}
    		
    	}else if(StringUtils.startsWithIgnoreCase(cmd,ICMD.REFRESH)){
    		if (StringUtils.equalsIgnoreCase(cmd,ICMD.REFRESH)) 
    			;	
    		else{
    			String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.REFRESH));
    			result = exe.refresh(dcode);
    		}
    	}else if (StringUtils.equalsIgnoreCase(cmd,ICMD.TEST)){
    				result = "TESTOK";
    				DebugTool.printMsg("SERVER IS RUNNING!");
    }else if (StringUtils.startsWithIgnoreCase(cmd,ICMD.QUERY)){
    				String dcode = StringUtils.trim(StringUtils.removeStartIgnoreCase(cmd, ICMD.QUERY));
    				result = (exe.isRunning(dcode)? "RUN":"STOP");
		}else if (StringUtils.startsWithIgnoreCase(cmd,ICMD.BYE)){
			result = "";
			result = result + "|" + exe.stopAllSupport() ;
			
			result = result + "|" + exe.stop() ;
			
		}
    	else{
    		DebugTool.printErr("ERROR COMMAND!");
    		result = "ERROR COMMAND!";
    	}
    	return result ;
    }
  
 private void resetService(){
	  DeviceDAO ddao = new DeviceDAO();
	  ddao.changeAllState(0);
	  DsviewDAO dvdao = new DsviewDAO();
	  dvdao.resetRunStop(0);
  }
  public MQeyeServer()   
    {  
	  String line = "first running!";
		 long start = System.currentTimeMillis();

   try   
   		{  				
	   					DebugTool.showConsole("The server is waiting your input...");
	   					ss = new ServerSocket(7610);  
             
            while(!line.equals("bye")){  
            			if (line.equals("first running!")){
            								resetService(); line = "";
            								if (BaseCommonFunc.isPropertyValid("autoStart", "yes"))
            										parser("start");	//自动启动各种监控服务
            					}
            		 try{
	                socket = ss.accept();  
	                if (socket !=null){
	                	
	                					/* 控制连接的IP */
	                						
					                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));  
					                out = new PrintWriter(socket.getOutputStream(), true );  
						                
						                String cmd = in.readLine();
						                String result = ICMD.EMPTY ; 
						                if (!(cmd==null)){ 
						                		DebugTool.showConsole("Service receive a Client's Command: " + cmd);
						                	  if (StringUtils.equalsIgnoreCase(cmd,ICMD.SSH))
						                	  {
						                		  DebugTool.showConsole("ssh open operate.........");
						                		  SelfCureTool.sshOpen();
									             in.close();    out.close();   socket.close(); 
						                		  continue;
						                	  } else  
						                		  /* 控制连接的IP */	
						                		  if (!socket.getInetAddress().toString().equals("/127.0.0.1")) {	
				                						DebugTool.showConsole("One Untrust Client want Connect!!"); 
				                						in.close();    out.close();   socket.close();   continue; }
						                	 
						                	  if (StringUtils.equalsIgnoreCase(cmd,ICMD.BYE))
							                	{	 cmd = ICMD.BYE; line = "bye"; parser(cmd);
							                		resetService(); result = "SHUTDOWN OK";
							                	}
							                else
					                	   {	result = parser(cmd);}
					                	   result = UnicodeConverter.toEncodedUnicode(result,true);
					                	   Thread.sleep(100);
					                	   if (StringUtils.isEmpty(result)) {  result = ICMD.EMPTY ;}
							                	out.write(result);  
							   	 		 		out.flush(); 
						                		}
						                out.close();  
						                in.close();  
						                socket.close();  
	                							}
                	}catch(Exception e){
                				DebugTool.printErr("Error.Socket");
                				DebugTool.printExc(e);
                							}
            						}  
              
            	ss.close();  
            						
   } catch (Exception e) {  
	   		DebugTool.printErr("Error.Server");
	   		DebugTool.printExc(e);
   } finally{
	   			long end = System.currentTimeMillis();
	   			double duration = ( end - start)/(1000.0 * 60 * 60 );
	   			DebugTool.showConsole("Monitor Service has running :" + duration + "h" );
	   			exe.shutdown();
        }
    }  

    
   
  public static void main(String[] args)   
    {  
	  	long start = System.currentTimeMillis();
	  	long span = 0;
	  	
	  		DebugTool.init();
	  		DebugTool.setErrLogFile(false);
	  		DebugTool.showConsole("Waiting database run..."); 
	  		
	  		try {
		      while(!LocalDBController.isReady() && span < 60*1000)
		      					{
		      				Thread.sleep(2000);
		      				span = System.currentTimeMillis() - start;
		      					}
		      
		      if (!LocalDBController.isReady()){
		      				DebugTool.showConsole("Manual start database ..."); 
		      				LocalDBController.startDB();
		      				Thread.sleep(1000);
		      					}
		      
		      if (LocalDBController.isReady()){
		  		if (BaseCommonFunc.isPropertyValid("isAdjTime", "yes"))
		  		{	    	DebugTool.showConsole("Adjust service time ..");
		  					new TimeSynTask();
		  		}
		  		
		  		if (BaseCommonFunc.isPropertyValid("isRoute", "yes"))
		  		{ 		DebugTool.showConsole("Init route config.."); 
  	  					new RouteTask();
		  		}
		    	
		    	if (BaseCommonFunc.isPropertyValid("isRepair", "yes"))
		    	{	DebugTool.showConsole("Check and repair Database.."); 
		    		new DatabaseRepairTask();
		    	}
		    	
		    	DebugTool.showConsole("Start MQeyeServer..."); 
		    	  			new MQeyeServer(); 
		    	  			
		    	  					
		      }else{
		      			DebugTool.printErr("database is not ready,Please check database is not damage!"); 
		      					}
	  		}catch(Exception err){
	  						DebugTool.printExc(err);
	  			}
      
	  		
    }  
}  
