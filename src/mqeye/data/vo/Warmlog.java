package mqeye.data.vo;

import org.apache.commons.lang.StringUtils;

	/**
	*	*@author yangsj
	*/

	public class Warmlog{
	private int WMID;
	private String WMDateTime;
	private int WMCode;
	private String WMLevel;
	private String DCode;
	private String SVCode;
	private String WMContent;
	private int Closed;
	private String Reserve1;
	private String Reserve2;

	public int getWMID(){
		return this.WMID;
	}
	public void setWMID(int WMID){
		this.WMID=WMID;
	}
	public String getWMDateTime(){
		return this.WMDateTime;
	}
	public void setWMDateTime(String WMDateTime){
		this.WMDateTime=WMDateTime;
	}
	public int getWMCode(){
		return this.WMCode;
	}
	public void setWMCode(int WMCode){
		this.WMCode=WMCode;
	}
	public String getWMLevel(){
		return this.WMLevel;
	}
	public void setWMLevel(String WMLevel){
		this.WMLevel=WMLevel;
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
	public String getWMContent(){
		return this.WMContent;
	}
	public void setWMContent(String WMContent){
		this.WMContent=WMContent;
	}
	public int getClosed(){
		return this.Closed;
	}
	public void setClosed(int Closed){
		this.Closed=Closed;
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
		return "WarmLog(Time:" +	WMDateTime+	"|Dcode:" + DCode + "|SVCode:" + SVCode +
		"|Content:" + (StringUtils.isEmpty(WMContent)?"":WMContent);
		
	}
}