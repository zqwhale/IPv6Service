package mqeye.sys.execute;

import java.io.BufferedReader;
import java.io.IOException;

public interface ICommand {
	public void setTimeOut(long timeout);
	public long getTimeOut();
	public String command();
	public int result(BufferedReader in) throws IOException ;
	public int result(CommandResult in,CommandResult err) throws IOException ;
}
