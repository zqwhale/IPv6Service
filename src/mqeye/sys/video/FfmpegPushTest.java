package mqeye.sys.video;

import java.io.IOException;
import java.util.List;

import mqeye.sys.execute.CommandResult;
import mqeye.sys.execute.ICommand;

class FfmpegExecutor implements Runnable{
	  private ICommand cmd = null;
	  private int resultFlag = -1;
	  
	  
	  public FfmpegExecutor(ICommand cmd){
		  this.cmd = cmd ;
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
	        	System.out.println(cmd.command());
	            p = r.exec(cmd.command());
	           
	      	 	if (p != null) {
	      	 			CommandResult stdoutUtil = new CommandResult(p.getInputStream(), System.out);
	      	 			CommandResult erroroutUtil = new CommandResult(p.getErrorStream(), System.err);
	      	 			stdoutUtil.start();
	      	 			erroroutUtil.start();

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
	  }
}
public class FfmpegPushTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		CloudVideoDAO dao2 = new CloudVideoDAO();
		CloudVideo cvideo = dao2.getValidMasterCVideo();
		
		CameraBeanDAO dao1 = new CameraBeanDAO();
		CameraBean camera = dao1.getCameraBean(args[0]);
		{
			System.out.println(camera.getCmName());
			System.out.println(cvideo.getCvName() + ":" + cvideo.getCvIPAddr());
			
			CloudCameraHolder holder = new CloudCameraHolder();
			camera = holder.getValidCameraBean(cvideo, camera);
			
			ICommand cmd1 = new FfmpegPushStreamCmd(camera,cvideo);
			cmd1.setTimeOut(5*1000);
			
			FfmpegExecutor exe1 = new FfmpegExecutor(cmd1);
			exe1.start();
			if (exe1.getResultFlag()==1)
				System.out.println("This is a Success!");

		}
		
		
		
		
		
		
		

		
	}

}
