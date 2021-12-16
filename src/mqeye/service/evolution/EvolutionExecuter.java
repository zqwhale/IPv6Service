package mqeye.service.evolution;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mqeye.data.vo.Device;
import mqeye.data.vo.DeviceDAO;

public class EvolutionExecuter {
	ProgressObserver o = new ProgressObserver();
	List<Device> devices = new ArrayList<Device>() ;
	List<Thread> threads = new ArrayList<Thread>();
	Map<String , Runnable> detects = new HashMap<String , Runnable>();
	public EvolutionExecuter(String[] tpCodes){
		DeviceDAO dao = new DeviceDAO();
		for(String code:tpCodes)
		{	List<Device> list = dao.getAllValidByTPCode(code);
			if (list!=null && list.size()>0)
			{	devices.addAll(list); }
		}

		for(Device d:devices){
			String dcode = d.getDCode() ;
			PingDetect detect = new PingDetect( d );
			detect.addObserver(o);
			detects.put(dcode, detect);
			o.total++;
			o.pecents.put(dcode, (Float)(0f));
		}	
		
	}
	
	public void execute(){
		for(Device d:devices){
			PingDetect detect = (PingDetect) detects.get(d.getDCode());
			Thread t = new Thread(detect,d.getDCode());
			t.start();
			threads.add(t);
		}
	}
	
	public float getProgress(){
		float progress = 0f;
		for(Device d:devices){
			Float pecent = o.pecents.get(d.getDCode());
			progress = progress + pecent.floatValue();
		}
		if (o.total>0)		
			progress = progress/o.total;
		else
			progress = 1f;

		return progress ;
	}
	
	public boolean isOver(){
		return (o.cnt == o.total);
	}
	
	public void viewThread()
	{
		for(Thread t:threads)
		{
			if (t.isAlive())
			{
				System.out.println(t.getName());
			}
		}
	}
	public List<Evolution> getEvo(){
		return o.evos;
	}
	
	public static void main(String[] args) {
		String[] tpcodes = {"JHJ001"};
		
		
		EvolutionExecuter exe = new EvolutionExecuter(tpcodes);
		exe.execute();
		long begin = System.currentTimeMillis();
		while(!exe.isOver())
		{
		
			System.out.println("----------------" + exe.getProgress());
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		long end = System.currentTimeMillis();
		System.out.println(end -begin);
		exe.viewThread();
		
		EvolutionDAO dao = new EvolutionDAO();
		dao.recordToDB(exe.getEvo());
		
	}
	
}

