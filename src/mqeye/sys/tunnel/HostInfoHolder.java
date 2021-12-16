package mqeye.sys.tunnel;

import org.apache.commons.lang.StringUtils;

import mqeye.service.detect.PortResult;
import mqeye.service.tools.ErrorLog;
import mqeye.service.tools.PortConnect;


public class HostInfoHolder {
	private ErrorLog elog = new ErrorLog(this.getClass());
	
	private int portValid(String port)
	{	int flag = 0;
		if (StringUtils.isNotEmpty(port) && StringUtils.isNumeric(port))
		{	PortConnect conn = new PortConnect();
			String localhost = "localhost";
			PortResult r = conn.connect( localhost ,  port );
			if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
				flag = 1;
			else
				elog.log(localhost + " " + port + " Port is Close!");
		}else
			elog.log("localhost port value is error.");
		
		return flag;
	}
	public int dbPortValid( HostInfo host )
	{
		return portValid(host.getHiDbPort());
	}
	
	public int webPortValid( HostInfo host )
	{
		return portValid(host.getHiWebPort());
	}

	public int sshPortValid( HostInfo host )
	{
		return portValid(host.getHiSshPort());
	}
	
	
	
}
