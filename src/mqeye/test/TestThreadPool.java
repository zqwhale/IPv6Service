package mqeye.test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.tomcat.util.buf.HexUtils;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

class TestTask implements Runnable{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		try {
			System.out.println(BaseCommonFunc.getStrFromDateTime(new Date()));
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
public class TestThreadPool {
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		 int coreCpuNum = Runtime.getRuntime().availableProcessors(); 						/* 记录服务器上的CPU核数*/
//		 ScheduledExecutorService  executor = Executors.newScheduledThreadPool(coreCpuNum);	/* 线程池服务器 */
//		 
//		 executor.scheduleAtFixedRate( new TestTask(),0,10,TimeUnit.SECONDS);
//		 executor.scheduleAtFixedRate( new TestTask(),2,10,TimeUnit.SECONDS);
		
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0xA0^0x00)})); 
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0xA0^0x01)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x01^0x00)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x07^0x08)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x17^0x10)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x1A^0x13)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x1C^0x1D)}));
		System.out.println(HexUtils.convert(new byte[]{(byte)~(0x85^0x0E^0x00)}));
	}

}
