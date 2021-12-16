package mqeye.sys.tunnel;

import java.io.BufferedReader;
import java.io.IOException;

import mqeye.sys.execute.AbstractCommand;

public class TunnelBuildCmd extends AbstractCommand
{
	//public static final String APP_ROOT_PATH = "/root/workspace/MQeyeTunnel";
	public static final String APP_ROOT_PATH = "/root/workspace/MQeyeService";
	public static final String ssh_R_login = APP_ROOT_PATH + "/sys/tunnel_build.exp";
	private TInfoBean tInfo = null;
	
	public TunnelBuildCmd( TInfoBean tInfo){
		this.tInfo = tInfo;
	}
	
	@Override
	public String command(){
		if (this.tInfo == null)
			return null;
		else
		{
			String cmd = ssh_R_login ;
			String params = " " + tInfo.getMapPort() + " " + tInfo.getLocalhost() + " " +
			tInfo.getActPort() + " " + tInfo.getCsUser() + " " +
			tInfo.getCsIpAddr() + " " + tInfo.getCsSshPort() + " " +
			tInfo.getCsPwd() ;
			return cmd + params ; 
		}	
	}
	
	@Override
	public int result(BufferedReader in) throws IOException {
		// TODO Auto-generated method stub
		String line = "";
        while ((line = in.readLine()) != null) {   
        		System.out.println(line);
        }
  		return 0;
	}
}
