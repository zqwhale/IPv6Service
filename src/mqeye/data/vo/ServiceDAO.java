package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ServiceDAO extends AbstractDAO {
	public Service getServiceByPK(String svcode){
		Connection conn = null ;
		Statement stmt	= null;
		Service s = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT SVCode , SVName , SVDesc , WMCode , WMName , WMLevel , Reserve1 , Reserve2 , Reserve3 " +
						" FROM service WHERE SVCode = '" +svcode + "'" ;
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				s = new Service();
				s.setSVCode(rs.getString("SVCode"));
				s.setSVName(rs.getString("SVName"));
				s.setSVDesc(rs.getString("SVDesc"));
				s.setWMCode(rs.getInt("WMCode"));
				s.setWMName(rs.getString("WMName"));
				s.setWMLevel(rs.getString("WMLevel"));
				s.setReserve1(rs.getString("Reserve1"));
				s.setReserve2(rs.getString("Reserve2"));
				s.setReserve3(rs.getString("Reserve3"));
				
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
		
		return s;
	}
}
