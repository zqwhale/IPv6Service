package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import mqeye.service.Constant;

import org.apache.commons.lang.StringUtils;

/* ̽��ĳIP��ַ�Ƿ����Pingͨ,
 * ����  ϵͳ�汾����, ��Linux
 * ��Ҫ���²���
 * */
public class Ping {
	private String os = "WIN";
	
	public void setOs(String os) {
		this.os = os;
	}
	private static int timeout = 1000;
	public static int times = 1;
	
	
	public String ping(String ip){
		
		String flag = Constant.PING_FAIL;
		String pingCommand =  "ping " + ip + " -n " + times + " -w " + timeout; 
        if (os.equals("Linux"))
        	pingCommand = "ping " + " -c " + times + " " + ip ;
        Runtime r = Runtime.getRuntime();   
        BufferedReader in = null;  
        
        try {   
            Process p = r.exec(pingCommand);   
            if (p != null) {   
	            in = new BufferedReader(new InputStreamReader(p.getInputStream()));   
	            String line = null;   
	            while ((line = in.readLine()) != null) {   
	                if (StringUtils.contains(line, "Reply from") || 
	                	StringUtils.contains(line, "�ظ�")	|| 
	                	StringUtils.contains(line, "bytes from") ){   
	                	flag = Constant.PING_SUCCESS;
	                }   
	            }   
            }
        } catch (Exception ex) {   
            ex.printStackTrace();   
            
        } finally {   
            try {   
                in.close();   
            } catch (IOException e) {   
                e.printStackTrace();   
            }   
        }   
       
        return flag ;
		
	}
}
