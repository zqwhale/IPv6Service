package mqeye.sys.execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public final class CommandExecutor {
	   public static int execute(ICommand cmd){
			int flag = -1;
			Runtime r = Runtime.getRuntime();   
	       BufferedReader in = null;  
	       try {   
	      	 	System.out.println(cmd.command());
	      	 	Process p = r.exec(cmd.command());
	      	 	long timeout = cmd.getTimeOut() ;
	      	 	if (timeout > 0)  Thread.sleep(timeout);
	          if (p != null) {
		            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   
		            flag = cmd.result(in);
	          }   
	          
	       }catch (Exception ex) {   
	          ex.printStackTrace();   
	          
	       }finally {   
	          try {   
	              in.close();   
	          } catch (IOException e) {   
	              e.printStackTrace();   
	          }   
	       }   
			return flag;
		 }

}
