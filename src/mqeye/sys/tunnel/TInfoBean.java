package mqeye.sys.tunnel;

public class TInfoBean {
	//CloudServer Bean
	private String csCode;
	private String csName;
	private String csIpAddr;
	private String csUser;
	private String csYH;
	private String csPwd;
	private String csKL;
	private String csWebPort;
	private String csSshPort;
	private String csRSAKey;
	private String heartBeat;
	private int	 isValid;
	private int	 isMaster;
	
	//HostInfo Bean
	private String localhost="localhost";
	public String getLocalhost() {
		return localhost;
	}
	private String hiCode;
	private String hiName;
	private String hiPubIP;
	private String hiUser;
	private String hiYH;
	private String hiPwd;
	private String hiKL;
	private String hiDBPort;
	private String hiWebPort;
	private String hiSshPort;
	private String hiRSAKey;
	private String hiMac;
	private String hiSN;
	
	//TunnelInfo Bean
	private String tiCode;
	private String tiName;
	private String ActPort;
	private String MapPort;
	private int tiState;
	private int tiValid;
	
	public int getTiValid() {
		return tiValid;
	}
	public void setTiValid(int tiValid) {
		this.tiValid = tiValid;
	}
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
	public String getCsIpAddr() {
		return csIpAddr;
	}
	public void setCsIpAddr(String csIpAddr) {
		this.csIpAddr = csIpAddr;
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
	public String getCsRSAKey() {
		return csRSAKey;
	}
	public void setCsRSAKey(String csRSAKey) {
		this.csRSAKey = csRSAKey;
	}
	public String getHeartBeat() {
		return heartBeat;
	}
	public void setHeartBeat(String heartBeat) {
		this.heartBeat = heartBeat;
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
	public String getHiCode() {
		return hiCode;
	}
	public void setHiCode(String hiCode) {
		this.hiCode = hiCode;
	}
	public String getHiName() {
		return hiName;
	}
	public void setHiName(String hiName) {
		this.hiName = hiName;
	}
	public String getHiPubIP() {
		return hiPubIP;
	}
	public void setHiPubIP(String hiPubIP) {
		this.hiPubIP = hiPubIP;
	}
	public String getHiUser() {
		return hiUser;
	}
	public void setHiUser(String hiUser) {
		this.hiUser = hiUser;
	}
	public String getHiYH() {
		return hiYH;
	}
	public void setHiYH(String hiYH) {
		this.hiYH = hiYH;
	}
	public String getHiPwd() {
		return hiPwd;
	}
	public void setHiPwd(String hiPwd) {
		this.hiPwd = hiPwd;
	}
	public String getHiKL() {
		return hiKL;
	}
	public void setHiKL(String hiKL) {
		this.hiKL = hiKL;
	}
	public String getHiDBPort() {
		return hiDBPort;
	}
	public void setHiDBPort(String hiDBPort) {
		this.hiDBPort = hiDBPort;
	}
	public String getHiWebPort() {
		return hiWebPort;
	}
	public void setHiWebPort(String hiWebPort) {
		this.hiWebPort = hiWebPort;
	}
	public String getHiSshPort() {
		return hiSshPort;
	}
	public void setHiSshPort(String hiSshPort) {
		this.hiSshPort = hiSshPort;
	}
	public String getHiRSAKey() {
		return hiRSAKey;
	}
	public void setHiRSAKey(String hiRSAKey) {
		this.hiRSAKey = hiRSAKey;
	}
	public String getHiMac() {
		return hiMac;
	}
	public void setHiMac(String hiMac) {
		this.hiMac = hiMac;
	}
	public String getHiSN() {
		return hiSN;
	}
	public void setHiSN(String hiSN) {
		this.hiSN = hiSN;
	}
	public String getTiCode() {
		return tiCode;
	}
	public void setTiCode(String tiCode) {
		this.tiCode = tiCode;
	}
	public String getTiName() {
		return tiName;
	}
	public void setTiName(String tiName) {
		this.tiName = tiName;
	}
	public String getActPort() {
		return ActPort;
	}
	public void setActPort(String actPort) {
		ActPort = actPort;
	}
	public String getMapPort() {
		return MapPort;
	}
	public void setMapPort(String mapPort) {
		MapPort = mapPort;
	}
	public int getTiState() {
		return tiState;
	}
	public void setTiState(int tiState) {
		this.tiState = tiState;
	}

	
}
