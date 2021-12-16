package mqeye.comm.serial.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;


public class SocketSendTool {
	
    
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
						System.out.println("Socket Port is openning!");
				}
			}catch (Exception e) {
				isConn = false ;
				System.err.println("Socket Port is closed!");
			}finally{
				 try {
					 if (s!=null) s.close();
					// Thread.sleep(500);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
		return isConn ;
	}
	
	
	
	public byte[] sendCmd(String ip , int port , byte[] cmd , int validlen,int delay)
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
						
						outputStream.write(cmd);
						outputStream.flush();
						
						try {
							Thread.sleep(delay);
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
						}while(numBytes<=validlen && cnt<=10);	
						result = new byte[numBytes];
						System.arraycopy( readBuffer, 0, result, 0, numBytes );
						
					outputStream.close();
				   inputStream.close();
				}
				
			}catch (UnknownHostException e) {
				System.err.println("TCPSocketTool:Remote IP :" + ip + " Unknow error!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.err.println("TCPSocketTool:Socket Client Build error !|| " +
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
	

}
