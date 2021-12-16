package mqeye.service.vmware;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

import com.vmware.vim25.AlarmInfo;
import com.vmware.vim25.AlarmState;
import com.vmware.vim25.InvalidProperty;
import com.vmware.vim25.RuntimeFault;
import com.vmware.vim25.mo.Alarm;
import com.vmware.vim25.mo.AlarmManager;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ManagedEntity;
import com.vmware.vim25.mo.ServerConnection;
import com.vmware.vim25.mo.ServiceInstance;

public class VMWareConnection {
	
	private VCenterCfg cfg ;
	private int ENTITY_NUM = 0;
	
	private ServiceInstance si = null ;
	private AlarmManager am = null ;  // Alarm Manager
	
	private Folder rf = null;	//Root Folder
	List<VMWareAlarm> alarmList = new ArrayList<VMWareAlarm>() ; // Store All Alarm
	
	public VMWareConnection(VCenterCfg cfg) throws RemoteException, MalformedURLException {
			this.cfg = cfg ;
				
			si = new ServiceInstance(new URL(cfg.getvCenterAddress()),
					cfg.getvCenterUserName(), cfg.getvCenterPwd(), true);
			
			am = si.getAlarmManager();
			rf = si.getRootFolder(); 
			ENTITY_NUM = 0;
	}
	

	public void retrieveEntitiesAlarm(String eType)
	{	
		try {
			ManagedEntity[] mes  = new InventoryNavigator(rf).searchManagedEntities(eType);
			ENTITY_NUM = ENTITY_NUM + mes.length;
			for(ManagedEntity me:mes){
				detectAlarm(me , eType);
			}
		} catch (InvalidProperty e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RuntimeFault e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void detectAlarm(ManagedEntity me , String etype)
	{
		
		AlarmState[]  ass = me.getTriggeredAlarmState();
			if (ass!=null && ass.length > 0) 
				for(AlarmState a:ass)
				{
					Alarm alarm = new Alarm(am.getServerConnection() , a.getAlarm());
					AlarmInfo ai = alarm.getAlarmInfo();
					String status = a.getOverallStatus().toString();
					
					VMWareAlarm bean = new VMWareAlarm();
					bean.setVmCode(cfg.getVmCode());
					bean.setAlarmKey(a.getKey());
					bean.setEntityName(me.getName());
					bean.setEntityType(etype);
					bean.setOverallStatus(status);
					Date tTime = a.getTime().getTime() ;
					String strTTime = BaseCommonFunc.getStrFromDateTime( tTime ) ;
					bean.setTriggerTime( tTime!=null ? strTTime : null );
					bean.setAlarmName( ai.getName() );
					bean.setAlarmDescription( ai.getDescription() );
					boolean acknowledged = a.getAcknowledged() ;
					bean.setAcknowledged( acknowledged );
					bean.setAcknowledgedByUser( acknowledged ? a.getAcknowledgedByUser():null );
					
					Date akTime = (acknowledged ? a.getAcknowledgedTime().getTime() : null) ;
					String strAkTime = (akTime!=null? BaseCommonFunc.getStrFromDateTime(akTime): null );
					bean.setAcknowledgedTime( acknowledged ? strAkTime :null );
					
					alarmList.add(bean);
				}
	}
	
	
	
	public int getEntityNun(){
		return ENTITY_NUM ;
	}
	
	public void logout()
	{
		if (si!=null)
		{	alarmList.clear();
			ENTITY_NUM = 0;
			ServerConnection c = si.getServerConnection();
			c.logout();
		}
		  
	}

	public List<VMWareAlarm> getCurrAlarmBean()
	{
		return alarmList ;
	}
	public void clearCurrAlarmBean(){
		alarmList.clear();
	}
	
	public static void main(String[] args){

		VMWareDAO dao = new VMWareDAO();
		List<VCenterCfg> cfgList = dao.getVCenterCfg();
		
		
		int flag = 1 ;
		while(flag==1)
		{
			for(VCenterCfg cfg:cfgList)
			{
				try {
					System.out.println("Detect Begin .................");
					
					VMWareConnection conn = new VMWareConnection(cfg);
					
					long begin = System.currentTimeMillis();
					conn.retrieveEntitiesAlarm(VMWareEntityType.DATASTORE);
					conn.retrieveEntitiesAlarm(VMWareEntityType.HOST_SYSTEM);
					conn.retrieveEntitiesAlarm(VMWareEntityType.NETWORK);
					conn.retrieveEntitiesAlarm(VMWareEntityType.VIRTUAL_MACHINE);
					long end = System.currentTimeMillis();
					System.out.println( (end -begin) );
				
					//dao.refreshCurrAlarm(conn.getCurrAlarmBean() , cfg.getVmCode());
					conn.clearCurrAlarmBean();
					try {
						Thread.sleep(1000*5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					conn.logout();
					System.out.println("Detect End .................");
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			
			}	
			
		
		
		}
	}
}

