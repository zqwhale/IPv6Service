package mqeye.data.vo;

	/**
	*	*@author zq.whale
	*/

	public class Device{
	private String DCode;
	private String BSCode;
	private String DName;
	private String TPCode ;
	
	private String DICode;
	private String Position;
	private String DDesc;
	private String IPAddr;
	private String SnmpCommity;
	private int IsValid;
	private int State;
	private String Reserve1;
	private String Reserve2;
	private String Reserve3;
	private int DefaultLoop ;

	public int getDefaultLoop() {
		return DefaultLoop;
	}
	public void setDefaultLoop(int defaultLoop) {
		DefaultLoop = defaultLoop;
	}
	public String getTPCode() {
		return TPCode;
	}
	public void setTPCode(String tPCode) {
		TPCode = tPCode;
	}
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
		return this.IsValid;
	}
	public void setIsValid(int IsValid){
		this.IsValid=IsValid;
	}
	public int getState(){
		return this.State;
	}
	public void setState(int State){
		this.State=State;
	}
	public String getReserve1(){
		return this.Reserve1;
	}
	public void setReserve1(String Reserve1){
		this.Reserve1=Reserve1;
	}
	public String getReserve2(){
		return this.Reserve2;
	}
	public void setReserve2(String Reserve2){
		this.Reserve2=Reserve2;
	}
	public String getReserve3(){
		return this.Reserve3;
	}
	public void setReserve3(String Reserve3){
		this.Reserve3=Reserve3;
	}
	public String toString(){
		String devStr = "";
		devStr = this.DCode + ":" + this.DName + ":" + this.TPCode + ":" + this.IPAddr ;
		return devStr ;
	}
}