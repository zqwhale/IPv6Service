package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;





public class BrandservicemapviewDAO extends AbstractDAO{
	

	
	public List<Brandservicemapview> getAll(){
		Connection conn = null ;
		Statement stmt	= null;
		List<Brandservicemapview> bmvlist = null ;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT BSCode,TPCode,TPName,Brand,Specification,SVCode," +
						" SVName,Method,SNMPVer,SNMPCommity,SnmpParam,SNMPOID, " +
						" ValueType,ValueUnit,DefaultLoop,DefaultThreshold,WarmExpress" +
						" ,SubPort,SubModule FROM brandservicemapview " ;
			ResultSet rs = stmt.executeQuery(sql);
			bmvlist = new ArrayList<Brandservicemapview>() ;
			while (rs.next()) {
				Brandservicemapview bmv = new Brandservicemapview();
				bmv.setBSCode(rs.getString("BSCode"));
				bmv.setTPCode(rs.getString("TPCode"));
				bmv.setTPName(rs.getString("TPName"));
				bmv.setBrand(rs.getString("Brand"));
				bmv.setSpecification(rs.getString("Specification"));
				bmv.setSVCode(rs.getString("SVCode"));
				bmv.setSVName(rs.getString("SVName"));
				bmv.setMethod(rs.getString("Method"));
				bmv.setSNMPVer(rs.getString("SNMPVer"));
				bmv.setSNMPCommity(rs.getString("SNMPCommity"));
				bmv.setSnmpParam(rs.getString("SnmpParam"));
				bmv.setSNMPOID(rs.getString("SNMPOID"));
				bmv.setValueType(rs.getString("ValueType"));
				bmv.setValueUnit(rs.getString("ValueUnit"));
				bmv.setDefaultLoop(rs.getInt("DefaultLoop"));
				bmv.setDefaultThreshold(rs.getString("DefaultThreshold"));
				bmv.setWarmExpress(rs.getString("WarmExpress"));
				bmv.setSubPort(rs.getString("SubPort"));
				bmv.setSubModule(rs.getString("SubModule"));
				bmvlist.add(bmv);
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
		return bmvlist;
	}
	
	public Brandservicemapview getBeanByPK(String bscode , String svcode){
		Connection conn = null ;
		Statement stmt	= null;
		Brandservicemapview bmv = null;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT BSCode,TPCode,TPName,Brand,Specification,SVCode," +
						" SVName,Method,SNMPVer,SNMPCommity,SnmpParam,SNMPOID, " +
						" ValueType,ValueUnit,DefaultLoop,DefaultThreshold,WarmExpress" +
						" ,SubPort,SubModule FROM brandservicemapview WHERE BSCode = '" +
						bscode + "' AND SVCode = '" + svcode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				bmv = new Brandservicemapview();
				bmv.setBSCode(rs.getString("BSCode"));
				bmv.setTPCode(rs.getString("TPCode"));
				bmv.setTPName(rs.getString("TPName"));
				bmv.setBrand(rs.getString("Brand"));
				bmv.setSpecification(rs.getString("Specification"));
				bmv.setSVCode(rs.getString("SVCode"));
				bmv.setSVName(rs.getString("SVName"));
				bmv.setMethod(rs.getString("Method"));
				bmv.setSNMPVer(rs.getString("SNMPVer"));
				bmv.setSNMPCommity(rs.getString("SNMPCommity"));
				bmv.setSnmpParam(rs.getString("SnmpParam"));
				bmv.setSNMPOID(rs.getString("SNMPOID"));
				bmv.setValueType(rs.getString("ValueType"));
				bmv.setValueUnit(rs.getString("ValueUnit"));
				bmv.setDefaultLoop(rs.getInt("DefaultLoop"));
				bmv.setDefaultThreshold(rs.getString("DefaultThreshold"));
				bmv.setWarmExpress(rs.getString("WarmExpress"));
				bmv.setSubPort(rs.getString("SubPort"));
				bmv.setSubModule(rs.getString("SubModule"));
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
		return bmv;
	}
}


