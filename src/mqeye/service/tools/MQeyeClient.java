package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import mqeye.service.Constant;
import mqeye.service.ICMD;

import org.apache.commons.lang.StringUtils;

public class MQeyeClient {
	
	
	private final static String TURNON = "turnon";
	private final static String START = "start";
	private final static String STOP = "stop";
	private final static String RESTART = "restart";
	private final static String BYE = "bye";
	private final static String TEST = "test";
	private final static String HELP = "help";
	private final static String QUERY = "query";
	private final static String REFRESH = "refresh";
	

	private boolean checkCommand(String cmd){
			boolean find = false;
			for (String c:ICMD.COMMADS){
					if (  find = StringUtils.equalsIgnoreCase(cmd, c)) break ;
			}
			return find ;
	}
	
	
	
	private String exec(String cmd){
		String result = ICMD.EMPTY ;
		long start = System.currentTimeMillis();
		try{
		      Socket socket=new Socket("127.0.0.1",7610);
		      PrintWriter os=new PrintWriter(socket.getOutputStream());
		      BufferedReader is=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		      if(StringUtils.isNotEmpty(cmd) ){
		          os.println(cmd);
		          os.flush();
		      			
		          result = is.readLine();
		          result = UnicodeConverter.fromEncodedUnicode(result.toCharArray(), 0, result.length());
		          System.out.println("Server return:"+ result);
		      			}
		      os.close();
		      is.close();
		      socket.close();
		    }catch (UnknownHostException e) {
				// TODO Auto-generated catch block
		    	System.out.println("UnKnownHost Error"+e); 
		    	}
					catch(IOException e) {
		      System.out.println("Client Error"+e); 
		    	} finally{
			   			long end = System.currentTimeMillis();
			   			long duration = ( end - start);
			   			System.out.println("Client has running :" + duration + "ms" );
		    	 	}
		    	return result ;
	}
	
	public String start(){
					return this.exec(START);
	}
	
	public String start(String dcode){
				return this.exec(START + " " + dcode);
	}
	
	public String stop(){
				return this.exec(STOP);
	}

	public String stop(String dcode){
			return this.exec(STOP + " " + dcode);
	}
	
	public String restart(){
			return this.exec(RESTART);
	}

	public String restart(String dcode){
		return this.exec(RESTART + " " + dcode);
	}

	public String query(String dcode){
		return this.exec(QUERY + " " + dcode);
	}
	public String refresh(String dcode){
		return this.exec(REFRESH + " " + dcode);
	}

	public String test(){
		return this.exec(TEST);
	}
	public String turnoff( ){
		return this.exec(BYE);
	}
	
	
	private void showHelp(){
		System.out.println("help 		------------- 查看帮助");
		System.out.println("turnon		------------- 启动程序");
		System.out.println("bye		------------- 停止程序");
		System.out.println("start		------------- 启动所有监控进程");
		System.out.println("start ### 	------------- 启动###监控进程，例如 start JHJ000001");
		System.out.println("stop		------------- 停止所有监控进程");
		System.out.println("stop ### 	------------- 停止###监控进程，例如 stop JHJ000001");
		System.out.println("restart		------------- 重启所有监控进程");
		System.out.println("restart ### 	------------- 重启###监控进程，例如 restart JHJ000001");	
		System.out.println("query ### ------------- 查询###监控进程的运行状态");
	}
	
	public String turnOn(){
		String flag = Constant.TURNON_FAIL ;
		String cmdOfTurnOn = "java -jar /root/workspace/MQeyeService/MQeyeServer.jar > MQeyeServer.log ";
		Runtime r = Runtime.getRuntime();   
		BufferedReader in = null;
		try {   
		          Process p = r.exec(cmdOfTurnOn);
		          if (p != null) {   
			            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   
			            String line = null;   
			            while ((line = in.readLine()) != null){
			            			System.out.println(line);
			            			if (StringUtils.contains(line, "The server is waiting your input")){
			            								flag = Constant.TURNON_SUCCESS;break;}
			            						}
			            
			            			}
		}catch (Exception ex) {   
	            ex.printStackTrace();   
	  } finally {   
	            try {   
	                in.close();   
	            } catch (IOException e) { e.printStackTrace();   }   
			
		}
	  return flag ;
	}
	
	public static void main(String[] args)   
	  {  	  		
		MQeyeClient q = new MQeyeClient();		;
		
		
	  String cmd = "help";									
	  String param = null ;							
		  	
			if (args!=null && args.length>0){
					cmd = args[0];  
					if (args.length >=2)  param = args[1]; 
					if (q.checkCommand(cmd)) {
						if (StringUtils.equalsIgnoreCase(cmd, HELP)){
										q.showHelp();
						}
						if (StringUtils.equalsIgnoreCase(cmd, TURNON)){
										System.out.println(q.turnOn());
							}
							if (StringUtils.equalsIgnoreCase(cmd, BYE)){
									System.out.println(q.turnoff());
							}
							if (StringUtils.equalsIgnoreCase(cmd, TEST)){
								System.out.println(q.test());
							}
							if (StringUtils.equalsIgnoreCase(cmd, START)&& param==null){
								System.out.println(q.start());
							}
							if (StringUtils.equalsIgnoreCase(cmd, START)&& param!=null){
								System.out.println(q.start(param));
							}
							if (StringUtils.equalsIgnoreCase(cmd, STOP)&& param==null){
								System.out.println(q.stop());
							}
							if (StringUtils.equalsIgnoreCase(cmd, STOP)&& param!=null){
								System.out.println(q.stop(param));
							}
							if (StringUtils.equalsIgnoreCase(cmd, RESTART)&& param==null){
								System.out.println(q.restart());
							}
							if (StringUtils.equalsIgnoreCase(cmd, RESTART)&& param!=null){
								System.out.println(q.restart(param));
							}
							if (StringUtils.equalsIgnoreCase(cmd, QUERY)&& param!=null){
								System.out.println(q.query(param));
							}							
							if (StringUtils.equalsIgnoreCase(cmd, REFRESH)&& param!=null){
								System.out.println(q.refresh(param));
							}							

					}else{ 	q.showHelp();	}  /* Error command String */
			}else{ 		 q.showHelp();	}	/* Without parameters */
	  	}
}
