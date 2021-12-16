package mqeye.service;

public class MQeyeError {
	private int errState = 0;	/* 0表示正常,1表示空监控对象,2表示错误IP ............*/
	private String errMsg = "";	/* ""表示正常 ............*/
	private String errDCode = null; /* 表示引起错误的设备编号 */
	
	public int getErrState() {
		return errState;
	}
	public void setErrState(int errState) {
		this.errState = errState;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
	public String getErrDCode() {
		return errDCode;
	}
	public void setErrDCode(String errDCode) {
		this.errDCode = errDCode;
	}
}
