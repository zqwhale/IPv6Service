package mqeye.data.vo;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import mqeye.service.tools.SnmpV3Param;

public class SnmpV3Device {
	private String dcode ;
	private String bscode ;
	private String usmuser ;
	private String seclevel;
	private String authalg ;
	private String authpass;
	private String privalg ;
	private String privpass ;
	
	// prepare columns
	private String context ;
	private String engineid ;
	private String localauthkey ;
	private String localprivkey ;
	private int timeout ;
	private int retry;
	
	// reserve columns
	private String reserve1 ;
	private String reserve2 ;
	private String reserve3 ;
	
	
	public Map<String , String> toMap( )
	{
		Map<String , String>  params = new HashMap<String , String>();
		
		params.put(SnmpV3Param.USMUser,usmuser);
		params.put(SnmpV3Param.SECLevel,seclevel);
		params.put(SnmpV3Param.AUTHAlg,authalg);
		params.put(SnmpV3Param.AUTHPass,authpass);
		params.put(SnmpV3Param.PRIVAlg, privalg);
		params.put(SnmpV3Param.PRIVPass,privpass);
		
		return params;
	}
	
	public String toString( )
	{
		String paramStr = " -v 3" ;
		if (StringUtils.equals(seclevel,"noAuthNoPriv"))
				paramStr = paramStr + " -u " + usmuser ;
		else if (StringUtils.equals(seclevel,"authNoPriv"))
				paramStr = paramStr + " -u " + usmuser 
				+ " -a " + authalg + " -A " + authpass;
		else if (StringUtils.equals(seclevel,"authNoPriv"))
				paramStr = paramStr + " -u " + usmuser 
				+ " -a " + authalg + " -A " + authpass 
				+ " -y " + privalg + " -Y "+ privpass;
		else
		{
			String uParam = (StringUtils.isNotEmpty(usmuser)? " -u " + usmuser:"");
			String aParam = (StringUtils.isNotEmpty(authalg)? " -a " + authalg:"");
			String AParam = (StringUtils.isNotEmpty(authpass)? " -A " + authpass:"");
			String xParam = (StringUtils.isNotEmpty(privalg)? " -y " + privalg:"");
			String XParam = (StringUtils.isNotEmpty(privpass)? " -Y " + privpass:"");
			paramStr = paramStr + uParam + aParam + AParam + xParam + XParam ;
		}
		
		return paramStr;
	}
	
	
	public String getDcode() {
		return dcode;
	}
	public void setDcode(String dcode) {
		this.dcode = dcode;
	}
	public String getBscode() {
		return bscode;
	}
	public void setBscode(String bscode) {
		this.bscode = bscode;
	}
	public String getUsmuser() {
		return usmuser;
	}
	public void setUsmuser(String usmuser) {
		this.usmuser = usmuser;
	}
	public String getSeclevel() {
		return seclevel;
	}
	public void setSeclevel(String seclevel) {
		this.seclevel = seclevel;
	}
	public String getAuthalg() {
		return authalg;
	}
	public void setAuthalg(String authalg) {
		this.authalg = authalg;
	}
	public String getAuthpass() {
		return authpass;
	}
	public void setAuthpass(String authpass) {
		this.authpass = authpass;
	}
	public String getPrivalg() {
		return privalg;
	}
	public void setPrivalg(String privalg) {
		this.privalg = privalg;
	}
	public String getPrivpass() {
		return privpass;
	}
	public void setPrivpass(String privpass) {
		this.privpass = privpass;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
	public String getEngineid() {
		return engineid;
	}
	public void setEngineid(String engineid) {
		this.engineid = engineid;
	}
	public String getLocalauthkey() {
		return localauthkey;
	}
	public void setLocalauthkey(String localauthkey) {
		this.localauthkey = localauthkey;
	}
	public String getLocalprivkey() {
		return localprivkey;
	}
	public void setLocalprivkey(String localprivkey) {
		this.localprivkey = localprivkey;
	}
	public int getTimeout() {
		return timeout;
	}
	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
	public int getRetry() {
		return retry;
	}
	public void setRetry(int retry) {
		this.retry = retry;
	}
	public String getReserve1() {
		return reserve1;
	}
	public void setReserve1(String reserve1) {
		this.reserve1 = reserve1;
	}
	public String getReserve2() {
		return reserve2;
	}
	public void setReserve2(String reserve2) {
		this.reserve2 = reserve2;
	}
	public String getReserve3() {
		return reserve3;
	}
	public void setReserve3(String reserve3) {
		this.reserve3 = reserve3;
	}
}
