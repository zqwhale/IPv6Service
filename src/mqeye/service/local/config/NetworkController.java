package mqeye.service.local.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.NetworkInterface;
import java.net.SocketException;

import mqeye.service.Constant;

import org.apache.commons.lang.StringUtils;

public class NetworkController {
	 public final static String IFUPCMD = "ifup";
	 public final static String IFDOWNCMD = "ifdown";
	 public final static String IFCONFIGCMD = "ifconfig";
	 public final static String ETHTOOL = "ethtool";
	 
	 

	 public static String isWithLine(String name){
		 	String flag = Constant.NETWORK_UNKNOW;
			Runtime r = Runtime.getRuntime();
			BufferedReader in = null ;
			try {
				Process p = r.exec(IFCONFIGCMD + " " + name );
				if (p!=null){
						in = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = null;
						while((line = in.readLine())!=null){
							if (StringUtils.contains(line, "UP BROADCAST RUNNING MULTICAST")){
								flag = Constant.NETWORK_PHUP;  break ;
							}
									
							if (StringUtils.contains(line, "BROADCAST MULTICAST")){
								 	flag = Constant.NETWORK_PHDOWN;	  break; }
							
						} in.close();
				}p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
	 }

	  //getSpeed need Up-State eth
	 public static String getEthSpeed(String name){
		 	String speed = "100Mb/s";
		 	Runtime r = Runtime.getRuntime();
			BufferedReader in = null ;
			try {
				Process p = r.exec(ETHTOOL + " " + name );
				if (p!=null){
						in = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = null;
						while((line = in.readLine())!=null){
							line = line.trim();
							if (line.startsWith("Speed:")){
									String[] temps = line.split(":");
									speed = temps[1]; 		break;}
						}
						in.close();
				} 
				p.destroy();
				}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return speed;
	 }
	 
	// IFCONFIG
	 public static String getIpAddr(String name){
		 String flag = Constant.NETWORK_UNKNOW;
			Runtime r = Runtime.getRuntime();
			BufferedReader in = null ;
			try {
				Process p = r.exec(IFCONFIGCMD + " " + name );
				if (p!=null){
						in = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = null;
						while((line = in.readLine())!=null){
							if (StringUtils.contains(line, "inet addr:")){
									int b = line.indexOf(":");
									int e=  line.indexOf(" ",b+1);
									String ip = line.substring(b+1,e);
									if (StringUtils.isNotEmpty(ip)) flag = ip; 
									break;
								}else{
										flag = Constant.NETWORK_UNBIND;
								}
						} in.close();
				}  p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
	 }
	 
	
	 public static String isActivity(String name){
		 String flag = Constant.NETWORK_DEACTIVITY;
			try {
					NetworkInterface ni =  NetworkInterface.getByName(name);
					if (ni!=null && ni.isUp())
							flag = Constant.NETWORK_ACTIVITY;
			} catch (SocketException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		    return flag ;

	 }
	 
	  
	 public static String Activity(String name){
		 String flag = Constant.NETWORK_UNKNOW;
		 
			Runtime r = Runtime.getRuntime();
			BufferedReader in = null ;
			try {
				Process p = r.exec(IFUPCMD + " " + name );
				if (p!=null){
						in = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = null;
						if((line = in.readLine())!=null){
								flag = Constant.NETWORK_DEACTIVITY;
						}else{
								 flag = Constant.NETWORK_ACTIVITY;
						} in.close();
				}  p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
	}
	 
	 
	 public static String Deactivity(String name){
		  String flag = Constant.NETWORK_UNKNOW;
			Runtime r = Runtime.getRuntime();
			BufferedReader in = null;
			try {
				Process p = r.exec(IFDOWNCMD + " " + name );
				if (p!=null){
						in = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line = null;
						if((line = in.readLine())!=null){
						}else{
								flag = Constant.NETWORK_DEACTIVITY;
						} in.close();
				}  p.destroy();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return flag;
	}

	public static void main(String[] args) {
		
		System.out.println(NetworkController.getIpAddr("eth0") + ":" + NetworkController.getEthSpeed("eth0"));
		System.out.println(NetworkController.getIpAddr("eth1") + ":" + NetworkController.getEthSpeed("eth1")); 
		
		
		
}
	
}
