package mqeye.service.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import mqeye.service.Constant;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;


public class SerialPortTool implements Observer{
	SerialPortReader sr = null;
	byte[] result = null;
	int flag = 0;		
	
	Map<String, String> params = new HashMap<String, String>();
	
	public void initParams(Map<String, String> params){
		this.params = params ;
	}

	public void initParams(String paramStr){
		for(String valuePair: StringUtils.split(paramStr,",")){
			if (StringUtils.contains(valuePair , "="))
			{
				String[] datas = StringUtils.split(valuePair,"=");
				if (datas.length >= 2)
				{
					String key =  StringUtils.split(valuePair,"=")[0].trim();
					String valueStr =  StringUtils.split(valuePair,"=")[1].trim();
				
					if (StringUtils.equals(key, SerialPortParam.PARAMS_PORT) || 
						StringUtils.equals(key, SerialPortParam.PARAMS_RATE) ||
						StringUtils.equals(key, SerialPortParam.PARAMS_DATABITS)	||
						StringUtils.equals(key, SerialPortParam.PARAMS_STOPBITS)||
						StringUtils.equals(key, SerialPortParam.PARAMS_DELAY)||
						StringUtils.equals(key, SerialPortParam.PARAMS_TIMEOUT)||
						StringUtils.equals(key, SerialPortParam.PARAMS_PARITY)||
						StringUtils.equals(key, SerialPortParam.PARAMS_FLOWCTL))
						
					{
						params.put(key, valueStr);
					}
				}
			}
		}
}
	
	public SerialPortTool(SerialPortReader sr){
		this.sr = sr;
	}
	
	public void removeLCK( ){
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec("sh " + Constant.UNLOCK_TTY_FILE);
		   	if (p!=null){
		   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
				String line = null;   
				while ((line = error.readLine()) != null) {
    	   			DebugTool.printErr(line);
       			}
				reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
				while ((line = reader.readLine()) != null) {   
						DebugTool.printMsg(line);
				}
				p.waitFor();
		   	}
		} catch (IOException ex) {   
			DebugTool.printErr("Remove lock file  Error!");
			DebugTool.printExc(ex);
		} catch (InterruptedException ex) {
			// TODO Auto-generated catch block
			DebugTool.printErr("Wait For remove Error!");
			DebugTool.printExc(ex);
		}
		finally {   
		   try {   
			   error.close();
			   reader.close();   
		   } catch (IOException e) {
			   DebugTool.printErr("Remove lock file Runtime InputStream Error!");
			   DebugTool.printExc(e); 	}  
		}  
		
	}
	public void openSerialPort(  ) 
    { 
		try {
					removeLCK( );
					sr.open(params);
					sr.addObserver(this);
		}catch(UnsatisfiedLinkError err){
						DebugTool.printErr("RXTXCommDriver Lib Error");
		}catch(Exception err){
			DebugTool.printErr("Unknow Exception Error");
			err.printStackTrace();
}
    }
	
	public void closeSerialPort(  ){
		try {
			if (sr!=null)
			{
				if (sr.inputStream!=null) 	sr.inputStream.close();
				if (sr.outputStream!=null) 	sr.outputStream.close();
				sr.close();
			}
			removeLCK( );
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		byte[] values = (byte[])arg ;
		if (values!=null && values.length > 0)
		{	
			result = values;
			flag = 1;
			DebugTool.printMsg("Serial Tool get " + result.length + " bits datas....");
		}
		
	}

	public synchronized void flush(  ){
		openSerialPort( );
		while(flag==1) { result=null;flag=0;}
		closeSerialPort();
	}
	public synchronized byte[] read( byte[]  hexcmd ){
		int cnt=0;
		byte[] r = null;
		//openSerialPort( );
		if (hexcmd != null && hexcmd.length >0 ) 
			sr.sendMessage(hexcmd);
		
		//wait result return
		while(flag!=1 && cnt<=5){
				cnt++;
				try {
					int timeout = 500; 
					String timeout_str = params.get(SerialPortParam.PARAMS_TIMEOUT);
					if (StringUtils.isNotEmpty(timeout_str))
						timeout = Integer.parseInt(timeout_str);
					
					Thread.sleep(timeout+10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		//wait result return
		if (flag==1) { r=result; result=null;  flag = 0; }
		if (cnt>10) { DebugTool.printErr("Unable Collect Data!"); flag = -1;}
		//closeSerialPort();
		
		return r;
	}
	
	public static void main ( String[] args )  {
	
		SerialPortReader sr = new SerialPortReader(args[1]);
		SerialPortTool tool = new SerialPortTool(sr);
		Map<String , String>  params = new HashMap<String , String>();
		params.put(SerialPortParam.PARAMS_PORT,args[1]);
		params.put(SerialPortParam.PARAMS_RATE,args[2]+"");
		params.put(SerialPortParam.PARAMS_DATABITS,8+"");
		params.put(SerialPortParam.PARAMS_STOPBITS,1 +"");
		params.put(SerialPortParam.PARAMS_PARITY,0+"");
		params.put(SerialPortParam.PARAMS_FLOWCTL,0+"");
		params.put(SerialPortParam.PARAMS_DELAY, 1000 +"");
		params.put(SerialPortParam.PARAMS_TIMEOUT,3000+"");
		tool.initParams(params);
		
		int span = 1000;
		if (args[4]!=null) span = Integer.parseInt(args[4]);
		while(true)
		{	
			tool.openSerialPort();
			String strcmd = null;
			byte[] hexcmd = null;
			byte[] result = null;
			if (args[3]!=null && args[3].equals("ASCII"))
			{
				strcmd = args[0]+"\r";
				hexcmd = strcmd.getBytes();
				result = tool.read(hexcmd);
				if (result!=null)
					System.out.println(new String(result));
			}
			else
			{	strcmd = args[0];
				hexcmd = HexUtils.convert(strcmd);
				result = tool.read(hexcmd);
				if (result!=null)
					System.out.println(HexUtils.convert(result));
			}
			try {
				Thread.sleep(span);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tool.closeSerialPort();
		}
		
		
		
   }
}

