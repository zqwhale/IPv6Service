package mqeye.service.evolution;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mqeye.data.vo.AbstractDAO;

public class EvolutionDAO extends AbstractDAO {
	public int recordToDB(List<Evolution> elist){
		int rs =0 ;
		Connection conn = null ;
		Statement stmt	= null ;
		
		if ( elist.size()<=0) return 0 ;
		String insertStr = "    " ;
		for(Evolution e:elist){
			String evodateTime = isNull(1,StringUtils.trimToNull(e.getEvoDateTime()));
			String dcode = isNull(1, StringUtils.trimToNull(e.getDcode()));
			String method  =  isNull(1 , StringUtils.trimToNull(e.getMethod()));
			String pingFlag = isNull(2 , StringUtils.trimToNull(e.getPingFlag()));   /* modify SubPort value from null to ''*/
			String pingTimeConsum = new Float(e.getPingTimeConsum()).toString();
			String pingStatus = isNull(2 , StringUtils.trimToNull(e.getPingStatus()));
			String pingLosePacket = new Long(e.getPingLosePacket()).toString();
			String pingLosePacketRate = new Float(e.getPingLosePacketRate()).toString();
 			
			insertStr = insertStr + "("+ evodateTime +"," + dcode  + "," + method + "," + pingFlag + "," + pingTimeConsum + "," +
				pingStatus + "," + pingLosePacket + "," + pingLosePacketRate + ") , " ;
		}
		insertStr = StringUtils.substring(insertStr, 0, insertStr.length()- 3 );
		
		try {
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql=" INSERT INTO evolution ( EvoDateTime , DCode , Method , PingFlag , PingTimeConsum , PingStatus , PingLosePacket , PingLosePacketRate ) " +
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
		return rs ;
	}
}
