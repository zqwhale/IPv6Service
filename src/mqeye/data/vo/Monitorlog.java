package mqeye.data.vo;

import org.apache.commons.lang.StringUtils;

	/**
	*	*@author ZQ
	*/

	public class Monitorlog{
	private long MDID;
	private String MLDateTime;
	private String DCode;
	private String SVCode;
	private String SubPort;
	private String SubModule;
	private String Value1;
	private String Value2;
	private int IsWarming = 0 ;
	private String Reserve1;
	private String Reserve2;

	public long getMDID(){
		return this.MDID;
	}
	public void setMDID(long MDID){
		this.MDID=MDID;
	}
	public int getIsWarming() {
		return IsWarming;
	}
	public void setIsWarming(int isWarming) {
		this.IsWarming = isWarming;
	}
	public String getMLDateTime(){
		return this.MLDateTime;
	}
	public void setMLDateTime(String MLDateTime){
		this.MLDateTime=MLDateTime;
	}
	public String getDCode(){
		return this.DCode;
	}
	public void setDCode(String DCode){
		this.DCode=DCode;
	}
	public String getSVCode(){
		return this.SVCode;
	}
	public void setSVCode(String SVCode){
		this.SVCode=SVCode;
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
	public String getValue1(){
		return this.Value1;
	}
	public void setValue1(String Value1){
		this.Value1=Value1;
	}
	public String getValue2(){
		return this.Value2;
	}
	public void setValue2(String Value2){
		this.Value2=Value2;
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
	
	
	@Override
	public String toString(){
		return "MonitorLog(Time:" +	MLDateTime+	"|Dcode:" + DCode + "|SVCode:" + SVCode +
							"|SubPort:" + (StringUtils.isEmpty(SubPort)?"":SubPort) + 
							"|Value1:" + (StringUtils.isEmpty(Value1)?"":Value1) +
							"|Value2:" + (StringUtils.isEmpty(Value2)?"":Value2) ;
	}
}