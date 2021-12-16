package mqeye.service;

public interface ICMD {
	public final static String START = "START";
	public final static String STOP = "STOP";
	public final static String RESTART = "RESTART";
	public final static String REFRESH = "REFRESH";
	public final static String BYE = "BYE";
	public final static String TURNON = "TURNON";
	public final static String TEST = "TEST";
	public final static String QUERY = "QUERY";
	
	public final static String PUSH = "PUSH";
	public final static String DISCON = "DISCON";
	public final static String ISLIVE = "ISLIVE";
	
	public final static String SSH = "MQeye7.7!For@Mobile#SSH$Connect%key=148";
	
	public final static String ERRCMD = "#ERROR#";		//ERROR COMMAND
	public final static String COMMADS[] = {  TURNON , START , STOP , RESTART , REFRESH , BYE , QUERY ,PUSH , DISCON,ISLIVE};
	
	/* Return Message */
	public final static String EMPTY = "#EMPTY#" ; //Nothing return 
	public final static String BOC = "#BEGIN#";		//BEGIN OF COMMAND EXECUTE
	public final static String EOC = "#END#";		//END OF COMMAND EXECUTE
	public final static String DOC = "#DEAD#";		//DEAD OF COMMAND EXECUTE
}
