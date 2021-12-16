package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

public class DsviewDAO  extends AbstractDAO {
	
	
	
	public void resetRunStop(String dcode , int state){
		Connection conn = null ;
		Statement stmt	= null;
		String sql = null;
		String currentTime = BaseCommonFunc.getStrFromDateTime(new Date());
		
		try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				if (state == Constant.SERVICE_STOP)
						sql	=	" Update deviceservicemap Set  RunStop =  " + state + ", RunningTime = 'stop:" + currentTime +	"'" +
						" WHERE DCode = '" + dcode + "'";
				else
						sql	=	" Update deviceservicemap Set  RunStop =  " + state + ", RunningTime = 'start:" + currentTime +	"'" +
						" WHERE DCode = '" + dcode + "' AND OnOff = " + state  ;
				
				int rs = stmt.executeUpdate(sql);
				
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
		
	}
	
	
	public void resetRunStop(int state){
		Connection conn = null ;
		Statement stmt	= null;
		String sql = null;
		String currentTime = BaseCommonFunc.getStrFromDateTime(new Date());
		try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				sql	=	" Update deviceservicemap Set RunStop =  " + state + ", RunningTime = 'reset:" + currentTime +	"'";  
				stmt.executeUpdate(sql);
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
		
	}
	public boolean isNotConsist( String dcode){
					
		boolean flag = false ; 
		Connection conn = null ;
		Statement stmt	= null;
		if (dcode != null){
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql = "SELECT * FROM deviceservicemap WHERE " +
				"DCode = '" + dcode + "' AND ((OnOff = 0 AND RunStop = 1) OR (OnOff = 1 AND RunStop = 0)) ";
				ResultSet rs = stmt.executeQuery(sql);
				flag = rs.next();
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
		}
		return flag;
	}
	public void recordStartTime( String dcode )
	{
		Connection conn = null ;
		Statement stmt	= null;
		String sql = null;
		if (dcode!=null)
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String currentTime = BaseCommonFunc.getStrFromDateTime(new Date());
				sql	=	" Update deviceservicemap Set FirstStartDate = '" + currentTime +	"'" +
						" Where DCode = '" + dcode + "' AND RunStop = 1 ";  /*???RunStop = 1 , ??? ??????? */
				stmt.executeUpdate(sql);
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
		}
			
	}
	public void refreshService(  String dcode  ){
		Connection conn = null ;
		Statement stmt	= null;
		String sql = null;
		if (dcode!=null)
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String currentTime = BaseCommonFunc.getStrFromDateTime(new Date());
				sql	=	" Update deviceservicemap Set RunStop = 0 , RunningTime = 'stop:" + currentTime +	"'" +
						" Where DCode = '" + dcode + "' AND OnOff = 0 AND RunStop = 1 ";  
				stmt.executeUpdate(sql);
				sql=" Update deviceservicemap Set RunStop = 1 , RunningTime = 'start:" + currentTime +	"'" +
									" Where DCode = '" + dcode + "' AND OnOff = 1 AND RunStop = 0 "; 	
				stmt.executeUpdate(sql);
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
		}
 }
	
	public int recordBeginTime(Dsview dsv ){
			Connection conn = null ;
			Statement stmt	= null;
			if (dsv!=null)
			{
				String dcode = dsv.getDCode();
				String svcode = dsv.getSVCode();
				String submodule = dsv.getSubModule();
				String subport = dsv.getSubPort();
				
				try {
					conn = cpm.getConnection("mqeye");
					stmt = conn.createStatement();
					String beginTime = BaseCommonFunc.getStrFromDateTime(new Date());
					String sql=" Update deviceservicemap Set FirstStartDate = '"	+ beginTime + "'"+
					" Where DCode = '" + dcode + "' and RunStop = 1 and SVCode = '" + svcode + "' and SubModule = '" + 
					submodule + "' and (SubPort = '" + subport + "' OR SubPort is NULL)";
					int rs = stmt.executeUpdate(sql);
					stmt.close();
					return rs ;
				}catch(SQLException e){
					e.printStackTrace();
				}finally{
					try{ 
						conn.close();
					}catch(SQLException err){
						err.printStackTrace();
					}
				}
			}
			return 0;
	}
	
	/* when subport changed , Inconsist , this method will modify subport to a  new subport value. */
	public void refreshSubPort(Dsview dsv , String newSubport){
		Connection conn = null ;
		Statement stmt	= null;
		if (dsv!=null)
		{
			String dcode = dsv.getDCode();
			String svcode = dsv.getSVCode();
			String submodule = dsv.getSubModule();
			String subport = dsv.getSubPort();
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update deviceservicemap Set Subport = '"	+ newSubport + "'"+
				" Where DCode = '" + dcode + "' and RunStop = 1 and SVCode = '" + svcode + "' and SubModule = '" + 
				submodule + "' and (SubPort = '" + subport + "')";
				int rs = stmt.executeUpdate(sql);
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
		}
	}
	
	
	public void syncSubPort(Dsview dsv , String newSubport){
		Connection conn = null ;
		Statement stmt	= null;
		if (dsv!=null)
		{
			String dcode = dsv.getDCode();
			String svcode = dsv.getSVCode();
			String submodule = dsv.getSubModule();
			String subport = dsv.getSubPort();
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update deviceservicemap Set Subport = '"	+ newSubport + "'"+
				" Where DCode = '" + dcode + "' and SVCode = '" + svcode + "' and SubModule = '" + 
				submodule + "' and (SubPort = '" + subport + "')";
				int rs = stmt.executeUpdate(sql);
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
		}
	}
	public String getSubPortOID(String bscode , String svcode , String submodule ){
		Connection conn = null ;
		Statement stmt	= null;
		String subPortOID = null;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = "select SubPortOID from brandservicemap where BSCode = '" 
				+ bscode + "' AND SVCode = '" + svcode + "' AND SubModule = '" + submodule + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				subPortOID = rs.getString("SubPortOID");
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
		return subPortOID;
		
	}
	
		
	public List<Dsview> getBeanForSubPort(String dcode){
		Connection conn = null ;
		Statement stmt	= null;
		List<Dsview> dsvlist = null ;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT DCode,DName, Position,BSCode,TPCode,TPName,Brand,Specification," +
					   " Method,IPAddr,SnmpCommity,SNMPVer,SnmpOID , SubPortOID, SnmpParam , isValid , SubPort, SubPortName , SubModule,Url," +
					   " SVCode,SVName, TLoop, Threshold,WMCode,WMName,WMLevel, ValueType , ValueUnit , ValueConvert, WarmExpress ,Delay, OnOff, RunStop, DICode FROM dsview " +
					   " WHERE DCode = '" + dcode + "' AND SVCode in ('SVLL','SVCLL')" + " AND OnOff = 1 ";
			
			ResultSet rs = stmt.executeQuery(sql);
			dsvlist = new ArrayList<Dsview>() ;
			while (rs.next()) {
				Dsview dsv = new Dsview();
				dsv.setDCode(rs.getString("DCode"));
				dsv.setDName(rs.getString("DName"));
				dsv.setPosition(rs.getString("Position"));
				dsv.setBSCode(rs.getString("BSCode"));
				dsv.setTPCode(rs.getString("TPCode"));
				dsv.setTPName(rs.getString("TPName"));
				dsv.setBrand(rs.getString("Brand"));
				dsv.setSpecification(rs.getString("Specification"));
				dsv.setMethod(rs.getString("Method"));
				dsv.setIPAddr(rs.getString("IPAddr"));
				dsv.setSnmpCommity(rs.getString("SnmpCommity"));
				dsv.setSNMPVer(rs.getString("SNMPVer"));
				dsv.setSnmpOID(rs.getString("SnmpOID"));
				dsv.setSubPortOID(rs.getString("SubPortOID"));
				
				dsv.setSnmpParam(rs.getString("SnmpParam"));
				dsv.setIsValid(rs.getInt("isValid"));
				dsv.setSubPort(rs.getString("SubPort"));
				dsv.setSubPortName(rs.getString("SubPortName"));
				dsv.setSubModule(rs.getString("SubModule"));
				dsv.setSVCode(rs.getString("SVCode"));
				dsv.setSVName(rs.getString("SVName"));
				dsv.setTLoop(rs.getInt("TLoop"));
				dsv.setThreshold(rs.getString("Threshold"));
				dsv.setWMCode(rs.getInt("WMCode"));
				dsv.setWMName(rs.getString("WMName"));
				dsv.setWMLevel(rs.getString("WMLevel"));
				dsv.setValueType(rs.getString("ValueType"));
				dsv.setValueUnit(rs.getString("ValueUnit"));
				dsv.setValueConvert(rs.getString("ValueConvert"));
				dsv.setWarmExpress(rs.getString("WarmExpress"));
				dsv.setDelay(rs.getInt("Delay"));
				dsv.setOnOff(rs.getInt("OnOff"));
				dsv.setRunStop(rs.getInt("RunStop"));
				dsv.setUrl(rs.getString("Url"));
				dsv.setDICode(rs.getString("DICode"));
				
				dsvlist.add(dsv);
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
		return dsvlist;
		
	}
	
	
	public Dsview getBeanByPK(String dcode , String svcode , String submodule){
		Connection conn = null ;
		Statement stmt	= null;
		Dsview dsv = null ; 
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT DCode,DName, Position,BSCode,TPCode,TPName,Brand,Specification," +
					   " Method,IPAddr,SnmpCommity,SNMPVer,SnmpOID , SubPortOID, SnmpParam , isValid , SubPort, SubPortName , SubModule,Url," +
					   " SVCode,SVName, TLoop, Threshold,WMCode,WMName,WMLevel, ValueType , ValueUnit , ValueConvert , WarmExpress ,Delay, OnOff, RunStop , DICode FROM dsview " +
					   " WHERE DCode = '" + dcode + "' AND SVCode = '" + svcode + "' AND SubModule = '" + submodule + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				dsv = new Dsview();
				dsv.setDCode(rs.getString("DCode"));
				dsv.setDName(rs.getString("DName"));
				dsv.setPosition(rs.getString("Position"));
				dsv.setBSCode(rs.getString("BSCode"));
				dsv.setTPCode(rs.getString("TPCode"));
				dsv.setTPName(rs.getString("TPName"));
				dsv.setBrand(rs.getString("Brand"));
				dsv.setSpecification(rs.getString("Specification"));
				dsv.setMethod(rs.getString("Method"));
				dsv.setIPAddr(rs.getString("IPAddr"));
				dsv.setSnmpCommity(rs.getString("SnmpCommity"));
				dsv.setSNMPVer(rs.getString("SNMPVer"));
				dsv.setSnmpOID(rs.getString("SnmpOID"));
				dsv.setSubPortOID(rs.getString("SubPortOID"));
				
				dsv.setSnmpParam(rs.getString("SnmpParam"));
				dsv.setIsValid(rs.getInt("isValid"));
				dsv.setSubPort(rs.getString("SubPort"));
				dsv.setSubPortName(rs.getString("SubPortName"));
				dsv.setSubModule(rs.getString("SubModule"));
				dsv.setSVCode(rs.getString("SVCode"));
				dsv.setSVName(rs.getString("SVName"));
				dsv.setTLoop(rs.getInt("TLoop"));
				dsv.setThreshold(rs.getString("Threshold"));
				dsv.setWMCode(rs.getInt("WMCode"));
				dsv.setWMName(rs.getString("WMName"));
				dsv.setWMLevel(rs.getString("WMLevel"));
				dsv.setValueType(rs.getString("ValueType"));
				dsv.setValueUnit(rs.getString("ValueUnit"));
				dsv.setValueConvert(rs.getString("ValueConvert"));
				dsv.setWarmExpress(rs.getString("WarmExpress"));
				dsv.setDelay(rs.getInt("Delay"));
				dsv.setOnOff(rs.getInt("OnOff"));
				dsv.setRunStop(rs.getInt("RunStop"));
				dsv.setUrl(rs.getString("Url"));
				dsv.setDICode(rs.getString("DICode"));
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
		return dsv;
	}

	public List<Dsview> getBeanByDCode( String dcode  ){
		Connection conn = null ;
		Statement stmt	= null;
		List<Dsview> dsvlist = null ;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT DCode,DName, Position,BSCode,TPCode,TPName,Brand,Specification," +
					   " Method,IPAddr,SnmpCommity,SNMPVer,SnmpOID , SubPortOID, SnmpParam , isValid , SubPort, SubPortName , SubModule,Url," +
					   " SVCode,SVName, TLoop, Threshold,WMCode,WMName,WMLevel, ValueType , ValueUnit , ValueConvert, WarmExpress ,Delay, OnOff, RunStop , DICode FROM dsview " +
					   " WHERE DCode = '" + dcode + "' AND OnOff = 1 ";
			
			ResultSet rs = stmt.executeQuery(sql);
			dsvlist = new ArrayList<Dsview>() ;
			while (rs.next()) {
				Dsview dsv = new Dsview();
				dsv.setDCode(rs.getString("DCode"));
				dsv.setDName(rs.getString("DName"));
				dsv.setPosition(rs.getString("Position"));
				dsv.setBSCode(rs.getString("BSCode"));
				dsv.setTPCode(rs.getString("TPCode"));
				dsv.setTPName(rs.getString("TPName"));
				dsv.setBrand(rs.getString("Brand"));
				dsv.setSpecification(rs.getString("Specification"));
				dsv.setMethod(rs.getString("Method"));
				dsv.setIPAddr(rs.getString("IPAddr"));
				dsv.setSnmpCommity(rs.getString("SnmpCommity"));
				dsv.setSNMPVer(rs.getString("SNMPVer"));
				dsv.setSnmpOID(rs.getString("SnmpOID"));
				dsv.setSubPortOID(rs.getString("SubPortOID"));
				
				dsv.setSnmpParam(rs.getString("SnmpParam"));
				dsv.setIsValid(rs.getInt("isValid"));
				dsv.setSubPort(rs.getString("SubPort"));
				dsv.setSubPortName(rs.getString("SubPortName"));
				dsv.setSubModule(rs.getString("SubModule"));
				dsv.setSVCode(rs.getString("SVCode"));
				dsv.setSVName(rs.getString("SVName"));
				dsv.setTLoop(rs.getInt("TLoop"));
				dsv.setThreshold(rs.getString("Threshold"));
				dsv.setWMCode(rs.getInt("WMCode"));
				dsv.setWMName(rs.getString("WMName"));
				dsv.setWMLevel(rs.getString("WMLevel"));
				dsv.setValueType(rs.getString("ValueType"));
				dsv.setValueUnit(rs.getString("ValueUnit"));
				dsv.setValueConvert(rs.getString("ValueConvert"));
				dsv.setWarmExpress(rs.getString("WarmExpress"));
				dsv.setDelay(rs.getInt("Delay"));
				dsv.setOnOff(rs.getInt("OnOff"));
				dsv.setRunStop(rs.getInt("RunStop"));
				dsv.setUrl(rs.getString("Url"));
				dsv.setDICode(rs.getString("DICode"));
				dsvlist.add(dsv);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				if ( conn!=null )	conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return dsvlist;
	}
	
}
