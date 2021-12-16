package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang.StringUtils;

public class HtmlCodeUtil
{
    /**
     * 按给定的网址得到页面代码
     * @param httpUrl
     */
	public static String DEFAULT_OUTPUT_ENCODING ="UTF-8";


 
    public static String doPost(String url, Map<String, String> headParams, Map<String, String> params, String charset) {  
        StringBuffer response = new StringBuffer();  
        HttpClient client = new HttpClient();  
        PostMethod method = new PostMethod(url);  
        // 设置Http Post数据  
        method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=" + charset);  
        if(params != null){  
            Set<String> keySet = params.keySet();  
            NameValuePair[] param = new NameValuePair[keySet.size()];  
            int i = 0;  
            for(String key : keySet){  
                param[i] = new NameValuePair(key, params.get(key));  
                i++;  
            }  
            method.setRequestBody(param);  
        } 
        
        if(headParams!=null){  
            for(String key : headParams.keySet()){  
            	method.setRequestHeader(key, headParams.get(key));
            }  
        } 
        InputStream responseBodyStream = null;  
        InputStreamReader streamReader = null;  
        BufferedReader reader = null;  
        try {  
            client.executeMethod(method);  
            if (method.getStatusCode() == HttpStatus.SC_OK) {  
                responseBodyStream = method.getResponseBodyAsStream();  
                streamReader = new InputStreamReader(responseBodyStream, charset);  
                reader = new BufferedReader(streamReader);  
                String line;  
                while ((line = reader.readLine()) != null) {  
                    response.append(line);  
                }  
            }else{
            	System.out.println("method.getStatusCode()--->"+method.getStatusCode());
            }  
        } catch (IOException e) {  
        } finally {  
            try {  
                responseBodyStream.close();  
                streamReader.close();  
                reader.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
            method.releaseConnection();  
        }  
        return response.toString();  
    }  
    
   public static String readUrl(String url , Map<String,String> params )
   {
	   	Map<String,String> headParams = new HashMap<String,String>();
		String backString = "";
		try {
			backString = HtmlCodeUtil.doPost(	url, headParams, params, "UTF-8");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error Url!");
		}
		return backString;
   }
    
    public static void main(String[] args) {
    	// 获取时间
    	String url = "http://219.231.0.211:8080/MQeyeYun/private/business/interfaze.zm";
		Map<String,String> params = new HashMap<String,String>();
		params.put("doAction", "getServerTime");
		String result = readUrl(url,params);
		if (StringUtils.isNotEmpty(result))
				System.out.println(readUrl(url,params));
		
		// 获取端口状态
		
		params = new HashMap<String,String>();
		params.put("doAction", "checkPortState");
		params.put("ports", "22");

		System.out.println(readUrl(url,params));
		
	}
}
