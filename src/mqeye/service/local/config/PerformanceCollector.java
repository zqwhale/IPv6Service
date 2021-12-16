package mqeye.service.local.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/*
 *   Local CPU Information Collect Tool
 * */
public class PerformanceCollector {
		public final static String DMIDECODE = "dmidecode -t processor";
		public final static String CATCMDCPU = "cat /proc/stat";
		public final static String CATCMDMEM = "cat /proc/meminfo";
		public final static String CATCMDETH = "cat /proc/net/dev";
		
		public static CpuUsageBean getCpuUsage(String devName) {
			// TODO Auto-generated method stub
			CpuUsageBean cpuUsage = null;
			Process proc1 , proc2 ;
			Runtime r = Runtime.getRuntime();
			try{
				long start = System.currentTimeMillis();
				long idleCpuTime1 = 0 , totalCpuTime1 = 0;
				proc1 = r.exec( CATCMDCPU );
				if (proc1!=null){
					BufferedReader in1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
					String line = null;
					while((line = in1.readLine())!=null){
							if (line.startsWith(devName)){
									line = line.trim();
									String temps[] = line.split("\\s+");
									
									String user = temps[1];
									String nice = temps[2];
									String system = temps[3];
									String idle = temps[4];
									String iowait = temps[5];
									String irq = temps[6];
									String softirq = temps[7];
									
									idleCpuTime1 = Long.parseLong(idle);
									totalCpuTime1 = Long.parseLong(user) + 	Long.parseLong(nice) +
											Long.parseLong(system) + Long.parseLong(idle)  + Long.parseLong(iowait) +
											Long.parseLong(irq) + Long.parseLong(softirq) ;
								
									//System.out.println(line + ":" + temps.length);
									//System.out.println(user +":" +nice +":" + system +":" +  idle +":" + iowait +":" + irq +":" + softirq);
										break;
							}
					}
					in1.close();
					proc1.destroy();
				}
				
				try {
						Thread.sleep(1000);
				} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						System.out.println("CPU Sleep is Occur Interrupt ERROR!");
						e.printStackTrace();
				}
				
				long end = System.currentTimeMillis();
				long idleCpuTime2 = 0 , totalCpuTime2 = 0;
				proc2 = r.exec( CATCMDCPU );
				if (proc2!=null){
					BufferedReader in2 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
					String line = null;
					while((line = in2.readLine())!=null){
							if (line.startsWith(devName)){
									line = line.trim();
									String temps[] = line.split("\\s+");
									
									String user = temps[1];
									String nice = temps[2];
									String system = temps[3];
									String idle = temps[4];
									String iowait = temps[5];
									String irq = temps[6];
									String softirq = temps[7];
									
									idleCpuTime2 = Long.parseLong(idle);
									totalCpuTime2 = Long.parseLong(user) + 	Long.parseLong(nice) +
											Long.parseLong(system) + Long.parseLong(idle)  + Long.parseLong(iowait) +
												Long.parseLong(irq) + Long.parseLong(softirq) ;
									
									//System.out.println(line + ":" + temps.length);
									//System.out.println(user +":" +nice +":" + system +":" +  idle +":" + iowait +":" + irq +":" + softirq);
									break;
							}
					}
					in2.close(); 
					proc2.destroy();
				}
				
				
				
				if (idleCpuTime1!=0 && idleCpuTime2!=0 && totalCpuTime1 != 0 && totalCpuTime2 != 0){
					cpuUsage = new CpuUsageBean();
					cpuUsage.setCpuid(devName);
					cpuUsage.setTimePoint((start+end)/2);
					float usage = 1-(float)(idleCpuTime2 -idleCpuTime1)/(float)(totalCpuTime2 -totalCpuTime1);
					cpuUsage.setCupUsage(usage);
					
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return cpuUsage;
		}
		public static MemUsageBean getMemUsage( ){
			 MemUsageBean memUsage = null ;
				Process proc = null ;
				Runtime r = Runtime.getRuntime();
				try{
						long time = System.currentTimeMillis();
						proc = r.exec( CATCMDMEM );
						float usage = 0.0f;
						long totalMem = 0 , freeMem = 0 , buffers = 0, cached = 0;
						if (proc!=null){
							 BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
							 String line = null;
							 int cnt = 0;
								while((line = in.readLine())!=null){
										String[] memInfo = line.split("\\s+");
										if(memInfo[0].startsWith("MemTotal:")){
												totalMem = Long.parseLong(memInfo[1]); cnt++;
										}
										if(memInfo[0].startsWith("MemFree:")){
												freeMem = Long.parseLong(memInfo[1]); cnt++;
										}
										if(memInfo[0].startsWith("Buffers:")){
												buffers = Long.parseLong(memInfo[1]); cnt++;
										}
										if(memInfo[0].startsWith("Cached:")){
												cached = Long.parseLong(memInfo[1]); cnt++;
										}
										
										if (cnt==4){
												memUsage = new MemUsageBean();
												usage = 1 - (float)(freeMem+buffers+cached)/(float)totalMem ;
												memUsage.setTimePoint(time);
												memUsage.setMemfree(freeMem);
												memUsage.setMemtotal(totalMem);
												memUsage.setMemUsage(usage);
												System.out.println("Date:" + new Date() + "Usage:" + usage *100 + "%");
												System.out.println("MemTotal:" + totalMem + " MemFree:" + freeMem + " Buffers:" + buffers + " Cached:" + cached );
												break;
										}
								}
								in.close();
								proc.destroy();
						}
				}catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return memUsage ;
		}
		
		 public static List<CpuInfoBean> readCpuInfo(){
				Runtime r = Runtime.getRuntime();
				BufferedReader in = null ;
				List<CpuInfoBean> cbs= null;
				try {
					Process p = r.exec( DMIDECODE );
					if (p!=null){
							in = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line = null;
							cbs = new ArrayList<CpuInfoBean>();
							
							while((line = in.readLine())!=null){
										if (StringUtils.contains(line, "Processor Information")){
												CpuInfoBean cb = new CpuInfoBean();
												
												while((line = in.readLine())!=null){
													if (StringUtils.contains(line, "Socket Designation:"))	cb.setSocketDesignation(line.split(":")[1]);
													else if (StringUtils.contains(line, "Type:"))	cb.setCpuType(line.split(":")[1]);
													else if (StringUtils.contains(line, "Family:"))	cb.setCpuFamily(line.split(":")[1]);
													else if (StringUtils.contains(line, "Manufacturer:"))	cb.setCpuManufactory(line.split(":")[1]);
													else if (StringUtils.contains(line, "ID:"))	cb.setCpuID(line.split(":")[1]);
													else break;
												}
												
												cbs.add(cb);
										}	
							}
							in.close();
							p.destroy();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return cbs;
		 }
		 		 
		 public static NetUsageBean getNetUsage(String devName) {
				// TODO Auto-generated method stub
			 NetUsageBean netUsage = null;
				Process proc1 , proc2 ;
				Runtime r = Runtime.getRuntime();
				try{
					long start = System.currentTimeMillis();
					long inBound1 = 0 , outBound1 = 0;
					proc1 = r.exec( CATCMDETH );
					if (proc1!=null){
						BufferedReader in1 = new BufferedReader(new InputStreamReader(proc1.getInputStream()));
						String line = null;
						while((line = in1.readLine())!=null){
								line = line.trim();
								if (line.startsWith(devName)){
									 	line = line.replace(devName+":", "");
									 	line = line.trim();
										String temps[] = line.split("\\s+");
										inBound1 = Long.parseLong(temps[0]);
									  outBound1 = Long.parseLong(temps[8]);
							    
										 break;
								}
						}
						in1.close();
						proc1.destroy();
					}
					
					try {
							Thread.sleep(1000);
					} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							System.out.println("Net Sleep is Occur Interrupt ERROR!");
							e.printStackTrace();
					}
					
					long end = System.currentTimeMillis();
					long inBound2 = 0 , outBound2 = 0;
					proc2 = r.exec( CATCMDETH );
					if (proc2!=null){
						BufferedReader in2 = new BufferedReader(new InputStreamReader(proc2.getInputStream()));
						String line = null;
						while((line = in2.readLine())!=null){
								line = line.trim();
								if (line.startsWith(devName)){
										line = line.replace(devName+":", "");
										line = line.trim();
										String temps[] = line.split("\\s+");
										inBound2 = Long.parseLong(temps[0]);
										outBound2 = Long.parseLong(temps[8]);
										
										break;
								}
						}
						in2.close();
						proc2.destroy();
					}
					if (inBound1!=0 || inBound2!=0 || outBound1 != 0 || outBound2 != 0){
						String speed = NetworkController.getEthSpeed(devName);
						speed = speed.replace("Mb/s", "").trim();
						long spd = 100;  //Î´ÖªÊ±£¬Ä¬ÈÏ100Mb/s
						if (StringUtils.isNumeric(speed)) spd = Long.parseLong(speed);
						float interval = (float)(end - start)/1000;
						netUsage = new NetUsageBean();
						netUsage.setEthid(devName);
						netUsage.setTimePoint((start+end)/2);
						netUsage.setInBound((inBound2-inBound1)/interval);
						netUsage.setOutBound((outBound2-outBound1)/interval);
						float rate = (float)(inBound2 -inBound1 + outBound2 - outBound1) * 8/(1000000*interval);
						float usage = rate/spd;
						
						netUsage.setNetUsage(usage);
						
					}else{
						netUsage = new NetUsageBean();
						netUsage.setEthid(devName);
						netUsage.setTimePoint((start+end)/2);
						netUsage.setInBound(0);
						netUsage.setOutBound(0);
						netUsage.setNetUsage(0);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return netUsage;
			}
		
 		 public static CpuUsageBean getCpuUsage() {
				// TODO Auto-generated method stub
					return getCpuUsage("cpu");
			}
			


}
