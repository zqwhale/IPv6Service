package mqeye.data.vo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.PropertiesUtil;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

public class MonitorlogDAO extends AbstractDAO {
	
	
	
	public boolean hasRecentMonitorlog(	String dcode ){
		boolean flag = false ;
		Connection conn = null ;
		Statement stmt	= null;
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MINUTE, -10);
		Date rect = c.getTime();
		String recent = BaseCommonFunc.getStrFromDateTime(rect);
		
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT MDID,MLDateTime,DCode,SVCode,SubPort,SubModule,Value1,Value2,IsWarming,Reserve1,Reserve2" + 
						" FROM mqeye.monitorlog  WHERE DCode = '" + dcode + "' AND MLDateTime >= '" + recent + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) flag = true ;
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
		return flag ;
	}

	
	
	public Monitorlog getMonitorlog(String dcode , String svcode , String subport){
		Connection conn = null ;
		Statement stmt	= null;
		Monitorlog ml = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT MDID , MLDateTime , DCode , SVCode , SubPort , SubModule , Value1 , Value2 ,IsWarming , Reserve1 , Reserve2 "+
						" FROM monitorlog WHERE DCode = '" + dcode + "' AND SVCode = '" + svcode + "' AND SubPort ='" + subport + "' AND Value2 is not null " +
						" ORDER BY MDID DESC LIMIT 0, 1";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				ml = new Monitorlog();
				ml.setMDID(rs.getLong("MDID"));
				ml.setMLDateTime(rs.getString("MLDateTime"));
				ml.setDCode(rs.getString("DCode"));
				ml.setSubPort(rs.getString("SubPort"));
				ml.setSubModule(rs.getString("SubModule"));
				ml.setValue1(rs.getString("Value1"));
				ml.setValue2(rs.getString("Value2"));
				ml.setIsWarming(rs.getInt("IsWarming"));
				ml.setReserve1(rs.getString("Reserve1"));
				ml.setReserve2(rs.getString("Reserve2"));
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
		
		return ml;
	}
	
		
	public int recordToDB(List<Monitorlog> mlist){
		int rs =0 ;
		Connection conn = null ;
		Statement stmt	= null ;
		if (mlist.size()<=0) return 0 ;
		String insertStr = "    " ;
		for(Monitorlog m:mlist){
			String mldateTime = isNull(1,StringUtils.trimToNull(m.getMLDateTime()));
			String dcode = isNull(1, StringUtils.trimToNull(m.getDCode()));
			String svcode  =  isNull(1 , StringUtils.trimToNull(m.getSVCode()));
			String subport = isNull(2 , StringUtils.trimToNull(m.getSubPort()));   /* modify SubPort value from null to ''*/
			String submodule = isNull(1 , StringUtils.trimToNull(m.getSubModule()));
			String value1 = isNull(1 , StringUtils.trimToNull(m.getValue1()));
			String value2 = isNull(1 , StringUtils.trimToNull(m.getValue2()));
			String isWarming = new Integer(m.getIsWarming()).toString();
			
			/* Record currentTable status*/
 			CurrentTblDAO ctdao = new CurrentTblDAO();
 			CurrentTbl ct = new CurrentTbl();
 			ct.setDcode(dcode);
 			ct.setSvcode(svcode);
 			ct.setSubport(subport);
 			ct.setMldatetime(mldateTime);
 			ct.setIsWarning(m.getIsWarming());
 			ctdao.markStatus(ct);
 			
 			
 			/* Record currentTable status*/
 			
			insertStr = insertStr + "("+ mldateTime +"," + dcode  + "," + svcode + "," + subport + "," + submodule + "," +
						value1 + "," + value2 + "," + isWarming + ") , " ;
		}
		insertStr = StringUtils.substring(insertStr, 0, insertStr.length()- 3 );
		
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" INSERT INTO monitorlog ( MLDateTime , DCode , SVCode , SubPort , SubModule , Value1 , Value2 , IsWarming ) " +
						" Values " + insertStr ;
			System.out.println(" Exception size:-------------------" + sql);
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
	
	
	public int exportByJSON(String fname , List<Monitorlog> mlist){
		int flag = 0;
		if (mlist == null || mlist.size()<=0) return 0 ;
		net.sf.json.JSONObject jsonO = null ;
		try {
			FileOutputStream out;
   		  	PrintStream p; 
   		  	out = new FileOutputStream(fname);
   		  	p = new PrintStream( out ); 
   		  	p.print("[");
   		  	for(Monitorlog m:mlist){
   		  			jsonO = net.sf.json.JSONObject.fromObject(m);
   		  			p.println(jsonO.toString()+",");
   		  	}
   		  	p.print("]");
   		  	flag = 1;
   		  	p.close(); 
   		  	out.close();
   		  	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag ;
	}
	
	
	public int removeMonitorlogByDate(String begin , String end){
		int flag = 0;
		Connection conn = null ;
		Statement stmt	= null;
			
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " DELETE FROM mqeye.monitorlog " + 
						" WHERE  MLDateTime >= '" + begin +"' and  MLDateTime <= '" + end + "'";
			flag = stmt.executeUpdate(sql);
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
		return flag ;
	}
	
	public int removeMonitorlogByDate(String end){
		int flag = 0;
		Connection conn = null ;
		Statement stmt	= null;
			
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " DELETE FROM mqeye.monitorlog " + 
						" WHERE  MLDateTime <= '" + end + "'";
			flag = stmt.executeUpdate(sql);
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
		return flag ;
	}
	public List<Monitorlog> getMonitorlogByDate( String begin , String end ){
		Connection conn = null ;
		Statement stmt	= null;
		List<Monitorlog> mlist = null;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT MDID,MLDateTime,DCode,SVCode,SubPort,SubModule,Value1,Value2,IsWarming,Reserve1,Reserve2" + 
						" FROM mqeye.monitorlog  WHERE  MLDateTime >= '" + begin +"' and  MLDateTime <= '" + end + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
			mlist = new ArrayList<Monitorlog>();
			while (rs.next()) {
				Monitorlog m = new Monitorlog();
				m.setMDID(rs.getLong("MDID"));
				m.setMLDateTime(rs.getString("MLDateTime"));
				m.setDCode(rs.getString("DCode"));
				m.setSVCode(rs.getString("SVCode"));
				m.setSubPort(rs.getString("SubPort"));
				m.setSubModule(rs.getString("SubModule"));
				m.setValue1(rs.getString("Value1"));
				m.setValue2(rs.getString("Value2"));
				m.setIsWarming(rs.getInt("IsWarming"));
				m.setReserve1(rs.getString("Reserve1"));
				m.setReserve2(rs.getString("Reserve2"));
				mlist.add(m);
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
		return mlist;
	}

	public void export(String end ,String file){
		PropertiesUtil util=new PropertiesUtil(Constant.MQEYE_FILE);
		String charSet = util.getProperty("charSet");
		
		Connection conn = null ;
		Statement stmt	= null;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT MDID,MLDateTime,DCode,SVCode,SubPort,SubModule,Value1,Value2,IsWarming,Reserve1,Reserve2" + 
						" FROM mqeye.monitorlog  WHERE MLDateTime <= '" + end + "'" ;
			File f = new File(file);
			List<String> tmps = new ArrayList<String>();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				String log = rs.getLong("MDID")+ "," +
				rs.getString("MLDateTime") + "," +
				rs.getString("DCode") + "," + 
				rs.getString("SVCode") + "," +
				rs.getString("SubPort") + "," +
				rs.getString("SubModule") + "," +
				rs.getString("Value1")+ "," +
				rs.getString("Value2")+ "," +
				rs.getInt("IsWarming")+ "," +
				rs.getString("Reserve1")+ "," +
				rs.getString("Reserve2") ;
				
				tmps.add(log);
					
			}
			FileUtils.writeLines(f, charSet, tmps);
			sql = "DELETE FROM mqeye.monitorlog  WHERE MLDateTime <= '" + end + "'";
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
	
	public List<Monitorlog> getMonitorlogByDate(String end ){
		Connection conn = null ;
		Statement stmt	= null;
		List<Monitorlog> mlist = null;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " SELECT MDID,MLDateTime,DCode,SVCode,SubPort,SubModule,Value1,Value2,IsWarming,Reserve1,Reserve2" + 
						" FROM mqeye.monitorlog  WHERE MLDateTime <= '" + end + "'";
			
			ResultSet rs = stmt.executeQuery(sql);
			mlist = new ArrayList<Monitorlog>();
			while (rs.next()) {
				Monitorlog m = new Monitorlog();
				m.setMDID(rs.getLong("MDID"));
				m.setMLDateTime(rs.getString("MLDateTime"));
				m.setDCode(rs.getString("DCode"));
				m.setSVCode(rs.getString("SVCode"));
				m.setSubPort(rs.getString("SubPort"));
				m.setSubModule(rs.getString("SubModule"));
				m.setValue1(rs.getString("Value1"));
				m.setValue2(rs.getString("Value2"));
				m.setIsWarming(rs.getInt("IsWarming"));
				m.setReserve1(rs.getString("Reserve1"));
				m.setReserve2(rs.getString("Reserve2"));
				mlist.add(m);
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
		return mlist;
	}
	
	
	public int recordToFile(List<Monitorlog> mlist){
		int flag = 0;
		if (mlist.size()<=0) return 0 ;
		String insertStr = "    " ;
		for(Monitorlog m:mlist){
			String mldateTime = isNull(1,StringUtils.trimToNull(m.getMLDateTime()));
			String dcode = isNull(1, StringUtils.trimToNull(m.getDCode()));
			String svcode  =  isNull(1 , StringUtils.trimToNull(m.getSVCode()));
			String submodule = isNull(1 , StringUtils.trimToNull(m.getSubModule()));
			String subport = isNull(1 , StringUtils.trimToNull(m.getSubPort()));
			String value1 = isNull(1 , StringUtils.trimToNull(m.getValue1()));
			String value2 = isNull(0 , StringUtils.trimToNull(m.getValue2()));
			String isWarming = new Integer(m.getIsWarming()).toString();
			insertStr = insertStr + "("+ mldateTime +"," + dcode  + "," + svcode + "," + subport + "," + submodule + "," +
						value1 + "," + value2 + "," + isWarming + ") , " ;
		}
		StringUtils.substring(insertStr, 0, insertStr.length()- 3 );
		
		try {
			File file = new File("monitor.tmp");
			FileUtils.writeStringToFile(file, insertStr , "gbk");
			flag = 1;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return flag ;
		
	}
	
	
	
	public int restore(){

		String insertStr = "";
		File file = new File("monitor.tmp");
		try {
			insertStr = FileUtils.readFileToString(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (insertStr.trim().length()>0) return 0;
		
		Connection conn = null ;
		Statement stmt	= null ;
		int rs = 0;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			
			String sql=" INSERT INTO Monitorlog ( MLDateTime , DCode , SVCode , SubPort , SubModule , Value1 , Value2 , IsWarming ) " +
			" Values " + insertStr ;
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
		
		try {
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rs ;
	}
	
	
	
}
