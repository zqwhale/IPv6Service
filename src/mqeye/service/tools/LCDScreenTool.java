package mqeye.service.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;

public class LCDScreenTool {
	private final static String cmd = Constant.LCD_PATH + "/LCMPrint ";
	private final static String ioAdd = "378 ";
	
	public static void lcmPrint(String msg){
		
		
		
		if (BaseCommonFunc.isPropertyValid("lcdSet", "on"))
		{
						System.out.println(cmd + ioAdd + msg);
						Runtime r = Runtime.getRuntime();   
						BufferedReader reader = null;
						BufferedReader error = null;
						try { 
						   	Process p = r.exec(cmd + ioAdd + msg);
						   	if (p!=null){
						   	error =  new BufferedReader(new InputStreamReader(p.getErrorStream()));
								String line = null;   
								while ((line = error.readLine()) != null) {
				    	   			DebugTool.printErr(line);
				       			}
								reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
								while ((line = reader.readLine()) != null) {   
											DebugTool.showConsole(line);
								}
								p.waitFor();
						   	}
						} catch (IOException ex) {   
							DebugTool.printErr("LCD Screen Error!");
							DebugTool.printExc(ex);
						} catch (InterruptedException ex) {
							// TODO Auto-generated catch block
							DebugTool.printErr("Wait For Error!");
							DebugTool.printExc(ex);
						}
						finally {   
						   try {   
							   error.close();
							   reader.close();   
						   } catch (IOException e) {
							   DebugTool.printErr("LCD Screen Runtime InputStream Error!");
							   DebugTool.printExc(e); 	}  
						   catch(NullPointerException ex){
									DebugTool.printErr("LCD Screen drive File not find Error!");
									DebugTool.printExc(ex);
								}
						}   
			}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
			if (args!=null && args.length > 0)
				LCDScreenTool.lcmPrint(args[0]);
			else{
				int usage = 20; 
				String msg = "Status:Running CPU:" +usage +"%";
				LCDScreenTool.lcmPrint(msg);
			}
	}

}
