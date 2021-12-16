package mqeye.service;

import java.util.Date;
import java.util.List;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;
import mqeye.data.vo.Dsview;
import mqeye.data.vo.DsviewDAO;
import mqeye.data.vo.Monitorlog;
import mqeye.data.vo.MonitorlogDAO;
import mqeye.data.vo.Warmlog;
import mqeye.data.vo.WarmlogDAO;
import mqeye.service.detect.AbstractDetector;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

import org.apache.commons.lang.StringUtils;




/*  ����̽���� �����Լ��ز�ͬ��̽����*/
public class MQeyeDetectTask implements Runnable {
	
	private Device device = null ;
	private List<Dsview> dslist = null;
	
	private String errMsg = "";
	public int Loop = 300;
	private boolean isReady = false ;		//���ںϸ�ķ������, ��������̽������
	
	
	private List<Monitorlog> mls = null;		//������ݴ���б�
	private List<Warmlog> wls = null;		//��ظ澯����б�
	
	public String getErrMsg(){ return errMsg; } 
	/*�������ʱ�䣬������С��ѯ���� */
	private void getLoopAndMarkStartTime(){
		if (device!=null ) 
					Loop = (device.getDefaultLoop()>0?device.getDefaultLoop():300); 
					DebugTool.printMsg(Loop + "is Loop Time");
	}					

	/*����Ʒ�Ʒ�����еļ�¼, ���Ҳ��������,����ɾ�� */
	
	
	private void refreshDevice( ){
		if (device != null){
			String dcode = device.getDCode() ;
			DeviceDAO ddao = new DeviceDAO();
			device = ddao.getValidByPK(dcode);
		}
	}
	/*  ˢ�¼���б� ȷ���ܹ��������� */
	private void refreshDsList( ){
				if (device != null){
						if (StringUtils.isBlank(device.getIPAddr())) errMsg =  device.getDName()+"(" + device.getDCode() + ")" + "��ض���IP��ַΪ��";		
						String dcode = device.getDCode() ;
						DsviewDAO dao = new DsviewDAO();
						if (dao.isNotConsist(dcode)) 
										dao.refreshService(dcode);  /*ˢ�����ݿ��з��������״̬*/
						dslist = dao.getBeanByDCode(dcode);
						if (dslist == null || dslist.size()==0 ) errMsg =  device.getDName()+"(" + device.getDCode() + ")" + "�޼�ط���"; 
						if (dslist.size() > 0) {  isReady=true; getLoopAndMarkStartTime(); }			// ѡ����ѯ������С������ 		 
				} else 
						errMsg = "��ض���Ϊ��,DEVICE IS NULL!"; 
	}
	
	/* ����̽��������*/
	public MQeyeDetectTask(Device d){
			this.device  = d ;
			refreshDsList();
	}
	
	/* �����������������������*/
	private String getMatchTypeKey(String tpcode){
			String key = null ;
			for(String t:Constant.typeNames){
						if (StringUtils.startsWith(tpcode, t)) {key = t; break;}
			}
			return key;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		/*---------------׼������̽����---------------------*/
		String detectTime = BaseCommonFunc.getStrFromDateTime(new Date());
		DebugTool.showConsole("PREPARE START DETECT :" + device.getDName() + "....." + detectTime);
		Reset r = Reset.getInstance() ;
		String dcode = device.getDCode() ;
		if (r.getFlag(dcode)){
					DebugTool.showConsole("BEGIN REFRESH :" + device.getDName() + "SERVICE");
					refreshDevice();
					refreshDsList();
					r.reset(dcode, false);
		}
		/*---------------׼������̽����---------------------*/
		
		/*---------------����̽����---------------------*/
		if (isReady){
				try {
					String tpcode = device.getTPCode();
					String tpHeadChar = getMatchTypeKey(tpcode);
					String clazz = Constant.detectMap.get(tpHeadChar);
					if (StringUtils.isNotEmpty(clazz)){
							AbstractDetector detector = (AbstractDetector)Class.forName(clazz).newInstance();
							detector.init(device, dslist);
							try{
							detector.detect();
							}catch(Exception err){
								DebugTool.printErr("****************�豸̽���쳣......");
								DebugTool.printExc(err);
							}
							mls = detector.getMonitorlog();
							wls = detector.getWarmLog();
					}else
						DebugTool.printMsg("û��֧��:" + device.getDName() + "��̽����..........");
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("�豸:" + device.getDName() + "��̽����������");
						DebugTool.printExc(e);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("�豸:" + device.getDName() + "��̽����������");
						DebugTool.printExc(e);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
						DebugTool.printErr("�豸:" + device.getDName() + "��̽����������");
						DebugTool.printExc(e);
				} 
		}else{
			DebugTool.printErr("��ض�����������δ���������!");
			DebugTool.printErr(errMsg);
		}
		/*---------------����̽����---------------------*/
		
		
		/*---------------��¼̽����������ݣ�̽�����---------------------*/
				
			 if (mls!=null && mls.size()	>	0){
				 MonitorlogDAO mdao = new MonitorlogDAO();
			 		mdao.recordToDB(mls);
			 		for(Monitorlog ml:mls)
				    	DebugTool.printMsg("*****"+ml.getMLDateTime() + ":" + ml.getDCode()+ ":" + ml.getSVCode() + ":" + ml.getValue1() + ":" + StringUtils.trimToEmpty(ml.getValue2()));
				   
			 		mls.clear();
			 }
			 if (wls!=null && wls.size() > 0){
	    WarmlogDAO wdao = new WarmlogDAO();
	    wdao.recordToDB(wls);
	    for(Warmlog wl:wls)
	    		DebugTool.printErr("*****"+wl.getWMDateTime() + ":" + wl.getDCode()+ ":" + wl.getSVCode() + ":" + wl.getWMLevel() + ":" + wl.getWMContent());
	    	wls.clear();
			 }
			
		/*---------------��¼̽����������ݣ�̽�����---------------------*/
		
	}
	

	

}
