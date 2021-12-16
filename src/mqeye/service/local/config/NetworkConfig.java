package mqeye.service.local.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import mqeye.service.tools.UnicodeConverter;

import org.apache.commons.lang.StringUtils;

public class NetworkConfig {
   	
	private 	 int SUCCESS = 1 ;
	private			int FAIL = -1 ;
	
	public NetworkConfig(){
	}
		
	public List<NetworkBean> getEthList(){
		List<NetworkBean> beans = null ;
		File f= new File("/etc/sysconfig/network-scripts");
		if (f.isDirectory()){
				File[] flist = f.listFiles(new FileFilter(){
					@Override
					public boolean accept(File pathname) {
						// TODO Auto-generated method stub
						String filename = pathname.getName();
						return (filename.matches("ifcfg-eth\\d{1,2}(:\\d{1})?"));		
					}});
				
				if (flist.length>0){
				beans = new ArrayList<NetworkBean>();
					for(File file:flist){
						NetworkBean bean = read("/etc/sysconfig/network-scripts/"+file.getName());
						beans.add(bean);
					}
				}
				
		}
		return beans;
	}
	
	

	public int cfgMultIP(String eth , String newID ,String newIP , String newNetmask ){
			int flag = FAIL;
			String filePath = "/etc/sysconfig/network-scripts/ifcfg-" + eth;
			NetworkBean bean = read(filePath);
			if (bean!=null){
				bean.setDevice(bean.getDevice() + ":" + newID);
				bean.setIpaddr(newIP);
				bean.setNetmask(newNetmask);
				flag = write(bean);
			}
			
			return flag;
	}
	/*  Modify ifcfg_ethi File , By a NetworkBean */
	public int write(NetworkBean bean){
		 int flag = FAIL;
		 if (bean!=null){
			 Properties props = new Properties();
			 try {
				String filePath = "/etc/sysconfig/network-scripts/ifcfg-"+ bean.getDevice();
				OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath));
				
				if (bean.getDevice()!=null) props.setProperty(NetworkBean.DEVICE,bean.getDevice());
				if (bean.getBootproto()!=null) 	props.setProperty(NetworkBean.BOOTPROTO, bean.getBootproto());
				if (bean.getOnboot()!=null) 	props.setProperty(NetworkBean.ONBOOT, bean.getOnboot());
				if (bean.getHwaddr()!=null) 	props.setProperty(NetworkBean.HWADDR, bean.getHwaddr());
				if (bean.getNetmask()!=null) 	props.setProperty(NetworkBean.NETMASK, bean.getNetmask());
				if (bean.getIpaddr()!=null) 	props.setProperty(NetworkBean.IPADDR, bean.getIpaddr());
				if (bean.getGateway()!=null) 	props.setProperty(NetworkBean.GATEWAY, bean.getGateway());
				if (bean.getType()!=null) 	props.setProperty(NetworkBean.TYPE, bean.getType());
				if (bean.getUserctl()!=null) 	props.setProperty(NetworkBean.USERCTL, bean.getUserctl());
				if (bean.getIp6init()!=null) 	props.setProperty(NetworkBean.IPV6INIT, bean.getIp6init());
				if (bean.getPeerdns()!=null) 	props.setProperty(NetworkBean.PEERDNS, bean.getPeerdns());			
				
				if (bean.getDefineName()!=null) 				
					props.store(outputStream,NetworkBean.DEFINENAME+"="+bean.getDefineName());
				else
					props.store(outputStream,NetworkBean.DEFINENAME);
				
				outputStream.close(); 
				flag = SUCCESS;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   }
		
			return flag;
	}
	
	private String readDefineName(String filePath ){
		String defineName = null;
		try {
			FileInputStream input = new FileInputStream(filePath);
			BufferedReader reader = new BufferedReader(new InputStreamReader(input)) ;
			
			String line = null ;
			while((line = reader.readLine())!=null){
					if (StringUtils.contains(line, NetworkBean.DEFINENAME)){
							String temp[] = line.split("=");
							if (temp.length>1) defineName = temp[1];
							break;
					}
			}
			reader.close();
			input.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return defineName ;
		
	}
	/*  Read ifcfg_ethi File , Convert a NetworkBean */
	public NetworkBean read( String filePath ){
		Properties props = new Properties();
		NetworkBean bean = null ;
		
		try {
			FileInputStream input = new FileInputStream(filePath);
			InputStream in = new BufferedInputStream(input);
			props.load(in);
			bean = new NetworkBean();
			bean.setDevice(props.getProperty (NetworkBean.DEVICE));
			bean.setBootproto(props.getProperty (NetworkBean.BOOTPROTO));
			bean.setOnboot(props.getProperty (NetworkBean.ONBOOT));
			bean.setHwaddr(props.getProperty (NetworkBean.HWADDR));
			bean.setNetmask(props.getProperty (NetworkBean.NETMASK));
			bean.setIpaddr(props.getProperty (NetworkBean.IPADDR));
			bean.setGateway(props.getProperty(NetworkBean.GATEWAY));
			bean.setType(props.getProperty (NetworkBean.TYPE));
			bean.setUserctl(props.getProperty (NetworkBean.USERCTL));
			bean.setIp6init(props.getProperty (NetworkBean.IPV6INIT));
			bean.setPeerdns(props.getProperty (NetworkBean.PEERDNS));
			in.close();
			input.close();
			String dname = readDefineName(filePath);
			
			if (dname!=null) 	{	
				dname = UnicodeConverter.fromEncodedUnicode(dname.toCharArray(),0, dname.length());
				bean.setDefineName(dname); }
			String state = NetworkController.isActivity(bean.getDevice());
			bean.setState(state);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}

}
