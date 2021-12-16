package mqeye.service.routine;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import org.apache.commons.lang.StringUtils;

import mqeye.sys.execute.CommandExecutor;
import mqeye.sys.execute.CommandResult;
import mqeye.sys.execute.ICommand;
import mqeye.sys.video.CameraBean;
import mqeye.sys.video.CameraBeanDAO;
import mqeye.sys.video.CloudCameraHolder;
import mqeye.sys.video.CloudVideo;
import mqeye.sys.video.CloudVideoDAO;
import mqeye.sys.video.FfmpegCloseStreamCmd;
import mqeye.sys.video.FfmpegPushStreamCmd;

class FfmpegExecutor implements Observer , Runnable{
	  private ICommand cmd = null;
	  private CameraBean camera = null;
	  private int cnt=0;
	  private int resultFlag = 0;
	  public static final String SUCCESS_MARK1 = "frame=";
	  public static final String SUCCESS_MARK2 = "fps=";
	  public static final String SUCCESS_MARK3 = "q=";
	  
	  public FfmpegExecutor(ICommand cmd,CameraBean camera){
		  this.cmd = cmd ;
		  this.camera = camera ;
		  intoDB(resultFlag);
	  }
	  public int getResultFlag()
	  {
		  return resultFlag ;
	  }
	  public void start() {
	        Thread thread = new Thread(this);
	        thread.start();
	  }
	  
	  public void run() {
		  Runtime r = Runtime.getRuntime();   
			Process p = null;
	        try {
	            p = r.exec(cmd.command());
	            System.out.println("New-------------" + cmd.command());
	      	 	if (p != null) {
	      	 			CommandResult stdoutUtil = new CommandResult(p.getInputStream(), System.out);
	      	 			CommandResult erroroutUtil = new CommandResult(p.getErrorStream(), System.err);
	      	 			stdoutUtil.start();
	      	 			erroroutUtil.start();
	      	 			stdoutUtil.addObserver(this);
	      	 			erroroutUtil.addObserver(this);
	      	 			long timeout = cmd.getTimeOut() ;
	    	      	 	if (timeout > 0)  Thread.sleep(timeout);
	    	      	 	
	    	      	 	resultFlag = cmd.result(stdoutUtil,erroroutUtil);
	      	 	}
	      	 	p.waitFor();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        resultFlag = -1;	    
	    	 intoDB(resultFlag);
	  }
	private void intoDB(int resultFlag ){
		CameraBeanDAO dao = new CameraBeanDAO();
		String cmCode = camera.getCmCode();
		dao.isLive(cmCode, resultFlag);
		
	}
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		CommandResult result = (CommandResult) o;
		String currentline = result.current() ;
		//System.out.println(currentline);
		boolean flag1 = StringUtils.contains(currentline, SUCCESS_MARK1);
		boolean flag2 = StringUtils.contains(currentline, SUCCESS_MARK1);
		boolean flag3 = StringUtils.contains(currentline, SUCCESS_MARK1);
		if (flag1 && flag2 && flag3 )
		{	
			resultFlag = 1;
			if (cnt%50==0){
				cnt = 0;
				intoDB(resultFlag);
			}
		}
		cnt++;
		
	}
}
public class VideoManageTask implements Runnable{
	public final static int delay = 30;
	public final static int loop = 30;
	private static VideoManageTask instance = null;
	private Map<String,FfmpegExecutor> exes = new HashMap<String,FfmpegExecutor>();
	private VideoManageTask()
	{
	}
	
	public static VideoManageTask getInstance()
	{
		if (instance==null)
			instance = new VideoManageTask();
		return instance;
	}
	
	public void startAllPush(){
		CameraBeanDAO dao = new CameraBeanDAO();
		List<CameraBean> list = dao.getAllCameraBean();
		for(CameraBean camera:list)
		{
			startPush(camera);
		}
	}
	public String startOnePush(String cmCode)
	{
		String result = "fail";
		CameraBeanDAO dao = new CameraBeanDAO();
		CameraBean camera = dao.getCameraBean(cmCode);
		if (camera!=null){
			int flag = startPush(camera);
			if (flag == 1)
				result = "success";
		}
		
		return result;
	}
	
	public void stopAllPush(){
		CameraBeanDAO dao = new CameraBeanDAO();
		List<CameraBean> list = dao.getAllCameraBean();
		for(CameraBean camera:list)
		{
			stopPush(camera);
		}
	}
	public String stopOnePush(String cmCode)
	{	String result = "fail";
		CameraBeanDAO dao = new CameraBeanDAO();
		CameraBean camera = dao.getCameraBean(cmCode);
		if (camera!=null){
			stopPush(camera);
			result = "success";
		}
		return result;
	}
	
	private void addFfmpeg(String cmCode , FfmpegExecutor exe)
	{
		if (exes.containsKey(cmCode))	exes.remove(cmCode);
		exes.put(cmCode, exe);
	}
	
	private void removeFfmpeg(String cmCode )
	{
		exes.remove(cmCode);
	}
	private int startPush(CameraBean camera)
	{
		int flag = 0;
		CloudCameraHolder holder = new CloudCameraHolder();
		
		CloudVideoDAO dao2 = new CloudVideoDAO();
		CloudVideo cvideo = dao2.getValidMasterCVideo();
		
		if ( camera!=null && holder.cmPortValid(camera)==1 )
		{
			camera = holder.getValidCameraBean(cvideo, camera);
			ICommand cmd = new FfmpegPushStreamCmd(camera,cvideo);
			cmd.setTimeOut(5*1000);
			
			FfmpegExecutor exe1 = new FfmpegExecutor(cmd,camera);
		
			exe1.start();
			addFfmpeg(camera.getCmCode(),exe1);
			
			int cnt = 0;
			
			do 
			{
				flag = exe1.getResultFlag();
				System.out.println("Video Push Task is starting......");
				cnt++;
				if (cnt>=15) break;
				try {
					Thread.sleep(5*1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}while(flag!=1);
			
			if (cnt<15)
			{
				System.out.println("Video Push Task is start OK.");
				flag = 1;
			}
			else
				System.out.println("Video Push Task is start Fail.");
		}else
			System.out.println("Camera Connect error!");
		
		return flag ;
	}
	
	private void stopPush(CameraBean camera){
			CloudCameraHolder holder = new CloudCameraHolder();
			CloudVideoDAO dao = new CloudVideoDAO();
			CloudVideo cvideo = dao.getValidMasterCVideo();
			
			camera = holder.getValidCameraBean(cvideo, camera);
			ICommand cmd = new FfmpegCloseStreamCmd(camera,cvideo);
			CommandExecutor.execute(cmd);
			removeFfmpeg(camera.getCmCode());
			System.out.println("Video Push Task is stop OK.");
	}
	
	
	public int isLive(String cmCode)
	{
		FfmpegExecutor exe = exes.get(cmCode);
		if (exe!=null)
			return exe.getResultFlag();
		else
			return -3;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		for(Map.Entry<String,FfmpegExecutor> entry:exes.entrySet())
		{
			FfmpegExecutor exe = entry.getValue();
			if (exe.getResultFlag()!=1)
				startOnePush(entry.getKey());
		}
	}

}
