package mqeye.service.tools;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.detect.PortResult;


class Conn implements Runnable
{
	String ip;
	String port ;
	public Conn(String ip , String port){
		this.ip = ip; this.port = port;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ip ,  port );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN)){
			System.out.println("--------------" + port + " is openned.");
		}
	}
}
public class PortConnect {
	
	/*PORT RESULT*/
	public  static final String PORT_CLOSED = "closed";
	public  static final String PORT_OPEN = "openning";
	public static final int PORT_TIMEOUT = 5000;
	
	public static void main(String[] args){
		String ip = "219.231.0.139";
		int start_port = 10;
		int end_port = 100000;
		
		for(int port = start_port ; port <=end_port ; port++)
		{
			Conn c = new Conn(ip,port+"");
			new Thread(c).start();
			
		}
	}
	public PortResult connect(String ip , String port ){
		PortResult pr = new PortResult();
		pr.setPort(port);
		long pre=System.currentTimeMillis();
		Socket s = null ;
		try {
			s = new Socket();
			SocketAddress add = new InetSocketAddress(ip,Integer.parseInt(port));
			s.connect(add,PortConnect.PORT_TIMEOUT);
			
			pr.setStatus(PortConnect.PORT_OPEN);
		} catch (SocketTimeoutException e) {
			DebugTool.printErr("Socket Time out EXCEPTION!");
			pr.setStatus(PortConnect.PORT_CLOSED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			DebugTool.printErr("IO port closed EXCEPTION!");
			pr.setStatus(PortConnect.PORT_CLOSED);
		}
		finally { 
				try {
					 if (s != null) s.close();
				} catch (IOException e) {
						DebugTool.printErr("PORT SOCKET EXCEPTION!");
						DebugTool.printExc(e);
						pr.setStatus(PortConnect.PORT_CLOSED);
				} 
		 }
		long post =System.currentTimeMillis();
		long timeConsum = post-pre;
		timeConsum = ( timeConsum < PortConnect.PORT_TIMEOUT? timeConsum : PortConnect.PORT_TIMEOUT);
		pr.setTimeConsum(timeConsum);
		return pr ;
	} 
}
