package mqeye.data.vo;

	/**
	*	*@author yangsj
	*/

	public class Deviceservicemapview{
	private String DCode;
	private String BSCode;
	private String DName;
	private String DICode;
	private String Position;
	private String DDesc;
	private String IPAddr;
	private String SnmpCommity;
	private int isValid;
	private int State;
	private String SVCode;
	private String SVName;
	private int TLoop;
	private String Threshold;
	private String SubPort;
	private int OnOff;
	private int RunStop;

	public String getDCode(){
		return this.DCode;
	}
	public void setDCode(String DCode){
		this.DCode=DCode;
	}
	public String getBSCode(){
		return this.BSCode;
	}
	public void setBSCode(String BSCode){
		this.BSCode=BSCode;
	}
	public String getDName(){
		return this.DName;
	}
	public void setDName(String DName){
		this.DName=DName;
	}
	public String getDICode(){
		return this.DICode;
	}
	public void setDICode(String DICode){
		this.DICode=DICode;
	}

	public String getPosition(){
		return this.Position;
	}
	public void setPosition(String Position){
		this.Position=Position;
	}
	public String getDDesc(){
		return this.DDesc;
	}
	public void setDDesc(String DDesc){
		this.DDesc=DDesc;
	}
	public String getIPAddr(){
		return this.IPAddr;
	}
	public void setIPAddr(String IPAddr){
		this.IPAddr=IPAddr;
	}
	public String getSnmpCommity(){
		return this.SnmpCommity;
	}
	public void setSnmpCommity(String SnmpCommity){
		this.SnmpCommity=SnmpCommity;
	}
	public int getIsValid(){
		return this.isValid;
	}
	public void setIsValid(int isValid){
		this.isValid=isValid;
	}
	public int getState(){
		return this.State;
	}
	public void setState(int State){
		this.State=State;
	}
	public String getSVCode(){
		return this.SVCode;
	}
	public void setSVCode(String SVCode){
		this.SVCode=SVCode;
	}
	public String getSVName(){
		return this.SVName;
	}
	public void setSVName(String SVName){
		this.SVName=SVName;
	}
	public int getTLoop(){
		return this.TLoop;
	}
	public void setTLoop(int TLoop){
		this.TLoop=TLoop;
	}
	public String getThreshold(){
		return this.Threshold;
	}
	public void setThreshold(String Threshold){
		this.Threshold=Threshold;
	}
	public String getSubPort(){
		return this.SubPort;
	}
	public void setSubPort(String SubPort){
		this.SubPort=SubPort;
	}
	public int getOnOff(){
		return this.OnOff;
	}
	public void setOnOff(int OnOff){
		this.OnOff=OnOff;
	}
	public int getRunStop(){
		return this.RunStop;
	}
	public void setRunStop(int RunStop){
		this.RunStop=RunStop;
	}

}