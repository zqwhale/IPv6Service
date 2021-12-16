package mqeye.service.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import mqeye.service.tools.DebugTool;

import org.apache.tomcat.util.buf.HexUtils;

public class TCPSocketTool {
	public final static int ERR = -1 ;
	public final static int PASS = 1 ;
	
	private TaskLock lock = null ;
	
	public TCPSocketTool(){
		
	}
	public TCPSocketTool(TaskLock lock){
		this.lock = lock;
	}
	public synchronized boolean isOpen(String ip , int port ){
		boolean isConn = false ;
		InetAddress addr;
		Socket s = null ;
		try {
			addr = InetAddress.getByName(ip);
			s = new Socket(addr, port);
				if (s !=null)
				{
						s.setKeepAlive(true);
						s.setSoTimeout(500);
						//s.sendUrgentData(0xFF);
						isConn = true ;
						DebugTool.showConsole("Socket Port is openning!");
				}
			}catch (Exception e) {
				isConn = false ;
				DebugTool.printErr("Socket Port is closed!");
			}finally{
				 try {
					 if (s!=null) s.close();
					// Thread.sleep(500);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
//					catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
			}
		return isConn ;
	}
	public synchronized byte[] session(String ip, int port , CmdBean cmd){
		
		byte[] result = null ; 
		if (!lock.islock())
		{	lock.lock();
				result = sendCmd(ip, port, cmd);
				lock.unlock();
		}
		return result ;
	}
	
	private byte[] sendCmd(String ip , int port , CmdBean cmd)
	{
		byte[] readBuffer = new byte[1024];
		byte[] result = null ; int numBytes = 0 ;
		InetAddress addr;
		Socket socket = null ;
		try {
			addr = InetAddress.getByName(ip);
			socket = new Socket(addr, port);
			socket.setKeepAlive(true);
			socket.setSoTimeout(5000);
			
			if (socket!=null )
			{			OutputStream outputStream = socket.getOutputStream();
						outputStream.write(cmd.getHexcmd());
						outputStream.flush();
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						InputStream inputStream = socket.getInputStream();
						int cnt = 0;
						do{
							while (inputStream.available() > 0) {
									numBytes = inputStream.read(readBuffer);
							}
							cnt++;
							try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}while(numBytes<=cmd.getValidLen() && cnt<=5);	
						result = new byte[numBytes];
						System.arraycopy( readBuffer, 0, result, 0, numBytes );
						
						
						
					outputStream.close();
				   inputStream.close();
				}
				
			}catch (UnknownHostException e) {
				DebugTool.printErr("TCPSocketTool:Remote IP :" + ip + " Unknow error!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				DebugTool.printErr("TCPSocketTool:Socket Client Build error !|| " +
						"TCPSocketTool:OutputStream/InputStream Send cmd error!");
			}finally{
				 try {
					 if (socket!=null) socket.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		
		
		return result ;
	}
	
	
	public static void main(String[] args) {
		
		String cmds2[] ={	 "0301000000083C2E", "0301000000083C2E", "0301000000083C2E", "0301000000083C2E"};
		Temp t1 = new Temp("192.168.254.2",4196,cmds2);
		
		try{
			new Thread(t1).start(); Thread.sleep(100);
		

		}catch(InterruptedException e){
			
		}
	}
}



class Temp implements Runnable{
	private String ip ;
	int port ;
	String[] cmds ;
	
	public Temp(String ip , int port ,String[] cmds){
		this.ip = ip ;
		this.port = port ;
		this.cmds = cmds ;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		String key = ip + ":" + port;
		ShareObject share = ShareObject.getInstance();
		TaskLock lock = share.getLock(key);
		TCPSocketTool t = new TCPSocketTool(lock);
		while( t.isOpen(ip,port) )
		{
			for(String cmdstr:cmds)
			{
				CmdBean cmd = new CmdBean(cmdstr , HexUtils.convert(cmdstr) , 5);
				byte[] result = null;
				int cnt=0;			
				do{
					//System.out.println(name + " wait for 1 seconds " );
					try {
					Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					cnt++;
					//System.out.println(lock.islock());
				}while(lock.islock()&& cnt<100);
				
				cnt = 0;
				do{
					result = t.session(ip, port, cmd);
					if (result !=null && result.length > cmd.getValidLen() )
						break;
					try {
						Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
					}
					cnt++;
				}while( cnt<10);
				
				if (result!=null && result.length > 0)
				{
					String mt=  HexUtils.convert(result);
					System.out.println("mt:" + mt + ":result size:" + result.length);

				}
				else
				{
					System.out.println(": null ");
				}


				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}	
	
		}	
	}
	
}


