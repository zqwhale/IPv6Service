package mqeye.service.local.config;

public class NetworkBean {
	public final static String DEFINENAME = "defineName" ;
	public final static String DEVICE = "DEVICE" ;
	public final static String BOOTPROTO = "BOOTPROTO" ;
	public final static String ONBOOT = "ONBOOT" ;
	public final static String HWADDR = "HWADDR" ;
	public final static String NETMASK = "NETMASK" ;
	public final static String IPADDR = "IPADDR" ;
	public final static String GATEWAY = "GATEWAY" ;
	public final static String TYPE = "TYPE" ;
	public final static String USERCTL = "USERCTL" ;
	public final static String IPV6INIT = "IPV6INIT" ;
	public final static String PEERDNS = "PEERDNS" ;
	
	
	private String defineName ;	//mqeye define name
	public String getDefineName() {
		return defineName;
	}
	public void setDefineName(String defineName) {
		this.defineName = defineName;
	}
	
	private String state ;	//eth0~n is Up/Down
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

	private String device ;			//eth0~n
	private String bootproto ; //DHCP: dhcp|static|none
	private String onboot ;		//auto boot:  yes|no
	private String hwaddr ;		//MAC
	private String netmask ;	//
	private String ipaddr ;	//IP
	private String gateway ;	//
	private String type = "Ethernet";	//Ethernet
	private String userctl = "no" ;  // Only root activity yes|no	
	private String ip6init = "no"; // yes|no
	private String peerdns = "yes";	//yes|no
	
	public String getDevice() {
		return device;
	}
	public void setDevice(String device) {
		this.device = device;
	}
	public String getBootproto() {
		return bootproto;
	}
	public void setBootproto(String bootproto) {
		this.bootproto = bootproto;
	}
	public String getOnboot() {
		return onboot;
	}
	public void setOnboot(String onboot) {
		this.onboot = onboot;
	}
	public String getHwaddr() {
		return hwaddr;
	}
	public void setHwaddr(String hwaddr) {
		this.hwaddr = hwaddr;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public String getIpaddr() {
		return ipaddr;
	}
	public void setIpaddr(String ipaddr) {
		this.ipaddr = ipaddr;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getUserctl() {
		return userctl;
	}
	public void setUserctl(String userctl) {
		this.userctl = userctl;
	}
	public String getIp6init() {
		return ip6init;
	}
	public void setIp6init(String ip6init) {
		this.ip6init = ip6init;
	}
	public String getPeerdns() {
		return peerdns;
	}
	public void setPeerdns(String peerdns) {
		this.peerdns = peerdns;
	}
	
	
	
}
