package mqeye.service.tools;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mqeye.service.Constant;
import mqeye.service.detect.HttpResult;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.util.EntityUtils;


class HttpsClient {
	
	public static DefaultHttpClient getNewHttpsClient(HttpClient httpClient){

		try {
			SSLContext ctx = SSLContext.getInstance("TLS");
			X509TrustManager tm = new X509TrustManager() {
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] arg0,
						String arg1) throws CertificateException {
				}
			};
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new SSLSocketFactory(ctx,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("https", 443, ssf));
			ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
		
			return new DefaultHttpClient(mgr, httpClient.getParams());
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	
	}
}
public class HttpsConnect {
	
	public HttpResult visit( String urlstr ) {
		HttpResult  hr = new HttpResult() ;
		hr.setUrl(urlstr);
		long begin=System.currentTimeMillis();
		HttpClient httpClient=new DefaultHttpClient();
		httpClient = HttpsClient.getNewHttpsClient(httpClient);
		
		HttpGet request = new HttpGet(urlstr);  
		HttpResponse response = null;
		 try {
			 response = httpClient.execute(request);
			 int status = response.getStatusLine().getStatusCode();
          if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
            	HttpEntity mEntity = response.getEntity();
            	String html  = EntityUtils.toString(mEntity);
             }
          long end =System.currentTimeMillis();
          
			if (status>=200 && status <=299) {
				status = 200;
				hr.setStatus(status+"");
				long time = end - begin;
				if (time < Constant.HTTP_TIMEOUT)
						hr.setTimeConsum(time);
				else{
						hr.setTimeConsum(Constant.HTTP_TIMEOUT);
						DebugTool.printMsg("https time big than HTTP_TIMEOUT!");
					}
			}else{
				DebugTool.printErr("https Run state err!");
				hr.setStatus(Constant.HTTP_ERR_STATE);
				hr.setTimeConsum(Constant.HTTP_TIMEOUT);
			}
         }catch(IOException e){
        	 	DebugTool.printErr("https Exception err!");
 				hr.setStatus(Constant.HTTP_ERR_STATE);
 				hr.setTimeConsum(Constant.HTTP_TIMEOUT);
         }
         return hr;
	}
	
	public HttpResult connect(String urlstr)  {
		HttpResult r = null;
		HttpResult[] rr = new HttpResult[3];
		
		rr[0] = visit(urlstr);
		rr[1] = visit(urlstr);
		rr[2] = visit(urlstr);
		
		if(rr[0].getTimeConsum()>rr[1].getTimeConsum()){r = rr[0];rr[0]=rr[1];rr[1]=r;}
		if(rr[0].getTimeConsum()>rr[2].getTimeConsum()){r = rr[0];rr[0]=rr[2];rr[2]=r;}
		if(rr[1].getTimeConsum()>rr[2].getTimeConsum()){r = rr[1];rr[1]=rr[2];rr[2]=r;}
		
		return rr[1];
}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		HttpResult r = new HttpsConnect().connect("https://220.178.74.13/por/login_psw.csp?0.9305300621204164");
		System.out.println(r.getStatus());
		System.out.println(r.getTimeConsum());
		System.out.println(r.getUrl());
		
	}

}
