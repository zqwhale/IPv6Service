package mqeye.service.local.config;

public class RouteBean {
	
		private String destination ;
		private String gateway ;
		private String genmask ;
		private String flags ;
		private String metric ;
		private String ref ;
		private String use ;
		private String iface ;
		
		public String getDestination() {
			return destination;
		}
		public void setDestination(String destination) {
			this.destination = destination;
		}
		public String getGateway() {
			return gateway;
		}
		public void setGateway(String gateway) {
			this.gateway = gateway;
		}
		public String getGenmask() {
			return genmask;
		}
		public void setGenmask(String genmask) {
			this.genmask = genmask;
		}
		public String getFlags() {
			return flags;
		}
		public void setFlags(String flags) {
			this.flags = flags;
		}
		public String getMetric() {
			return metric;
		}
		public void setMetric(String metric) {
			this.metric = metric;
		}
		public String getRef() {
			return ref;
		}
		public void setRef(String ref) {
			this.ref = ref;
		}
		public String getUse() {
			return use;
		}
		public void setUse(String use) {
			this.use = use;
		}
		public String getIface() {
			return iface;
		}
		public void setIface(String iface) {
			this.iface = iface;
		}
		
}
