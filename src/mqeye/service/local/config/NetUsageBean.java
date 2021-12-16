package mqeye.service.local.config;

public class NetUsageBean {
	private String ethid ; 
	private String Ip ;
	private String state ;
	private long timePoint = 0;	//collect time point
	
	private float netUsage = 0.0f; //Important, Memory Usage Rate
	private float inBound = 0;	//inBound Total
	private float outBound = 0; //inBound free

	
	public String getIp() {
		return Ip;
	}
	public void setIp(String ip) {
		Ip = ip;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	public String getEthid() {
		return ethid;
	}
	public void setEthid(String ethid) {
		this.ethid = ethid;
	}
	public long getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}
	public float getNetUsage() {
		return netUsage;
	}
	public void setNetUsage(float netUsage) {
		this.netUsage = netUsage;
	}
	public float getInBound() {
		return inBound;
	}
	public void setInBound(float inBound) {
		this.inBound = inBound;
	}
	public float getOutBound() {
		return outBound;
	}
	public void setOutBound(float outBound) {
		this.outBound = outBound;
	}
	
	
}
