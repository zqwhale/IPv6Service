package mqeye.sys.execute;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class AbstractCommand implements ICommand{
	public final static String EMPTY = "#EMPTY#" ; 
	
	private long timeout = -1;
	public void setTimeOut(long timeout){
		this.timeout = timeout ;
	}
	public long getTimeOut(){
		return timeout;
	}
	
	@Override
	public int result(BufferedReader in) throws IOException {
		return 0;
	}
	@Override
	public int result(CommandResult in,CommandResult err) throws IOException {
		return 0;
	}
}
