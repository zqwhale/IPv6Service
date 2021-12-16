package mqeye.data.vo;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class CurrentTblDAO extends AbstractDAO {
	public void markStatus( CurrentTbl tbl )
	{
		Connection conn = null ;
		Statement stmt	= null;
		
		{
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update currentTbl set  mldatetime = " + tbl.getMldatetime() +
				", isWarning = " + tbl.getIsWarning()  +   
				"  Where DCode = " + tbl.getDcode() + " AND SVCode = " + tbl.getSvcode() + " AND SubPort = " + tbl.getSubport() + " " ;
				
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

}
