package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DeviceservicemapviewDAO extends AbstractDAO {
	
	
	
	
	
	public List<Deviceservicemapview> getAll(){
		Connection conn = null ;
		Statement stmt	= null;
		List<Deviceservicemapview> dmvlist = null ;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT DCode,BSCode,DName,DICode,Position,DDesc,IPAddr," +
					   " SnmpCommity,isValid,State,SVCode,SVName,TLoop,Threshold,SubPort, " +
					   " OnOff, RunStop FROM deviceservicemapview " ;
			
			ResultSet rs = stmt.executeQuery(sql);
			dmvlist = new ArrayList<Deviceservicemapview>() ;
			while (rs.next()) {
				Deviceservicemapview dmv = new Deviceservicemapview();
				dmv.setDCode(rs.getString("DCode"));
				dmv.setBSCode(rs.getString("BSCode"));
				dmv.setDName(rs.getString("DName"));
				dmv.setDICode(rs.getString("DICode"));
				dmv.setPosition(rs.getString("Position"));
				dmv.setDDesc(rs.getString("DDesc"));
				dmv.setIPAddr(rs.getString("IPAddr"));
				dmv.setSnmpCommity(rs.getString("SnmpCommity"));
				dmv.setIsValid(rs.getInt("isValid"));
				dmv.setState(rs.getInt("State"));
				dmv.setSVCode(rs.getString("SVCode"));
				dmv.setSVName(rs.getString("SVName"));
				dmv.setTLoop(rs.getInt("TLoop"));
				dmv.setThreshold(rs.getString("Threshold"));
				dmv.setSubPort(rs.getString("SubPort"));
				dmv.setOnOff(rs.getInt("OnOff"));
				dmvlist.add(dmv);
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
		return dmvlist;
	}
		
	public Deviceservicemapview getBeanByPK(String dcode , String svcode){
		Connection conn = null ;
		Statement stmt	= null;
		Deviceservicemapview dmv = null;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT DCode,BSCode,DName,DICode,Position,DDesc,IPAddr," +
			   			" SnmpCommity,isValid,State,SVCode,SVName,TLoop,Threshold,SubPort, " +
			   			" OnOff, RunStop FROM deviceservicemapview WHERE DCode = '" +
						 dcode + "' AND SVCode = '" + svcode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				dmv = new Deviceservicemapview();
				dmv.setDCode(rs.getString("DCode"));
				dmv.setBSCode(rs.getString("BSCode"));
				dmv.setDName(rs.getString("DName"));
				dmv.setDICode(rs.getString("DICode"));
				dmv.setPosition(rs.getString("Position"));
				dmv.setDDesc(rs.getString("DDesc"));
				dmv.setIPAddr(rs.getString("IPAddr"));
				dmv.setSnmpCommity(rs.getString("SnmpCommity"));
				dmv.setIsValid(rs.getInt("isValid"));
				dmv.setState(rs.getInt("State"));
				dmv.setSVCode(rs.getString("SVCode"));
				dmv.setSVName(rs.getString("SVName"));
				dmv.setTLoop(rs.getInt("TLoop"));
				dmv.setThreshold(rs.getString("Threshold"));
				dmv.setSubPort(rs.getString("SubPort"));
				dmv.setOnOff(rs.getInt("OnOff"));
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
		return dmv;
	}
}
