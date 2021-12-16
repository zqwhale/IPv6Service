package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DeviceDAO extends AbstractDAO {
	
	public Device getValidByPK(String dcode){
		Connection conn = null ;
		Statement stmt	= null;
		Device d = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode , DefaultLoop " +
						" FROM device WHERE IsValid = 1 AND DCode = '" +dcode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
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
		
		return d;
	}
	
	
	
	public List<Device> getAllRunState(){
		Connection conn = null ;
		Statement stmt	= null;
		List<Device> dlist = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode ,DefaultLoop " +
						" FROM device WHERE IsValid = 1 && State = 1 " ;
			ResultSet rs = stmt.executeQuery(sql);
			dlist = new ArrayList<Device>() ;
			while (rs.next()) {
				Device d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
				dlist.add(d);
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
		return dlist;
	}
	
	public List<Device> getAllValidByTPCode(String tpCode){
		Connection conn = null ;
		Statement stmt	= null;
		List<Device> dlist = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode ,DefaultLoop " +
						" FROM device WHERE IsValid = 1 AND TPCode ='" + tpCode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			dlist = new ArrayList<Device>() ;
			while (rs.next()) {
				Device d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
				dlist.add(d);
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
		return dlist;
	}
	
	public List<Device> getAll(){
		Connection conn = null ;
		Statement stmt	= null;
		List<Device> dlist = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode ,DefaultLoop " +
						" FROM device " ;
			ResultSet rs = stmt.executeQuery(sql);
			dlist = new ArrayList<Device>() ;
			while (rs.next()) {
				Device d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
				dlist.add(d);
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
		return dlist;
	}

	public List<Device> getSerialServerDevice()
	{
		Connection conn = null ;
		Statement stmt	= null;
		List<Device> dlist = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode ,DefaultLoop " +
						" FROM device WHERE IsValid = 1 AND Reserve1 LIKE 'RS-%'" ;
			ResultSet rs = stmt.executeQuery(sql);
			dlist = new ArrayList<Device>() ;
			while (rs.next()) {
				Device d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
				dlist.add(d);
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
		return dlist;
	}
	
	public List<Device> getAllValid(){
		Connection conn = null ;
		Statement stmt	= null;
		List<Device> dlist = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode ,DName ,DICode,Position," +
						" DDesc,IPAddr,SNMPCommity,IsValid,State," +
						" Reserve1,Reserve2,Reserve3,TPCode ,DefaultLoop " +
						" FROM device WHERE IsValid = 1 " ;
			ResultSet rs = stmt.executeQuery(sql);
			dlist = new ArrayList<Device>() ;
			while (rs.next()) {
				Device d = new Device();
				d.setDCode(rs.getString("DCode"));
				d.setBSCode(rs.getString("BSCode"));
				d.setDName(rs.getString("DName"));
				d.setDICode(rs.getString("DICode"));
				d.setPosition(rs.getString("Position"));
				d.setDDesc(rs.getString("DDesc"));
				d.setIPAddr(rs.getString("IPAddr"));
				d.setSnmpCommity(rs.getString("SnmpCommity"));
				d.setIsValid(rs.getInt("IsValid"));
				d.setState(rs.getInt("State"));
				d.setReserve1(rs.getString("Reserve1"));
				d.setReserve2(rs.getString("Reserve2"));
				d.setReserve3(rs.getString("Reserve3"));
				d.setTPCode(rs.getString("TPCode"));
				d.setDefaultLoop(rs.getInt("DefaultLoop"));
				dlist.add(d);
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
		return dlist;
	}
	
	
	public int markRunMessage(Device d , String state , String desc ){
		Connection conn = null ;
		Statement stmt	= null;
		if (d!=null && StringUtils.isNotBlank(d.getDCode()))
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update device Set Reserve1 = '" + state + "', Reserve2 = '" + desc + "'" + 
				"  Where DCode = '" + d.getDCode() + "'";
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
	
	public int markValid( Device d , int isValid){
		
		Connection conn = null ;
		Statement stmt	= null;
		int rs = 0 ;
		if (d!=null && StringUtils.isNotBlank(d.getDCode()))
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update device Set isValid = " + isValid + " Where DCode = '" + d.getDCode() + "'";
				
				rs = stmt.executeUpdate(sql);
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
		return rs ;
	}
	
	public int changeSerial( Device d , String port){
		
		Connection conn = null ;
		Statement stmt	= null;
		int rs = 0 ;
		if (d!=null && StringUtils.isNotBlank(d.getDCode()))
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update device Set IPAddr = '" + port + "' Where DCode = '" + d.getDCode() + "'";
				
				rs = stmt.executeUpdate(sql);
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
		return rs ;
	}
	public int changeState( Device d , int state){
		
		Connection conn = null ;
		Statement stmt	= null;
		int rs = 0 ;
		if (d!=null && StringUtils.isNotBlank(d.getDCode()))
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update device Set State = " + state + " Where DCode = '" + d.getDCode() + "'";
				
				rs = stmt.executeUpdate(sql);
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
		return rs ;
	}
	
	public int changeAllState( int state){
		
		Connection conn = null ;
		Statement stmt	= null;
		int rs = 0;
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update device Set State = " + state ;
				
				rs = stmt.executeUpdate(sql);
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

}
