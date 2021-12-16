package mqeye.service.serial;
public class SerialPort{
	private String  spName;
	private String ipAdd ;
	private int port ;
	public SerialPort(String spName , String ipAdd  , int port ){
		this.spName = spName ;
		this.ipAdd = ipAdd ;
		this.port = port;
	}
	
	public String getSpName() {
		return spName;
	}
	public void setSpName(String spName) {
		this.spName = spName;
	}
	public String getIpAdd() {
		return ipAdd;
	}
	public void setIpAdd(String ipAdd) {
		this.ipAdd = ipAdd;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return port;
	}
	
	
}
