package mqeye.service;

public class MQeyeError {
	private int errState = 0;	/* 0��ʾ����,1��ʾ�ռ�ض���,2��ʾ����IP ............*/
	private String errMsg = "";	/* ""��ʾ���� ............*/
	private String errDCode = null; /* ��ʾ���������豸��� */
	
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
