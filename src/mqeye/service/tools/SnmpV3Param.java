package mqeye.service.tools;

import java.util.HashMap;
import java.util.Map;

import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.smi.OID;

public class SnmpV3Param{
	 public static String USMUser = "USMUser";
	 public static String SECLevel = "SECLevel"; 
	 public static String AUTHAlg = "AUTHAlg";
	 public static String AUTHPass = "AUTHPass";
	 public static String PRIVAlg = "PRIVAlg";
	 public static String PRIVPass = "PRIVPass";
	 public static String EngineID = "EngineID";
	 
	 public static Map<String,Integer> SECLevel_VALUES = new HashMap<String,Integer>(){
		 {
			 put("noAuthNoPriv",SecurityLevel.NOAUTH_NOPRIV);
			 put("authNoPriv",SecurityLevel.AUTH_NOPRIV);
			 put("authPriv",SecurityLevel.AUTH_PRIV);
		 }
	 };
	 
	 public static Map<String,OID> AUTHAlg_VALUES = new HashMap<String,OID>(){
		 {
			 put("MD5",AuthMD5.ID);
			 put("SHA",AuthSHA.ID);
		 }
	 };

	 public static Map<String,OID> PRIVAlg_VALUES = new HashMap<String,OID>(){
		 {
			 put("DES",PrivDES.ID);
			 put("AES",PrivAES128.ID);
			 put("AES128",PrivAES128.ID);
			 put("AES192",PrivAES192.ID);
			 put("AES256",PrivAES256.ID);
		 }
	 };
		 
}
