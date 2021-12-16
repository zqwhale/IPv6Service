package mqeye.test;

import java.util.List;

import mqeye.data.vo.BrandSpec;
import mqeye.data.vo.BrandSpecDAO;
import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;
import mqeye.sys.tunnel.TInfoBean;
import mqeye.sys.tunnel.TInfoBeanDAO;

import org.apache.commons.lang.StringUtils;

public class TestDAO {
	public void testDeviceDao(){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true){
				DeviceDAO dao = new DeviceDAO();
				System.out.println(dao.getValidByPK("D00126").getDICode());
				System.out.println(dao.getValidByPK("D00126").getSnmpCommity());
				String bscode = dao.getValidByPK("D00126").getBSCode();
				BrandSpecDAO bsDao = new BrandSpecDAO();
				BrandSpec brand = bsDao.getByBSCode(bscode) ;
				System.out.println( brand.getSnmpParam() );
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dao.free();
				}
			}
		
		};
		Thread t = new Thread(r);
		t.start();
	}

	public void testDsviewDao(){
		Runnable r = new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(true){
				DsviewDAO dao = new DsviewDAO();
				List<Dsview> dsvlist = dao.getBeanByDCode("D00126");
				for(Dsview dsv : dsvlist){
					System.out.println("view:" + dsv.getDICode());
					System.out.println("view:" + dsv.getSnmpOID());
					System.out.println("view:" + dsv.getSnmpParam());
					
				}
				
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dao.free();
				}
			}
		
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	private static boolean isNormal(String dcode){
		boolean normalFlag = true ;
		boolean flag1 = false ;
		boolean flag2 = false ;
		DeviceDAO ddao = new DeviceDAO();
		Device d = ddao.getValidByPK(dcode);
		flag1 = ( d != null && d.getIsValid() == 1 );
		if  ( flag1 ) {
    		DsviewDAO dvdao = new DsviewDAO();
    		List<Dsview> dsvlist = dvdao.getBeanByDCode(dcode);
    		for(Dsview dsv:dsvlist){
    						flag2 = ( dsv.getOnOff()==1 );
    						break;
    			}
		}
		
		if (flag1 && flag2){
			MonitorlogDAO mdao = new MonitorlogDAO();
			normalFlag = mdao.hasRecentMonitorlog(dcode);
		}
		return normalFlag ;
	}
	
	public boolean isWarming( String value , String threshold , String valueType , String express){
		
		boolean flag = false;
		if (StringUtils.isEmpty(value)){
			flag = true;
		}else if (StringUtils.isEmpty(threshold) || StringUtils.isEmpty(express) || StringUtils.isEmpty(valueType)){
			flag = false ;
		}else if (StringUtils.equals(valueType, Constant.NUMBER_TYPE) && StringUtils.containsNone(threshold,",")){
			try{
			double dbValue = Double.parseDouble((StringUtils.isEmpty(value)?"0":value));
			double dbThreshold = Double.parseDouble(threshold);
			if (StringUtils.equals(express, Constant.LT))	flag = (dbValue < dbThreshold );
			if (StringUtils.equals(express, Constant.GT))	flag = (dbValue > dbThreshold );
			if (StringUtils.equals(express, Constant.LTEQ))	flag = (dbValue <= dbThreshold );
			if (StringUtils.equals(express, Constant.GTEQ))	flag = (dbValue >= dbThreshold );
			if (StringUtils.equals(express, Constant.EQ))	flag = (dbValue == dbThreshold );
			if (StringUtils.equals(express, Constant.NEQ))	flag = (dbValue != dbThreshold );
			}catch(NumberFormatException err){
				flag=false;
				DebugTool.printErr("********Warming Express dataType error! Please check config!");
			}
		}else if (StringUtils.equals(valueType, Constant.STRING_TYPE) && StringUtils.containsNone(threshold,",")){
			String strValue = (StringUtils.isEmpty(value)?"":value);
			String strThreshold = threshold ;
			if (StringUtils.equals(express, Constant.LT))	flag = (strValue.compareToIgnoreCase(strThreshold)<0);
			if (StringUtils.equals(express, Constant.GT))	flag = (strValue.compareToIgnoreCase(strThreshold)>0);
			if (StringUtils.equals(express, Constant.LTEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)<=0);
			if (StringUtils.equals(express, Constant.GTEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)>=0);
			if (StringUtils.equals(express, Constant.EQ))	flag = (strValue.compareToIgnoreCase(strThreshold)==0);
			if (StringUtils.equals(express, Constant.NEQ))	flag = (strValue.compareToIgnoreCase(strThreshold)!=0);
		
		}else if (StringUtils.equals(valueType, Constant.NUMBER_TYPE) && StringUtils.contains(threshold,","))	{
			double dbValue = Double.parseDouble((StringUtils.isEmpty(value)?"0":value));
			String[] thresholds = StringUtils.split(threshold, ",");
			if (StringUtils.equals(express, Constant.JY)&& thresholds.length>=2) 
			{	double threshold1 = Double.parseDouble(thresholds[0]);
				double threshold2 = Double.parseDouble(thresholds[1]);
				flag = (threshold1<= dbValue && dbValue <= threshold2);
			}
			if (StringUtils.equals(express, Constant.NJY)&& thresholds.length>=2) 
			{	double threshold1 = Double.parseDouble(thresholds[0]);
				double threshold2 = Double.parseDouble(thresholds[1]);
				flag = !(threshold1<= dbValue && dbValue <= threshold2);
			}
			if (StringUtils.equals(express, Constant.SY)) 
			{
				flag = ( StringUtils.contains(threshold, value));
			}
			if (StringUtils.equals(express, Constant.NSY)) 
			{
				flag = (!StringUtils.contains(threshold, value));
			}
		}else if (StringUtils.equals(valueType, Constant.STRING_TYPE) && StringUtils.contains(threshold,","))	{
			String strValue = (StringUtils.isEmpty(value)?"":value);
			String[] thresholds = StringUtils.split(threshold, ",");
			if (StringUtils.equals(express, Constant.JY)&& thresholds.length>=2) 
			{	
				boolean c1 = (strValue.compareToIgnoreCase(thresholds[0])>=0);
				boolean c2 = (strValue.compareToIgnoreCase(thresholds[1])<=0);
				flag = ( c1 && c2);
			}
			if (StringUtils.equals(express, Constant.NJY)&& thresholds.length>=2) 
			{						
				boolean c1 = (strValue.compareToIgnoreCase(thresholds[0])>=0);
				boolean c2 = (strValue.compareToIgnoreCase(thresholds[1])<=0);
				flag = !( c1 && c2);
			}
			if (StringUtils.equals(express, Constant.SY)) 
			{
				flag = ( StringUtils.contains(threshold, value));
			}
			if (StringUtils.equals(express, Constant.NSY)) 
			{
				flag = (!StringUtils.contains(threshold, value));
			}
		}	
		
		return flag ;
	}
	
	
	public void getTunnelDAO()
	{
		TInfoBeanDAO tidao =  new TInfoBeanDAO();
		for(TInfoBean ti : tidao.getAllTInfoBean())
		{
			System.out.println(ti.getHiUser() + "--" + ti.getHiPwd());
			System.out.println(ti.getCsUser() + "--" + ti.getCsPwd());
		}
	}
	
	private long span(){
		String date1 = "2017-07-21 10:10:10";
		String date2 = "2017-07-21 10:11:10";
		long d1 = BaseCommonFunc.getDateTimeFromStr(date1).getTime();
		long d2 = BaseCommonFunc.getDateTimeFromStr(date2).getTime();
		return Math.abs(d1 -d2) ;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println(new TestDAO().span());
		new TestDAO().getTunnelDAO();
		
//		System.out.println(new TestDAO().isWarming("0", "0,A,2", "字符", "不属于"));
//		System.out.println(new TestDAO().isWarming("0", "0,1,2", "字符", "属于"));
//		System.out.println(new TestDAO().isWarming("0", "0,2", "字符", "介于"));
//		System.out.println(new TestDAO().isWarming("0", "0,2", "字符", "不介于"));
//		
//		System.out.println(new TestDAO().isWarming("0", "0,A,2", "数值", "不属于"));
//		System.out.println(new TestDAO().isWarming("0", "0,1,2", "数值", "属于"));
//		System.out.println(new TestDAO().isWarming("0", "0,2", "数值", "介于"));
//		System.out.println(new TestDAO().isWarming("0", "0,2", "数值", "不介于"));

		
	
	}

}
