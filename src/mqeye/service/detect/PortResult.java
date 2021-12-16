package mqeye.service.detect;

import mqeye.service.tools.PortConnect;

public class PortResult {
	private String port = null;
	private long timeConsum = PortConnect.PORT_TIMEOUT ;
	private String status = "closed" ;
	
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public long getTimeConsum() {
		return timeConsum;
	}
	public void setTimeConsum(long timeConsum) {
		this.timeConsum = timeConsum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
