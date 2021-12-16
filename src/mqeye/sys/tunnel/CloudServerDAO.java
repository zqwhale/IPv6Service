package mqeye.sys.tunnel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.tools.EncryDecryUtil;

public class CloudServerDAO  extends AbstractDAO{

	public int setHeartBeat(CloudServer server )
	{
		Connection conn = null ;
		Statement stmt	= null;
		int rs = 0;
		if (server !=null && StringUtils.isNotEmpty(server.getHeartBeat()))
		{
			String heartbeat = server.getHeartBeat();
			try {		
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update cloudserver Set HeartBeat = '" + heartbeat + "' Where CSCode = '" + server.getCsCode() + "'";
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
		}
		return rs ;
	}
	public CloudServer getValidMasterCServer()
	{
		Connection conn = null ;
		Statement stmt	= null;
		CloudServer cserver = null;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " select CSCode , CSName ,CSIPAddr,CSYH,CSKL,CSWebPort,CSSshPort,CSRSAKey,HeartBeat,IsValid,IsMaster " +
			" from cloudserver where IsValid = 1 and IsMaster = 1 ";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				cserver = new CloudServer() ;
				
				String csUser = EncryDecryUtil.MyDecry(rs.getString("CSYH"));
				String csPwd = EncryDecryUtil.MyDecry(rs.getString("CSKL"));
				cserver.setCsCode(rs.getString("CSCode"));
				cserver.setCsName(rs.getString("CSName"));
				cserver.setCsIPAddr(rs.getString("CSIPAddr"));
				cserver.setCsUser(csUser);
				cserver.setCsPwd(csPwd);
				cserver.setCsWebPort(rs.getString("CSWebPort"));
				cserver.setCsSshPort(rs.getString("CSSshPort"));
				cserver.setCsRsaKey(rs.getString("CSRSAKey"));
				cserver.setHeartBeat(rs.getString("HeartBeat"));
				cserver.setIsValid(rs.getInt("IsValid"));
				cserver.setIsMaster(rs.getInt("IsMaster"));
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
		return cserver ;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloudServer cserver = new CloudServerDAO().getValidMasterCServer();
		System.out.println(cserver.getCsName());
	}

}
