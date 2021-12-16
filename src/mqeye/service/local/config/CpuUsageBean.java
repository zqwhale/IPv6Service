package mqeye.service.local.config;

public class CpuUsageBean {
	private String Cpuid ;   //if cpuid is "cpu", this indicate total core 	
	private long timePoint = 0;	//collect time point
	private float cupUsage = 0; //Important, CPU Usage Rate
	public String getCpuid() {
		return Cpuid;
	}
	public void setCpuid(String cpuid) {
		Cpuid = cpuid;
	}
	public long getTimePoint() {
		return timePoint;
	}
	public void setTimePoint(long timePoint) {
		this.timePoint = timePoint;
	}
	public float getCupUsage() {
		return cupUsage;
	}
	public void setCupUsage(float cupUsage) {
		this.cupUsage = cupUsage;
	}
		
		
}
