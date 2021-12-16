package mqeye.service.tools;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.Constant;

public class ErrorLog extends AbstractDAO {
	private String from = "UNKNOW";
	public ErrorLog(Class c){
		from = c.getName();
	}
	public int log(String err)
	{
		String eldate = BaseCommonFunc.getStrFromDateTime(new Date());
		
		int rs =0 ;
		Connection conn = null ;
		Statement stmt	= null ;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = "Insert into errorlog ( ELDate , ErrorLog , ErrorFrom )" + 
						"Values ('" + eldate + "','" + err + "','" + from + "')";
			rs = stmt.executeUpdate(sql);
			stmt.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException e2){
				e2.printStackTrace();
			}
		}
		return rs ;
	}
}
