package mqeye.test;

import java.util.ArrayList;
import java.util.List;

import mqeye.service.detect.SnmpResult;
import mqeye.service.tools.SnmpGetList;
import mqeye.service.tools.SnmpWalk;

public class TestSnmp {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 List<String> oids = new  ArrayList<String>() ;
//		 oids.add(".1.3.6.1.4.1.6339.100.1.8.1.11.999");
//		 oids.add(".1.3.6.1.4.1.6339.100.1.8.1.10.999");
//		 oids.add(".1.3.6.1.2.1.2.2.1.10.1");
//		 
//		 List<SnmpResult> svs = new SnmpGetList().snmpGet("172.16.254.4","public",oids);
//		 for(SnmpResult sv:svs){
//			 System.out.println(sv.getOid() + ":" + sv.getValue());
//		 }
		 
//		 oids.add(".1.3.6.1.4.1.318.1.1.1.4.1.1.0");
//		 oids.add(".1.3.6.1.4.1.318.1.1.1.4.2.1.0");
//		 oids.add(".1.3.6.1.4.1.318.1.1.1.4.2.4.0");
//		 oids.add(".1.3.6.1.4.1.318.1.1.1.2.2.3.0");
		 

		 //oids.add(".iso.org.dod.internet.mgmt.mib-2.ifMIB.ifMIBObjects.ifXTable.ifXEntry.ifHCInOctets.27");
		 oids.add(".1.3.6.1.2.1.31.1.1.1.6.38");
		 List<SnmpResult> svs = new SnmpGetList().snmpv2Get("172.16.0.22","public",oids);
		 for(SnmpResult sv:svs){
			 System.out.println(sv.getOid() + ":" + sv.getValue());
		 }
		 
//		 oids.add(".1.3.6.1.2.1.31.1.1.1.6");
//		 List<SnmpResult> svs = new SnmpWalk().snmpWalk("172.16.0.22","public",".1.3.6.1.2.1.31.1.1.1.6");
//		 for(SnmpResult sv:svs){
//			 System.out.println(sv.getOid() + ":" + sv.getValue());
//		 }
	}

}
