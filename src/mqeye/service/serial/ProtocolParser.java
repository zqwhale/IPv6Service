package mqeye.service.serial;

import java.util.HashMap;
import java.util.Map;

import mqeye.service.tools.DebugTool;
import mqeye.service.tools.CrcHexUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;



public class ProtocolParser {
	Map<String, String> confs = new HashMap<String, String>();
	
	public void initParser(Map<String, String> confs){
			this.confs = confs ;
	}
	public void initParser(String key , String value){
			confs.put(key, value) ;
	}
	public void initParser(String paramStr){
			for(String valuePair: StringUtils.split(paramStr,",")){
				if (StringUtils.contains(valuePair , "="))
				{
					String[] datas = StringUtils.split(valuePair,"=");
					if (datas.length >= 2)
					{
						String key =  StringUtils.split(valuePair,"=")[0].trim();
						String valueStr =  StringUtils.split(valuePair,"=")[1].trim();
					
						if (StringUtils.equals(key, ProtocolCode.PARAMS_CHECK) || 
							StringUtils.equals(key, ProtocolCode.PARAMS_PATTERN) ||
							StringUtils.equals(key, ProtocolCode.PARAMS_ENDCHAR)	||
							StringUtils.equals(key, ProtocolCode.PARAMS_STARTCHAR)||
							StringUtils.equals(key, ProtocolCode.PARAMS_SHORTADDR))
							
						{
							confs.put(key, valueStr);
						}
					}
				}
			}
	}
	
	public String getPattern()
	{
		String pattern = confs.get(ProtocolCode.PARAMS_PATTERN);
		return pattern ;
	}
	
	public String chksum(String cmdStr){
		String dataStr = StringUtils.remove(cmdStr, "#CRC#");
		byte[] dataByt = dataStr.getBytes();
		String crc = CrcHexUtil.CHKSUM(dataByt);
		cmdStr = StringUtils.replace(cmdStr, "#CRC#", crc);
		return cmdStr;
	}
	private String crc16(String cmdStr){
		String dataStr = StringUtils.remove(cmdStr, "#CRC#");
		byte[] databyt = HexUtils.convert(dataStr);
		String crc =  CrcHexUtil.CRC16(databyt);
		cmdStr = StringUtils.replace(cmdStr, "#CRC#", crc);
		return cmdStr;
	}
	private String shortAddr(String cmdStr , String shortAddr){
		if (StringUtils.isEmpty(shortAddr)) shortAddr = "01";
		cmdStr = cmdStr.trim().toUpperCase();
		if (StringUtils.contains(cmdStr, "?"))
			cmdStr = StringUtils.replace( cmdStr, "?" , shortAddr);
		return cmdStr;
	}
	
	
	private String endChar(String cmdStr , String endChar){
		if (StringUtils.isEmpty(endChar)) endChar = ProtocolCode.NONE;
		if (!StringUtils.equalsIgnoreCase(endChar, ProtocolCode.NONE)) 
		{
			endChar = StringUtils.replace(endChar, "<cr>", "\r");
			endChar = StringUtils.replace(endChar, "<lf>", "\n");
			cmdStr = cmdStr + endChar ;
		}

		return cmdStr ;
	}
	
	private String startChar(String cmdStr , String startChar){
		if (StringUtils.isEmpty(startChar)) startChar = ProtocolCode.NONE;
		if (!StringUtils.equalsIgnoreCase(startChar, ProtocolCode.NONE)) 
		{
			cmdStr =  startChar + cmdStr; 
		}
		return cmdStr;
	}
	private byte[] asctoHex( String cmdasc ){
		byte[] cmdhex= cmdasc.getBytes();
		return cmdhex;
	}
	
	private byte[] rtutoHex(String cmdrtu){
		byte[] cmdhex = HexUtils.convert(cmdrtu);
		return cmdhex;
	}
	
	public String convertCmdStr(String cmdStr){
		String cmd = "";
		String pattern = confs.get(ProtocolCode.PARAMS_PATTERN);
		byte[] hexcmd = convertCmd(cmdStr);
		if (StringUtils.equalsIgnoreCase(pattern,ProtocolCode.ASCII))
		{
			cmd = hextoAsc(hexcmd);
		}else if (StringUtils.equalsIgnoreCase(pattern,ProtocolCode.RTU))
		{
			cmd = hextoRtu(hexcmd);
		}else{
			
		}
		return cmd;
	}
	
	public byte[] convertCmd(String cmdStr){
		byte[] cmdhex = null ;
		if (StringUtils.isNotEmpty(cmdStr))
		{		
			
			String pattern = confs.get(ProtocolCode.PARAMS_PATTERN);
			if (StringUtils.equalsIgnoreCase(pattern,ProtocolCode.ASCII))
			{
				String shortAddr =  confs.get(ProtocolCode.PARAMS_SHORTADDR);
				cmdStr = shortAddr(cmdStr , shortAddr);
				String check = confs.get(ProtocolCode.PARAMS_CHECK);
				if (StringUtils.equalsIgnoreCase(check, ProtocolCode.CHKSUM))
					cmdStr = chksum(cmdStr);

				String endChar = confs.get(ProtocolCode.PARAMS_ENDCHAR);
				cmdStr = endChar(cmdStr , endChar); 
				String startChar = confs.get(ProtocolCode.PARAMS_STARTCHAR);
				cmdStr = startChar(cmdStr , startChar);
				
				cmdhex = asctoHex(cmdStr );
			}
			else if (StringUtils.equalsIgnoreCase(pattern,ProtocolCode.RTU))
			{
				String shortAddr =  confs.get(ProtocolCode.PARAMS_SHORTADDR);
				cmdStr = shortAddr(cmdStr , shortAddr);
				String check = confs.get(ProtocolCode.PARAMS_CHECK);
				if (StringUtils.equalsIgnoreCase(check, ProtocolCode.CRC16))
					cmdStr = crc16(cmdStr);
				// now not support crc8 , crc16 ........
				
				cmdhex = rtutoHex(cmdStr);
			}else
			{				// zigbee not need cmd!
			}
			
		}	
		return cmdhex;
	}
	
	
	public boolean checkRTUCmd(String cmdStr)
	{
		int len = 0;
		if (StringUtils.contains(cmdStr, "?")) { len = len + 2; cmdStr = StringUtils.remove(cmdStr, "?");}
		if (StringUtils.contains(cmdStr, "#CRC#")) { len = len + 4;cmdStr = StringUtils.remove(cmdStr, "#CRC#");}
		len = len + cmdStr.length();
		
		int flag = 0 ;
		for(int i=0;i< cmdStr.length();i++)
		{
			char ch = cmdStr.charAt(i);
			if ((ch >='0' && ch<='9') || (ch>='A' && ch<='F') || (ch>='a' && ch<='f'))
				continue;
			else
			{	flag = 1 ; break;	}
		}
		
		if ((len & 1)==1) DebugTool.printErr("Error Odd bits Cmd........");
		if (flag==1) DebugTool.printErr("Bad hexadecimal digit for HexCmd.........");
		
		return ((len&1)==0 ) && (flag==0);
	}
	
	private String hextoAsc( byte[] hexresult)
	{
		String result = null;
		if (hexresult!=null && hexresult.length > 0)
			result = new String(hexresult);
		return result;
	}

	private String hextoRtu( byte[] hexresult){
		String result = null;
		if (hexresult!=null && hexresult.length > 0)
			result = HexUtils.convert(hexresult);
		return result;
	}
	
	private boolean checkByCrc16( byte[] hexresult)
	{
		boolean flag = false;
		if (hexresult!=null && hexresult.length > 2)
		{	int length = hexresult.length ;
			byte[]   crcBytes = {0,0};	
			System.arraycopy( (byte[])hexresult, length - 2 , crcBytes, 0, 2 );
			String crc1 = CrcHexUtil.Bytes2HexString(crcBytes);
			
			byte[] tempBytes =new byte[length-2];
			System.arraycopy( (byte[])hexresult, 0 , tempBytes, 0, length - 2 );
			String crc2 = CrcHexUtil.CRC16(tempBytes);
			
			flag = (StringUtils.equalsIgnoreCase(crc1, crc2));
		}
		
		return flag;  
	}
	
	private boolean checkByCrc( byte[] hexresult){	//zigbee device crc
		boolean flag = false;
		if (hexresult!=null && hexresult.length > 2)
		{	int length = hexresult.length ;
			byte   crc1 = hexresult[length-1];	
			
			byte[] temp = new byte[length-1];
		 	System.arraycopy( hexresult, 0, temp, 0, length-1 );
		 	byte crc2 = CrcHexUtil.CRC(temp,length-1);
		 	flag = (crc2==crc1);
		}
		return flag;  
	}
	
	public String convertResult(byte[] hexresult){
		byte[] result = null;
		String check = confs.get(ProtocolCode.PARAMS_CHECK);
		if (StringUtils.equalsIgnoreCase(check, ProtocolCode.CRC16) && checkByCrc16(hexresult)){
			result = hexresult ;
		}
		if (StringUtils.equalsIgnoreCase(check, ProtocolCode.CRC) && checkByCrc(hexresult)){
			result = hexresult ;
		}
		if (	StringUtils.equalsIgnoreCase(check, ProtocolCode.NONE)||
				StringUtils.equalsIgnoreCase(check, ProtocolCode.CRC8)||
				StringUtils.equalsIgnoreCase(check, ProtocolCode.CRC32)||
				StringUtils.equalsIgnoreCase(check, ProtocolCode.CHKSUM)){
			result = hexresult ;
		}
		
		String resultStr = null;
		String pattern = confs.get(ProtocolCode.PARAMS_PATTERN);
		if (StringUtils.equalsIgnoreCase(pattern,ProtocolCode.ASCII))
		{
			resultStr = hextoAsc(result);
		}else
		{
			resultStr = hextoRtu(result);
		}
		return resultStr;
	}
	
	public static void main ( String[] args )  {
		
		
		
		
		String cmdStr = "Q1#CRC#";
		System.out.println(new ProtocolParser().chksum(cmdStr));
		 cmdStr = "210160430000#CRC#";
		System.out.println(new ProtocolParser().chksum(cmdStr));
		 
	}
	
}
