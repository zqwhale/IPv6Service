package mqeye.sys.tunnel;

import java.util.HashMap;
import java.util.Map;

import mqeye.service.detect.PortResult;
import mqeye.service.tools.ErrorLog;
import mqeye.service.tools.HtmlCodeUtil;
import mqeye.service.tools.PortConnect;
import mqeye.sys.execute.CommandExecutor;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

public class CloudServerHolder {
	
	private ErrorLog elog = new ErrorLog(this.getClass());
	public int sshPortValid(CloudServer server)
	{
		int flag = 0;
		String ipAddr = server.getCsIPAddr();
		String sshPort = server.getCsSshPort();
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ipAddr ,  sshPort );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
			flag  = 1;
		else
				elog.log(ipAddr + " " + sshPort + " Port is Close!");
		return flag;
	}
	public int webPortValid(CloudServer server)
	{
		int flag = 0;
		String ipAddr = server.getCsIPAddr();
		String webPort = server.getCsWebPort();
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ipAddr ,  webPort );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
			flag  = 1;
		else
			elog.log(ipAddr + " " + webPort + " Port is Close!");
		return flag;
	}

	
	public int sshLoginValid(CloudServer server)
	{	int flag = 0;
		String ipAddr = server.getCsIPAddr();
		String sshPort = server.getCsSshPort();
		String user = server.getCsUser();
		String pwd = server.getCsPwd();
		SSHInfo info = new SSHInfo();
		info.setSshSrv(ipAddr);
		info.setSshPort(sshPort);
		info.setSshUser(user);
		info.setSshPwd(pwd);
		flag = CommandExecutor.execute(new SshLoginCommand(info));
		if (flag==0)
			elog.log(ipAddr + " SSH Login Failed!");
		return flag;
	}
	
	public int heartBeatValid(CloudServer server)
	{
		int flag = 0;
		String ipAddr = server.getCsIPAddr();
		String webPort = server.getCsWebPort();
		
		String url = "http://"+ipAddr+":"+webPort+"/MQeyeYun/private/business/interfaze.zm";
		Map<String,String> params = new HashMap<String,String>();
		params.put("doAction", "getServerTime");
		String result = HtmlCodeUtil.readUrl(url,params);
		
		CloudServerDAO dao = new CloudServerDAO();
		if (StringUtils.isNotEmpty(result) && result.length()>10)
		{
			
			JSONObject obj = net.sf.json.JSONObject.fromObject(result);
			String dateStr = (String) obj.get("currentTime");
			server.setHeartBeat(dateStr);
			dao.setHeartBeat(server);
			flag = 1;
		}
		else{
			server.setHeartBeat("UNKNOW");
			dao.setHeartBeat(server);
			elog.log(ipAddr + " Heart Beat Get Failed!");
		}
			
		return flag;
	}
	
	
	public Map<String,Integer> portValid(CloudServer server, String mapPorts)
	{
		
		String ipAddr = server.getCsIPAddr();
		String webPort = server.getCsWebPort();
		Map<String,Integer> pstates = new HashMap<String,Integer>();
		String url = "http://"+ipAddr+":"+webPort+"/MQeyeYun/private/business/interfaze.zm";
		Map<String,String> params = new HashMap<String,String>();
		params.put("doAction", "checkPortState");
		params.put("ports", mapPorts);
		String result = HtmlCodeUtil.readUrl(url,params);
		
		if (StringUtils.isNotEmpty(result) && result.length()>10)
		{
			String[] ports = mapPorts.split(",");
			JSONObject obj = net.sf.json.JSONObject.fromObject(result);
			for(String port:ports)
			{
				Integer data = (Integer) obj.get(port);
				pstates.put(port, data);
			}
		}else{
			elog.log(ipAddr + " MapPort state Get Failed!");
		}
		
		return pstates;
	}
	
	
	
}
