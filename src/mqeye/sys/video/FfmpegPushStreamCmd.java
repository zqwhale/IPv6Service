package mqeye.sys.video;

import java.io.IOException;

import mqeye.sys.execute.AbstractCommand;
import mqeye.sys.execute.CommandResult;

public class FfmpegPushStreamCmd extends AbstractCommand {

	//frame=   46 fps=0.0 q=-1.0 size=      89kB time=00:00:01.56 bitrate= 464.8kbits/s speed=2.97x    
	public static final String APP_ROOT_PATH = "/usr/local/ffmpeg/bin/";
	public static final String ffmpeg = APP_ROOT_PATH + "ffmpeg";
	
	public static final String SUCCESS_MARK1 = "frame=";
	public static final String SUCCESS_MARK2 = "fps=";
	public static final String SUCCESS_MARK3 = "q=";

	
	private CameraBean camera ;
	private CloudVideo cvideo ;
	
	public FfmpegPushStreamCmd(CameraBean camera , CloudVideo cvideo)
	{
		this.camera = camera;
		this.cvideo = cvideo;
	}
	
	@Override
	public String command() {
		// TODO Auto-generated method stub
		String cmd = null;
		if (this.camera == null)
				cmd = null;
		else if(this.cvideo == null)
		{
			cmd = ffmpeg ;
			String params1 = " " + camera.getInputParam() + " ";
			String inputUrl = camera.getInputUrl();
			
			String params2 = " " + camera.getOutputLocalParam() + " ";
			String outputLocalUrl = camera.getOutputLocalUrl();
			cmd = cmd + params1 + inputUrl + params2 + outputLocalUrl ;
		}
		else
		{
			cmd = ffmpeg ;
			String params1 = " " + camera.getInputParam() + " ";
			String inputUrl = camera.getInputUrl();
			
			String params3 = " " + camera.getOutputRemoteParam() + " ";
			String outputRemoteUrl = camera.getOutputRemoteUrl();
			cmd = cmd + params1 + inputUrl + params3 + outputRemoteUrl ;
		}
		return cmd;
		
	}

	@Override
	public int result(CommandResult in,CommandResult err) throws IOException{
		boolean flag1 = err.success(SUCCESS_MARK1);
		boolean flag2 = err.success(SUCCESS_MARK2);
		boolean flag3 = err.success(SUCCESS_MARK3);
		int flag = 0 ;
		
		if (flag1 && flag2 && flag3) 		
									flag = 1;
		
		return flag;
	}
	


}
