package mqeye.data.vo;

import org.apache.commons.lang.StringUtils;

	/**
	*	*@author yangsj
	*/

	public class Dsview{
	private String DCode;
	private String DName;
	private String Position;
	private String BSCode;
	private String TPCode;
	private String TPName;
	private String Brand;
	private String Specification;
	private String Method;
	private String IPAddr;
	private String SnmpCommity;
	private String SNMPVer;
	private String SnmpOID;
	private String SubPortOID;
	
	public String getSubPortOID() {
		return SubPortOID;
	}
	public void setSubPortOID(String subPortOID) {
		SubPortOID = subPortOID;
	}
	private String SnmpParam;
	private int isValid;
	private String SubPort;
	private String SubPortName;
	private String DICode ;
	
	public String getDICode() {
		return DICode;
	}
	public void setDICode(String dICode) {
		DICode = dICode;
	}
	public String getSubPortName() {
		return SubPortName;
	}
	public void setSubPortName(String subPortName) {
		SubPortName = subPortName;
	}
	private String SubModule;
	private String Url;

	private String SVCode;
	private String SVName;
	private int TLoop;
	private String Threshold;
	private String ValueType;
	private String ValueUnit;
	private String ValueConvert;
	public String getValueConvert() {
		return ValueConvert;
	}
	public void setValueConvert(String valueConvert) {
		ValueConvert = valueConvert;
	}
	private String WarmExpress;
	
	private int Delay;
	
	public int getDelay() {
		return Delay;
	}
	public void setDelay(int delay) {
		Delay = delay;
	}
	
	private int OnOff;
	private int RunStop;
	
	public int getWMCode() {
		return WMCode;
	}
	public void setWMCode(int wMCode) {
		WMCode = wMCode;
	}
	public String getWMName() {
		return WMName;
	}
	public void setWMName(String wMName) {
		WMName = wMName;
	}
	public String getWMLevel() {
		return WMLevel;
	}
	public void setWMLevel(String wMLevel) {
		WMLevel = wMLevel;
	}
	private int WMCode;
	private String WMName;
	private String WMLevel;
	
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	public String getDCode(){
		return this.DCode;
	}
	public void setDCode(String DCode){
		this.DCode=DCode;
	}
	public String getDName(){
		return this.DName;
	}
	public void setDName(String DName){
		this.DName=DName;
	}

	public String getPosition(){
		return this.Position;
	}
	public void setPosition(String Position){
		this.Position=Position;
	}
	public String getBSCode(){
		return this.BSCode;
	}
	public void setBSCode(String BSCode){
		this.BSCode=BSCode;
	}
	public String getTPCode(){
		return this.TPCode;
	}
	public void setTPCode(String TPCode){
		this.TPCode=TPCode;
	}
	public String getTPName(){
		return this.TPName;
	}
	public void setTPName(String TPName){
		this.TPName=TPName;
	}
	public String getBrand(){
		return this.Brand;
	}
	public void setBrand(String Brand){
		this.Brand=Brand;
	}
	public String getSpecification(){
		return this.Specification;
	}
	public void setSpecification(String Specification){
		this.Specification=Specification;
	}
	public String getMethod(){
		return this.Method;
	}
	public void setMethod(String Method){
		this.Method=Method;
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
	public String getSNMPVer(){
		return this.SNMPVer;
	}
	public void setSNMPVer(String SNMPVer){
		this.SNMPVer=SNMPVer;
	}
	public String getSnmpOID(){
		return this.SnmpOID;
	}
	public void setSnmpOID(String SnmpOID){
		this.SnmpOID=SnmpOID;
	}
	public String getSnmpParam(){
		return this.SnmpParam;
	}
	public void setSnmpParam(String SnmpParam){
		this.SnmpParam=SnmpParam;
	}
	public int getIsValid(){
		return this.isValid;
	}
	public void setIsValid(int isValid){
		this.isValid=isValid;
	}
	public String getSubPort(){
		return this.SubPort;
	}
	public void setSubPort(String SubPort){
		this.SubPort=SubPort;
	}
	public String getSubModule(){
		return this.SubModule;
	}
	public void setSubModule(String SubModule){
		this.SubModule=SubModule;
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
	public String getValueType(){
		return this.ValueType;
	}
	public void setValueType(String ValueType){
		this.ValueType=ValueType;
	}
	public String getValueUnit(){
		return this.ValueUnit;
	}
	public void setValueUnit(String ValueUnit){
		this.ValueUnit=ValueUnit;
	}
	public String getWarmExpress(){
		return this.WarmExpress;
	}
	public void setWarmExpress(String WarmExpress){
		this.WarmExpress=WarmExpress;
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
	
	public String getPK(){
		String dcodeStr =  (StringUtils.isNotEmpty(DCode) ? DCode :"");
		String svcodeStr = (StringUtils.isNotEmpty(SVCode) ? SVCode :"");
		String subportStr = (StringUtils.isNotEmpty(SubPort) ? SubPort :"");
		return dcodeStr + "_" + svcodeStr + "_" + subportStr ;
	}
}