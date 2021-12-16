package mqeye.sys.tunnel;

public class CloudServer {
	public static int ONLINE = 1;
	public static int OFFLINE = 0;
	private String csCode;
	private String csName;
	private String csIPAddr;
	private String csUser;
	private String csYH;
	private String csPwd;
	private String csKL;
	private String csWebPort;
	private String csSshPort;
	private String csRsaKey;
	private String HeartBeat ;
	private int isValid;
	private int isMaster;
	
	public String getCsCode() {
		return csCode;
	}
	public void setCsCode(String csCode) {
		this.csCode = csCode;
	}
	public String getCsName() {
		return csName;
	}
	public void setCsName(String csName) {
		this.csName = csName;
	}
	public String getCsIPAddr() {
		return csIPAddr;
	}
	public void setCsIPAddr(String csIPAddr) {
		this.csIPAddr = csIPAddr;
	}
	public String getCsUser() {
		return csUser;
	}
	public void setCsUser(String csUser) {
		this.csUser = csUser;
	}
	public String getCsYH() {
		return csYH;
	}
	public void setCsYH(String csYH) {
		this.csYH = csYH;
	}
	public String getCsPwd() {
		return csPwd;
	}
	public void setCsPwd(String csPwd) {
		this.csPwd = csPwd;
	}
	public String getCsKL() {
		return csKL;
	}
	public void setCsKL(String csKL) {
		this.csKL = csKL;
	}
	public String getCsWebPort() {
		return csWebPort;
	}
	public void setCsWebPort(String csWebPort) {
		this.csWebPort = csWebPort;
	}
	public String getCsSshPort() {
		return csSshPort;
	}
	public void setCsSshPort(String csSshPort) {
		this.csSshPort = csSshPort;
	}
	public String getCsRsaKey() {
		return csRsaKey;
	}
	public void setCsRsaKey(String csRsaKey) {
		this.csRsaKey = csRsaKey;
	}
	public String getHeartBeat() {
		return HeartBeat;
	}
	public void setHeartBeat(String heartBeat) {
		HeartBeat = heartBeat;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public int getIsMaster() {
		return isMaster;
	}
	public void setIsMaster(int isMaster) {
		this.isMaster = isMaster;
	}
	
	
	
	
	
}
