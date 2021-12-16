package mqeye.service.vmware;


public class VMWareAlarm
{
	//VMCode
	private String vmCode ;
	
	public String getVmCode() {
		return vmCode;
	}
	public void setVmCode(String vmCode) {
		this.vmCode = vmCode;
	}
	//Alarm Key
	private String alarmKey ;
	public String getAlarmKey() {
		return alarmKey;
	}
	public void setAlarmKey(String alarmKey) {
		this.alarmKey = alarmKey;
	}
	//Entity Name
	private String entityName;
	//Entity Type
	private String entityType ;
	//Alarm Status
	private String overallStatus;
	//Alarm System Name
	private String alarmName;
	//Alarm Name Description
	private String alarmDescription;
	
	public String getAlarmDescription() {
		return alarmDescription;
	}
	public void setAlarmDescription(String alarmDescription) {
		this.alarmDescription = alarmDescription;
	}
	//Trigger Time
	private String triggerTime;
	//Acknowledged Time
	private String acknowledgedTime;
	//Acknowledged by User
	private String acknowledgedByUser;
	// Acknowledged or Not
	private Boolean acknowledged;
	
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getOverallStatus() {
		return overallStatus;
	}
	public void setOverallStatus(String overallStatus) {
		this.overallStatus = overallStatus;
	}
	public String getAlarmName() {
		return alarmName;
	}
	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}
	
	public String getTriggerTime() {
		return triggerTime;
	}
	public void setTriggerTime(String triggerTime) {
		this.triggerTime = triggerTime;
	}
	public String getAcknowledgedTime() {
		return acknowledgedTime;
	}
	public void setAcknowledgedTime(String acknowledgedTime) {
		this.acknowledgedTime = acknowledgedTime;
	}
	public String getAcknowledgedByUser() {
		return acknowledgedByUser;
	}
	public void setAcknowledgedByUser(String acknowledgedByUser) {
		this.acknowledgedByUser = acknowledgedByUser;
	}
	public Boolean getAcknowledged() {
		return acknowledged;
	}
	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}
	
	
	public String toString()
	{
		String str = alarmKey + " :" + entityName + ":" +  alarmName ;
		return str;
		
	}
	
	
	
}
