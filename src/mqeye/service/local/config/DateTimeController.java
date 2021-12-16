package mqeye.service.local.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeController {
	public final static String SETDATE = "/bin/date -s ";
	public static Date readDate(){
			return new Date();
	}
	public void writeDate(Date d){
			Runtime r = Runtime.getRuntime();
			try {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String command = SETDATE+ "'" +f.format(d)+"'";
				String cmd[] = new String[]{"/bin/sh","-c", command ,"clock -w","exit"};
				Process p = r.exec(cmd);
				System.out.println("date -s '" +f.format(d)+"'"  );
				
				if (p!=null){
					BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line = null;
					while((line = in.readLine())!=null){
						System.out.println(line);
					}
					in.close();
					p.destroy();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
}


	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DateTimeController cr = new DateTimeController();
		Calendar c = Calendar.getInstance();
		c.add(Calendar.YEAR, -1);
			cr.writeDate(c.getTime());
			System.out.println(readDate().toString());
	}

}
