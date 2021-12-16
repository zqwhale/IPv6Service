package mqeye.sys.video;

public class CameraBean {
	
	private String cmCode ;
	private String cmName ;
	private String cmIPAddr ;
	
	private String cmPort ;
	
	public String getCmPort() {
		return cmPort;
	}
	public void setCmPort(String cmPort) {
		this.cmPort = cmPort;
	}
	private String cmuser ;
	private String cmpwd ;
	private String cmyh ;
	private String cmkl ;
	
	private String inputParam ;
	private String inputUrl ;

	private String outputLocalParam ;
	private String outputLocalUrl ;
	private String outputRemoteParam ;
	private String outputRemoteUrl ;
	
	private String httpRemoteUrl ;
	private String httpLocalUrl ;
	
	private int isValid ;
	private int isLive ;
	
	private String liveDateTime ;
	
	public String getLiveDateTime() {
		return liveDateTime;
	}
	public void setLiveDateTime(String liveDateTime) {
		this.liveDateTime = liveDateTime;
	}
	public String getCmCode() {
		return cmCode;
	}
	public void setCmCode(String cmCode) {
		this.cmCode = cmCode;
	}
	public String getCmName() {
		return cmName;
	}
	public void setCmName(String cmName) {
		this.cmName = cmName;
	}

	public String getCmIPAddr() {
		return cmIPAddr;
	}
	public void setCmIPAddr(String cmIPAddr) {
		this.cmIPAddr = cmIPAddr;
	}
	public String getCmuser() {
		return cmuser;
	}
	public void setCmuser(String cmuser) {
		this.cmuser = cmuser;
	}
	public String getCmpwd() {
		return cmpwd;
	}
	public void setCmpwd(String cmpwd) {
		this.cmpwd = cmpwd;
	}
	public String getCmyh() {
		return cmyh;
	}
	public void setCmyh(String cmyh) {
		this.cmyh = cmyh;
	}
	public String getCmkl() {
		return cmkl;
	}
	public void setCmkl(String cmkl) {
		this.cmkl = cmkl;
	}
	public String getInputParam() {
		return inputParam;
	}
	public void setInputParam(String inputParam) {
		this.inputParam = inputParam;
	}
	public String getInputUrl() {
		return inputUrl;
	}
	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}
	public String getOutputLocalParam() {
		return outputLocalParam;
	}
	public void setOutputLocalParam(String outputLocalParam) {
		this.outputLocalParam = outputLocalParam;
	}
	public String getOutputLocalUrl() {
		return outputLocalUrl;
	}
	public void setOutputLocalUrl(String outputLocalUrl) {
		this.outputLocalUrl = outputLocalUrl;
	}
	public String getOutputRemoteParam() {
		return outputRemoteParam;
	}
	public void setOutputRemoteParam(String outputRemoteParam) {
		this.outputRemoteParam = outputRemoteParam;
	}
	public String getOutputRemoteUrl() {
		return outputRemoteUrl;
	}
	public void setOutputRemoteUrl(String outputRemoteUrl) {
		this.outputRemoteUrl = outputRemoteUrl;
	}
	
	public String getHttpRemoteUrl() {
		return httpRemoteUrl;
	}
	public void setHttpRemoteUrl(String httpRemoteUrl) {
		this.httpRemoteUrl = httpRemoteUrl;
	}
	public String getHttpLocalUrl() {
		return httpLocalUrl;
	}
	public void setHttpLocalUrl(String httpLocalUrl) {
		this.httpLocalUrl = httpLocalUrl;
	}
	public int getIsValid() {
		return isValid;
	}
	public void setIsValid(int isValid) {
		this.isValid = isValid;
	}
	public int getIsLive() {
		return isLive;
	}
	public void setIsLive(int isLive) {
		this.isLive = isLive;
	}
	
	
	
}
