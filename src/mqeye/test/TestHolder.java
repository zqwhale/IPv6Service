package mqeye.test;

import org.apache.commons.lang.StringUtils;

import mqeye.sys.tunnel.CloudServer;
import mqeye.sys.tunnel.CloudServerDAO;
import mqeye.sys.tunnel.CloudServerHolder;

public class TestHolder {

	public void testConvert()
	{
		String value2 = "32880492,4147076,202304,4355656";
		String cvalue = null ;
		if ( StringUtils.isNotEmpty(value2) )
		{
				String[] values = StringUtils.split(value2 , ","); 
				long sum = 0 , max = 0 , cnt = 0;
				for (String v : values)
				{
					 if (StringUtils.isNumeric(v))
					  {
						 long data = Long.parseLong(v);
						 if (max < data ) max = data;
						 sum = sum + data;
						 cnt++;
					  }
				}
				
				sum = sum - max ;
				if ( max > sum ){
						long memsize =	(long) 100*(max - sum)/max;
						cvalue = memsize + "";
				}
		}
		System.out.println(cvalue);
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		CloudServerDAO dao = new CloudServerDAO();
//		CloudServerHolder holder = new CloudServerHolder();
//		CloudServer s = dao.getValidMasterCServer();
//		System.out.println(holder.heartBeatValid(s));
//		System.out.println(holder.sshLoginValid(s));
		new TestHolder().testConvert();
	}

}
