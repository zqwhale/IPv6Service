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
		// ���һ���¶��쳣�澯��Ϣ
		
		wl = new Warmlog();
		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
		wl.setWMCode(12);
		wl.setWMLevel("��Ҫ");
		wl.setDCode("KT00001");
		wl.setSVCode("SVWD");
		content = "InRow RD 10KW Air Cooled 50Hz[192.168.1.3:]:�����¶��쳣,��ǰ�ɼ�ֵΪ:" + (35+Math.abs(random.nextInt())%10) ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 	    // ���һ��UPS״̬�쳣�澯��Ϣ
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(8);
 		wl.setWMLevel("����");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVUPSZT");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:����UPS״̬�쳣,��ǰ�ɼ�ֵΪ:" + 3 ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(9);
 		wl.setWMLevel("����");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVGDSJ");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:����UPS���ʣ��ʱ���쳣,��ǰ�ɼ�ֵΪ:04:30:15" ;
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
 		wl = new Warmlog();
 		wl.setWMDateTime(BaseCommonFunc.getStrFromDateTime(new Date()));
 		wl.setWMCode(10);
 		wl.setWMLevel("����");
 		wl.setDCode("UPS00001");
 		wl.setSVCode("SVDCDL");
 		content = "APC InfraStruXure Symmetra 160K[192.168.1.2:]:����UPS���ʣ������쳣,��ǰ�ɼ�ֵΪ:" + (50 + Math.abs(random.nextInt())%20);
 		wl.setWMContent(content);
 		wl.setClosed(0);
 		wls.add(wl);
 		
		dao.recordToDB(wls);
	}
}
