package mqeye.data.cv;

import java.util.Date;

import mqeye.data.vo.Dsview;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

import org.apache.commons.lang.StringUtils;


/* 交换机设备  通用 流量数据 ，子端口 转换类 */
public class SvllConvert extends AbstractConvert {
	
	private static long MAX_VALUE = 4294967295L;		// Counter32 最大值  /* MAX_VALUE 的值有待确定*/
	
	/* 将采集的数据包信息转换为流量信息，转换公式:
	 * ((当前采集数据包 - 前一次采集数据包) )/间隔秒数 */
	@Override
	public String convert(Dsview dsv, String value1 ,String value2) {
		// TODO Auto-generated method stub
		String cvalue = "0" ;
		if (StringUtils.isNotEmpty(value2)){
			String dcode = dsv.getDCode();
			String svcode = dsv.getSVCode() ;
			String subport = dsv.getSubPort() ;
			MonitorlogDAO dao = new MonitorlogDAO();
			Monitorlog ml = dao.getMonitorlog(dcode , svcode , subport);
			if (ml!=null){
				Date recent = BaseCommonFunc.getDateTimeFromStr(ml.getMLDateTime());
				Date current = new Date();
				long diff = (current.getTime() - recent.getTime())/1000 ;	/* 计算时间差*/
				if ( diff < 60 ) diff = 60 ;   /* 时间间隔最小60秒*/
				String rvalue2 = ml.getValue2();
				rvalue2 = (StringUtils.isNotBlank(rvalue2)?rvalue2:"0");
				value2 = (StringUtils.isNotBlank(value2)? value2:"0");
				
				long bt = Long.parseLong(value2) - Long.parseLong(rvalue2) ;
				
				bt = (bt>=0?bt:bt+MAX_VALUE);	 /* 计算传输字节数  */
				
				long ll = bt / diff ;
				cvalue = String.valueOf(ll);
			}
		}
		return cvalue;
	}
	
	
}
