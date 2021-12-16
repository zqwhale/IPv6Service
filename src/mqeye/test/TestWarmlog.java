package mqeye.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import mqeye.data.vo.Warmlog;
import mqeye.data.vo.WarmlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
 
public class TestWarmlog {
	private static Random random = new Random();
	
	public static void main(String[] args) {
		WarmlogDAO dao = new WarmlogDAO();
		List<Warmlog> wls = new ArrayList<Warmlog>();
		Warmlog wl = null;
		String content = null;
		// 添加一条温度异常告警信息
		
		wl = new Warmlog();
		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
		wl.setWMCode(12);
		wl.setWMLevel("重要");
		wl.setDCode("KT00001");
		wl.setSVCode("SVWD");
		content = "InRow RD 10KW Air Cooled 50Hz[192.168.1.3:]:出现温度异常,当前采集值为:" + (35+Math.abs(random.nextInt())%10) ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 	    // 添加一条UPS状态异常告警信息
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(8);
 		wl.setWMLevel("严重");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVUPSZT");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:出现UPS状态异常,当前采集值为:" + 3 ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(9);
 		wl.setWMLevel("严重");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVGDSJ");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:出现UPS电池剩余时间异常,当前采集值为:04:30:15" ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(10);
 		wl.setWMLevel("严重");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVDCDL");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:出现UPS电池剩余电量异常,当前采集值为:" + (50 + Math.abs(random.nextInt())%20);
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
		dao.recordToDB(wls);
	}
}
