package mqeye.data.vo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import mqeye.service.serial.SerialUsbCfgTool;
import mqeye.service.tools.DebugTool;

public class SerialDeviceDAO extends AbstractDAO {
	
	public String getCurrSysPort(String dcode)
	{
		SerialDevice sd = getSerialDeviceByDCode(dcode);
		String phyid = sd.getPhyid();
		String uscode = sd.getSysport();
		if (uscode==null || StringUtils.equals( uscode , SerialUsbCfgTool.UNKNOW_PORT))
		{
				DebugTool.printErr("Serial (" + phyid + ") USB port is Not Exist!");
		}
		return uscode;
	}
	
	
	public List<SerialDevice> getSerialDeviceAll( ){
		Connection conn = null ;
		Statement stmt	= null;
		List<SerialDevice> sdlist = new ArrayList<SerialDevice>();
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= "SELECT DCode , BSCode , Protocol , CfgPort , PhyID , rate ," +
					"databits, stopbits, delayread, timeout, parity , flowcontrol, shortaddr," +
					"checkalg,	 pattern, startchar, endchar, Reserve1, Reserve2, Reserve3 " +
					"from serialDevice ";
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
				SerialDevice sd = new SerialDevice();
				sd.setDcode(rs.getString("DCode"));
				sd.setBscode(rs.getString("BSCode"));
				sd.setProtocol(rs.getString("Protocol"));
				sd.setCfgport(rs.getString("CfgPort"));
				sd.setPhyid(rs.getString("PhyID"));
				sd.setRate(rs.getInt("rate"));
				sd.setDatabits(rs.getInt("databits"));
				sd.setStopbits(rs.getInt("stopbits"));
				sd.setDelayread(rs.getInt("delayread"));
				sd.setTimeout(rs.getInt("timeout"));
				sd.setParity(rs.getInt("parity"));
				sd.setFlowcontrol(rs.getInt("flowcontrol"));
				sd.setShortaddr(rs.getString("shortaddr"));
				sd.setCheckalg(rs.getString("checkalg"));
				sd.setPattern(rs.getString("pattern"));
				sd.setStartchar(rs.getString("startchar"));
				sd.setEndchar(rs.getString("endchar"));
				sd.setReserve1(rs.getString("Reserve1"));
				sd.setReserve2(rs.getString("Reserve2"));
				sd.setReserve3(rs.getString("Reserve3"));
				sdlist.add(sd);
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				if(conn!=null)	conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		
		return sdlist ;
	}
	
	public SerialDevice getSerialDeviceByDCode( String dcode){
		Connection conn = null ;
		Statement stmt	= null;
		SerialDevice sd = null ;
		try {			
			conn = cpm.getConnection("mqeye");
			stmt = conn.createStatement();
			String sql= "SELECT DCode , BSCode , Protocol , CfgPort , PhyID , rate ," +
					"databits, stopbits, delayread, timeout, parity , flowcontrol, shortaddr," +
					"checkalg,pattern,startchar, endchar,Reserve1,Reserve2,Reserve3 " +
					"from serialDevice WHERE DCode = '" + dcode + "'";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				sd = new SerialDevice();
				sd.setDcode(rs.getString("DCode"));
				sd.setBscode(rs.getString("BSCode"));
				sd.setProtocol(rs.getString("Protocol"));
				sd.setCfgport(rs.getString("CfgPort"));
				sd.setPhyid(rs.getString("PhyID"));
				sd.setRate(rs.getInt("rate"));
				sd.setDatabits(rs.getInt("databits"));
				sd.setStopbits(rs.getInt("stopbits"));
				sd.setDelayread(rs.getInt("delayread"));
				sd.setTimeout(rs.getInt("timeout"));
				sd.setParity(rs.getInt("parity"));
				sd.setFlowcontrol(rs.getInt("flowcontrol"));
				sd.setShortaddr(rs.getString("shortaddr"));
				sd.setCheckalg(rs.getString("checkalg"));
				sd.setPattern(rs.getString("pattern"));
				sd.setStartchar(rs.getString("startchar"));
				sd.setEndchar(rs.getString("endchar"));
				sd.setReserve1(rs.getString("Reserve1"));
				sd.setReserve2(rs.getString("Reserve2"));
				sd.setReserve3(rs.getString("Reserve3"));
			}
			rs.close();
			stmt.close();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			try{ 
				if(conn!=null)	conn.close();
			}catch(SQLException err){
				err.printStackTrace();
			}
		}
		return sd;
	}
}
