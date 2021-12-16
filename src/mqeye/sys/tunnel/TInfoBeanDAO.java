package mqeye.sys.tunnel;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mqeye.data.vo.AbstractDAO;
import mqeye.service.tools.EncryDecryUtil;

public class TInfoBeanDAO extends AbstractDAO{
	
	public int validTInfo(String actPort,int valid)
	{	int rs = 0;
		Connection conn = null ;
		Statement stmt	= null;
		try{
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql = " Update tunnelInfo set TIValid = " + valid + " where ActPort ='" + actPort + "'";
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
	
	public TInfoBean getTInfoBean(String TICode)
	{
		Connection conn = null ;
		Statement stmt	= null;
		TInfoBean tinfo = null ;
		try {		
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " select CSCode, CSName, CSIPAddr, CSUser, CSYH, CSPwd, CSKL, CSWebPort, CSSshPort, CSRSAKey, HeartBeat, IsValid, IsMaster," +
						 " HICode, HIName, HIPubIP, HIUser, HIYH, HIPwd, HIKL, HIDBPort, HIWebPort, HISshPort, HIRSAKey, HIMac, HISN," +
						 " TICode , TIName , ActPort, MapPort, TIState, TIValid " +
						 " from tinfoview where TICode = '" + TICode +"'";
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				tinfo = new TInfoBean() ;
				String hiUser = EncryDecryUtil.MyDecry(rs.getString("HIYH"));
				String hiPwd = EncryDecryUtil.MyDecry(rs.getString("HIKL"));
				String csUser = EncryDecryUtil.MyDecry(rs.getString("CSYH"));
				String csPwd = EncryDecryUtil.MyDecry(rs.getString("CSKL"));
				
				tinfo.setCsCode(rs.getString("CSCode"));
				tinfo.setCsName(rs.getString("CSName"));
				tinfo.setCsIpAddr(rs.getString("CSIPAddr"));
				tinfo.setCsUser(csUser);
				tinfo.setCsYH(rs.getString("CSYH"));
				tinfo.setCsPwd(csPwd);
				tinfo.setCsKL(rs.getString("CSKL"));
				tinfo.setCsWebPort(rs.getString("CSWebPort"));
				tinfo.setCsSshPort(rs.getString("CSSshPort"));
				tinfo.setCsRSAKey(rs.getString("CSRSAKey"));
				tinfo.setHeartBeat(rs.getString("HeartBeat"));
				tinfo.setIsValid(rs.getInt("IsValid"));
				tinfo.setIsMaster(rs.getInt("IsMaster"));
				
				tinfo.setHiCode(rs.getString("HICode"));
				tinfo.setHiName(rs.getString("HIName"));
				tinfo.setHiUser(hiUser);
				tinfo.setHiYH(rs.getString("HIYH"));
				tinfo.setHiPwd(hiPwd);
				tinfo.setHiKL(rs.getString("HIKL"));
				tinfo.setHiDBPort(rs.getString("HIDBPort"));
				tinfo.setHiWebPort(rs.getString("HIWebPort"));
				tinfo.setHiSshPort(rs.getString("HISshPort"));
				tinfo.setHiRSAKey(rs.getString("HIRSAKey"));
				tinfo.setHiMac(rs.getString("HIMac"));
				tinfo.setHiSN(rs.getString("HISN"));
				
				tinfo.setTiCode(rs.getString("TICode"));
				tinfo.setTiName(rs.getString("TIName"));
				tinfo.setActPort(rs.getString("ActPort"));
				tinfo.setMapPort(rs.getString("MapPort"));
				tinfo.setTiState(rs.getInt("TIState"));
				tinfo.setTiState(rs.getInt("TIValid"));
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
		return tinfo ;
	}
	
	public List<TInfoBean> getAllTInfoBean( )
	{
		Connection conn = null ;
		Statement stmt	= null;
		List<TInfoBean> list = new ArrayList<TInfoBean>() ;
		try {		
			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= " select CSCode, CSName, CSIPAddr, CSUser, CSYH, CSPwd, CSKL, CSWebPort, CSSshPort, CSRSAKey, HeartBeat, IsValid, IsMaster," +
						 " HICode, HIName, HIPubIP, HIUser, HIYH, HIPwd, HIKL, HIDBPort, HIWebPort, HISshPort, HIRSAKey, HIMac, HISN," +
						 " TICode , TIName , ActPort, MapPort, TIState, TIValid " +
						 " from tinfoview  ";
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) {
				TInfoBean tinfo = new TInfoBean() ;
				String hiUser = EncryDecryUtil.MyDecry(rs.getString("HIYH"));
				String hiPwd = EncryDecryUtil.MyDecry(rs.getString("HIKL"));
				String csUser = EncryDecryUtil.MyDecry(rs.getString("CSYH"));
				String csPwd = EncryDecryUtil.MyDecry(rs.getString("CSKL"));
				
				tinfo.setCsCode(rs.getString("CSCode"));
				tinfo.setCsName(rs.getString("CSName"));
				tinfo.setCsIpAddr(rs.getString("CSIPAddr"));
				tinfo.setCsUser(csUser);
				tinfo.setCsYH(rs.getString("CSYH"));
				tinfo.setCsPwd(csPwd);
				tinfo.setCsKL(rs.getString("CSKL"));
				tinfo.setCsWebPort(rs.getString("CSWebPort"));
				tinfo.setCsSshPort(rs.getString("CSSshPort"));
				tinfo.setCsRSAKey(rs.getString("CSRSAKey"));
				tinfo.setHeartBeat(rs.getString("HeartBeat"));
				tinfo.setIsValid(rs.getInt("IsValid"));
				tinfo.setIsMaster(rs.getInt("IsMaster"));
				
				tinfo.setHiCode(rs.getString("HICode"));
				tinfo.setHiName(rs.getString("HIName"));
				tinfo.setHiUser(hiUser);
				tinfo.setHiYH(rs.getString("HIYH"));
				tinfo.setHiPwd(hiPwd);
				tinfo.setHiKL(rs.getString("HIKL"));
				tinfo.setHiDBPort(rs.getString("HIDBPort"));
				tinfo.setHiWebPort(rs.getString("HIWebPort"));
				tinfo.setHiSshPort(rs.getString("HISshPort"));
				tinfo.setHiRSAKey(rs.getString("HIRSAKey"));
				tinfo.setHiMac(rs.getString("HIMac"));
				tinfo.setHiSN(rs.getString("HISN"));
				
				tinfo.setTiCode(rs.getString("TICode"));
				tinfo.setTiName(rs.getString("TIName"));
				tinfo.setActPort(rs.getString("ActPort"));
				tinfo.setMapPort(rs.getString("MapPort"));
				tinfo.setTiState(rs.getInt("TIState"));
				tinfo.setTiValid(rs.getInt("TIValid"));
				
				list.add(tinfo);
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
		return list ;
	}
	
	
//	public TunnelInfo getTunnelInfo(String TICode)
//	{
//		Connection conn = null ;
//		Statement stmt	= null;
//		TunnelInfo tinfo = null ;
//		try {		
//			conn = cpm.getConnection("mqeye");
//			stmt = conn.createStatement();
//			String sql= "  SELECT TICode , TunnelInfoName , MapPort , localUser , BDYH , localPwd , BDKL ,localHost, localSSHPort, " +
//						  " remoteUser , YCYH , remotePwd , YCKL , remoteServer , remoteSSHPort FROM tunnelinfo " +
//						  " WHERE IsValid = 1 AND TICode = '" +  TICode + "'" ;
//			ResultSet rs = stmt.executeQuery(sql);
//
//			if (rs.next()) {
//				tinfo = new TunnelInfo() ;
//				String localUser = EncryDecryUtil.MyDecry(rs.getString("BDYH"));
//				String localPwd = EncryDecryUtil.MyDecry(rs.getString("BDKL"));
//				String remoteUser = EncryDecryUtil.MyDecry(rs.getString("YCYH"));
//				String remotePwd = EncryDecryUtil.MyDecry(rs.getString("YCKL"));
//				
//				tinfo.setMapPort(rs.getString("MapPort"));
//				tinfo.setLocalUser(localUser);
//				tinfo.setLocalPwd(localPwd);
//				tinfo.setLocalhost(rs.getString("localHost"));
//				tinfo.setLocalSSHPort(rs.getString("localSSHPort"));
//				
//				tinfo.setRemoteUser(remoteUser);
//				tinfo.setRemotePwd(remotePwd);
//				tinfo.setRemoteServer(rs.getString("remoteServer"));
//				tinfo.setRemoteSSHPort(rs.getString("remoteSSHPort"));
//			}
//			rs.close();
//			stmt.close();
//		}catch(SQLException e){
//			e.printStackTrace();
//		}finally{
//			try{ 
//				conn.close();
//			}catch(SQLException err){
//				err.printStackTrace();
//			}
//		}
//		return tinfo ;
//	}
//	
//	
//	public List<TunnelInfo> getAllTunnelInfo()
//	{
//		Connection conn = null ;
//		Statement stmt	= null;
//		List<TunnelInfo> tiList = null ;
//		try {		
//			conn = cpm.getConnection("mqeye");
//			stmt = conn.createStatement();
//			String sql= "  SELECT TICode , TunnelInfoName , MapPort , localUser , BDYH , localPwd , BDKL ,localHost, localSSHPort, " +
//						  " remoteUser , YCYH , remotePwd , YCKL , remoteServer , remoteSSHPort FROM tunnelinfo " +
//						  " WHERE IsValid = 1 " ;
//			
//			ResultSet rs = stmt.executeQuery(sql);
//			tiList = new ArrayList<TunnelInfo>() ;
//			while (rs.next()) {
//				String localUser = EncryDecryUtil.MyDecry(rs.getString("BDYH"));
//				String localPwd = EncryDecryUtil.MyDecry(rs.getString("BDKL"));
//				String remoteUser = EncryDecryUtil.MyDecry(rs.getString("YCYH"));
//				String remotePwd = EncryDecryUtil.MyDecry(rs.getString("YCKL"));
//				
//				TunnelInfo tinfo = new TunnelInfo();
//				tinfo.setMapPort(rs.getString("MapPort"));
//				tinfo.setLocalUser(localUser);
//				tinfo.setLocalPwd(localPwd);
//				tinfo.setLocalhost(rs.getString("localHost"));
//				tinfo.setLocalSSHPort(rs.getString("localSSHPort"));
//				
//				tinfo.setRemoteUser(remoteUser);
//				tinfo.setRemotePwd(remotePwd);
//				tinfo.setRemoteServer(rs.getString("remoteServer"));
//				tinfo.setRemoteSSHPort(rs.getString("remoteSSHPort"));
//				tiList.add(tinfo);
//			}
//			rs.close();
//			stmt.close();
//		}catch(SQLException e){
//			e.printStackTrace();
//		}finally{
//			try{ 
//				conn.close();
//			}catch(SQLException err){
//				err.printStackTrace();
//			}
//		}
//		return tiList ;
//	}

}
