package mqeye.service.routine;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import mqeye.data.vo.MonitorlogDAO;
import mqeye.data.vo.WarmlogDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.DebugTool;

import org.apache.commons.io.FileUtils;

public class LogFileTask implements Runnable {

		private void export(String table ,String file){
			Calendar c = Calendar.getInstance();
			c.add(Calendar.DATE, -7);
			if (table.equals("monitorlog")){
				MonitorlogDAO mldao = new MonitorlogDAO();
				String end = BaseCommonFunc.getStrFromDateTime(c.getTime());
				mldao.export(end, file);
			}
			if (table.equals("warmlog")){
				WarmlogDAO wldao = new WarmlogDAO();
				String end = BaseCommonFunc.getStrFromDateTime(c.getTime());
				wldao.export(end, file);
			}
		}
		private void backup(String logFile){
						File f = new File(logFile);
						File fBak = new File(logFile+ ".bak");
						if (f.exists()){
								try {
									FileUtils.copyFile(f, fBak);	
									FileUtils.writeStringToFile(f, null);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
						}
						
			}

			@Override
			public void run() {
				DebugTool.showConsole("system is begin backup the log file and data:系统开始备份日志......");
				
				backup(Constant.ERROR_LOG_FILE);
				backup(Constant.WRAPP_LOG_FILE);
				backup(Constant.DATAB_LOG_FILE);
				export("monitorlog",Constant.MONITOR_TMP_FILE);
				export("warmlog",Constant.WARN_TMP_FILE);
				
				DebugTool.showConsole("system is end backup the log file and data:系统结束备份日志......");
			}

}
