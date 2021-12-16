package mqeye.service.local.config;

public class MemUsageBean {
	 	
	private long timePoint = 0;	//collect time point
	private float memUsage = 0.0f; //Important, Memory Usage Rate
	private long memtotal = 0;	//Memory Total
	private long memfree = 0; //Memory free
	public long getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}
	public float getMemUsage() {
		return memUsage;
	}
	public void setMemUsage(float memUsage) {
		this.memUsage = memUsage;
	}
	public long getMemtotal() {
		return memtotal;
	}
	public void setMemtotal(long memtotal) {
		this.memtotal = memtotal;
	}
	public long getMemfree() {
		return memfree;
	}
	public void setMemfree(long memfree) {
		this.memfree = memfree;
	}

}
