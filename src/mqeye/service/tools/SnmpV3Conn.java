package mqeye.service.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import mqeye.service.Constant;
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

public class SnmpV3Conn {
	 	
    /**
     * 创建共同体对象communityTarget
     * @param address
     * @param community
     * @return CommunityTarget
     */
    private static String TEST_OID = "";
     
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
    
    private static  String sendRequest(Snmp snmp, PDU pdu, UserTarget target)  
    throws IOException {  
    	  String flag = "UNKNOW";
        ResponseEvent responseEvent = snmp.send(pdu, target);  
        PDU response = responseEvent.getResponse();  
      
        if (response != null && response.getErrorStatus() == PDU.noError) {  
        			flag = Constant.SNMP_SUCCESS ;
        	} 
       
      return flag ;
    }

    
    public static String Connect(String ip, Map<String, String> params){
    				String flag = Constant.PING_FAIL;
    	 
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
        			 String address = ip + "/161" ;
        			 target.setAddress(new UdpAddress(address));  
        			 target.setSecurityName(usmuser_v);  
        		    target.setTimeout(3000);    //3s  
        		    target.setRetries(0);  
        		   
        		    List<String> oidList = new ArrayList<String>();
        		    oidList.add(TEST_OID);
        		    PDU pdu = createGetPdu(contextEngineId,oidList);
        		    flag = sendRequest(snmp, pdu, target); 
        		    if (StringUtils.equals(flag, "UNKNOW")) 
        		    				flag = new Ping().ping(ip);
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
        
        return flag ;
    }
    
}
