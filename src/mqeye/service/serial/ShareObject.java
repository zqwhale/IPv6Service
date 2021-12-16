package mqeye.service.serial;

import java.util.HashMap;
import java.util.Map;


public class ShareObject {
	private static ShareObject instance = null ;
	public static Map<String,TaskLock> locks = new HashMap<String,TaskLock>();
	
	
	public static Map<String,SerialPortReader> serials = new HashMap<String,SerialPortReader>();
	
	private ShareObject(){
		
	} 
	public static ShareObject getInstance()
	{
		if (instance == null)
			instance = new ShareObject();
		return instance;
	}
	
	@Deprecated
	public void setReader(String sport , SerialPortReader reader){
		SerialPortReader sr = serials.get(sport);
		if (sr!=null) sr.close();
		serials.remove(sport);
		serials.put(sport, reader);
	}
	
	public SerialPortReader initReader(String sport){
		serials.remove(sport);
		SerialPortReader reader = new SerialPortReader(sport);
		serials.put(sport, reader);
		return reader;
	}
	public SerialPortReader getReader(String sport)
	{
		if (serials.containsKey(sport))
				return serials.get(sport);
		else
		{
			SerialPortReader reader = new SerialPortReader(sport);
			serials.put(sport, reader);
			return reader;
		}
	}
	
	@Deprecated
	public void setLock(String ipPort , TaskLock lock ){
		locks.remove(ipPort);
		locks.put(ipPort, lock);
	}
	
	public TaskLock getLock(String ipPort ){
		if (locks.containsKey(ipPort))
			return locks.get(ipPort);
		else
		{
			TaskLock lock = new TaskLock(ipPort);
			locks.put(ipPort, lock);
			return lock;
		}
	}
	
}
