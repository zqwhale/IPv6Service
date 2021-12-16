package mqeye.service.local.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class RouteController {
				
				public final static 	String 	ROUTE_SHOW = "route -n";
				public final static	 	String	ROUTE_ADD = "route add -net #ip# gw #gw# netmask #netmask# metric #metric#";
				public final static	 	String	ROUTE_DEL = "route del -net #ip# gw #gw# netmask #netmask# metric #metric#";
				
				public final static String  ADD_SUCCESS = "Add Success";
				public final static String  DEL_SUCCESS = "Del Success";
				
				public final static int ADD_FLAG = 1 ;
				public final static int DEL_FLAG = -1 ;
				
				public final static String ROUTE_FILE = "/root/workspace/MQeyeService/bin/runroute.sh";
				
				
				public static List<RouteBean> showRoute( ){
						List<RouteBean> rblist = new ArrayList<RouteBean>();
						Runtime r = Runtime.getRuntime();
						BufferedReader in = null ;
						try {
							Process p = r.exec(ROUTE_SHOW);
							if (p!=null){
									in = new BufferedReader(new InputStreamReader(p.getInputStream()));
									String line = null; 
									while((line = in.readLine())!=null){
										//219.231.0.128   0.0.0.0         255.255.255.128 U     0      0        0 eth0
										RouteBean rb = null ;    String data[] = null ;
										if (!StringUtils.contains(line, "Destination") && !StringUtils.contains(line, "Kernel")) 
										{					line = line.replaceAll("\\s+"," ");
															data = line.split(" "); 				
															if (data.length == 8){
																		rb = new RouteBean();
																		rb.setDestination( data[0] );
																		rb.setGateway( data[1] );
																		rb.setGenmask( data[2] );
																		rb.setFlags(	data[3] );
																		rb.setMetric( data[4] );
																		rb.setRef(		data[5] );
																		rb.setUse( data[6] );
																		rb.setIface( data[7] );
																		rblist.add( rb ) ;
															}
															
										}
									} 
									in.close();
							}
							p.destroy();
						} catch (IOException e) {
							// TODO Auto-generated catch block
											e.printStackTrace();
						}
						return rblist;
				 }
				
				private static String execRoute(	int flag , String ip , String gw ,	String netmask ,	String metric	){
					String retMsg = "";
					String cmd = (flag==1 ? ROUTE_ADD : ROUTE_DEL);
					cmd = cmd.replaceAll("#ip#", ip);
					cmd = cmd.replaceAll("#gw#", gw);
					cmd = cmd.replaceAll("#netmask#", netmask);
					cmd = cmd.replaceAll("#metric#", metric);
					System.out.println(cmd);
					Runtime r = Runtime.getRuntime();
					BufferedReader error = null ;
					BufferedReader in = null ;
					try {
						Process p = r.exec(cmd);
						if (p!=null){
									error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
	   						String line = null;   
				      while ((line = error.readLine()) != null) {
				    	   								retMsg = retMsg + line;
				       			}
								in = new BufferedReader(new InputStreamReader(p.getInputStream()));
 								while((line = in.readLine())!=null){
										retMsg = retMsg + line;
								} 
								in.close();
						}
						p.destroy();
					} catch (IOException e) {
						// TODO Auto-generated catch block
										e.printStackTrace();
					}
					
					if (StringUtils.isEmpty(retMsg)) {
							retMsg = (flag==ADD_FLAG ? ADD_SUCCESS : DEL_SUCCESS);	
							if(flag==ADD_FLAG)
									addRouteCmd(cmd) ;
							else 
									delRouteCmd(cmd.replaceAll("del", "add"));
					}
					
					return retMsg ;
				}
				
				private static void addRouteCmd(String cmd ){
							File f = new File(ROUTE_FILE);
							if (!f.exists()){
									try {
										f.createNewFile();
									} catch (IOException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								
								
							}
							try {
								@SuppressWarnings("unchecked")
								List<String> routeCmds = FileUtils.readLines(f);
								if (!routeCmds.contains(cmd))
								{		routeCmds.add(cmd);
										FileUtils.writeLines(f, routeCmds);
								}
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				}
				
				private static void delRouteCmd(String cmd ){
					File f = new File(ROUTE_FILE);
					if (f.exists()){
								try {
									@SuppressWarnings("unchecked")
									List<String> routeCmds = FileUtils.readLines(f);
									for(String c:routeCmds)
										if (c.equals(cmd)) {routeCmds.remove(c);break;}
										FileUtils.writeLines(f, routeCmds);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
					}else{
							try {
								f.createNewFile();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
					}
		}
				public static String addRoute(String ip , String gw ,	String netmask ,	String metric	){
						return execRoute(ADD_FLAG,ip,gw,netmask,metric);
				}
				
				public static String delRoute(String ip , String gw ,	String netmask ,	String metric	){
					return execRoute(DEL_FLAG,ip,gw,netmask,metric);
			   }
				
				public static void main(String[] args) {
								System.out.println(addRoute("192.168.1.0","192.168.1.254" , "255.255.255.0" , "0"));
								
									for(RouteBean b:showRoute())
											System.out.println(b.getGateway());
				}
				
}
