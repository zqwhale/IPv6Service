package mqeye.test;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.math.BigInteger;

public class TestData {
	public static void main(String[] args) {
		byte[] datas1 = {(byte)0x02,(byte) 0x57};
		String d_str = new BigInteger(1,datas1).toString(10);
		int d = (int) (Integer.parseInt(d_str)*0.001*40);
		System.out.println(d);
		byte[] datas2 = {(byte)0x41,(byte) 0x11,(byte)0xeb,(byte)0x85};
		DataInputStream dis=new DataInputStream(new ByteArrayInputStream(datas2));
	     
		try {
			float f = dis.readFloat();
			dis.close();
			 System.out.println(f);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	     
	}
}
