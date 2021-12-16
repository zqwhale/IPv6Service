package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

public class LocalDBController {
	
	  public static boolean isReady(){
	  		boolean flag = false ;
  		String testCmd = "service mysql status";
  		Runtime r = Runtime.getRuntime();   
  		BufferedReader in = null;  
  		try {   
              Process p = r.exec(testCmd);
              if (p != null) {   
	    	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	            String line = null;   
	    	            while ((line = in.readLine()) != null) {   
	    	                if (StringUtils.contains(line, "is not") || StringUtils.contains(line, "失败")	)  
	    	                								flag = false;
	    	                	else
	    	                								flag = true ;
	    	                						}   
  	            							}
  		}catch(IOException e){
  					DebugTool.printErr("Test DB Status Execute fail!");
  					DebugTool.printExc(e);
  			}
  		
  		
  		return flag ;
  }
	  
	public static boolean startDB( ){
		boolean flag = false;
		String startCmd = "service mysql start";
  		Runtime r = Runtime.getRuntime();   
  		BufferedReader in = null;  
  		try {   
              Process p = r.exec(startCmd);
              if (p != null) {   
	    	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	            String line = null;   
	    	            while ((line = in.readLine()) != null) {   
	    	                if (StringUtils.contains(line, "Starting") || StringUtils.contains(line, "确定")	)  
	    	                										flag = true;
	    	                						}   
  	            							}
  		}catch(IOException e){
  					DebugTool.printErr("Start DB Execute fail!");
  					DebugTool.printExc(e);
  			}
  		return flag ;
	}
	
	public static boolean stopDB( ){
		boolean flag = false;
		String stopCmd = "service mysql stop";
  		Runtime r = Runtime.getRuntime();   
  		BufferedReader in = null;  
  		try {   
              Process p = r.exec(stopCmd);
              if (p != null) {   
	    	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));
	    	            String line = null;   
	    	            while ((line = in.readLine()) != null) {   
	    	                if (StringUtils.contains(line, "Shutting") || StringUtils.contains(line, "确定")	)  
	    	                										flag = true;
	    	                						}   
  	            							}
  		}catch(IOException e){
  					DebugTool.printErr("Stop DB Execute fail!");
  					DebugTool.printExc(e);
  			}
  		return flag ;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
				stopDB();
				System.out.println(isReady());
				startDB();
				System.out.println(isReady());
	}

}
