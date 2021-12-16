package mqeye.data.vo;

public class CurrentTbl {
	private String dcode ;
	private String svcode ;
	private String subport ;
	
	public String getSubport() {
		return subport;
	}
	public void setSubport(String subport) {
		this.subport = subport;
	}
	private String mldatetime ;
	private int isWarning = 0 ;
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getSvcode() {
		return svcode;
	}
	public void setSvcode(String svcode) {
		this.svcode = svcode;
	}
	public String getMldatetime() {
		return mldatetime;
	}
	public void setMldatetime(String mldatetime) {
		this.mldatetime = mldatetime;
	}
	public int getIsWarning() {
		return isWarning;
	}
	public void setIsWarning(int isWarning) {
		this.isWarning = isWarning;
	}
	
	
}
