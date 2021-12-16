package mqeye.sys.tunnel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException; 
import java.sql.Statement;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.tools.EncryDecryUtil;

public class HostInfoDAO extends AbstractDAO {
	
	public HostInfo getHostInfo(){
		Connection conn = null ;
		Statement stmt	= null;
		HostInfo info = null ;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " select HICode ,HIName ,HIPubIP,HIUser,HIYH,HIPwd,HIKL,HIDBPort," + 
			" HIWebPort,HISshPort,HIRSAKey,HIMac,HISN " +
			" from hostinfo ";
			
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				info = new HostInfo() ;
				
				String hiUser = EncryDecryUtil.MyDecry(rs.getString("HIYH"));
				String hiPwd = EncryDecryUtil.MyDecry(rs.getString("HIKL"));
				info.setHiCode(rs.getString("HICode"));
				info.setHiName(rs.getString("HIName"));
				info.setHiPubIp(rs.getString("HIPubIP"));
				info.setHiUser(hiUser);
				info.setHiYH(rs.getString("HIYH"));
				info.setHiPwd(hiPwd);
				info.setHiKL(rs.getString("HIKL"));
				info.setHiDbPort(rs.getString("HIDBPort"));
				info.setHiWebPort(rs.getString("HIWebPort"));
				info.setHiSshPort(rs.getString("HISshPort"));
				info.setHiRsaKey(rs.getString("HIRSAKey"));
				info.setHiMac(rs.getString("HIMac"));
				info.setHiSn(rs.getString("HISN"));
				
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
		
		return info;
	}
}
