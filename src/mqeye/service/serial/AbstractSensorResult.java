package mqeye.service.serial;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

public class AbstractSensorResult {
	
	private long timeStamp = 0;
	
	public long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	private String shortAddress ;  //¶ÌµØÖ·
	private String cmd;
	private byte[] result; 
	
	
	public String getCmd() {
		return cmd;
	}
	public void setCmd(String cmd) {
		this.cmd = cmd;
	}
	
	public String getResultStr(String pattern)
	{
		if (StringUtils.endsWithIgnoreCase(pattern, "RTU"))
		{
			return getResultRTU();
		}else
		{
			return getResultAscii();
		}
	}
	
	public String getResultRTU(){
		String rstr = "";
		if (result!=null && result.length > 0)
		{
			rstr = HexUtils.convert(result);
		}
		return rstr;
	}
	
	public String getResultAscii(){
		String ascii = "";
		if (result!=null && result.length > 0)
		{
			ascii = new String(result);
		}
		return ascii;
	}
	public byte[] getResult() {
		return result;
	}
	public void setResult(byte[] result) {
		this.result = result;
	}
	
	public String getShortAddress() {
		return shortAddress;
	}
	public void setShortAddress(String shortAddress) {
		this.shortAddress = shortAddress;
	}
}
