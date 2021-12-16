package mqeye.data.cv;

import java.util.Date;

import mqeye.data.vo.Dsview;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

import org.apache.commons.lang.StringUtils;


/* �������豸  ͨ�� �������� ���Ӷ˿� ת���� */
public class SvllConvert extends AbstractConvert {
	
	private static long MAX_VALUE = 4294967295L;		// Counter32 ���ֵ  /* MAX_VALUE ��ֵ�д�ȷ��*/
	
	/* ���ɼ������ݰ���Ϣת��Ϊ������Ϣ��ת����ʽ:
	 * ((��ǰ�ɼ����ݰ� - ǰһ�βɼ����ݰ�) )/������� */
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
				long diff = (current.getTime() - recent.getTime())/1000 ;	/* ����ʱ���*/
				if ( diff < 60 ) diff = 60 ;   /* ʱ������С60��*/
				String rvalue2 = ml.getValue2();
				rvalue2 = (StringUtils.isNotBlank(rvalue2)?rvalue2:"0");
				value2 = (StringUtils.isNotBlank(value2)? value2:"0");
				
				long bt = Long.parseLong(value2) - Long.parseLong(rvalue2) ;
				
				bt = (bt>=0?bt:bt+MAX_VALUE);	 /* ���㴫���ֽ���  */
				
				long ll = bt / diff ;
				cvalue = String.valueOf(ll);
			}
		}
		return cvalue;
	}
	
	
}
