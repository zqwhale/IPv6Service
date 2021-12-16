package mqeye.data.vo;

	/**
	*	*@author yangsj
	*/

	public class Service{
	private String SVCode;
	private String SVName;
	private String SVDesc;
	private int WMCode;
	private String WMName;
	private String WMLevel;
	private String Reserve1;
	private String Reserve2;
	private String Reserve3;

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
	public String getSVDesc(){
		return this.SVDesc;
	}
	public void setSVDesc(String SVDesc){
		this.SVDesc=SVDesc;
	}
	public int getWMCode(){
		return this.WMCode;
	}
	public void setWMCode(int WMCode){
		this.WMCode=WMCode;
	}
	public String getWMName(){
		return this.WMName;
	}
	public void setWMName(String WMName){
		this.WMName=WMName;
	}
	public String getWMLevel(){
		return this.WMLevel;
	}
	public void setWMLevel(String WMLevel){
		this.WMLevel=WMLevel;
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

}