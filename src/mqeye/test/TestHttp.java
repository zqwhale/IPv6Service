package mqeye.test;

import mqeye.service.detect.HttpResult;
import mqeye.service.tools.HttpConnect;

public class TestHttp {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
				// TODO Auto-generated method stub
				do{
						HttpResult hr = new HttpConnect().connect("http://219.231.0.139");
						System.out.println("OA time:" + hr.getTimeConsum() + " Statu:" + hr.getStatus());					
						try {
							Thread.sleep(2*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						hr = new HttpConnect().connect("http://219.231.0.187");
						System.out.println("JW N time:" + hr.getTimeConsum() + " Statu:" + hr.getStatus());					
						try {
							Thread.sleep(2*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						hr = new HttpConnect().connect("http://219.231.0.2");
						System.out.println("EMAIL  time:" + hr.getTimeConsum() + " Statu:" + hr.getStatus());					
						try {
							Thread.sleep(2*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}while(true);
		}

}
