package mqeye.service.vmware;

public class VMConnResult {
	public final static String NORMAL = "0";
	public final static String WARM = "1";
	public final static String EXCEPT = "2";
	private String vmCode = null;
	public String getVmCode() {
		return vmCode;
	}

	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}

	private String state = EXCEPT;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
}
