package mqeye.test;

import mqeye.service.tools.SnmpConn;

public class TestSnmpConn {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(SnmpConn.Connect("172.16.254.4", "Public"));		//2��ʾSNMP����ͨ\ 1 ��ʾPing ͨ\  0 ��ʾ Ping��ͨ
		
		
	}
}
