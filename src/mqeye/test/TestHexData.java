package mqeye.test;

import java.math.BigDecimal;
import java.math.BigInteger;

import mqeye.service.tools.CrcHexUtil;

import org.apache.tomcat.util.buf.HexUtils;
import org.snmp4j.smi.Counter64;

public class TestHexData {
	public static boolean testString(String strValue , String strThreshold){
		return (strValue.compareToIgnoreCase(strThreshold)<0);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//				Counter64 data1 = new Counter64();
//				data1.setValue(-1);
//				System.out.println(data1);
//				Counter64 data2 = new Counter64();
//				data2.setValue(-9);
//				BigInteger value1 = new BigInteger(data1.toString());
//				BigInteger value2 = new BigInteger(data2.toString());
//				System.out.println(value1.subtract(value2));
//				System.out.println(testString("5:04:03.00","2:01:02"));
				
				
				String hexStr = "00FF000F";
				byte[] test = HexUtils.convert(hexStr);
				System.out.println(CrcHexUtil.CRC16(test));
				System.out.println(CrcHexUtil.CRC(test, 4));
				
	}

}
