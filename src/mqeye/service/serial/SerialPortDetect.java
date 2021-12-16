package mqeye.service.serial;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mqeye.service.Constant;
import mqeye.service.detect.PingResult;
import mqeye.service.tools.PingTool;

import org.apache.tomcat.util.buf.HexUtils;


class SocketPool{
	private static SocketPool instance = null ;
	Map<String , Socket> spool = new HashMap<String , Socket>();
	
	public static SocketPool getInstance(){
		if (instance == null)
			instance = new SocketPool();
		return instance;
	}
	boolean isReachable( String ip)
	{
		PingTool tool = new PingTool();
		PingResult r = tool.ping(ip, 1, 1000);
		return (r.getFlag()==Constant.PING_SUCCESS);
	}
	
	public Socket getSocket(String ip , int port)
	{
		return spool.get(ip + ":" + port);
	}
	
	private Socket initSocket( String ip , int port )
	{	
		Socket socket = null;
		int state = -1;
		if (isReachable(ip))
		{
			try {
					socket = new Socket();
					socket.setReuseAddress(true);
//					SocketAddress localAddr = new InetSocketAddress("192.168.254.251",19123);
//					socket.bind(localAddr);
					SocketAddress remoteAddr = new InetSocketAddress(ip,port);
					socket.connect(remoteAddr); 
					socket.setKeepAlive(true);
					socket.setSoTimeout(5000);
					if (socket!=null ) 
					{	
						state = 0;
						System.out.println("Init Socket success....");
					}
				}catch (UnknownHostException e) {
					System.out.println("TCPSocketTool:Remote IP :" + ip + " Unknow error!");
					state = -1;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					System.out.println("TCPSocketTool:Socket Client Build error !|| " +
					"TCPSocketTool:OutputStream/InputStream Send cmd error!");
					state = -1;
			}
		}		
		if (state==-1)
		{
			try {
				 if (socket != null) socket.close();
			} catch (IOException e) {
				System.err.println("PORT SOCKET ClOSE EXCEPTION!");
				
			} 
			return null;
		}
		else
			return socket;
	}
	public Socket putSocket(String ip, int port)
	{
		String key = ip + ":" + port;
		Socket socket = initSocket(ip,port);
		
		spool.remove(key);
		spool.put(key,socket);
		return socket;
	}
	
	
}
public class SerialPortDetect  {
	SerialPort sp ;
	List<CmdBean> cmds = new ArrayList<CmdBean>();
	Map<String,ResultBean> results = new HashMap<String , ResultBean>();
	int cnt=0;int err=0;
	
	Socket socket = null ;
	OutputStream outputStream = null ;
	InputStream inputStream = null;

	public Socket getSocket(){
		return socket ;
	}
	public SerialPortDetect(SerialPort sp){
		this.sp = sp ;
		SocketPool pool = SocketPool.getInstance();
		socket = pool.getSocket(sp.getIpAdd(), sp.getPort());
		if (socket==null)
		{
			socket = pool.putSocket(sp.getIpAdd(), sp.getPort());
		}
	}
	
	public Socket initSocket(SerialPort sp){
		closeSocket(socket);
		SocketPool pool = SocketPool.getInstance();
		socket = pool.putSocket(sp.getIpAdd(), sp.getPort());
		return socket;
	}
	
	public boolean isOpen(  )
	{	boolean flag = false;
		try {
			socket.sendUrgentData(0xFF);
			flag = true;
			System.out.println("Socket is Open!");
		}catch (IOException e) {
			System.out.println("Socket is Closed!");
			flag = false;
		}
		return flag;
	}
	
	
	
	void closeSocket(Socket socket   ){
		try {
			 if (outputStream!=null) outputStream.close();
			 if (inputStream!=null) inputStream.close();
			 if (socket != null) socket.close();
		} catch (IOException e) {
			System.err.println("PORT SOCKET ClOSE EXCEPTION!");
			socket =null;
		} 
	}
	
	public ResultBean getResult(CmdBean cmd)
	{
		boolean find = false ;
		for(CmdBean c:cmds)
		{
			String cmdStr1 = cmd.getCmdStr();
			String cmdStr2 = c.getCmdStr();
			if (cmdStr1.equals(cmdStr2))
			{
				find = true ;
			}
		}
		
		ResultBean bean = null;
		if (find){
			bean = results.get(cmd.getCmdStr());
		}else{
			cmds.add(cmd);
			byte[] result = sendCmdTmp(socket , cmd);
			addResult(cmd,result);
		}
		return bean;
	}
	
	
	private ResultBean addResult(CmdBean cmd , byte[] result){
		ResultBean bean = null ;
		if (result!=null && result.length > cmd.getValidLen())
		{
			bean = new ResultBean();
			long timeStamp = System.currentTimeMillis();
			String cmdStr = cmd.getCmdStr() ; 
			bean.setCmdStr(cmdStr);
			bean.setRtn(0);
			bean.setResult(result);
			bean.setTimeStamp(timeStamp);
			results.remove(cmdStr);
			results.put(cmdStr, bean);
		}
		else
		{	err++;
			bean = new ResultBean();
			long timeStamp = System.currentTimeMillis();
			String cmdStr = cmd.getCmdStr() ; 
			bean.setCmdStr(cmdStr);
			bean.setRtn(-1); 
			bean.setResult(result);
			bean.setTimeStamp(timeStamp);
			results.remove(cmdStr);
			results.put(cmdStr, bean);
		}
		return bean ;
	}
	public void detect( ){
			for(CmdBean cmd:cmds)
			{
					try {
						Thread.sleep(cmd.t_detectspan1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					byte[] result = sendCmdTmp(socket, cmd);
					if (result ==null) {
						err++;
						System.out.println("Exec: " + cmd.getCmdStr() + " result:null" );
					}
					else if(result.length<=cmd.getValidLen())
					{	err++;
						System.out.println("Exec: " + cmd.getCmdStr() + " result:" + HexUtils.convert(result));
					}
					else
					System.out.println("Exec: " + cmd.getCmdStr() + " result:" + HexUtils.convert(result));

					
					try {
						Thread.sleep(cmd.t_detectspan2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
					addResult(cmd, result);
			}
				
	}
	
	
	
	public byte[] sendCmdTmp(Socket socket , CmdBean cmd)
	{	byte[] readBuffer = new byte[1024];
		byte[] result = null ; int numBytes = 0 ;
		try{
			outputStream = socket.getOutputStream();
			inputStream = socket.getInputStream();
			outputStream.write(cmd.getHexcmd());
			outputStream.flush();
			
			try {
				Thread.sleep(cmd.t_sendspan1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			int cnt = 0;
			do{
				
				while (inputStream.available() > 0) {
						numBytes = inputStream.read(readBuffer);
				}
				cnt++;
				
				try {
					Thread.sleep(cmd.t_sendspan2*10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(numBytes<=cmd.getValidLen() && cnt<=10);	
			
			result = new byte[numBytes];
			System.arraycopy( readBuffer, 0, result, 0, numBytes );
		

		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Error Cmd Send........");
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static void main(String[] args) {
		
		SerialPort sp = new SerialPort("ZLDEV01","172.31.1.201",4196);
		String cmdstr = "Q3GS\r\n";
		SerialPortDetect spd = new SerialPortDetect(sp);
		CmdBean cmd1 = new CmdBean( cmdstr , cmdstr.getBytes() ,10 );
		spd.cmds.add(cmd1);
	//	spd.getResult(cmd1);
		while(true)
		{
			if(spd.socket!=null)
			{
				for(CmdBean cmd:spd.cmds){
					try {
						Thread.sleep(cmd.t_detectspan1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					int cnt = 0;
					byte[] result = null ;
					do{
						result = spd.sendCmdTmp(spd.socket, cmd);
						try {
							Thread.sleep(cmd.t_detectspan1*10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						cnt++;
					}while(result ==null && result.length < cmd.getValidLen() && cnt < 100);
					
					if (result ==null) {
						spd.err++;
						System.out.println("Exec: " + cmd.getCmdStr() + " result:null" );
					}
					else if (result.length<cmd.getValidLen()) {
						spd.err++;
						System.out.println("Exec1: " + cmd.getCmdStr() + " result1:" +new String(result) );
					}
					else
						System.out.println("Exec2: " + cmd.getCmdStr() + " result2:" + new String(result));
					try {
						Thread.sleep(cmd.t_detectspan2);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				
			}else{
				
				System.out.println("Socket is init again ---- ");
			}
		}
		
	}


}
