package mqeye.service.serial;

import mqeye.service.tools.CrcHexUtil;
//Switch Value get
public class SensorReader {
	
	protected static boolean checkPassByCRC( byte[] result )
	{
		if (result	==null || result.length <=2 )
				return false ;
		else
		{
			byte[] values = result ;
			int length = values.length;
			byte[]   crcBytes = {0,0};  
			System.arraycopy( result, length - 2 , crcBytes, 0, 2 );
			String crc1 = CrcHexUtil.Bytes2HexString(crcBytes);
			byte[] tempBytes =new byte[length-2];
			System.arraycopy( result, 0 , tempBytes, 0, length - 2 );
			String crc2 = CrcHexUtil.CRC16(tempBytes);
			return (crc1.equals(crc2));
		}
	}
	
	protected static int getLine8Status(byte value , int line )		
	{
		byte[] pattern = {(byte)0x00,(byte)0x01,(byte)0x02,(byte)0x04,(byte)0x08,(byte)0x10,(byte)0x20,(byte)0x40,(byte)0x80};
		return ((value & pattern[line])==(byte)pattern[line]?1:0) ; 
	}
	protected static int getLine16Status(byte vlow , byte vhigh , int line )		
	{	int r = 1;
		if (line>=1 && line<=8) 
			r = getLine8Status(vlow, line);
		else if ( line>=9 && line <=16 )
		{
			int i = line - 8;
			r = getLine8Status(vhigh, i);
		}
		else
			System.out.println( "data bit is 1-16 , the " + line + " is error!");
		return r;
	}
	public static void main(String[] args) {
		
		System.out.println( getLine16Status( (byte)0x01 , (byte)0x00 , 1 ) );
		System.out.println( getLine16Status( (byte)0x02 , (byte)0x00 , 2 ) );
		System.out.println( getLine16Status( (byte)0x03 , (byte)0x00 , 1 ) );
		System.out.println( getLine16Status( (byte)0x03 , (byte)0x00 , 2 ) );
		System.out.println("----------------");
		System.out.println( getLine16Status( (byte)0x00 , (byte)0x40 , 15 ) );
		System.out.println( getLine16Status( (byte)0x00 , (byte)0x60 , 15 ) );
		System.out.println( getLine16Status( (byte)0x00 , (byte)0x60 , 12 ) );
		System.out.println( getLine16Status( (byte)0x00 , (byte)0x80 , 16 ) );
		
		
	}
	

	
}
