package mqeye.service.serial;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Observable;
import java.util.TooManyListenersException;

import mqeye.service.tools.DebugTool;



public class SerialPortReader  extends Observable implements SerialPortEventListener{
	 
    static CommPortIdentifier portId;
    int delayRead = 1000;
    int numBytes; // buffer中的实际数据字节数
    private static byte[] readBuffer = new byte[1024]; // 4k的buffer空间,缓存串口读入的数据
    int err_cnt = 0;
     
    public InputStream inputStream;
    public OutputStream outputStream;
    static SerialPort serialPort;
    Map<String,String> serialParams;
        
    //端口是否打开了
   
    boolean isDamage = false;
    boolean isOpen = false;
    String sPort ;
    String currentCmd = null;
    
    public boolean isDamage(){
    	return isDamage ;
    }
    public boolean isOpen(){
    	return isOpen;
    }
   
    /**
     * 初始化端口操作的参数.
     * @throws SerialPortException 
     * 
     * @see
     */
    public SerialPortReader(String sport)
    {
    	isOpen = false;
    	sPort = sport ;
    }
    
    public void close() 
    { 
        if (isOpen)
  			{
            try{
            	serialPort.notifyOnDataAvailable(false);
            	serialPort.removeEventListener();
               inputStream.close();
               serialPort.close();
               isOpen = false;
               
            } catch (IOException ex)
            {
            								//"关闭串口失败";
            			DebugTool.printErr("Close Serial Port fail!");
            			DebugTool.printExc(ex);
            }
        }
    }
    
    
    
    public boolean open(Map params) 
    { 
    	serialParams = params;
    	if(isOpen){
    		close();
    	}
        try
        {
            // 参数初始化
            int timeout = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_TIMEOUT ).toString() );
            int rate = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_RATE )
                .toString() );
            int dataBits = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_DATABITS ).toString() );
            int stopBits = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_STOPBITS ).toString() );
            int parity = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_PARITY ).toString() );
            delayRead = Integer.parseInt( serialParams.get( SerialPortParam.PARAMS_DELAY ).toString() );
            
            String port = serialParams.get( SerialPortParam.PARAMS_PORT ).toString();
            // 打开端口
            portId = CommPortIdentifier.getPortIdentifier( port );
            	
            serialPort = ( SerialPort ) portId.open( "SerialReader", timeout );
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            
            serialPort.addEventListener( this );
            serialPort.notifyOnDataAvailable( true );
            serialPort.setSerialPortParams( rate, dataBits, stopBits, parity );
            isOpen = true;
            isDamage = false;
        } 
       catch(UnsatisfiedLinkError e){
						DebugTool.printErr("RXTXCommDriver库文件有错");
						isDamage = true;
		}
        catch ( PortInUseException e )
        {
        	DebugTool.printErr("Port:"+serialParams.get( SerialPortParam.PARAMS_PORT ).toString()+"Already be Ocuppy!");
			DebugTool.printExc(e);
			isDamage = true;
        }
        catch ( TooManyListenersException e )
        {
           //"端口"+serialParams.get( PARAMS_PORT ).toString()+"监听者过多";
        	DebugTool.printErr("Port:"+serialParams.get( SerialPortParam.PARAMS_PORT ).toString()+"Monitor too many!");
						DebugTool.printExc(e);
						isDamage = true;
        }
        catch ( UnsupportedCommOperationException e )
        {
           //"端口操作命令不支持";
        	DebugTool.printErr("Port Operate Command Not Support!");
        	DebugTool.printExc(e);
        	isDamage = true;
        }
        catch ( NoSuchPortException e )
        {
        	DebugTool.printErr("Port:"+serialParams.get( SerialPortParam.PARAMS_PORT ).toString()+"not exsit!");
        	DebugTool.printExc(e);
        	
        	isDamage = true;
        }
        catch ( IOException e )
        {
        	DebugTool.printErr("Open Port:"+serialParams.get( SerialPortParam.PARAMS_PORT ).toString()+"fail!");
        	DebugTool.printExc(e);
        	isDamage = true;
        }
       // serialParams.clear();
        return isOpen;
    }

	
    public void serialEvent( SerialPortEvent event )
    {
        try
        {
            Thread.sleep( delayRead );
        }
        catch ( InterruptedException e )
        {
            e.printStackTrace();
        }
        switch ( event.getEventType() )
        {
            case SerialPortEvent.BI: // 10
            case SerialPortEvent.OE: // 7
            case SerialPortEvent.FE: // 9
            case SerialPortEvent.PE: // 8
            case SerialPortEvent.CD: // 6
            case SerialPortEvent.CTS: // 3
            case SerialPortEvent.DSR: // 4
            case SerialPortEvent.RI: // 5
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 2
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 1
              try
                {
                    // 多次读取,将所有数据读入
                     while (inputStream.available() > 0) {
                     numBytes = inputStream.read(readBuffer);
                     	}

                     changeMessage( readBuffer, numBytes );
                }
                catch ( IOException e )
                {
                	DebugTool.printErr("Data Transfer fail!"+(err_cnt++)+"times");
                	DebugTool.printExc(e);
                	isDamage = true;
                }
                break;
        }
    }

   public void sendMessage(byte[] hexcmd) {
	   if (hexcmd != null && hexcmd.length >0 && !isDamage) {
		   try {
			   currentCmd = new String(hexcmd);
				outputStream.write(hexcmd);
				outputStream.flush();
		   }catch (NumberFormatException e) {
				e.printStackTrace();
			}catch( IOException e)
			{
				e.printStackTrace();
			}
	   }
    }
    public void sendMessage(String cmd) {
		String temp = "";
		// 每两位字符转化为16进制数字发送，忽略空格
		for (int i = 0; i < cmd.length(); i++) {
			if (cmd.charAt(i) == ' ') 	continue;
			temp = temp + cmd.charAt(i);
			if ((temp.length() == 2) || (i == cmd.length() - 1)) {
				try {
					outputStream.write(Integer.parseInt(temp, 16));
					outputStream.flush();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}catch( IOException e)
				{
					e.printStackTrace();
				}
				temp = "";
			}
		}
	}
	
    public void changeMessage( byte[] message, int length )
    {
    	try{
    					if (length>0){
    									setChanged();
    									byte[] temp = new byte[length];
    									System.arraycopy( message, 0, temp, 0, length ); 
    									notifyObservers( temp );
    								} 
    								
        }catch(NegativeArraySizeException e){
									DebugTool.printErr("Serial Port Data Format Error !");
									DebugTool.printExc(e);
									isDamage = true;
        			}
    			
    }
    
	

}
