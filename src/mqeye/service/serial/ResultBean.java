package mqeye.service.serial;

public class ResultBean{
	private String cmdStr ;
	private long timeStamp ;
	private  int rtn ;
	private  byte[] result ;
	public String getCmdStr() {
		return cmdStr;
	}
	public void setCmdStr(String cmdStr) {
		this.cmdStr = cmdStr;
	}
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public int getRtn() {
		return rtn;
	}
	public void setRtn(int rtn) {
		this.rtn = rtn;
	}
	public byte[] getResult() {
		return result;
	}
	public void setResult(byte[] result) {
		this.result = result;
	}
	
	
}
