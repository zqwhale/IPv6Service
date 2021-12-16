package mqeye.service.local.config;

public class CpuInfoBean {
	
	private String socketDesignation = "";  //CPU 插槽
	private String cpuType = ""; //CPU类别
	private String cpuFamily= ""; //CPU系列
	private String cpuManufactory = ""; //CPU生产厂家
	private String cpuID = ""; //CPU物理编号
	
	public String getSocketDesignation() {
		return socketDesignation;
	}
	public void setSocketDesignation(String socketDesignation) {
		this.socketDesignation = socketDesignation;
	}
	public String getCpuType() {
		return cpuType;
	}
	public void setCpuType(String cpuType) {
		this.cpuType = cpuType;
	}
	public String getCpuFamily() {
		return cpuFamily;
	}
	public void setCpuFamily(String cpuFamily) {
		this.cpuFamily = cpuFamily;
	}
	public String getCpuManufactory() {
		return cpuManufactory;
	}
	public void setCpuManufactory(String cpuManufactory) {
		this.cpuManufactory = cpuManufactory;
	}
	public String getCpuID() {
		return cpuID;
	}
	public void setCpuID(String cpuID) {
		this.cpuID = cpuID;
	}

	
	
}
