package mqeye.service.local.config;

public class CpuInfoBean {
	
	private String socketDesignation = "";  //CPU ���
	private String cpuType = ""; //CPU���
	private String cpuFamily= ""; //CPUϵ��
	private String cpuManufactory = ""; //CPU��������
	private String cpuID = ""; //CPU������
	
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
