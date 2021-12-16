package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import mqeye.service.Constant;
import mqeye.service.detect.HttpResult;

public class HttpConnect {
	
		public HttpResult visit( String urlstr ) {
			// TODO Auto-generated method stub
			HttpResult  hr = new HttpResult() ;
			hr.setUrl(urlstr);
			try {
				URL url = new URL(urlstr);
				HttpURLConnection c = (HttpURLConnection)url.openConnection();
				c.setUseCaches(false); 	
				c.setConnectTimeout(5000);
				long begin=System.currentTimeMillis();
				c.connect();
				int status = c.getResponseCode();
				BufferedReader reader = new BufferedReader(new InputStreamReader(
						c.getInputStream()));
//				System.out.println("-------------------------------------------");
//				System.out.println(" Content of get request ---");
//				System.out.println("-------------------------------------------");
				
				while((reader.readLine())!=null){
					//	System.out.println(lines);
							;
				}
				reader.close();
				c.disconnect();
//				System.out.println("-------------------------------------------");
//				System.out.println(" Content of get request ends!");
//				System.out.println("-------------------------------------------");
				long end =System.currentTimeMillis();
				
				if (status>=200 && status <=299) {
					status = 200;
					hr.setStatus(status+"");
					long time = end - begin;
					if (time < Constant.HTTP_TIMEOUT)
							hr.setTimeConsum(time);
					else{
							hr.setTimeConsum(Constant.HTTP_TIMEOUT);
							DebugTool.printMsg("http time big than HTTP_TIMEOUT!");
						}
				}else{
					DebugTool.printErr("http Run state err!");
					hr.setStatus(Constant.HTTP_ERR_STATE);
					hr.setTimeConsum(Constant.HTTP_TIMEOUT);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				DebugTool.printErr("http Exception err!");
				//DebugTool.printExc(e);
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
}
