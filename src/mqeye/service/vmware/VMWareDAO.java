package mqeye.service.vmware;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.tools.EncryDecryUtil;

import org.apache.commons.lang.StringUtils;

public class VMWareDAO extends AbstractDAO{
	
	public int getVMLoop()
	{
		Connection conn = null ;
		Statement stmt	= null;
		int loop = 5*60 ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = "select max(VMLoop) from vcenter " ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				loop = rs.getInt(1); 
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return loop;
	}
	
	public List<VCenterCfg> getVCenterCfg( ){
		Connection conn = null ;
		Statement stmt	= null;
		List<VCenterCfg> cfgList = new ArrayList<VCenterCfg>() ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = "select VMCode, VMCenterName , VMLJ , VMYH , VMKL from vcenter " ;
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String url = EncryDecryUtil.MyDecry(rs.getString("VMLJ"));
				String user = EncryDecryUtil.MyDecry(rs.getString("VMYH"));
				String pwd = EncryDecryUtil.MyDecry(rs.getString("VMKL"));
				
				if (url.contains("http:")) url.replace("http:", "https:"); //not support http
				
				VCenterCfg cfg = new VCenterCfg(url , user , pwd );
				cfg.setVmCode(rs.getString("VMCode"));
				cfg.setVmCenterName(rs.getString("VMCenterName"));
				cfgList.add(cfg);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return cfgList ;
	}
	
	public VCenterCfg getVCenterCfg(String vmCode){
		Connection conn = null ;
		Statement stmt	= null;
		VCenterCfg cfg = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = " select VMCode, VMCenterName , VMLJ , VMYH , VMKL from vcenter where " +
			" VMCode = '" + vmCode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				String url = EncryDecryUtil.MyDecry(rs.getString("VMLJ"));
				String user = EncryDecryUtil.MyDecry(rs.getString("VMYH"));
				String pwd = EncryDecryUtil.MyDecry(rs.getString("VMKL"));
				cfg = new VCenterCfg(url , user , pwd );
				cfg.setVmCode(rs.getString("VMCode"));
				cfg.setVmCenterName(rs.getString("VMCenterName"));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return cfg ;
	}
	
	public int clearCurrAlarm( String vmCode )
	{   int rs = 0;
		Connection conn = null ;
		Statement stmt	= null ;
		
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String del_sql = " DELETE FROM vmwarealarm WHERE VMCode = '" + vmCode + "'";
			rs = stmt.executeUpdate(del_sql);
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return rs ;
	}
	public int refreshCurrAlarm(List<VMWareAlarm> alist , String code)
	{
		int rs = 0 ;
		Connection conn = null ;
		Statement stmt	= null ;
		if (alist.size()<=0) return 0 ;
		String insertStr = "    " ;
		for(VMWareAlarm a:alist){
			String vmCode = isNull(2,StringUtils.trimToNull(a.getVmCode()));
			String alarmKey = isNull(2,StringUtils.trimToNull(a.getAlarmKey()));
			String entityName = isNull(2,StringUtils.trimToNull(a.getEntityName()));
			String entityType = isNull(2,StringUtils.trimToNull(a.getEntityType()));
			String overallStatus = isNull(2,StringUtils.trimToNull(a.getOverallStatus()));
			String alarmName = isNull(2,StringUtils.trimToNull(a.getAlarmName()));
			String alarmDescription = isNull(2,StringUtils.trimToNull(a.getAlarmDescription().replace("'", " ")));
			String triggerTime = isNull(2,StringUtils.trimToNull(a.getTriggerTime()));
			String acknowledgedTime = isNull(2,StringUtils.trimToNull(a.getAcknowledgedTime()));
			String acknowledgedByUser = isNull(2,StringUtils.trimToNull(a.getAcknowledgedByUser()));
			Boolean acknowledged = (a.getAcknowledged()!=null? a.getAcknowledged(): false );
			
			insertStr = insertStr + "("+ vmCode + "," + alarmKey +"," + entityName  + "," + entityType + "," + overallStatus + "," + alarmName + "," +
			alarmDescription + "," + triggerTime + "," + acknowledgedTime + "," + acknowledgedByUser +  "," + acknowledged +  ") , " ;
		}
		insertStr = StringUtils.substring(insertStr, 0, insertStr.length()- 3 );
		try {
			conn = cpm.getConnection("mqeye");
			
			stmt = conn.createStatement();
			String del_sql = " DELETE FROM vmwarealarm  WHERE VMCode = '" + code + "'";
			rs = stmt.executeUpdate(del_sql);
			String ins_sql=" INSERT INTO vmwarealarm ( VMCode , alarmKey , entityName , entityType , overallStatus , alarmName ,"
						+ " alarmDescription , triggerTime , acknowledgedTime , acknowledgedByUser ,acknowledged ) " +
						" Values " + insertStr ;

			rs = stmt.executeUpdate(ins_sql);
			stmt.close();
		
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return rs ;
	}
	public List<VMWareAlarm> getCurrAlarm(String vmCode){
		List<VMWareAlarm> list = new ArrayList<VMWareAlarm> () ;
		Connection conn = null ;
		Statement stmt	= null;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = " select VMCode , alarmKey , entityName , entityType , overallStatus , alarmName , " +
							" alarmDescription , triggerTime , acknowledged , acknowledgedTime , acknowledgedByUser " +
							" from vmwarealarm where VMCode = '"+ vmCode+"'";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				VMWareAlarm alarm = new VMWareAlarm();
				alarm.setVmCode(rs.getString("VMCode"));
				alarm.setEntityName(rs.getString("entityName"));
				alarm.setEntityType(rs.getString("entityType"));
				alarm.setOverallStatus(rs.getString("overallStatus"));
				alarm.setAlarmName(rs.getString("alarmName"));
				alarm.setAlarmDescription(rs.getString("alarmDescription"));
				alarm.setTriggerTime(rs.getString("triggerTime"));
				alarm.setAcknowledged(rs.getBoolean("acknowledged"));
				alarm.setAcknowledgedTime(rs.getString("acknowledgedTime"));
				alarm.setAcknowledgedByUser(rs.getString("acknowledgedByUser"));
				list.add(alarm);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return list;	
		
	}
	public List<VMWareAlarm> getCurrAlarm(){
		List<VMWareAlarm> list = new ArrayList<VMWareAlarm> () ;
		Connection conn = null ;
		Statement stmt	= null;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = " select VMCode , alarmKey , entityName , entityType , overallStatus , alarmName , " +
							" alarmDescription , triggerTime , acknowledged , acknowledgedTime , acknowledgedByUser " +
							" from vmwarealarm ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				VMWareAlarm alarm = new VMWareAlarm();
				alarm.setVmCode(rs.getString("VMCode"));
				alarm.setEntityName(rs.getString("entityName"));
				alarm.setEntityType(rs.getString("entityType"));
				alarm.setOverallStatus(rs.getString("overallStatus"));
				alarm.setAlarmName(rs.getString("alarmName"));
				alarm.setAlarmDescription(rs.getString("alarmDescription"));
				alarm.setTriggerTime(rs.getString("triggerTime"));
				alarm.setAcknowledged(rs.getBoolean("acknowledged"));
				alarm.setAcknowledgedTime(rs.getString("acknowledgedTime"));
				alarm.setAcknowledgedByUser(rs.getString("acknowledgedByUser"));
				list.add(alarm);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return list;	
		
	}
}
