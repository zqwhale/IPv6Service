package mqeye.service.tools;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import mqeye.service.Constant;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.ProtocolCode;
import mqeye.service.serial.SerialPortParam;
import mqeye.service.serial.SerialPortReader;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

public class SerialModbusAsciiUtil implements Observer{
	public final static String default_paramStr = "rate=2400,data bits=8,stop bits=1,delay read=1000,timeout=3000,parity=0,flow control=0,check=none,pattern=ASCII,endchar=<cr>";
	SerialPortReader sr=new SerialPortReader(""); 
	AbstractSensorResult r = null;
	int flag = 0;		/* 0: 没有数据存入 | 1:有数据存入  | -1:超过默认周期，数据未刷新成功 */
	
	HashMap<String, Comparable> params = new HashMap<String, Comparable>();
	HashMap<String, Comparable> confs = new HashMap<String, Comparable>();
	String paramStr = default_paramStr ;
	
	public String getParamStr() {
		return paramStr;
	}

	public void setParamStr(String paramStr) {
		this.paramStr = paramStr;
	}

	public SerialModbusAsciiUtil( ){
		this.paramStr =  default_paramStr ;
	} 

	public SerialModbusAsciiUtil(String paramStr){
		this.paramStr = paramStr;
	} 
	
	public void initSerialParam(String paramStr)
	{
		if (paramStr !=null && paramStr.length() > 0)
		{
			for(String valuePair: StringUtils.split(paramStr,",")){
				if (StringUtils.contains(valuePair , "="))
				{
					
					String key =  StringUtils.split(valuePair,"=")[0].trim();
					String valueStr =  StringUtils.split(valuePair,"=")[1].trim();
					
					
					
					if (StringUtils.equals(key, ProtocolCode.PARAMS_CHECK) || 
						StringUtils.equals(key, ProtocolCode.PARAMS_PATTERN) ||
						StringUtils.equals(key, ProtocolCode.PARAMS_ENDCHAR)	)
					{
						confs.put(key, valueStr);
					}
					else if (StringUtils.equals(key, SerialPortParam.PARAMS_PARITY) || 
						StringUtils.equals(key, SerialPortParam.PARAMS_TIMEOUT)|| 
						StringUtils.equals(key, SerialPortParam.PARAMS_DELAY))
					{
						int valueNum = Integer.parseInt(valueStr);
						params.put(key, valueNum);
					}
					else
					{
						params.put(key, valueStr);
					}
					
				}
			}
			
		}
		  
		
	}

	/**
     * 打开串口
     * @param String message
     */
	public void openSerialPort( String port , String rate) 
    { 
		  initSerialParam(paramStr);
        params.put( SerialPortParam.PARAMS_PORT, port ); // 端口名称
        params.put( SerialPortParam.PARAMS_RATE, rate ); // 波特率
        
          
        try {
							sr.open(params);
							sr.addObserver(this);
							
			}catch(UnsatisfiedLinkError err){
						DebugTool.printErr("RXTXCommDriver Lib Error");
			}
			catch(Exception err){
				DebugTool.printErr("NoSuchPortException Error");
				err.printStackTrace();
			}		
    }
	
	public void closeSerialPort(){
		try {
			if (sr!=null)
			{
				if (sr.inputStream!=null) 	sr.inputStream.close();
				if (sr.outputStream!=null) 	sr.outputStream.close();
				sr.close();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void update(Observable o, Object arg) {
		byte[] values = (byte[])arg ;
		System.out.println("ASCII-----" + new String(values));
		if (values!=null && values.length > 0)
		{	
			AbstractSensorResult asr = new AbstractSensorResult();
			String shortAddr = "unknow";
			asr.setShortAddress(shortAddr);
			asr.setResult(values);
	   	r = asr ;
			flag = 1;
		}
		
	}
	
	//该方法用来获得校验帧
	private String getCheckFrame(byte[] datas) throws Exception 
	{
		String checkType =  (String)confs.get(ProtocolCode.PARAMS_CHECK);
		
		String cframe = "";
		if(StringUtils.equals(checkType,ProtocolCode.NONE)){
			cframe = "";
		}else if (StringUtils.equals(checkType,ProtocolCode.CRC16)){
			cframe = CrcHexUtil.CRC16(datas);
		}else{
			throw new Exception(checkType){
				private static final long serialVersionUID = 1L;
				
				{
					System.err.println("Not support " + this.getMessage() + " check algorithm now!");
				}
			};
		}
		return cframe;
			
	}
	
	// 该方法用来 获取最终的发送命令
	private byte[] getRTUOrASCII(String cmd ){
		String pattern = (String)confs.get(ProtocolCode.PARAMS_PATTERN);
		byte[] recmd = null ;
		if(StringUtils.equals(pattern,ProtocolCode.ASCII)){
			String endchar = (String)confs.get(ProtocolCode.PARAMS_ENDCHAR);
			if (StringUtils.isNotEmpty(endchar))
			{	endchar = StringUtils.replace(endchar, "<cr>", "\r");
				endchar = StringUtils.replace(endchar, "<lf>", "\n");
			}else
				endchar = "";
			cmd = cmd + endchar ;
			recmd = cmd.getBytes();
		}
		if(StringUtils.equals(pattern,ProtocolCode.RTU)){
			recmd = HexUtils.convert(cmd);
		}
		
		return recmd;
	}  
	
	private String convertCMD( String cmd ){
		
		byte[] cmdbytes = getRTUOrASCII(cmd);
		cmd = CrcHexUtil.Bytes2HexString(cmdbytes);
		
		String cframe = "";
		try {
			cframe = getCheckFrame(cmdbytes);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		cmd = cmd + cframe ;
		
		
		return cmd;
	}
	
	public AbstractSensorResult read( String port , String rate , String cmd ){
		int cnt=0;
			AbstractSensorResult asr = null ;
			openSerialPort(port ,rate );
			String hex_cmd = convertCMD(cmd); ;
			sr.sendMessage( hex_cmd );
			while(flag!=1 && cnt<=60){
				cnt++;
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (flag==1) { r.setCmd(cmd); asr = r;  flag = 0; }
			if (cnt>60) { DebugTool.printErr("Unable Collect Data!"); flag = -1;}
			closeSerialPort();
		
		return asr ;
	}
	
	public void removeLCK( ){
		Runtime r = Runtime.getRuntime();   
		BufferedReader reader = null;
		BufferedReader error = null;
		try { 
		   	Process p = r.exec("sh " + Constant.UNLOCK_TTY_FILE);//Modify temp
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
	
	//探测某个串口是否能够处理某命令
	public static boolean testCmd2Serail(String port , String rate , String readdelay , String pattern , String cmd)
	{
		String paramStr = "rate=2400,data bits=8,stop bits=1,delay read=1000,timeout=3000,parity=0,flow control=0,check=none,pattern=ASCII,endchar=<cr>";
		int num = 2;
		readdelay = "delay read=" + readdelay ;
		pattern = "pattern=" + pattern ;
		paramStr = StringUtils.replace(paramStr, "delay read=1000" , readdelay);
		paramStr = StringUtils.replace(paramStr, "pattern=ASCII" , pattern);
		System.out.println(paramStr);
		int cnt = 1 , cok = 0, cerr = 0;
		
		SerialModbusAsciiUtil smau = new SerialModbusAsciiUtil(paramStr);
		smau.removeLCK();
		
		while(cnt<=num){
			AbstractSensorResult asr = smau.read(port , rate , cmd) ;
			if (asr!=null){
				DebugTool.showCurr("shortAddr:" + asr.getShortAddress());
				DebugTool.showCurr("cmds:" +asr.getCmd());
				DebugTool.showCurr("results:"+asr.getResultAscii());
				DebugTool.showCurr("results:"+asr.getResultRTU());
				cok ++ ;
			}else{
				DebugTool.showConsole("Data Empty");
				cerr++;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			smau.closeSerialPort();
			System.out.println("OK:ERR---"+ cok + ":" + cerr);
			cnt++;
		}
		
		return (cok>0); 
			
	}
	
	public static void main ( String[] args )  {
		String paramStr = "rate=2400,databits=8,stopbits=1,delay read=1000,timeout=3000,parity=0,flow control=0,check=none,pattern=ASCII,endchar=<cr>";	  
		String port = null ;
		String rate = null ;
		
		int num = 1;
		String cmd = null ;
		String readdelay = "500" ;
		String pattern = "ASCII";
		if (args != null && args.length >= 5 )
		{
			port = args[0];
			rate = args[1];
			num = Integer.parseInt(args[2]);	
			cmd = args[3]+"\r";
			pattern = "pattern=" + args[4];
			readdelay = "delay read=" + args[5];
			
			paramStr = StringUtils.replace(paramStr, "delay read=1000" , readdelay);
			paramStr = StringUtils.replace(paramStr, "pattern=ASCII" , pattern);
			
			System.out.println(paramStr);
			
		}
		else
		{
			port = "/dev/ttyUSB0";
			rate = "2400";
			num = 1 ;
			cmd = "Q6\r";
			pattern = "pattern=ASCII";
			readdelay = "delay read=500";
			
		}
		
		int cnt = 1 , cok = 0, cerr = 0;
		SerialModbusAsciiUtil smau = new SerialModbusAsciiUtil(paramStr);
		
		smau.removeLCK();
		
		while(cnt<=num){

			AbstractSensorResult asr = smau.read(port , rate , cmd) ;
			if (asr!=null){
				DebugTool.showCurr("shortAddr:" + asr.getShortAddress());
				DebugTool.showCurr("cmds:" +asr.getCmd());
				DebugTool.showCurr("results:"+asr.getResultAscii());
				DebugTool.showCurr("results:"+asr.getResultRTU());
				cok ++ ;
			}else{
				DebugTool.showConsole("Data Empty");
				cerr++;
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		smau.closeSerialPort();
		System.out.println("OK:ERR---"+ cok + ":" + cerr);
		cnt++;
		}
			
	   }
   }
	

