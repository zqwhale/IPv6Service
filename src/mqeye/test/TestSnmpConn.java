package mqeye.test;

import mqeye.service.tools.SnmpConn;

public class TestSnmpConn {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		System.out.println(SnmpConn.Connect("172.16.254.4", "Public"));		//2表示SNMP可连通\ 1 表示Ping 通\  0 表示 Ping不通
		
		
	}
}
