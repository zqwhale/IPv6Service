package mqeye.service.vmware;

import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

public class VMWareDetectTask implements Runnable{
	
	public VMWareAlarm vCenterAlarm = new VMWareAlarm();
	public final static int delay = 30;
	public static int loop = 5*60;  // Loop is Adjust maybe good
	public VMWareDetectTask()
	{	VMWareDAO dao = new VMWareDAO();
		loop =  dao.getVMLoop();
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		VMWareDAO dao = new VMWareDAO();
		List<VCenterCfg> cfgList = dao.getVCenterCfg();
		int err = 0;
		for(VCenterCfg cfg:cfgList)
		{
			if (cfg!=null)
			{
				List<VMWareAlarm> list = null;
				String vmcode = cfg.getVmCode( ) ;
				try {
					VMWareConnection conn = new VMWareConnection(cfg);
					err = 0;
					DebugTool.showConsole("Detect VCenter Begin .................");
					conn.retrieveEntitiesAlarm(VMWareEntityType.DATASTORE);
					conn.retrieveEntitiesAlarm(VMWareEntityType.HOST_SYSTEM);
					conn.retrieveEntitiesAlarm(VMWareEntityType.NETWORK);
					conn.retrieveEntitiesAlarm(VMWareEntityType.VIRTUAL_MACHINE);
					list = conn.getCurrAlarmBean();
					if (list!=null && list.size()>0)
					{
						dao.refreshCurrAlarm(list,vmcode);
						DebugTool.showConsole("VCenter Alarm num :" + list.size());
					}
					else
						dao.clearCurrAlarm( vmcode );
					
					conn.logout();
	
					DebugTool.showConsole("Detect VCenter End .................");
		
				} catch (RemoteException e1) {
					// TODO Auto-generated catch block
					DebugTool.printErr("VMWareDetectTask RemoteException........");
					err = 1 ;
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					DebugTool.printErr("VMWareDetectTask MalformedURLException........");
					err = 1 ;
				}
				if (err==1){
					
					list = new ArrayList<VMWareAlarm> () ;
					vCenterAlarm.setVmCode(cfg.getVmCode());
					vCenterAlarm.setAlarmKey(cfg.getVmCode());
					vCenterAlarm.setAlarmName("Connect Fail!");
					vCenterAlarm.setAlarmDescription("VCenter Service Cannt connect, please check configuration or network!");
					vCenterAlarm.setEntityName(cfg.getVmCenterName());
					vCenterAlarm.setEntityType("VCenter");
					vCenterAlarm.setOverallStatus("red");
					vCenterAlarm.setTriggerTime(BaseCommonFunc.getStrFromDate(new Date()));
					vCenterAlarm.setAcknowledged(false);
					list.add(vCenterAlarm);
					dao.refreshCurrAlarm(list,vmcode);
					DebugTool.printErr("VCenter Connect Fail!");
				}
			}
			
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
}


