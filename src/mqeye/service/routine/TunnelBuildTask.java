package mqeye.service.routine;

import java.util.List;
import java.util.Map;

import mqeye.service.tools.DebugTool;
import mqeye.service.tools.ErrorLog;
import mqeye.sys.execute.CommandExecutor;
import mqeye.sys.tunnel.CloudServer;
import mqeye.sys.tunnel.CloudServerDAO;
import mqeye.sys.tunnel.CloudServerHolder;
import mqeye.sys.tunnel.HostInfo;
import mqeye.sys.tunnel.HostInfoDAO;
import mqeye.sys.tunnel.HostInfoHolder;
import mqeye.sys.tunnel.TInfoBean;
import mqeye.sys.tunnel.TunnelBuildCmd;
import mqeye.sys.tunnel.TunnelDestoryCmd;
import mqeye.sys.tunnel.TInfoBeanDAO;

import org.apache.commons.lang.StringUtils;

public class TunnelBuildTask implements Runnable{
	private ErrorLog elog = new ErrorLog(this.getClass());
	
	public final static int loop = 15*60;
	public final static int delay = 3;
	
	private CloudServer cserver = null;
	private HostInfo localhost = null;
	
	private List<TInfoBean> 	 tiblist = null; 
	
	public TunnelBuildTask()
	{
		CloudServerDAO csdao = new  CloudServerDAO();
		cserver = csdao.getValidMasterCServer();
		HostInfoDAO hidao = new HostInfoDAO();
		localhost = hidao.getHostInfo();
	}
	
	private int checkCloudServer(CloudServer cserver){
		int flag	=	CloudServer.OFFLINE;
		DebugTool.showConsole("Begin Check CloudServer .........");
		if (cserver!=null)
		{
			CloudServerHolder holder = new CloudServerHolder();
			int cs_sshflag = holder.sshPortValid(cserver);
			int cs_webflag = holder.webPortValid(cserver);
			//int cs_sshlogin = 0;
			int cs_heartbeat = 0;
			if (cs_webflag ==1 ) cs_heartbeat = holder.heartBeatValid(cserver);
			//if (cs_sshflag ==1 ) cs_sshlogin = holder.sshLoginValid(cserver);
			

			if (cs_sshflag==1  && cs_webflag==1 && cs_heartbeat==1) //&& cs_sshlogin==1
				flag = CloudServer.ONLINE;
		}else
			elog.log("CloudServer is NULL...");
		DebugTool.showConsole("End Check CloudServer .........");
		return flag;
	}
	
	private void checkLocalhost(HostInfo localhost){
	
		DebugTool.showConsole("Begin Check HostInfo .._.......");
		if (localhost!=null)
		{
			HostInfoHolder holder = new HostInfoHolder();
			int hi_dbflag = holder.dbPortValid(localhost);
			int hi_webflag = holder.webPortValid(localhost);
			int hi_sshflag = holder.sshPortValid(localhost);
			TInfoBeanDAO dao = new TInfoBeanDAO();
			dao.validTInfo(localhost.getHiDbPort(),hi_dbflag );
			dao.validTInfo(localhost.getHiWebPort(),hi_webflag );
			dao.validTInfo(localhost.getHiSshPort(),hi_sshflag );
			
		}else
			elog.log("Hostinfo is NULL...");
		DebugTool.showConsole("End Check HostInfo .........");
	}
	
	private int isOpen(String  mapPort,Map<String,Integer> mapPortOpens){
		int isopen = 0;
		if (mapPort!=null && mapPortOpens!=null)
		{
			isopen = mapPortOpens.get(mapPort);
		}
		return isopen;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		if (checkCloudServer(cserver)==CloudServer.ONLINE){
			checkLocalhost(localhost);
			TInfoBeanDAO dao = new TInfoBeanDAO();
			tiblist = dao.getAllTInfoBean();
			if (tiblist!=null && tiblist.size() > 0)
			{
				String mapPorts = "";
				for(TInfoBean tib:tiblist)
				{
					mapPorts = mapPorts + tib.getMapPort() + ",";
				}
				int len = mapPorts.length();
				mapPorts = StringUtils.substring(mapPorts, 0, len-1);
				CloudServerHolder holder = new CloudServerHolder();
				Map<String,Integer> mapPortOpens = holder.portValid(cserver, mapPorts);
				
				for(TInfoBean tib:tiblist)
				{
					
					if (!(isOpen(tib.getMapPort(),mapPortOpens)==1))
					{
						if (tib.getTiValid()==1)
						{
							CommandExecutor.execute(new TunnelDestoryCmd(tib));
							CommandExecutor.execute(new TunnelBuildCmd(tib));
						}
					}
				}
			}else
				elog.log("Without tunnel infomation."); 
			
		}
		
	}

}
