package mqeye.service;

import java.util.HashMap;
import java.util.Map;

public class Reset {
	private static Reset instance ;
	
	private Reset(){}
	public static Reset getInstance(){
		if ( instance == null){
			instance = new Reset();
		}
		return instance ;
	}
	
	private Map<String , Boolean> dflag =  new HashMap<String,Boolean>(); 
	
	public void reset(String dcode , Boolean flag){
		if (dflag.containsKey(dcode)) dflag.remove(dcode);
		dflag.put(dcode, flag);
	}
	
	public Boolean getFlag(String dcode){
		if (dflag.containsKey(dcode)) 
			return dflag.get(dcode);
		else
			return false;
	}
}
