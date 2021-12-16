package mqeye.comm.tool;

import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.OutputStream;  
import java.util.Enumeration;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.Properties;  
  
public class PropertiesUtil {  
  
    private Properties props;  
    private String fileName;  
    public PropertiesUtil(String fileName){  
        this.fileName=fileName;  
        readProperties(fileName);  
    }
    
    private void readProperties(String fileName) {  
        try {  
            props = new Properties();  
            FileInputStream fis =new FileInputStream(fileName);  
            props.load(fis);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    public Properties getProperty( ){
    	return props;  
    }
    /** 
     * ��ȡĳ������ 
     */  
    public String getProperty(String key){  
        return props.getProperty(key);  
    }  
    /** 
     * ��ȡ�������ԣ�����һ��map,������ 
     * ��������props.putAll(t) 
     */  
    public Map<String, String> getAllProperty(){  
        Map<String, String> map=new HashMap<String, String>();  
        Enumeration<?> enu = props.propertyNames();  
        while (enu.hasMoreElements()) {  
            String key = (String) enu.nextElement();  
            String value = props.getProperty(key);  
            map.put(key, value);  
        }  
        return map;  
    }  
    /** 
     * �ڿ���̨�ϴ�ӡ���������ԣ�����ʱ�á� 
     */  
    public void printProperties(){  
        props.list(System.out);  
    }  
    /** 
     * д��properties��Ϣ 
     */  
    public void writeProperties(String key, String value) {  
        try {  
            OutputStream fos = new FileOutputStream(fileName);  
            props.setProperty(key, value);  
            props.store(fos, "��comments��Update key��" + key);  
        } catch (IOException e) {  
        }  
    }     
    public static void main(String[] args) {  
        PropertiesUtil util=new PropertiesUtil(args[0]);  
        System.out.println("serialPort=" + util.getProperty("serialPort"));  
        System.out.println("serialRate=" + util.getProperty("serialRate"));  
    	}      
}