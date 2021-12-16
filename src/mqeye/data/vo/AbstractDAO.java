package mqeye.data.vo;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import mqeye.service.Constant;
import mqeye.service.tools.EncryDecryUtil;
import snaq.db.ConnectionPool;
import snaq.db.ConnectionPoolManager;
import snaq.db.PasswordDecoder;

class MQeyePasswordDecoder implements PasswordDecoder
{

	@Override
	public char[] decode(String arg0) {
		// TODO Auto-generated method stub
		return EncryDecryUtil.MyDecry(arg0).toCharArray();
	}
}
public abstract class AbstractDAO {
	protected ConnectionPoolManager cpm = null;
	private static  String dbfile = Constant.MQEYE_DB_FILE;
	
	/*如果输入的是字符串 ， 则返回带单引号字符 ， 否则   返回无单引号字符串 , 2 indicate return a '' String */
	protected String isNull(int strFlag ,String data ){
		
		if (strFlag==1)
			return ( data==null ? "null" : "'"+data+"'");
		else if(strFlag==2)
			return ( data==null ? "''" : "'"+ data +"'" );
		else
			return ( data==null ? "null" : data );
	}
	
	public  AbstractDAO(){
		try {
			
			File file = new File(dbfile);
			cpm = ConnectionPoolManager.getInstance(file);
			ConnectionPool pool = cpm.getPool("mqeye");
			MQeyePasswordDecoder decoder = new MQeyePasswordDecoder();
			pool.setPasswordDecoder(decoder);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void free(){
		cpm.release();
	}
}
