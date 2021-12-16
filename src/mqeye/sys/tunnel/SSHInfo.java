package mqeye.sys.tunnel;

public class SSHInfo {
	private String sshSrv ;
	private String sshUser ;
	private String sshPort ;
	private String sshPwd ;
	
	private String sshCmd = "date" ;
	private String sshMsg ;
	
	
	public String getSshCmd() {
		return sshCmd;
	}
	public void setSshCmd(String sshCmd) {
		this.sshCmd = sshCmd;
	}
	public String getSshMsg() {
		return sshMsg;
	}
	public void setSshMsg(String sshMsg) {
		this.sshMsg = sshMsg;
	}
	public String getSshSrv() {
		return sshSrv;
	}
	public void setSshSrv(String sshSrv) {
		this.sshSrv = sshSrv;
	}
	public String getSshUser() {
		return sshUser;
	}
	public void setSshUser(String sshUser) {
		this.sshUser = sshUser;
	}
	public String getSshPort() {
		return sshPort;
	}
	public void setSshPort(String sshPort) {
		this.sshPort = sshPort;
	}
	public String getSshPwd() {
		return sshPwd;
	}
	public void setSshPwd(String sshPwd) {
		this.sshPwd = sshPwd;
	}
	

}
