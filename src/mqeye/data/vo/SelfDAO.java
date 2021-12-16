package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;
import mqeye.service.tools.BaseCommonFunc;

public class SelfDAO extends AbstractDAO {
	public String getValue( Dsview dsv ){
		String value = null;
		if (dsv!=null)
		{
			if (StringUtils.equalsIgnoreCase(dsv.getSVCode(),Constant.SERVICE_CAMERA_ISLIVE))
			{
				String cmCode = dsv.getDCode();
				value = getIsLive(cmCode);
			}
		}
		return value;
	}
	
	private String getIsLive(String cmCode){
		Connection conn = null ;
		Statement stmt	= null;
		int isLive = -1;
		
		try {			
				conn = cpm.getConnection("mqeye");
				stmt = conn.createStatement();
				String sql= " select isLive,LiveDateTime from cameradevice where isValid = 1 and CMCode = '" + cmCode + "'";
				ResultSet rs = stmt.executeQuery(sql);
				if (rs.next()) {
					isLive = rs.getInt("isLive");
					Date date = BaseCommonFunc.getDateTimeFromStr(rs.getString("LiveDateTime"));
					if (isLive==1 && date !=null)
					{
						Date curr = new Date();
						long span = Math.abs(curr.getTime() - date.getTime());
						if (span > 3 * 60 * 1000) isLive=-2;
						
					}
					
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
			
			return isLive + "";
	}
}
