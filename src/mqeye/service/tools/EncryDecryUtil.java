package mqeye.service.tools;



public class EncryDecryUtil {
	public static String MyEncry(String data){
		String backStr ="";
		try {
			String FirstEncryData = Base64.encodeBytes(data.getBytes());
			String LastEncryData = DecodeUtil.Encrypt(FirstEncryData);
			backStr = LastEncryData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return backStr;
	}
	
	public static String MyDecry(String data){
		String backStr ="";
		try {
			String FirstEncryData = DecodeUtil.Decrypt(data);
			String LastEncryData = new String(Base64.decode(FirstEncryData));
			backStr = LastEncryData;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return backStr;
	}
	
	public static void main(String[] args) {
//		String test = "MQeye@DB7.7";
//		String Edata = MyEncry(test);//加密
//		System.out.println(Edata);
		String Edata = "263a4b8a2541035b101e112c1d0b6b0c";
		String Ddata = MyDecry(Edata);//解密
		System.out.println(Ddata);
		
	}
}
