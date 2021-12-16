/**
 * 
 */
package mqeye.service.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.lang.StringUtils;

import mqeye.service.Constant;

/**
 * @author zqing
 *		
 */
public class DebugTool {
				
				private  static PrintStream errOut = System.err;
				private  static String charSet = "GB2312";
				
				private  static boolean debug = false ;
				private  static boolean exception = false ;
				private  static boolean error = true ;
				
				private  static boolean console = true;
				private  static boolean curr = true;
				
				public static void init()
				{
					debug = StringUtils.equals(BaseCommonFunc.getProperty("debug"),"yes");
					exception = StringUtils.equals(BaseCommonFunc.getProperty("exception"),"yes");
					error = StringUtils.equals(BaseCommonFunc.getProperty("error"),"yes");
					console = StringUtils.equals(BaseCommonFunc.getProperty("console"),"yes");
					curr = StringUtils.equals(BaseCommonFunc.getProperty("curr"),"yes");
					charSet = BaseCommonFunc.getProperty("charSet");
					System.out.println("Current Code :@@@@@@@@@@@" + charSet);
				}
				
				public static void setErrLogFile(boolean flag){
								if (flag)
											try {
															errOut = new PrintStream(new FileOutputStream(Constant.ERROR_LOG_FILE,true));
											} catch (FileNotFoundException e) {
															// TODO Auto-generated catch block
															errOut = System.err;	
															System.err.print("Error Log Error!");
															e.printStackTrace(); 
											} 
								else
													errOut = System.err;	
								
								System.setErr(errOut);
				}
				
				public static void showCurr(String msg){
					if (curr) System.out.println("CURR----->" + msg);
				}
				public static void showConsole(String line){
					
					if (console)
						try {
							System.out.println("CONSOLE----->" + new String(line.getBytes(charSet)));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							System.out.println("CONSOLE----->" + line);
							//e.printStackTrace();
						}
				}
				
				public static void printMsg(String msg){
						if ( debug )
							try {
								System.out.println("DEBUG:" + new String(msg.getBytes(charSet)));
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								System.out.println("DEBUG:" + msg);
								//e.printStackTrace();
							}
						
						
				}
				public static void printErr(String err){
					if ( error )
						try {
							System.err.println("ERROR:" + new String(err.getBytes(charSet)));
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							System.err.println("ERROR:" + err );
							//e.printStackTrace();
						}
				}
				public static void printExc(Exception exc){
						exc.printStackTrace(errOut);
												
					
				}
				
}
