package mqeye.service.detect;

import mqeye.service.Constant;


/* 获取 HTTPConnect 采集的信息
 * 
 * */
public class HttpResult {
	
	private long timeConsum = Constant.HTTP_TIMEOUT ;
	private String status = Constant.HTTP_ERR_STATE ;
	private String url = null;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getTimeConsum() {
		return timeConsum;
	}
	public void setTimeConsum(long timeConsum) {
		this.timeConsum = timeConsum;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
