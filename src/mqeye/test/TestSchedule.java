package mqeye.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestSchedule {
	static long count = 0;
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws InterruptedException,ExecutionException
    {
			
			 List<ScheduledFuture> futures = new ArrayList<ScheduledFuture>();
			 ScheduledFuture f ;
			ScheduledExecutorService service=Executors.newScheduledThreadPool(6);
			for(int i=0;i<5;i++){
				Runnable task_i=new Runnable()
				{
					public void run()
					{
						new TestDB().insertData();
						count++;
						System.out.println( count + ":" +new Date() );
					}
				};
				f = service.scheduleAtFixedRate(task_i,i,10,TimeUnit.SECONDS);
				futures.add(f);
            }
			
			Runnable task_d = new Runnable()
			{
				public void run()
				{
					new TestDB().deleteData();
					count++;
					System.out.println( count + ":" +new Date() );
				}
			};
			f = service.scheduleAtFixedRate(task_d,6,10,TimeUnit.SECONDS);
    }
}
