package mqeye.sys.video;

import org.apache.commons.lang.StringUtils;

public class CloudVideo {
	
	private String cvCode;
	private String cvName;
	private String cvIPAddr;
	private String cvWebPort;
	private String cvProtocol;
	private String cvMdPort;

	private int isValid;
	private int isMaster;
	
	public String getCvCode() {
		return cvCode;
	}
	public void setCvCode(String cvCode) {
		this.cvCode = cvCode;
	}
	public String getCvName() {
		return cvName;
	}
	public void setCvName(String cvName) {
		this.cvName = cvName;
	}
	public String getCvIPAddr() {
		return cvIPAddr;
	}
	public void setCvIPAddr(String cvIPAddr) {
		this.cvIPAddr = cvIPAddr;
	}
	public String getCvWebPort() {
		return cvWebPort;
	}
	public void setCvWebPort(String cvWebPort) {
		this.cvWebPort = cvWebPort;
	}
	public String getCvProtocol() {
		return cvProtocol;
	}
	public void setCvProtocol(String cvProtocol) {
		this.cvProtocol = cvProtocol;
	}
	public String getCvMdPort() {
		return cvMdPort;
	}
	public void setCvMdPort(String cvMdPort) {
		this.cvMdPort = cvMdPort;
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
