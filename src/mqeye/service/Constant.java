package mqeye.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import mqeye.service.tools.PropertiesUtil;

public class Constant {
	public static final int POOL_SIZE  = 5;						// ����CPU��Ĭ�ϻ���ش�С
	public static final String BASIC_SERVICE_CODE = "SVTD" ;	// ������������
	public static final String DEFAULT_MODULE = "ģ��";			// Ĭ�� ģ�� ����
	
	
	public static final int SERVICE_RUN = 1;
	public static final int SERVICE_STOP = 0;
	/**
	 *    ���������ļ������ ӳ���ϵ
	 */
	public static final String[] typeNames = {"JHJ","FHQ","WEB","UPS","KT","CGQ","XLH","QT","FWQ","LYQ","SXT","XF"};
	public static final Map<String,String> detectMap = new HashMap<String,String>(){
	
		private static final long serialVersionUID = -2682736394248838427L;

		{
				put("JHJ","mqeye.service.detect.JHJDetector");
				put("LYQ","mqeye.service.detect.JHJDetector");
				put("FHQ","mqeye.service.detect.JHJDetector");
				put("WEB","mqeye.service.detect.CommonDetector");
				put("UPS","mqeye.service.detect.CommonDetector");
				put("KT","mqeye.service.detect.CommonDetector");
				put("CGQ","mqeye.service.detect.CommonDetector");
				put("XLH","mqeye.service.detect.CommonDetector");
				put("QT","mqeye.service.detect.CommonDetector");
				put("FWQ","mqeye.service.detect.FWQDetector");
				put("SXT","mqeye.service.detect.CommonDetector");
				put("XF","mqeye.service.detect.CommonDetector");
				
		}
	};
	
	
	public static final int MAX_LOOP = 10*60;
	/* Serial Type BSCode*/
	public static final String ZIGBEE_TH_CGQ = "BCG001";
	public static final String SWITCH_TH_CGQ = "BCG002";
	public static final String SWITCH_LS_CGQ = "BCG003";
	public static final String ZIGBEE_SERIAL_TYPE = "zigbee";
	public static final String MODBUS_SERIAL_TYPE = "modbus";
	public static final String MODBUS_ASCII_SERIAL_TYPE = "modbus_asc";
	
	/* Serial Server BSCode */
	public static final String STCP_TH_CGQ = "BCG004";
	public static final String STCP_SMOKE_CGQ = "BCG005";
	public static final String STCP_LEAK_CGQ = "BCG007";
	public static final String STCP_P_CGQ = "BCG006";

	/* NetworkController */
	public static final String NETWORK_ACTIVITY = "UP";			
	public static final String NETWORK_DEACTIVITY = "DOWN"; 
	public static final String NETWORK_PHUP = "PH_UP";			
	public static final String NETWORK_PHDOWN = "PH_DOWN";			
	public static final String NETWORK_BIND = "BIND IP";			
	public static final String NETWORK_UNBIND = "UNBIND IP";			
	public static final String NETWORK_UNKNOW = "UNKNOW"; 
	
	
	
	
	/* PING RESULT OLD Version */
	public static final String SNMP_SUCCESS = "2";			// �ɼ�أ���Pingͨ���Telnet����SNMP���ӣ�
	public static final String PING_SUCCESS = "1";			// ����ͨ����Pingͨ���Telnet������SNMP���ӣ�
	public static final String PING_FAIL = "0";				// �����ã�����Pingͨ�򲻿�Telnet������SNMP���ӣ�
	/*PING RESULT*/
	public static final int PING_TIMEOUT = 4000;
	public static final String PING_ERR_UNREACHABLE = "unreachable";		//�޷�pingͨ
	public static final String PING_ERR_PROHIBIT = "prohibit";						//��ֹping
	public static final String PING_REACHABLE = "reachable"; //pingͨ
	public static final float PING_LOSE_PACKET = 100;
	public static final int PING_TIMES = 10;		//Ping�Ĵ���
	public static final int PING_WAIT = 10;		//Ping�ĵȴ�ʱ��
	
	/*HTTP RESULT*/
	public static final long HTTP_TIMEOUT = 15*1000;
	public static final String HTTP_ERR_STATE = "error";
	
	/* SENSOR RESULT*/
	public static final int SENSOR_HUM = -9999;	//��ȡʪ��ʧ��
	public static final double SENSOR_TEMP = -9999; //��ȡ�¶�ʧ��

	/*  TURN ON */
	public static final String TURNON_SUCCESS = "SUCCESS";
	public static final String TURNON_FAIL = "FAIL";
	
	/* Method */
	public static final String SNMP = "SNMP"; 
	public static final String PING = "PING";
	public static final String PORT = "PORT";
	public static final String HTTP = "HTTP";
	public static final String SERI = "SERI";
	public static final String STCP = "STCP";	//Serial Server 
	public static final String VMC = "VMC";		//VMWare Connection 
	public static final String SELF = "SELF";	//MQeye Device 
	
	/* SNMP FAIL GET VALUE ,����ʵ�ʲɼ�ֵ, ���ڼ�¼��ȫ*/
	public static final String FAIL_GET_VALUE = "-99999L" ;    // ���ݲɼ�����
	/* SERVICE IS STOP VALUE  ,����ʵ�ʲɼ�ֵ, ���ڼ�¼��ȫ*/
	public static final String STOPPED_VALUE = "-89999L" ;    // ����ֹͣ�� ���ڼ�¼�˽׶�û�����вɼ����ݵ�״̬
	
	
	
	/* HTTP RESULT*/
	public  static final String STATUS_UNKNOW = "unknow";	
	
	/* ValueType*/
	public static final String STRING_TYPE = "�ַ�";
	public static final String NUMBER_TYPE = "��ֵ";
	
	/*Express */
	public static final String LT = "С��";
	public static final String GT = "����";
	public static final String EQ = "����";
	public static final String NEQ = "������";
	/* Express New Add */
	public static final String LTEQ = "С�ڵ���";
	public static final String GTEQ = "���ڵ���";
	
	/* Express New Add */
	public static final String JY = "����";
	public static final String NJY = "������";
	public static final String SY = "����";
	public static final String NSY = "������";
	
	public static final String SERVICE_PING = "SVTD";
	public static final String SERVICE_PING_TD = "SVTD";
	public static final String SERVICE_PING_DB = "SVWLDB";
	public static final String SERVICE_PING_SY = "SVWLSY";
	public static final String SERVICE_NET_INBOUND = "SVLL";
	public static final String SERVICE_NET_OUTBOUND = "SVCLL";
	public static final String SERVICE_NET_CPU = "SVCPU";
	
	/*HTTP Service Name*/
	public static final String SERVICE_HTTP_STATUS = "SVWEBZT";
	public static final String SERVICE_HTTP_LOADTIME = "SVWEBSY";
	
	/*Sensor Service Name*/
	public static final String SERVICE_SERSOR_WD = "SVWD";
	public static final String SERVICE_SERSOR_SD = "SVSD";
	public static final String SERVICE_SERSOR_YG = "SVSMOKE";
	public static final String SERVICE_SERSOR_LS = "SVLEAK";
	public static final String SERVICE_SERSOR_P = "SVPOWER";
	
	/*Camera Service Name*/
	public static final String SERVICE_CAMERA_ISLIVE = "SVSPZT";
	
	/*Tcp Socket Name*/
	public static final String TCP_SOCKET_KT_WD = "SVWD";
	public static final String TCP_SOCKET_KT_ZT = "SVACZT";
	
	/*Physical Server Name */
	public static final String FWQ_MEM = "SVMEM" ;
	public static final String FWQ_HD = "SVDISK" ;
	public static final String FWQ_CPU = "SVCPU" ;
	/* File Path */
	public static final String APP_ROOT_PATH = "/root/workspace/MQeyeService";
	public static final String WATCH_ROOT_PATH = "/root/workspace/MQeyeService/WatchTask.jar";
	public static final String MQEYE_FILE = APP_ROOT_PATH + "/conf/mqeye.properties"; 
	public static final String MQEYE_DB_FILE = APP_ROOT_PATH + "/conf/dbpool.properties"; 
	public static final String REPAIR_DB_FILE = APP_ROOT_PATH + "/sys/repair_db.sh";
	public static final String ROUTE_INIT_FILE =  APP_ROOT_PATH + "/conf/runroute.sh";
	public static final String REMOVE_PID_FILE = APP_ROOT_PATH + "/conf/removefile.sh";
	public static final String UNLOCK_TTY_FILE = APP_ROOT_PATH + "/conf/unlocktty.sh";
	
	public static final String LCD_PATH = APP_ROOT_PATH + "/lcmap";
	public static final String BIN_PATH = APP_ROOT_PATH + "/bin";
	
	public static final String	ERROR_LOG_FILE = APP_ROOT_PATH + "/logs/error.log";
	public static final String	WRAPP_LOG_FILE = APP_ROOT_PATH + "/logs/wrapper.log"; 
	public static final String	DATAB_LOG_FILE = APP_ROOT_PATH + "/logs/mqeyedb.log";
	public static final String	WATCH_LOG_FILE = APP_ROOT_PATH + "/logs/watch.log";
	
	public static final String	MONITOR_TMP_FILE = APP_ROOT_PATH + "/logs/monitorlog.tmp";
	public static final String	WARN_TMP_FILE = APP_ROOT_PATH + "/logs/warnlog.tmp";
		


}
