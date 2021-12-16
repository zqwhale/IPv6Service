package mqeye.service.serial;

import java.util.HashMap;
import java.util.Map;

import org.apache.tomcat.util.buf.HexUtils;




public class SerialPortServer implements Runnable {
	

	Map<String , SerialPort> serialPorts =  new HashMap<String ,SerialPort>();
	Map<String , SerialPortDetect> detects = new HashMap<String ,SerialPortDetect>();
	
	private static SerialPortServer instance = null ;
	private SerialPortServer(){
		init();
		new Thread(this).start();
	}
	
	public static SerialPortServer getInstance(){
		if (instance == null)
			instance = new SerialPortServer();
		return instance;
	}
		
	private void init()
	{
		int port_num = 4;
		SerialPort[] sps = new SerialPort[port_num];
		
		sps[0] = new SerialPort("ZLDEV01","172.31.1.201",4196);
//		sps[1] = new SerialPort("ZLDEV02","172.31.1.202",4196);
//		sps[2] = new SerialPort("ZLDEV03","172.31.1.203",4196);
//		sps[3] = new SerialPort("ZLDEV04","172.31.1.204",4196);
		
		
		for(int i=0;i<port_num;i++)
		{
			serialPorts.put(sps[i].getIpAdd()+":" + sps[i].getPort(), sps[i]);
			detects.put(sps[i].getIpAdd()+":" + sps[i].getPort(),new SerialPortDetect(sps[i]));
		}
	}
	
	public ResultBean sendCmd( String ip , int port , CmdBean cmd){
		SerialPortDetect d = detects.get(ip + ":" + port);
		ResultBean bean = d.getResult(cmd);
		return bean ;
	}  
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		while(true){
			for(SerialPortDetect d:detects.values()){
				
				d.detect();
				//System.out.println(d.sp.getSpName() + " cmd size:" + d.cmds.size() + " result size:" + d.results.size());
				for(ResultBean r:d.results.values())
				{
					if (r.getRtn()==0)
						System.out.println(r.getCmdStr() + " Result:" + new String(r.getResult()));
					else
						System.out.println(r.getCmdStr() + " Result:null");
				}
			}
			System.out.println("Detect over");
			try {
				Thread.sleep(1000*5);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SerialPortServer sps = SerialPortServer.getInstance();
		String cmd = "Q3GS\r\n";
		CmdBean cmd1 = new CmdBean( cmd , cmd.getBytes() , 0 );
		sps.sendCmd("172.31.1.201",4196, cmd1);

		
	}

	

}
