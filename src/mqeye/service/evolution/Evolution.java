package mqeye.service.evolution;


public class Evolution {
	private String evoDateTime ;
	private String dcode ;
	private String method ;
	private String pingFlag ;
	private float pingTimeConsum ;
	private String pingStatus ;
	private long pingLosePacket ;
	private float pingLosePacketRate ;
	
	
	public String getEvoDateTime() {
		return evoDateTime;
	}
	public void setEvoDateTime(String evoDateTime) {
		this.evoDateTime = evoDateTime;
	}
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getPingFlag() {
		return pingFlag;
	}
	public void setPingFlag(String pingFlag) {
		this.pingFlag = pingFlag;
	}
	public float getPingTimeConsum() {
		return pingTimeConsum;
	}
	public void setPingTimeConsum(float pingTimeConsum) {
		this.pingTimeConsum = pingTimeConsum;
	}
	public String getPingStatus() {
		return pingStatus;
	}
	public void setPingStatus(String pingStatus) {
		this.pingStatus = pingStatus;
	}
	public long getPingLosePacket() {
		return pingLosePacket;
	}
	public void setPingLosePacket(long pingLosePacket) {
		this.pingLosePacket = pingLosePacket;
	}
	public float getPingLosePacketRate() {
		return pingLosePacketRate;
	}
	public void setPingLosePacketRate(float pingLosePacketRate) {
		this.pingLosePacketRate = pingLosePacketRate;
	}
	
}
