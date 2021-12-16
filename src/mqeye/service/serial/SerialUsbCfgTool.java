package mqeye.service.serial;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;

public class SerialUsbCfgTool {
	public final static String UNKNOW_PORT = "UNKNOW";
		private List<SerialUsb> list = new ArrayList<SerialUsb>();
	
		private  String paserSPhyID( String line ){
			 String scode = paserttyCode(line);
			 String temp = StringUtils.substringBetween(line,"/devices",scode);
			 String[] codes =  StringUtils.split(temp,"/");
			 String sphyid = "";
			 if (codes.length > 2) sphyid = codes[1];
			 return sphyid ;
		}
	   private  String paserttyCode( String line ){
		   String code = StringUtils.substringBetween(line,"/sys/class/tty/","->");
		   return code.trim();
	   }
	   
	   private  String paserUSBPhyID( String line ){
		   String usbcode = paserttyCode(line);
		   String temp = StringUtils.substringBetween(line,"/usb",usbcode);
		   String[] codes =  StringUtils.split(temp,"/");
		   String usbphyid = "";
		   if (codes.length > 2) usbphyid = codes[1];
		   return usbphyid;
	   }

	   String USB = "USB"; // Serial to USB Port
	   String SER = "S" ; // Serial Port 
	   
	   private void configSerial(String type){
		   String[] cmd = {"/bin/sh","-c","ls -l /sys/class/tty/tty" + type +"*" };
		   Runtime r = Runtime.getRuntime();   
	 	   BufferedReader in = null;  	
	 	   
		 	try {   
		 		 Process p = r.exec(cmd);   
			      if (p != null) {   
			    	  in = new BufferedReader(new InputStreamReader(p.getInputStream()));   
			    	  		
			    	  		
			            String line = null;   
			            
			            
			            while ((line = in.readLine()) != null) {   
			            	if (StringUtils.contains(line, "serial8250")) continue;
			            	String uscode = paserttyCode(line);
			            	String usphyid = "";
			            	if (StringUtils.equals(type, USB))
			            		usphyid = paserUSBPhyID(line);
			            	else
			            		usphyid = paserSPhyID(line);
			            	
			            	
			            
			            	SerialUsb stu = new SerialUsb();
			            	stu.setUsbCode("/dev/" + uscode);
			            	stu.setUsbPhyId(usphyid);	
			            	list.add(stu);
			            		
			            	}
			      }
		 	  } catch (Exception ex) {   
					DebugTool.printErr("Get Serial Port ID Runtime Error!");
					DebugTool.printExc(ex);
		 	  } finally {   
					try {   
					in.close();   
					} catch (IOException e) {   
					DebugTool.printErr("Get Serial Port ID  Runtime InputStream Error!");
					DebugTool.printExc(e);
						}   
		 	  }  
	   }
	   
	   
	   public List<SerialUsb>  getAllSerialUsb(){
			configSerial(USB);
		  	configSerial(SER);
		   return list;
	   	}
	   public String getSerialUsbCode(String phyid){
		   	configSerial(USB);
		   	configSerial(SER);
		   	String sucode = UNKNOW_PORT;
		   	for(SerialUsb stu:list){
		  		if (StringUtils.equals( phyid, stu.getUsbPhyId()))
		  			{
		  				sucode = stu.getUsbCode();
		  				break;
		  			}
		  	}
		  	return sucode;
	   	}
	 
	 public static void main(String args[]) {
		  List<SerialUsb> list = new SerialUsbCfgTool().getAllSerialUsb();
		  for(SerialUsb su:list)
			   System.out.println(su);
	   }
}
