package mqeye.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.SnmpV3Device;
import mqeye.data.vo.SnmpV3DeviceDAO;
import mqeye.service.detect.SnmpResult;
import mqeye.service.detect.SubPort;
import mqeye.service.routine.SubPortRepairTask;
import mqeye.service.tools.SnmpWalk;

public class TestGetPort {

	/**
	 * @param args
	 */
	 public static void main(String[] args) {
		 	DeviceDAO dao = new DeviceDAO();
			//getPort1("172.16.0.25");
			getPort2("192.168.0.2");
			// getPhysicalEntity();
			 //getCPURate();
	    }

	 public static void getPort2(String ip){
		 SnmpV3DeviceDAO dao = new SnmpV3DeviceDAO();
		 SnmpV3Device s3d = dao.getSnmpV3DeviceByDCode("D00057");
		 String community = s3d.toString();
	    String targetOid = ".1.3.6.1.2.1.2.2.1.2";
	        
	    List<SnmpResult> svs = SnmpWalk.snmpWalk(ip, community, targetOid);
	    List<SubPort> sps = new ArrayList<SubPort>();
	    for(SnmpResult sv:svs){
	        	sps.add(new SubPort(sv,targetOid));
	        }
	        net.sf.json.JSONArray jsonofSubport = net.sf.json.JSONArray.fromObject(sps);
	        System.out.println(jsonofSubport.toString());
	 }
	 public static void getPort1(String ip){
		 	
	        String community = "-v 2c -c public ";
	        String targetOid = ".1.3.6.1.2.1.2.2.1.2";
	        
	        List<SnmpResult> svs = SnmpWalk.snmpWalk(ip, community, targetOid);
	        List<SubPort> sps = new ArrayList<SubPort>();
	        for(SnmpResult sv:svs){
	        	sps.add(new SubPort(sv,targetOid));
	        }
	        net.sf.json.JSONArray jsonofSubport = net.sf.json.JSONArray.fromObject(sps);
	        System.out.println(jsonofSubport.toString());
	 }
	 
	 public static void getPhysicalEntity(){
		 	String ip = "192.168.0.1";
	        String community = "-v 2c -c public";
	        String targetOid = ".1.3.6.1.2.1.47.1.1.1.1.2";
	        List<SnmpResult> svs = SnmpWalk.snmpWalk(ip, community, targetOid);
	        List<SubPort> sps = new ArrayList<SubPort>();
	        for(SnmpResult sv:svs){
	        	sps.add(new SubPort(sv,targetOid));
	        }
	        net.sf.json.JSONArray jsonofSubport = net.sf.json.JSONArray.fromObject(sps);
	        System.out.println(jsonofSubport.toString());
	 }
	 
	 public static void getCPURate(){
		 String ip = "192.168.0.1";
	        String community = "-v 2c -c public";
	        String targetOid = ".1.3.6.1.4.1.2011.5.25.31.1.1.1.1.5";
	        List<SnmpResult> svs = SnmpWalk.snmpWalk(ip, community, targetOid);
	        List<SubPort> sps = new ArrayList<SubPort>();
	        for(SnmpResult sv:svs){
	        	if (!StringUtils.equals(sv.getValue(), "0"))
	        		sps.add(new SubPort(sv,targetOid));
	        }
	        net.sf.json.JSONArray jsonofSubport = net.sf.json.JSONArray.fromObject(sps);
	        System.out.println(jsonofSubport.toString());
	 }
}
