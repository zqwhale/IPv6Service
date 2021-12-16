package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SnmpV3DeviceDAO extends AbstractDAO {
	
	
	public SnmpV3Device getSnmpV3DeviceByDCode( String dcode){
		Connection conn = null ;
		Statement stmt	= null;
		SnmpV3Device sv3d = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= "SELECT DCode , BSCode , USMUser , SecLevel , AuthAlg , AuthPass," +
					"PrivAlg , PrivPass , Context , EngineID , LocalAuthKey , LocalPrivKey," +
					"Timeout, Retry , Reserve1 ,Reserve2, Reserve3 " +
					"FROM snmpv3device WHERE DCode = '" + dcode + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sv3d = new SnmpV3Device();
				sv3d.setDcode(rs.getString("DCode"));
				sv3d.setBscode(rs.getString("BSCode"));
				sv3d.setUsmuser(rs.getString("USMUser"));
				sv3d.setSeclevel(rs.getString("SecLevel"));
				sv3d.setAuthalg(rs.getString("AuthAlg"));
				sv3d.setAuthpass(rs.getString("AuthPass"));
				sv3d.setPrivalg(rs.getString("PrivAlg"));
				sv3d.setPrivpass(rs.getString("PrivPass"));
				sv3d.setContext(rs.getString("Context"));
				sv3d.setEngineid(rs.getString("EngineID"));
				sv3d.setLocalauthkey(rs.getString("LocalAuthKey"));
				sv3d.setLocalprivkey(rs.getString("LocalPrivKey"));
				sv3d.setTimeout(rs.getInt("Timeout"));
				sv3d.setRetry(rs.getInt("Retry"));
				sv3d.setReserve1(rs.getString("Reserve1"));
				sv3d.setReserve2(rs.getString("Reserve2"));
				sv3d.setReserve3(rs.getString("Reserve3"));
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
	
		return sv3d;
	}
}
