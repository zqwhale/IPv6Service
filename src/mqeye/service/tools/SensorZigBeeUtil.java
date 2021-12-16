package mqeye.service.tools;


import gnu.io.SerialPort;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import mqeye.service.Constant;
import mqeye.service.detect.THSensorResult;
import mqeye.service.serial.SerialPortParam;
import mqeye.service.serial.SerialPortReader;

public class SensorZigBeeUtil implements Observer{
	SerialPortReader sr=new SerialPortReader(""); 
	THSensorResult r = null;
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
		int length = ((byte[])arg).length;
		byte[] temp = new byte[length-1];
		byte   crc1 = ((byte[])arg)[length-1]; 
		System.arraycopy( (byte[])arg, 0, temp, 0, length-1 );
		byte crc2 = CrcHexUtil.CRC(temp,length-1);
	    
	    
    	String data = CrcHexUtil.Bytes2HexString((byte[])arg);

    	if ( crc2==crc1){
    		String ddz	= data.substring(4,8);
    		String sd = data.substring(22, 24);
    		String wdz = data.substring(16, 18);
    		String wdx = data.substring(18, 20);
//    		System.out.println(ddz);
//    		System.out.println( sd+"湿度:"+Integer.parseInt(sd,16));
//    		System.out.println( wdz+":"+wdx+"温度:"+(Integer.parseInt(wdz,16)+Integer.parseInt(wdx,16)/256.0));
    		r = new THSensorResult();
    		r.setShortAddress(ddz);
    		r.setResult((byte[])arg);
    		
    		r.setHumidity(Integer.parseInt(sd,16));
    		double t = Integer.parseInt(wdz,16) + Integer.parseInt(wdx,16)/256.0;
    		BigDecimal b = new BigDecimal(t);
    		t = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
    		r.setTemperature(t);
    		
    		flag = 1;
    	}else{
    		
    	}
	}
	
	public THSensorResult read( String port , String rate ){
		int cnt=0;
		THSensorResult thsr = null ;
			openSerialPort(port ,rate );
			while(flag!=1 && cnt<=60){
				cnt++;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (flag==1) { thsr = r; flag = 0; }
			if (cnt>60) { DebugTool.printErr("Unable Collect Data ！"); flag = -1;}
			closeSerialPort();
		
		return thsr ;
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
			int num = 1;
			if (args == null || args.length ==0 )
			{
				port = BaseCommonFunc.getProperty("serialPort");
				rate = BaseCommonFunc.getProperty("serialRate");
				num = 1;
			}	
			if (args != null && args.length >= 3 )
			{
				port = args[0];
				rate = args[1];
				num = Integer.parseInt(args[2]);				
			}

			
			System.out.println(port + ":" + rate);
			int cnt = 1 , cok = 0, cerr = 0;
			SensorZigBeeUtil su = new SensorZigBeeUtil();
			su.removeLCK();
			
			while(cnt<=num){
				THSensorResult result = su.read(port,rate) ;
				if (result!=null){
					DebugTool.showCurr(result.getShortAddress());
					DebugTool.showCurr(result.getHumidity()+"");
					DebugTool.showCurr(result.getTemperature()+"");
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
	

