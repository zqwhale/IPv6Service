package mqeye.service.serial;

public class SerialUsb  {

	
	private String usbPhyId ="";
	private String usbCode ="";
	
	public String getUsbPhyId() {
		return usbPhyId;
	}
	public void setUsbPhyId(String usbPhyId) {
		this.usbPhyId = usbPhyId;
	}
	public String getUsbCode() {
		return usbCode;
	}
	public void setUsbCode(String usbCode) {
		this.usbCode = usbCode;
	}
	
	
	public String toString(){
		String str = usbCode + "(" + usbPhyId + ")";
		return str ;
	}
}
