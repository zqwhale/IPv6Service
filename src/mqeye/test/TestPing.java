package mqeye.test;

import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.service.tools.CrcHexUtil;
public class TestPing {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//01030400fe01f89bd1
		byte[] a = new byte[]{(byte) 0x00,(byte) 0xfe};
		double dd = (short)CrcHexUtil.Bytes2Integer(a)/10.0;
		System.out.println(dd);
		
//		List<Device> list = new ArrayList<Device>();
//		
//		DeviceDAO dao = new DeviceDAO();
//		
//		list.add(dao.getValidByPK("D00130"));
//		list.add(dao.getValidByPK("D00131"));
//		
//		
//		for(Device d:list){
//				d.setDName("Test");
//		}
//
//		for(Device d:list){
//			System.out.println(d.getDName());
//		}

	}

}
