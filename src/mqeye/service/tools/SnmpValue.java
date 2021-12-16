package mqeye.service.tools;


/* 获取的  OID 和  Value 键值对
 */
public class SnmpValue {
	private String oid ;
	private String value ;
	
	
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
