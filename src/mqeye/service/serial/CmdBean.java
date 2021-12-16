package mqeye.service.serial;

public class CmdBean {
	private String cmdStr = null;
	private byte[] hexcmd =  null;
	private int validLen = 0;
	
	public long t_sendspan1 = 200;
	public long t_sendspan2 = 0;
	
	public long t_detectspan1 =  500;
	public long t_detectspan2 = 500;
	
	
	public CmdBean(String cmdStr , byte[] hexcmd ){
		this.cmdStr = cmdStr;
		this.hexcmd = hexcmd;
	}
	
	public CmdBean(String cmdStr , byte[] hexcmd , int len){
		this.cmdStr = cmdStr;
		this.hexcmd = hexcmd;
		this.validLen = len ;
	}
	public int getValidLen() {
		return validLen;
	}

	public void setValidLen(int validLen) {
		this.validLen = validLen;
	}

	public String getCmdStr() {
		return cmdStr;
	}
	public void setCmdStr(String cmdStr) {
		this.cmdStr = cmdStr;
	}
	public byte[] getHexcmd() {
		return hexcmd;
	}
	public void setHexcmd(byte[] hexcmd) {
		this.hexcmd = hexcmd;
	}
}
