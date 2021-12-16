package mqeye.sys.execute;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Observable;

import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.buf.HexUtils;

public class CommandResult  extends Observable implements Runnable{
	
	    private String character = "UTF-8";
	    private InputStream inputStream;
	    private PrintStream printStream;
	    

	    private String currline = "";
	    
	    public CommandResult(InputStream inputStream, PrintStream print) {
	        this.inputStream = inputStream;
	        this.printStream = print;
	    }

	    public void start() {
	        Thread thread = new Thread(this);
	        thread.setDaemon(true);//
	        thread.start();
	    }
	    
	    public String current()
	    {
	    	return currline;
	    }
	    public boolean success(String success)
	    {
	    	return (StringUtils.contains(currline, success));
	    }
	    public boolean fail(String fail)
	    {
	    	return (StringUtils.contains(currline, fail));
	    }
	    
	    public void run() {
	        BufferedReader br = null;
	        try {
	            br = new BufferedReader(new InputStreamReader(inputStream, character));
	           
	            while ((currline = br.readLine()) != null) {
	                if (currline != null) {
	               
	                	this.setChanged();
	                	this.notifyObservers();
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	              
	                inputStream.close();
	                br.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    }

	
}
