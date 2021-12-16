package mqeye.comm.serial.server;

import mqeye.comm.tool.HexUtils;


public class PortConfig{
	private String  key;
	private String spName ;
	private String ipAdd ;
	private int port ;
	private String modbus ;
	
	private String[] cmds;
	
	private int rlen = 1;
	private int delay = 1000;
	
	
	
	public byte[] getByteData( String strValue)
	{
		if (modbus.equals("RTU"))
			return HexUtils.convert(strValue);
		else
			return strValue.getBytes();
	}
	public String getStringData( byte[] bytValue)
	{
		if (modbus.equals("RTU"))
			return HexUtils.convert(bytValue);
		else
			return new String(bytValue);
	}
	
	public int getRlen() {
		return rlen;
	}

	public void setRlen(int rlen) {
		this.rlen = rlen;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getModbus() {
		return modbus;
	}

	public void setModbus(String modbus) {
		this.modbus = modbus;
	}

	public String[] getCmds() {
		return cmds;
	}

	public void setCmds(String[] cmds) {
		this.cmds = cmds;
	}

	public PortConfig(String key, String spName , String ipAdd  , int port ,String modbus ,String[] cmds , int rlen , int delay ){
		this.key = key;
		this.spName = spName ;
		this.ipAdd = ipAdd ;
		this.port = port;
		this.modbus = modbus;
		this.cmds = cmds;
		this.rlen = rlen;
		this.delay = delay ;
	}
	
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getPort() {
		return port;
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

	

	
	
	
}
