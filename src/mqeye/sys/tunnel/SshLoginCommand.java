package mqeye.sys.tunnel;

import java.io.BufferedReader;
import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import mqeye.sys.execute.AbstractCommand;

public class SshLoginCommand extends AbstractCommand {
	public static final String APP_ROOT_PATH = "/root/workspace/MQeyeService";
	public static final String ssh_login = APP_ROOT_PATH + "/sys/ssh_login.exp";
	
	private static final String SUCCESS_MSG_1 = "Last login:";
	private static final String SUCCESS_MSG_2 = "Microsoft Windows";
	
	
	private SSHInfo sInfo = null;
	
	public SshLoginCommand(SSHInfo sInfo){
		this.sInfo = sInfo ;
	}
	@Override
	public String command() {
		// TODO Auto-generated method stub
		if (this.sInfo == null)
			return null;
		else
		{
			String cmd = ssh_login ;
			String params = " " + sInfo.getSshUser() + " " + sInfo.getSshSrv() + " " +
			sInfo.getSshPort() + " " + sInfo.getSshPwd() + " " + sInfo.getSshCmd() ;
			
			return cmd + params ; 
		}	
	}

	@Override
	public int result(BufferedReader in) throws IOException {
		// TODO Auto-generated method stub
		int flag = 0;
		String line = "";
        while ((line = in.readLine()) != null) {   
        		if (StringUtils.contains(line, SUCCESS_MSG_1)||
        				StringUtils.contains(line, SUCCESS_MSG_2)) 
        			flag = 1;
        }
  		return flag;
	}

}
