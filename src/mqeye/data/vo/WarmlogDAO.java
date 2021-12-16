package mqeye.data.vo;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mqeye.service.Constant;
import mqeye.service.tools.PropertiesUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class WarmlogDAO extends AbstractDAO {
	
	public void export(String end ,String file){
		PropertiesUtil util=new PropertiesUtil(Constant.MQEYE_FILE);
		String charSet = util.getProperty("charSet");
		
		Connection conn = null ;
		Statement stmt	= null;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT WMID,WMDateTime,WMCode,WMLevel,DCode,SVCode,WMContent,Closed,Reserve1,Reserve2 " + 
						" FROM mqeye.warmlog  WHERE WMDateTime <= '" + end + "'" ;
			File f = new File(file);
			List<String> tmps = new ArrayList<String>();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String log = rs.getLong("WMID")+ "," +
				rs.getString("WMDateTime") + "," +
				rs.getInt("WMCode") + "," + 
				rs.getString("WMLevel") + "," +
				rs.getString("DCode") + "," +
				rs.getString("SVCode") + "," +
				rs.getString("WMContent")+ "," +
				rs.getInt("Closed")+ "," +
				rs.getString("Reserve1")+ "," +
				rs.getString("Reserve2") ;
				
				tmps.add(log);
					
			}
			FileUtils.writeLines(f, charSet, tmps);
			sql = "DELETE FROM mqeye.warmlog  WHERE WMDateTime <= '" + end + "'";
			stmt.executeUpdate(sql)	;
			rs.close();
			stmt.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}

	}
	public int recordToDB(List<Warmlog> wlist){
		Connection conn = null ;
		Statement stmt	= null ;
		if (wlist.size()<=0) return 0 ;
		String insertStr = "    " ;
		for(Warmlog w:wlist){
			String wmdateTime = isNull(1,StringUtils.trimToNull(w.getWMDateTime()));
			String wmcode =  new Integer(w.getWMCode()).toString();
			String wmlevel = isNull(1, StringUtils.trimToNull(w.getWMLevel()));
			String dcode = isNull(1, StringUtils.trimToNull(w.getDCode()));
			String svcode  =  isNull(1 , StringUtils.trimToNull(w.getSVCode()));
			String wmcontent = isNull(1, StringUtils.trimToNull(w.getWMContent()));
			String closed =  new Integer(w.getClosed()).toString();
			insertStr = insertStr + "("+ wmdateTime + "," + wmcode +  "," + wmlevel + "," + dcode  + "," + svcode + "," + wmcontent +
			"," + closed + ") , " ;
		}
		insertStr = StringUtils.substring(insertStr, 0, insertStr.length()- 3 );
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" INSERT INTO warmlog ( WMDateTime , WMCode , WMLevel,DCode , SVCode , WMContent , Closed) " +
						" Values " + insertStr ;
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
		return 0;

	}
}
