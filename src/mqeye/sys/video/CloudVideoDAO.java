package mqeye.sys.video;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import mqeye.data.vo.AbstractDAO;

public class CloudVideoDAO extends AbstractDAO {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		CloudVideo cvideo = new CloudVideoDAO().getValidMasterCVideo();
		System.out.println(cvideo.getCvName());
	}
	public CloudVideo getValidMasterCVideo()
	{
		Connection conn = null ;
		Statement stmt	= null;
		CloudVideo cvideo = null;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " select CVCode, CVName, CVIPAddr, CVWebPort,CVProtocol,CVMdPort,IsValid,IsMaster from cloudvideo " +
			" where IsValid = 1 and IsMaster = 1 ";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				cvideo = new CloudVideo() ;
				cvideo.setCvCode(rs.getString("CVCode"));
				cvideo.setCvName(rs.getString("CVName"));
				cvideo.setCvIPAddr(rs.getString("CvIPAddr"));
				cvideo.setCvWebPort(rs.getString("CvWebPort"));
				cvideo.setCvProtocol(rs.getString("CVProtocol"));
				cvideo.setCvMdPort(rs.getString("CVMdPort"));
				cvideo.setIsValid(rs.getInt("IsValid"));
				cvideo.setIsMaster(rs.getInt("IsMaster"));
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
		return cvideo ;
	}
}
