package mqeye.service.detect;

import org.apache.commons.lang.StringUtils;


public class SubPort {
	
	private String oid ;
	private String subPort ;
	private String subPortName ;
	
	public String getSubPortName() {
		return subPortName;
	}
	public void setSubPortName(String subPortName) {
		this.subPortName = subPortName;
	}
	public SubPort( SnmpResult sv , String targetOid){
		this.oid = (StringUtils.startsWith(sv.getOid(),".")?sv.getOid():"."+sv.getOid());
		this.subPort = StringUtils.remove(this.oid, targetOid+".");
		this.subPortName = sv.getValue() ;
	}
	public SubPort( String subPort , String subPortName){
		this.subPort = subPort;
		this.subPortName = subPortName ;
	}
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getSubPort() {
		return subPort;
	}
	public void setSubPort(String subPort) {
		this.subPort = subPort;
	}
	
}
