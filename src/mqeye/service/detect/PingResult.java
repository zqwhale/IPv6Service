package mqeye.service.detect;

import mqeye.service.Constant;

public class PingResult {
	private String flag = Constant.PING_FAIL;								
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	private float timeConsum = Constant.PING_TIMEOUT;		
	private String status = Constant.PING_ERR_UNREACHABLE;			
	private long losePacket = 10;																
	private float losePacketRate = 100;											
	
	public float getTimeConsum() {
		return timeConsum;
	}
	public long getLosePacket() {
		return losePacket;
	}
	public void setLosePacket(long losePacket) {
		this.losePacket = losePacket;
	}
	public float getLosePacketRate() {
		return losePacketRate;
	}
	public void setLosePacketRate(float losePacketRate) {
		this.losePacketRate = losePacketRate;
	}
	public void setTimeConsum(float timeConsum) {
		this.timeConsum = timeConsum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
