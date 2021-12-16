package mqeye.service.vmware;

public class VCenterCfg {
	/* Configure file Bean */
		private String vCenterAddress ;
		private String vCenterUserName ;
		private String vCenterPwd ;
		
		
		public VCenterCfg( String addr , String user , String pwd ){
			this.vCenterAddress = addr ; 
			this.vCenterUserName = user ; 
			this.vCenterPwd = pwd ; 
		}
		
		private String vmCode ;
		private String vmCenterName ;
		
		
		public String getVmCode() {
			return vmCode;
		}
		public void setVmCode(String vmCode) {
			this.vmCode = vmCode;
		}
		public String getVmCenterName() {
			return vmCenterName;
		}
		public void setVmCenterName(String vmCenterName) {
			this.vmCenterName = vmCenterName;
		}
		public String getvCenterAddress() {
			return vCenterAddress;
		}
		public void setvCenterAddress(String vCenterAddress) {
			this.vCenterAddress = vCenterAddress;
		}
		public String getvCenterUserName() {
			return vCenterUserName;
		}
		public void setvCenterUserName(String vCenterUserName) {
			this.vCenterUserName = vCenterUserName;
		}
		public String getvCenterPwd() {
			return vCenterPwd;
		}
		public void setvCenterPwd(String vCenterPwd) {
			this.vCenterPwd = vCenterPwd;
		}
}
