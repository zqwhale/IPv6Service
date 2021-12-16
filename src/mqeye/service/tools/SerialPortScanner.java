package mqeye.service.tools;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;

import mqeye.service.serial.SerialUsb;
import mqeye.service.serial.SerialUsbCfgTool;

/* SCAN all service Serial Port*/
public class SerialPortScanner {
   
	
	static void listPorts()  
    {  
        @SuppressWarnings("unchecked")  
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();  
        while ( portEnum.hasMoreElements() )   
        {  
            CommPortIdentifier portIdentifier = portEnum.nextElement();  
            System.out.println(portIdentifier.getName()  +  " - " +  getPortTypeName(portIdentifier.getPortType()) );  
        }          
    }  
       
	static String getPortTypeName ( int portType )  
    {  
        switch ( portType )  
        {  
            case CommPortIdentifier.PORT_I2C:  
                return "I2C";  
            case CommPortIdentifier.PORT_PARALLEL:  
                return "Parallel";  
            case CommPortIdentifier.PORT_RAW:  
                return "Raw";  
            case CommPortIdentifier.PORT_RS485:  
                return "RS485";  
            case CommPortIdentifier.PORT_SERIAL:  
                return "Serial";  
            default:  
                return "unknown type";  
        }  
    } 
    
   static  HashSet<CommPortIdentifier> getAvailableSerialPorts()//±¾À´static
    {
        HashSet<CommPortIdentifier> h = new HashSet<CommPortIdentifier>();
        @SuppressWarnings("unchecked")
		Enumeration<CommPortIdentifier> thePorts = CommPortIdentifier.getPortIdentifiers();
        while ( thePorts.hasMoreElements() )
        {
            CommPortIdentifier com = ( CommPortIdentifier ) thePorts
                .nextElement();
            if ( com.getPortType()==CommPortIdentifier.PORT_SERIAL )
            {
                
                    try
                    {
                        CommPort thePort = com.open( "CommUtil", 50 );
                        thePort.close();
                        h.add( com );
                    }
                    catch ( PortInUseException e )
                    {
                        System.out.println( "Port, " + com.getName()
                            + ", is in use." );
                    }
                    catch ( Exception e )
                    {
                        System.out.println( "Failed to open port "
                            + com.getName() + e );
                    }
            }
        }
        return h;
    }
  

   public static void main ( String[] args )  
   {    
        for(CommPortIdentifier cpi:getAvailableSerialPorts())
        			System.out.println("Test:"+cpi.getName());
        List<SerialUsb> list = new SerialUsbCfgTool().getAllSerialUsb();
		   for(SerialUsb su:list)
			   System.out.println(su);
        
          
    }
}
