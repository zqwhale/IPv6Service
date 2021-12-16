package mqeye.data.vo;

import java.util.HashMap;
import java.util.Map;

import mqeye.service.serial.ProtocolCode;
import mqeye.service.serial.SerialPortParam;
import mqeye.service.serial.SerialUsbCfgTool;

import org.apache.commons.lang.StringUtils;

public class SerialDevice {
	
	private String dcode ;
	private String bscode ;
	private String protocol ;
	private String sysport ;
	
	private String cfgport ;
	private String phyid ;
	
	private int rate ;
	private int databits=8;
	private int stopbits=1 ;
	private int delayread=100 ;
	private int timeout=1000;
	private int parity=0 ;
	
	private int flowcontrol=0 ;
	
	private String shortaddr="01";
	public String getShortaddr() {
		return shortaddr;
	}

	public void setShortaddr(String shortaddr) {
		this.shortaddr = shortaddr;
	}
	private String checkalg = ProtocolCode.NONE;
	private String pattern = ProtocolCode.RTU;
	private String startchar  = ProtocolCode.NONE;
	private String endchar = ProtocolCode.NONE;
	
	private String reserve1 ;
	private String reserve2 ;
	private String reserve3 ;
	

	public Map<String , String> toConfMap( )
	{
		Map<String , String>  confs = new HashMap<String , String>();
		
		confs.put(ProtocolCode.PARAMS_PATTERN,pattern);
		confs.put(ProtocolCode.PARAMS_CHECK,checkalg);
		confs.put(ProtocolCode.PARAMS_SHORTADDR,shortaddr);
		confs.put(ProtocolCode.PARAMS_STARTCHAR,startchar);
		confs.put(ProtocolCode.PARAMS_ENDCHAR,endchar);
		
		return confs;
	}

	public Map<String , String> toParamMap( )
	{
		Map<String , String>  params = new HashMap<String , String>();
		
		params.put(SerialPortParam.PARAMS_PORT,sysport);
		params.put(SerialPortParam.PARAMS_RATE,rate+"");
		params.put(SerialPortParam.PARAMS_DATABITS,databits+"");
		params.put(SerialPortParam.PARAMS_STOPBITS,stopbits +"");
		params.put(SerialPortParam.PARAMS_PARITY,parity+"");
		params.put(SerialPortParam.PARAMS_FLOWCTL,flowcontrol+"");
		params.put(SerialPortParam.PARAMS_DELAY, delayread +"");
		params.put(SerialPortParam.PARAMS_TIMEOUT,timeout+"");
		return params;
	}
	
	private String maptoString( Map<String, String> ms){
		String str = "";
		for(Map.Entry<String, String> entry:ms.entrySet())
		{
			str = str + entry.getKey() + "=" + entry.getValue() + ",";
		}
		str =StringUtils.removeEnd(str, ",");
		return str;
		
	}
	public String toParamString( )
	{
		return maptoString(toParamMap());
	}
	
	public String toConfString( )
	{
		return maptoString(toConfMap());
	}
	
	
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getBscode() {
		return bscode;
	}
	public void setBscode(String bscode) {
		this.bscode = bscode;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	public String getCfgport() {
		return cfgport;
	}
	public void setCfgport(String cfgport) {
		this.cfgport = cfgport;
	}
	public String getPhyid() {
		return phyid;
	}
	public String getSysport( ){
			SerialUsbCfgTool tool = new SerialUsbCfgTool();
			this.sysport = tool.getSerialUsbCode(phyid);
			return sysport;
	}
	public void setPhyid(String phyid) {
		this.phyid = phyid;
		SerialUsbCfgTool tool = new SerialUsbCfgTool();
		this.sysport = tool.getSerialUsbCode(phyid);
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	public int getDatabits() {
		return databits;
	}
	public void setDatabits(int databits) {
		this.databits = databits;
	}
	public int getStopbits() {
		return stopbits;
	}
	public void setStopbits(int stopbits) {
		this.stopbits = stopbits;
	}
	public int getDelayread() {
		return delayread;
	}
	public void setDelayread(int delayread) {
		this.delayread = delayread;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getParity() {
		return parity;
	}
	public void setParity(int parity) {
		this.parity = parity;
	}
	public int getFlowcontrol() {
		return flowcontrol;
	}
	public void setFlowcontrol(int flowcontrol) {
		this.flowcontrol = flowcontrol;
	}
	public String getCheckalg() {
		return checkalg;
	}
	public void setCheckalg(String checkalg) {
		this.checkalg = checkalg;
	}
	public String getPattern() {
		return pattern;
	}
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}
	public String getStartchar() {
		return startchar;
	}
	public void setStartchar(String startchar) {
		this.startchar = startchar;
	}
	public String getEndchar() {
		return endchar;
	}
	public void setEndchar(String endchar) {
		this.endchar = endchar;
	}
	
}
