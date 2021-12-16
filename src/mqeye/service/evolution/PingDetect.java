package mqeye.service.evolution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Observable;

import mqeye.data.vo.Device;
import mqeye.service.Constant;
import mqeye.service.detect.PingResult;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;


public class PingDetect extends Observable implements Runnable {
	private Device dev = null ;
	private PingResult result = new PingResult() ;
	
	//Copy from PingTool
	protected  int totalNum = 0;
	protected  int packetNum = 0; 
	protected  int isFinish = 0 ;
	
	private float paserTC( String tc){
		// 处理：rtt min/avg/max/mdev = 1.487/3.896/6.288/1.603 ms
				float avgTc = Constant.PING_TIMEOUT;  
				tc = StringUtils.substringBetween(tc,"=","s");
				String values[] = StringUtils.split(tc,"/");
				if (values!=null && values.length>2 ){
						avgTc = Float.parseFloat(values[1]);
						avgTc = (float) (Math.round(avgTc*10)/10.0);
						if (avgTc > Constant.PING_TIMEOUT) avgTc = Constant.PING_TIMEOUT;
					}
				return avgTc ;
	}
	private float paserLPR( String lpr){
		//处理 ： 10 packets transmitted, 10 received, 0% packet loss, time 9007ms
		//处理 ： 1 packets transmitted, 0 received, +1 errors, 100% packet loss, time 0ms
		float lossRate = 100;
		
		if (StringUtils.contains(lpr, "errors,"))
				lpr = StringUtils.substringBetween(lpr, "errors,","%");
		else
				lpr = StringUtils.substringBetween(lpr, "received,","%");
		lpr = StringUtils.remove(lpr, " ");
		if (StringUtils.isNumeric(lpr))
					lossRate = Float.parseFloat(lpr);
		return lossRate ;
	}
	private long paserLP( String lpr , int count ){
		//处理 ： 10 packets transmitted, 10 received, 0% packet loss, time 9007ms
		//处理 ： 1 packets transmitted, 0 received, +1 errors, 100% packet loss, time 0ms
		long loss = count;
		lpr = StringUtils.replace(lpr,"packets transmitted,",":");
		lpr = StringUtils.replace(lpr,"received,",":");
		lpr = StringUtils.remove(lpr, " ");
		String values[] = StringUtils.split(lpr,":");
		if (values!=null && values.length>2){
					int transmitted = Integer.parseInt(values[0]);
					int received = Integer.parseInt(values[1]);
					loss = transmitted - received  ;
					loss = (loss>count?count:loss);
		}
		return loss ;
	}
	
	
	public PingResult ping(String ip , int count , int timeout ){
			PingResult pr = new PingResult(); 
			pr.setLosePacket(count);
			totalNum = count ;
			boolean status = false;
			try {
				status = InetAddress.getByName(ip).isReachable(3000);
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
					DebugTool.printErr("PingTool Runtime Error --- UnknownHostException!");
					DebugTool.printExc(e1);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				DebugTool.printErr("PingTool Runtime Error --- IOException!");
				DebugTool.printExc(e1);
			}
			if (status)
			{
				String pingCommand =  "ping " + " -c " + count + " -w " + timeout + " "+ ip ;
			   Runtime r = Runtime.getRuntime();   
		 	   BufferedReader in = null;  	
			    try {   
				      Process p = r.exec(pingCommand);   
				      if (p != null) {   
				    	  			
					            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   
					            String line = null;   
					            while ((line = in.readLine()) != null) {   
								            	if ( StringUtils.contains(line, "packets transmitted") && StringUtils.contains(line, "received")){
								            			pr.setLosePacket(paserLP(line,count));
								            			pr.setLosePacketRate(paserLPR(line));
								            						}
								            	if ( StringUtils.contains(line, "rtt min/avg/max/mdev") ){
								            			pr.setStatus(Constant.PING_REACHABLE);	
								            			pr.setTimeConsum(paserTC(line));
								            						} 	
								            	if ( StringUtils.contains(line, "Prohibited") ){
								            			pr.setStatus(Constant.PING_ERR_PROHIBIT);	
					                								}   
								            	if (StringUtils.contains(line, "bytes from") ){
								            			pr.setFlag(Constant.PING_SUCCESS);	
								            						}
								            	packetNum ++ ;
								            	this.setChanged();
								            	this.notifyObservers();
					            					}   
				      			}
				     } catch (Exception ex) {   
				    	 					DebugTool.printErr("PingTool Runtime Error!");
				    	 					DebugTool.printExc(ex);
				     } finally {   
				            try {   
				                in.close();   
				            } catch (IOException e) {   
				            	DebugTool.printErr("PingTool Runtime InputStream Error!");
				            	DebugTool.printExc(e);
				            					}   
				        }   
			}
		packetNum = count ;
		this.setChanged();
		this.notifyObservers();
		
		try{
			Thread.sleep(50);
		}catch(Exception e){		
		}
		
		
		if (StringUtils.equals(pr.getStatus(),Constant.PING_ERR_UNREACHABLE))
		{
			pr.setLosePacket(count);
			pr.setLosePacketRate(100);
		}
		isFinish = 1;
	   return pr ;
			
	}

	
	
	public PingDetect(Device d)
	{
		this.dev = d;
		
	}
	public Device getDevice()
	{
		return dev ;
	}
	
	public PingResult getResult()
	{
		return result ;
	}
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if (dev!=null && StringUtils.isNotEmpty(dev.getIPAddr())){
			String ipAddr = dev.getIPAddr();
			result = ping(ipAddr, 100, 500);
		}
		
		this.setChanged();
		this.notifyObservers();
		
	}

}
