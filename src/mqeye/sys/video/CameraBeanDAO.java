package mqeye.sys.video;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;
import mqeye.service.tools.EncryDecryUtil;

public class CameraBeanDAO extends AbstractDAO {
	
	public int isLive(String cmCode , int resultFlag)
	{
		Connection conn = null ;
		Statement stmt	= null;
		if (StringUtils.isNotEmpty(cmCode))
		{
			Date rect = new Date();
			String recent = BaseCommonFunc.getStrFromDateTime(rect);
			
			try {
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql=" Update cameradevice Set  isLive = " + resultFlag + " , LiveDateTime = '" +
				recent + "' where CMCode = '" + cmCode + "'";
				
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
		}
		return 0;
		
	}
	
	public List<CameraBean> getAllCameraBean(){
		Connection conn = null ;
		Statement stmt	= null;
		 List<CameraBean> list = new ArrayList<CameraBean>();
		 
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= 	"  SELECT CMCode , CMName , CMIPAddr , CMPort , CMUser , CMPwd , CMYH , CMKL , " + 
							"	InputParam , InputUrl , OutputLocalParam , OutputLocalUrl , " +
							"	OutputRemoteParam , OutputRemoteUrl , HTTPRemoteUrl ,HTTPLocalUrl , IsValid, IsLive , LiveDateTime" +
							" 	FROM cameradevice where IsValid = 1 ";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				CameraBean camera = new  CameraBean();
				
				String cmUser = EncryDecryUtil.MyDecry(rs.getString("CMYH"));
				String cmPwd = EncryDecryUtil.MyDecry(rs.getString("CMKL"));
				
				camera.setCmCode(rs.getString("CMCode"));
				camera.setCmName(rs.getString("CMName"));
				camera.setCmIPAddr(rs.getString("CMIPAddr"));
				camera.setCmPort(rs.getString("CMPort"));
				camera.setCmuser(cmUser);
				camera.setCmpwd(cmPwd);
				camera.setInputParam(rs.getString("InputParam"));
				camera.setInputUrl(rs.getString("InputUrl"));
				
				camera.setOutputLocalParam(rs.getString("OutputLocalParam"));
				camera.setOutputLocalUrl(rs.getString("OutputLocalUrl"));
				camera.setOutputRemoteParam(rs.getString("OutputRemoteParam"));
				camera.setOutputRemoteUrl(rs.getString("OutputRemoteUrl"));
				
				camera.setHttpRemoteUrl(rs.getString("HTTPRemoteUrl"));
				camera.setHttpLocalUrl(rs.getString("HTTPLocalUrl"));
				camera.setIsValid(rs.getInt("IsValid"));
				camera.setIsLive(rs.getInt("IsLive"));
				
				camera.setLiveDateTime(rs.getString("LiveDateTime"));
				list.add(camera);
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
		
		return list;
	}

	public CameraBean getCameraBean(String cmCode){
		Connection conn = null ;
		Statement stmt	= null;
		CameraBean camera = null ;
		 
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= 	"  SELECT CMCode , CMName , CMIPAddr , CMPort , CMUser , CMPwd , CMYH , CMKL , " + 
							"	InputParam , InputUrl , OutputLocalParam , OutputLocalUrl , " +
							"	OutputRemoteParam , OutputRemoteUrl ,  HTTPRemoteUrl ,HTTPLocalUrl  , IsValid, IsLive , LiveDateTime" +
							" 	FROM cameradevice where CMCode = '" + cmCode + "'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				camera = new  CameraBean();
				
				String cmUser = EncryDecryUtil.MyDecry(rs.getString("CMYH"));
				String cmPwd = EncryDecryUtil.MyDecry(rs.getString("CMKL"));
				
				camera.setCmCode(rs.getString("CMCode"));
				camera.setCmName(rs.getString("CMName"));
				camera.setCmIPAddr(rs.getString("CMIPAddr"));
				camera.setCmPort(rs.getString("CMPort"));
				camera.setCmuser(cmUser);
				camera.setCmpwd(cmPwd);
				camera.setInputParam(rs.getString("InputParam"));
				camera.setInputUrl(rs.getString("InputUrl"));
				
				camera.setOutputLocalParam(rs.getString("OutputLocalParam"));
				camera.setOutputLocalUrl(rs.getString("OutputLocalUrl"));
				camera.setOutputRemoteParam(rs.getString("OutputRemoteParam"));
				camera.setOutputRemoteUrl(rs.getString("OutputRemoteUrl"));
				
				camera.setHttpRemoteUrl(rs.getString("HTTPRemoteUrl"));
				camera.setHttpLocalUrl(rs.getString("HTTPLocalUrl"));
				
				camera.setIsValid(rs.getInt("IsValid"));
				camera.setIsLive(rs.getInt("IsLive"));
				
				camera.setLiveDateTime(rs.getString("LiveDateTime"));
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
		
		return camera;
	}
}
