package mqeye.service.tools;


import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import mqeye.service.Constant;
import mqeye.service.serial.AbstractSensorResult;
import mqeye.service.serial.SerialPortParam;
import mqeye.service.serial.SerialPortReader;

import org.apache.tomcat.util.buf.HexUtils;

public class SerialModbusUtil implements Observer{
	SerialPortReader sr=new SerialPortReader(""); 
	AbstractSensorResult r = null;
	int flag = 0;		/* 0: 没有数据存入 | 1:有数据存入  | -1:超过默认周期，数据未刷新成功 */
	
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
		System.out.println(values);
		int length = values.length;
		byte[]   crcBytes = {0,0};  
		System.arraycopy( (byte[])arg, length - 2 , crcBytes, 0, 2 );
		String crc1 = CrcHexUtil.Bytes2HexString(crcBytes);
		byte[] tempBytes =new byte[length-2];
		System.arraycopy( (byte[])arg, 0 , tempBytes, 0, length - 2 );
		String crc2 = CrcHexUtil.CRC16(tempBytes);
		
		if (crc1.equals(crc2))
		{ 
			AbstractSensorResult asr = new AbstractSensorResult();
			String data = CrcHexUtil.Bytes2HexString(values);
			String shortAddr = data.substring(0, 2);
			asr.setShortAddress(shortAddr);
			asr.setResult(values);
   			r = asr ;
			flag = 1;
		}
		
	}
	public AbstractSensorResult read( String port , String rate , String cmd ){
		int cnt=0;
		AbstractSensorResult asr = null ;
			openSerialPort(port ,rate );
			sr.sendMessage( cmd );
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
	public static boolean testCmd2Serail(String port , String rate , String cmd)
	{	
		byte[] cmdbytes = HexUtils.convert(cmd);
		String crc = CrcHexUtil.CRC16(cmdbytes);
		cmd = cmd + crc;
		
		int num = 2;
		
		
		int cnt = 1 , cok = 0, cerr = 0;
		
		SerialModbusUtil su = new SerialModbusUtil();
		su.removeLCK();
		
		while(cnt<=num){
			AbstractSensorResult asr = su.read(port , rate , cmd) ;
			if (asr!=null){
				DebugTool.showCurr("shortAddr:" + asr.getShortAddress());
				DebugTool.showCurr("cmds:" +asr.getCmd());
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

			su.closeSerialPort();
			System.out.println("OK:ERR---"+ cok + ":" + cerr);
			cnt++;
		}
		
		return (cok>0); 
			
	}

	public static void main ( String[] args )  {
			  
		String port = null ;
		String rate = null ;
		int num = 1;
		String cmd = null ;
		if (args != null && args.length >= 3 )
		{
			port = args[0];
			rate = args[1];
			num = Integer.parseInt(args[2]);	
			cmd = args[3];
			byte[] cmdbytes = HexUtils.convert(cmd);
			String crc = CrcHexUtil.CRC16(cmdbytes);
			cmd = cmd + crc;
			
		}
		
		int cnt = 1 , cok = 0, cerr = 0;
		TestSerialTool su = new TestSerialTool();
		
		su.removeLCK();
		
		while(cnt<=num){

			AbstractSensorResult asr = su.read(port , rate , cmd) ;
			if (asr!=null){
				DebugTool.showCurr("shortAddr:" + asr.getShortAddress());
				DebugTool.showCurr("cmds:" +asr.getCmd());
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

		su.closeSerialPort();
		System.out.println("OK:ERR---"+ cok + ":" + cerr);
		cnt++;
   }
			
	   }
   }
	

