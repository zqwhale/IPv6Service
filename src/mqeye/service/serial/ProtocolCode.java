package mqeye.service.serial;

public class ProtocolCode{
	public final static String DEFAULT_PARAM = "pattern=RTU, check=none ,endchar = none , startchar = none";
	
	public final static String RTU = "RTU";
	public final static String ASCII = "ASCII";

	public static final String PARAMS_SHORTADDR = "shortaddr";
	public static final String PARAMS_CHECK = "check";  //none |crc crc8 crc16 crc32 | cs У���  | DES  | HS ��ϣֵ
   public static final String PARAMS_PATTERN = "pattern" ; // ASCII | RTU(HEX) | none
   public static final String PARAMS_STARTCHAR = "startchar" ; 
   public static final String PARAMS_ENDCHAR = "endchar" ; // <cr> | <lf>
    
   public static final String NONE = "none";
   public static final String CRC = "crc";	//zigbee device
   public static final String CRC16 = "crc16";
   public static final String CHKSUM = "chksum";
   public static final String CRC8 = "crc8";
   public static final String CRC32 = "crc32";
   public static final String CS = "cs";
   public static final String DES = "des";
   public static final String HS = "hs";
   
}
