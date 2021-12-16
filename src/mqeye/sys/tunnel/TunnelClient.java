package mqeye.sys.tunnel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import mqeye.service.tools.UnicodeConverter;
import mqeye.sys.execute.AbstractCommand;

public class TunnelClient {
	public static String isOpen(String remote , String mapPort)
	{
		String cmd = "isOpen " + mapPort ;
		return exec(remote,cmd); 
	}
	
	private static String exec(String remote , String cmd){
		String result = AbstractCommand.EMPTY ;
		long start = System.currentTimeMillis();
		try{
		      Socket socket=new Socket(remote,7611);
		      PrintWriter os=new PrintWriter(socket.getOutputStream());
		      BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      if( !cmd.isEmpty() ){
		          os.println(cmd);
		          os.flush();
		      			
		          result = is.readLine();
		          result = UnicodeConverter.fromEncodedUnicode(result.toCharArray(), 0, result.length());
		          System.out.println("Server return:"+ result);
		      			}
		      os.close();
		      is.close();
		      socket.close();
		    }catch(Exception e) {
		      System.out.println("Client Error"+e); 
		    	} finally{
			   			long end = System.currentTimeMillis();
			   			long duration = ( end - start);
			   			System.out.println("Client has running :" + duration + "ms" );
		    	 	}
		    	return result ;
	}
}
