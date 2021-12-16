package mqeye.test;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.ICMD;
import mqeye.service.tools.BaseCommonFunc;

public class TestExport {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long pre=System.currentTimeMillis();
		MonitorlogDAO dao = new MonitorlogDAO();
		Date today = new Date();
		Calendar   calendar   =   new   GregorianCalendar(); 
		calendar.setTime(today); 
		calendar.add(Calendar.DATE,-1);
		Date ed = calendar.getTime(); 
		calendar.add(Calendar.DATE,-1);
		Date bd	=	calendar.getTime(); 
		String begin = BaseCommonFunc.getStrFromDateTime(bd);
		String end = BaseCommonFunc.getStrFromDateTime(ed);
		System.out.println(begin + ":" + end);
		List<Monitorlog> ml = dao.getMonitorlogByDate(begin, end);
		String fileName = BaseCommonFunc.getStrFromDate(ed);
		dao.exportByJSON("log/" + fileName+".log", ml);
		dao.removeMonitorlogByDate(begin, end);
		long post =System.currentTimeMillis();
		System.out.println(post-pre);
		
		System.out.println(StringUtils.removeStartIgnoreCase("START JHJ00001", ICMD.START));
	}

}
