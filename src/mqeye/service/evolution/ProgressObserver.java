package mqeye.service.evolution;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import mqeye.data.vo.Device;
import mqeye.service.Constant;
import mqeye.service.detect.PingResult;
import mqeye.service.tools.BaseCommonFunc;

public class ProgressObserver implements Observer
{
	List<Evolution> evos = new ArrayList<Evolution>();
	Map<String ,Float> pecents = new HashMap<String ,Float>();
	
	private String today = BaseCommonFunc.getStrFromDateTime(new Date());
	protected int total =0;
	protected int cnt = 0;
	protected float pecent = 0;
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		PingDetect detect = (PingDetect)o;
		if(detect.isFinish==1)
		{
			Device d = detect.getDevice();
			PingResult r = detect.getResult();
			Evolution evo = new Evolution();
			evo.setEvoDateTime(today);
			evo.setDcode(d.getDCode());
			evo.setMethod(Constant.PING);
			evo.setPingFlag(r.getFlag());
			evo.setPingStatus(r.getStatus());
			evo.setPingTimeConsum(r.getTimeConsum());
			evo.setPingLosePacket(r.getLosePacket());
			evo.setPingLosePacketRate(r.getLosePacketRate());
			evos.add(evo);
			pecents.put(d.getDCode(), (Float)(1.0f));
			cnt++ ;
		}else{
			float f = (float) (detect.packetNum*1.00 / detect.totalNum );
			Device d = detect.getDevice();
			pecents.put(d.getDCode(), (Float)f);
		}
		
	}
}
