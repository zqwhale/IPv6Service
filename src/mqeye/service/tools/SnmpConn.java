package mqeye.service.tools;

import java.io.IOException;

import mqeye.service.Constant;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpConn {
	 	private static int version = SnmpConstants.version2c;
	    private static String protocol = "udp";
	    private static int port = 161;
    /**
     * 创建共同体对象communityTarget
     * @param address
     * @param community
     * @return CommunityTarget
     */
    
    
    public static String Connect(String ip, String community){
    	String flag = Constant.PING_FAIL;
    	 String address = protocol + ":" + ip + "/" + port;
         CommunityTarget target = SnmpUtil.createCommunityTarget(address,
                 community, version, 2 * 1000L, 3);
         DefaultUdpTransportMapping udpTransportMapping = null;
         Snmp snmp = null;
         
        
        try {
        	PDU pdu = new PDU();
            pdu.add(new VariableBinding(new OID("")));
            pdu.setType(PDU.GET);

        	udpTransportMapping = new DefaultUdpTransportMapping();
			udpTransportMapping.listen();
			snmp =  new Snmp(udpTransportMapping);
			
            ResponseEvent response = snmp.send(pdu, target);
            System.out.println("PeerAddress:" + response.getPeerAddress());
            PDU responsePdu = response.getResponse();
            if (responsePdu != null) {
            	flag = Constant.SNMP_SUCCESS ;
            } else {
            	flag = new Ping().ping(ip);
            }
                
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        

        
        return flag ;
    }
    
}
