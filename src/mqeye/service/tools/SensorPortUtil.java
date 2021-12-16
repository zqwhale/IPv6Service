package mqeye.service.tools;


import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.detect.THSensorResult;
import mqeye.service.serial.SerialPortParam;
import mqeye.service.serial.SerialPortReader;

public class SensorPortUtil implements Observer{
	SerialPortReader sr=new SerialPortReader(""); 
	String data = "";
	int flag = 0;		/* 0: 没有数据存入 | 1:有数据存入  | -1:超过默认周期，数据未刷新成功 */
	
	/*
	 * remove File 
	 * */
	
	/**
     * 打开串口
     * @param String message
     */
	public void openSerialPort( String port , String rate) 
    { 
        HashMap<String, Comparable> params = new HashMap<String, Comparable>();  
        String dataBit = ""+SerialPort.DATABITS_8;
        String stopBit = ""+SerialPort.STOPBITS_1;
        String parity = ""+SerialPort.PARITY_NONE;    
        int parityInt = SerialPort.PARITY_NONE; 
        params.put( SerialPortParam.PARAMS_PORT, port ); // 端口名称
        params.put( SerialPortParam.PARAMS_RATE, rate ); // 波特率
        params.put( SerialPortParam.PARAMS_DATABITS,dataBit  ); // 数据位
        params.put( SerialPortParam.PARAMS_STOPBITS, stopBit ); // 停止位
        params.put( SerialPortParam.PARAMS_PARITY, parityInt ); // 无奇偶校验
        params.put( SerialPortParam.PARAMS_TIMEOUT,50 ); // 设备超时时间 100毫秒
        params.put( SerialPortParam.PARAMS_DELAY,50 ); // 端口数据准备时间50毫秒 考虑在修改小一点
        try {
							sr.open(params);
							sr.addObserver(this);
			}catch(UnsatisfiedLinkError err){
						DebugTool.printErr("RXTXCommDriver Lib Error");
			}catch(Exception err){
					DebugTool.printErr("NoSuchPortException Error");
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
    	data = CrcHexUtil.Bytes2HexString((byte[])arg);
  		flag = 1;    	
	}
	
	public String read( String port , String rate , String cmd){
		int cnt=0;
		String currData = "" ;
			openSerialPort(port ,rate );
			if (!StringUtils.isEmpty(cmd)) sr.sendMessage( cmd );
			while(flag!=1 && cnt<=60){
				cnt++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (flag==1) { currData = data; flag = 0; }
			if (cnt>60) { DebugTool.printErr("Unable Collect Data ！"); flag = -1;}
			closeSerialPort();
		
		return currData ;
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
	
	public static void main ( String[] args )  {
			  
			String port = null ;
			String rate = null ;
			String cmd = null;
			
			int num = 1;
			if (args == null || args.length ==0 )
			{
				port = BaseCommonFunc.getProperty("serialPort");
				rate = BaseCommonFunc.getProperty("serialRate");
				num = 1;
			}	
			if (args != null && args.length >= 4 )
			{
				port = args[0];
				rate = args[1];
				num = Integer.parseInt(args[2]);		
				cmd = args[3];
			}

			System.out.println(port + ":" + rate);
			int cnt = 1 , cok = 0, cerr = 0;
			SensorPortUtil su = new SensorPortUtil();
			su.removeLCK();
			
			while(cnt<=num){
				String data = su.read(port,rate,cmd) ;
				if (data!=""){
					DebugTool.showConsole(data);
					cok ++ ;
				}else{
					DebugTool.showConsole("Data Empty");
					cerr++;
				}
				cnt++;
			}
			System.out.println("OK:ERR---"+ cok + ":" + cerr);
	   }
   }
	

