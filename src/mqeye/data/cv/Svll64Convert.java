package mqeye.data.cv;

import java.math.BigInteger;
import java.util.Date;

import mqeye.data.vo.Dsview;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;


/* 交换机设备  通用 流量数据Counter64位 ，子端口 转换类 */
public class Svll64Convert extends AbstractConvert {
	
	private static BigInteger MAX_VALUE = new BigInteger("18446744073709551615");		// Counter64 最大值  /* MAX_VALUE 的值有待确定*/
	@Override
	public String convert(Dsview dsv, String value1, String value2) {
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
				/*  代码中没有处理十六进制的数据 */
				BigInteger biValue2 = new BigInteger(value2);
				BigInteger biRValue2 = new BigInteger(rvalue2);
				BigInteger bt = biValue2.subtract(biRValue2);
				bt = (bt.signum()>=0?bt:new BigInteger("0"));/* 计算传输字节数  */
				BigInteger biDiff = new BigInteger(diff+"");
				cvalue = bt.divide(biDiff).toString();
				DebugTool.printMsg(biValue2 + " - " + biRValue2 + "=" + bt + ":diff:" + 
						biDiff + "#value1#=" + cvalue);
				
			}
		}else{
			cvalue = null ;
		}
		return cvalue;
		
	}

}
