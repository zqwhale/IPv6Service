package mqeye.sys.video;

import java.util.List;

import mqeye.service.detect.PortResult;
import mqeye.service.tools.ErrorLog;
import mqeye.service.tools.PortConnect;
import mqeye.sys.tunnel.HostInfo;
import mqeye.sys.tunnel.HostInfoDAO;

import org.apache.commons.lang.StringUtils;

public class CloudCameraHolder {
	
	private ErrorLog elog = new ErrorLog(this.getClass());
	private HostInfo host = null ;
	
	public CloudCameraHolder(){
		HostInfoDAO dao = new HostInfoDAO();
		host = dao.getHostInfo();
		
	}
	
	public int webPortValid(CloudVideo cvideo)
	{
		int flag = 0;
		String ipAddr = cvideo.getCvIPAddr();
		String webPort = cvideo.getCvWebPort();
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ipAddr ,  webPort );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
			flag  = 1;
		else
				elog.log(ipAddr + " " + webPort + " Port is Close!");
		return flag;
	}
	
	public int mdPortValid(CloudVideo cvideo)
	{
		int flag = 0;
		String ipAddr = cvideo.getCvIPAddr();
		String mdPort = cvideo.getCvMdPort();
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ipAddr ,  mdPort );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
			flag  = 1;
		else
				elog.log(ipAddr + " " + mdPort + " Port is Close!");
		return flag;
	}
	
	public int cmPortValid(CameraBean camera)
	{
		int flag = 0;
		String ipAddr = camera.getCmIPAddr();
		String cmPort = camera.getCmPort();
		PortConnect conn = new PortConnect();
		PortResult r = conn.connect( ipAddr ,  cmPort );
		if (StringUtils.equals(r.getStatus(), PortConnect.PORT_OPEN))
			flag  = 1;
		else
				elog.log(ipAddr + " " + cmPort + " Port is Close!");
		return flag;
	}
	
	public String getInputUrl(CameraBean camera){
		String iurl = camera.getInputUrl();
		iurl = StringUtils.replace(iurl, "#USER#", camera.getCmuser());
		iurl = StringUtils.replace(iurl, "#PWD#", camera.getCmpwd());
		iurl = StringUtils.replace(iurl, "#IPADDR#", camera.getCmIPAddr());
		iurl = StringUtils.replace(iurl, "#PORT#", camera.getCmPort());
		return iurl;
	}
	
	public String getRemoteUrl(CloudVideo cvideo, CameraBean camera)
	{
		String rurl = camera.getOutputRemoteUrl();
		rurl = StringUtils.replace(rurl, "#IPADDR#", cvideo.getCvIPAddr());
		rurl = StringUtils.replace(rurl, "#PORT#", cvideo.getCvMdPort());
		rurl = StringUtils.replace(rurl, "#CODE#", host.getHiSn() +"_"+camera.getCmCode());
		return rurl;
	}
	
	public String getLocalUrl(CameraBean camera)
	{
		String lurl = camera.getOutputLocalUrl();
		lurl = StringUtils.replace(lurl, "#CODE#", host.getHiSn()+"_"+camera.getCmCode());
		return lurl;
	}
	
	public String getHttpRemoteUrl(CloudVideo cvideo,  CameraBean camera )
	{
		String hurl = camera.getHttpRemoteUrl();
		hurl = StringUtils.replace(hurl, "#IPADDR#", cvideo.getCvIPAddr());
		hurl = StringUtils.replace(hurl, "#PORT#", cvideo.getCvWebPort());
		hurl = StringUtils.replace(hurl, "#CODE#", host.getHiSn() +"_"+camera.getCmCode() );
		return hurl;
	}
	
	public String getHttpLocalUrl(CameraBean camera )
	{
		String hurl = camera.getHttpLocalUrl();
		hurl = StringUtils.replace(hurl, "#IPADDR#", host.getHiPubIp());
		hurl = StringUtils.replace(hurl, "#PORT#", host.getHiWebPort());
		hurl = StringUtils.replace(hurl, "#CODE#", host.getHiSn() +"_"+camera.getCmCode() );
		return hurl;
	}
	
	public CameraBean getValidCameraBean( CloudVideo cvideo, CameraBean camera){
		
		
		String iurl = 	getInputUrl(camera);
		String olurl = 	getLocalUrl(camera);
		String hlurl = 	getHttpLocalUrl(camera);
		
		
		camera.setInputUrl(iurl);
		camera.setOutputLocalUrl(olurl);
		camera.setHttpLocalUrl(hlurl);

		if (cvideo!=null)
		{
			String rurl = getRemoteUrl(cvideo , camera);
			String hrurl = getHttpRemoteUrl(cvideo , camera);
			
			camera.setHttpRemoteUrl(hrurl);
			camera.setOutputRemoteUrl(rurl);
		}

		return camera;
	}
	
	
	
	public static void main(String[] args) {
		CloudCameraHolder holder = new CloudCameraHolder();
		CloudVideoDAO dao2 = new CloudVideoDAO();
		CloudVideo cvideo = dao2.getValidMasterCVideo();
		
		CameraBeanDAO dao = new CameraBeanDAO();
		List<CameraBean> list = dao.getAllCameraBean();
		for(CameraBean camera:list)
		{
			camera = holder.getValidCameraBean(cvideo,camera);
			System.out.println(camera.getInputUrl());
			System.out.println(camera.getOutputLocalUrl());
			System.out.println(camera.getHttpLocalUrl());
			System.out.println(camera.getOutputRemoteUrl());
			System.out.println(camera.getHttpRemoteUrl());
			
		}
	}
}
