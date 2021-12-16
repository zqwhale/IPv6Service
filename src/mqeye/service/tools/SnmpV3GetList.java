package mqeye.service.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import mqeye.service.detect.SnmpResult;

import org.apache.commons.lang.StringUtils;
import org.snmp4j.PDU;
import org.snmp4j.ScopedPDU;
import org.snmp4j.Snmp;
import org.snmp4j.UserTarget;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.MPv3;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.security.SecurityModels;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.security.USM;
import org.snmp4j.security.UsmUser;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

public class SnmpV3GetList {  
	 
    private static PDU createGetPdu(OctetString contextEngineId , List<String> oidList) {  
	        ScopedPDU pdu = new ScopedPDU();  
	        pdu.setType(PDU.GET);  
	        pdu.setContextEngineID(contextEngineId);    //if not set, will be SNMP engine id  
	        //pdu.setContextName(contextName);  //must be same as SNMP agent  
	        for (String oid : oidList) {
	        	
                pdu.add(new VariableBinding(new OID(oid)));
            	}
	        return pdu;  
    }  
 
    public static  List<SnmpResult> snmpv3Get( String ipAddress, Map<String , String> params, List<String> oidList)
    {
    	
    	String usmuser = (params.get(SnmpV3Param.USMUser)!=null?params.get(SnmpV3Param.USMUser):"");
    	String seclevel= (params.get(SnmpV3Param.SECLevel)!=null?params.get(SnmpV3Param.SECLevel):"");
    	String authalg = (params.get(SnmpV3Param.AUTHAlg)!=null?params.get(SnmpV3Param.AUTHAlg):"");
    	String authpass = (params.get(SnmpV3Param.AUTHPass)!=null?params.get(SnmpV3Param.AUTHPass):"        ");
    	String privalg = (params.get(SnmpV3Param.PRIVAlg)!=null?params.get(SnmpV3Param.PRIVAlg):"");
    	String privpass = (params.get(SnmpV3Param.PRIVPass)!=null?params.get(SnmpV3Param.PRIVPass):"        ");
    	String engineID = (params.get(SnmpV3Param.EngineID)!=null?params.get(SnmpV3Param.EngineID):"");
    
    	Snmp snmp = null;
    	DefaultUdpTransportMapping transport = null;
    	List<SnmpResult> svs = null;
    	try {
    		transport = new DefaultUdpTransportMapping();
    		
    		snmp = new Snmp(transport);
    		
			USM usm = new USM(SecurityProtocols.getInstance(), new OctetString(MPv3.createLocalEngineID()), 0);  
		    SecurityModels.getInstance().addSecurityModel(usm);
		    snmp.listen(); 
		    
		   OctetString usmuser_v = new OctetString(usmuser);
		   int seclevel_v = (SnmpV3Param.SECLevel_VALUES.get(seclevel)!=null?
				   SnmpV3Param.SECLevel_VALUES.get(seclevel):SnmpV3Param.SECLevel_VALUES.get("authNoPriv"));
			OID authalg_v =  (SnmpV3Param.AUTHAlg_VALUES.get(authalg)!=null? 
					SnmpV3Param.AUTHAlg_VALUES.get(authalg):SnmpV3Param.AUTHAlg_VALUES.get("MD5"));
			OctetString authpass_v = new OctetString(authpass);
			OID privalg_v =  (SnmpV3Param.PRIVAlg_VALUES.get(privalg)!=null? 
					SnmpV3Param.PRIVAlg_VALUES.get(privalg):SnmpV3Param.PRIVAlg_VALUES.get("DES"));
			OctetString privpass_v = new OctetString(privpass);
			OctetString contextEngineId = new OctetString(engineID);
			
			UsmUser user = new UsmUser( usmuser_v, 
		                authalg_v, authpass_v,  
		                privalg_v, privpass_v);  
			
			 snmp.getUSM().addUser(usmuser_v, user);
			 
			 UserTarget target = new UserTarget();  
			 target.setVersion(SnmpConstants.version3);  
			 target.setSecurityLevel(seclevel_v); 
			 String address = ipAddress + "/161" ;
			 target.setAddress(new UdpAddress(address));  
			 target.setSecurityName(usmuser_v);  
		    target.setTimeout(3000);    //3s  
		    target.setRetries(0);  
		   
		   
		    PDU pdu = createGetPdu(contextEngineId,oidList);
		    svs = sendRequest(snmp, pdu, target); 
		   
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  finally {
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
    private static String FAIL1 = "noSuchInstance";
    private static String FAIL2 = "noSuchObject";
    
    private static  List<SnmpResult> sendRequest(Snmp snmp, PDU pdu, UserTarget target)  
    throws IOException {  
        ResponseEvent responseEvent = snmp.send(pdu, target);  
        PDU response = responseEvent.getResponse();  
        List<SnmpResult> svs = new ArrayList<SnmpResult>();
        if (response == null) {  
        		DebugTool.printErr("TimeOut...");  
        } else {  
            if (response.getErrorStatus() == PDU.noError) {  
            	
                Vector<? extends VariableBinding> vbs = response.getVariableBindings();  
                for (VariableBinding vb : vbs) {  
                    //System.out.println(vb + " ," + vb.getVariable().getSyntaxString());  
                    SnmpResult sv = new SnmpResult();
                    if ( StringUtils.equalsIgnoreCase(vb.getVariable().toString(),FAIL1)||
                        	StringUtils.equalsIgnoreCase(vb.getVariable().toString(),FAIL2))
                    		{
                    		DebugTool.printErr("Cannt get SNMP Value :("+ vb.getOid().toString()+")");
                        sv.setOid(vb.getOid().toString());
                        sv.setValue("");
                   	}else{
                   		sv.setOid(vb.getOid().toString());
		                  sv.setValue(vb.getVariable().toString());
                   			}
                    svs.add(sv);
                	}  
            } else {  
            	DebugTool.printErr("Error:" + response.getErrorStatusText());  
            }  
        }  
      return svs ;
    }
    
    public static void main(String[] args) throws IOException, InterruptedException {
    	
    	Map<String,String> TEST_PARAMS_1 = new HashMap<String,String>();
       		
    		
    	TEST_PARAMS_1.put(SnmpV3Param.USMUser,"snmpuser");
    	TEST_PARAMS_1.put(SnmpV3Param.SECLevel,"authNoPriv");
    	TEST_PARAMS_1.put(SnmpV3Param.AUTHAlg,"MD5");
    	TEST_PARAMS_1.put(SnmpV3Param.AUTHPass,"snmppass");
//    	TEST_PARAMS_1.put(PRIVAlg, "DES");
//    	TEST_PARAMS_1.put(PRIVPass,"snmppass");
    	
    	
    	List<String> TEST_OID_1 = new ArrayList<String>();
    		
    	TEST_OID_1.add("1.3.6.1.4.1.2021.10.1.1.1");
    	TEST_OID_1.add("1.3.6.1.4.1.2021.10.1.1.2");
    	TEST_OID_1.add("1.3.6.1.4.1.2021.10.1.1.3");
    	TEST_OID_1.add("1.3.6.1.4.1.2021.10.1.2.1");
    	TEST_OID_1.add("1.3.6.1.4.1.2021.10.1.2.2");

    		
    	List<SnmpResult> list1 = snmpv3Get("219.231.0.213",TEST_PARAMS_1,TEST_OID_1);
    	System.out.println("--------------" + SnmpV3Conn.Connect("219.231.0.213", TEST_PARAMS_1));
    	
    	for(SnmpResult r:list1)
    	{
    		System.out.println(r.getOid() + "=" + r.getValue());
    	}
    	TEST_PARAMS_1.clear();
    	TEST_OID_1.clear();
    	
    	TEST_PARAMS_1.put(SnmpV3Param.USMUser,"ahjzu");
    	TEST_PARAMS_1.put(SnmpV3Param.SECLevel,"authPriv");
    	TEST_PARAMS_1.put(SnmpV3Param.AUTHAlg,"SHA");
    	TEST_PARAMS_1.put(SnmpV3Param.AUTHPass,"ahjzu@20161118");
    	TEST_PARAMS_1.put(SnmpV3Param.PRIVAlg, "AES128");
    	TEST_PARAMS_1.put(SnmpV3Param.PRIVPass,"S9312@20161118");
    	
    	TEST_OID_1.add("1.3.6.1.2.1.2.2.1.2.101");
    	TEST_OID_1.add("1.3.6.1.2.1.2.2.1.2.102");
    	TEST_OID_1.add("1.3.6.1.2.1.2.2.1.2.103");
    	TEST_OID_1.add("1.3.6.1.2.1.2.2.1.2.104");
    
    	List<SnmpResult> list2 = snmpv3Get("192.168.0.2",TEST_PARAMS_1,TEST_OID_1);
    	for(SnmpResult r:list2)
    	{
    		System.out.println(r.getOid() + "=" + r.getValue());
    	}
    	
    	System.out.println("--------------" + SnmpV3Conn.Connect("192.168.0.3", TEST_PARAMS_1));
    }  
      
   
      
    
}  
