package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class BrandSpecDAO extends AbstractDAO {
	
	public BrandSpec getByBSCode(String bscode )
	{
		Connection conn = null ;
		Statement stmt	= null;
		BrandSpec brand = null;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" SELECT BSCode ,TPCode ,Brand ,Specification ,SNMPVer ,SNMPCommity ,SnmpParam ,CtrlType From brandspec WHERE BSCode = '"+ bscode +"'" ;
			
			ResultSet rs = stmt.executeQuery(sql);
			brand = new BrandSpec();
			if (rs.next())
			{
				brand.setBSCode(rs.getString("BSCode"));
				brand.setTPCode(rs.getString("TPCode"));
				brand.setBrand(rs.getString("Brand"));
				brand.setSpecification(rs.getString("Specification"));
				brand.setSNMPVer(rs.getString("SNMPVer"));
				brand.setSNMPCommity(rs.getString("SNMPCommity"));
				brand.setSnmpParam(rs.getString("SnmpParam"));
				brand.setCtrlType(rs.getString("CtrlType"));
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
		return brand ;
	}
	
}
