package mqeye.test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import mqeye.service.tools.ClassLoaderUtils;
import snaq.db.ConnectionPoolManager;


public class TestDB {
	private ConnectionPoolManager cpm = null;
	public  TestDB(){
			try {
				URL url = ClassLoaderUtils.getExtendResource("../conf/dbpool.properties");
				File file = new File(url.getFile());
				cpm = ConnectionPoolManager.getInstance(file);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
	}
	
	public void insertData(){
		Connection conn = null ;
		Statement stmt	= null;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql="INSERT INTO test.monitorlog(MLDateTime,DCode,SVCode,Value1,Value2) VALUES('2014-07-07 10:10:10',1,1,'1',1) ";
			int rs = stmt.executeUpdate(sql);
			System.out.println(rs);
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
	
	public void deleteData(){
		Connection conn = null ;
		Statement stmt	= null;
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql="Delete From test.monitorlog ";
			int rs = stmt.executeUpdate(sql);
			System.out.println(rs);
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
	public void free(){
		cpm.release();
	}
	
		
}
