package mqeye.comm.tool;



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
		String test = "mqeyemqeye";
		String Edata = MyEncry(test);//加密
		System.out.println(Edata);
		//7565c9105f7d19dbb05f4e2311261f38235e5f5d4f2e04
		//f2c603f2335e562528422538544bce3a25d20204022117053b35793e162fd37925705d17373a2207737835eb4e18604b7670441a
		String Ddata = MyDecry("7565c9105f7d19dbb05f4e2311261f38235e5f5d4f2e04");//解密
		System.out.println(Ddata);
	}
}
