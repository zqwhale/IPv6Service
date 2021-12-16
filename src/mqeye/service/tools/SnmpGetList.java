package mqeye.service.tools;


import java.lang.NullPointerException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mqeye.service.detect.SnmpResult;

import org.apache.commons.lang.StringUtils;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;



/**
 * SnmpGetList
 */
public class SnmpGetList {
    private int version = SnmpConstants.version2c;
    private static String protocol = "udp";
    private static int port = 161;
    private static String FAIL1 = "noSuchInstance";
    private static String FAIL2 = "noSuchObject";
     
  
    
    
    public void setSnmpVersion(int ver){
    	this.version = ver ;
    }

    
    
    /**
     * 
     * @param ipAddress
     * @param community
     * @param oid
     */
    
  @SuppressWarnings("unused")
	public List<SnmpResult> snmpv2Get(String ipAddress, String community,
            List<String> oidList) {
        String address = protocol + ":" + ipAddress + "/" + port;
        CommunityTarget target = SnmpUtil.createCommunityTarget(address,
                community, version, 2 * 1000L, 3);
        DefaultUdpTransportMapping transport = null;
        Snmp snmp = null;
        List<SnmpResult> svs = new ArrayList<SnmpResult>();
        try {
            PDU pdu = new PDU();
            pdu.setType(PDU.GET);
            for (String oid : oidList) {
                pdu.add(new VariableBinding(new OID(oid)));
            					}

            transport = new DefaultUdpTransportMapping();
            transport.listen();
            snmp = new Snmp(transport);

            ResponseEvent response = snmp.send(pdu, target);
            PDU resPdu = response.getResponse();
            if (resPdu == null) {		
                DebugTool.printErr(ipAddress + ":Request time out");
            } else {					
                for (int i = 0; i < resPdu.size(); i++) {
                    VariableBinding vb = resPdu.get(i);
                    SnmpResult sv = new SnmpResult();
                    if ( StringUtils.equalsIgnoreCase(vb.getVariable().toString(),FAIL1)||
                    	StringUtils.equalsIgnoreCase(vb.getVariable().toString(),FAIL2)){		
                    	DebugTool.printErr("Cannt get SNMP Value From:" + ipAddress +"("+ vb.getOid().toString()+")");
                    	sv.setOid(vb.getOid().toString());
                    	sv.setValue("");
				              }else{
					                  sv.setOid(vb.getOid().toString());
					                  sv.setValue(vb.getVariable().toString());
				                    				}
				              svs.add(sv);
				                 }
				            }
            
        } catch(NullPointerException e){
        				DebugTool.printErr("Null Pointer Exception:" + e);
        				DebugTool.printExc(e);
        } catch (Exception e) {
        			DebugTool.printErr("SNMP GetNext Exception:" + e);
        			DebugTool.printExc(e);
        } finally {
            if (snmp != null) {
                try {
                    snmp.close();
                } catch (IOException ex1) {
                    snmp = null;
                						  }
            					}
            if (transport != null) {
                try {
                    transport.close();
                } catch (IOException ex2) {
                    transport = null;
                							}
            				}
            
        		}
        return svs ;
    }

}

