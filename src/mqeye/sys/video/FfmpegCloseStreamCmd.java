package mqeye.sys.video;

import java.io.BufferedReader;
import java.io.IOException;

import mqeye.sys.execute.AbstractCommand;

public class FfmpegCloseStreamCmd extends AbstractCommand {
	public static final String APP_ROOT_PATH = "/root/workspace/MQeyeService/sys/";
	public static final String video_kill = APP_ROOT_PATH + "video_kill.sh ";
	
	private CameraBean camera ;
	private CloudVideo cvideo ;
	
	public FfmpegCloseStreamCmd(CameraBean camera , CloudVideo cvideo)
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
			cmd = video_kill;
			String inputUrl = camera.getInputUrl();
			String outputLocalUrl = camera.getOutputLocalUrl();
			cmd = cmd + inputUrl + " " + outputLocalUrl ;
			
		}
		else
		{
			cmd = video_kill;
			String inputUrl = camera.getInputUrl();
			String outputRemoteUrl = camera.getOutputRemoteUrl();
			cmd = cmd + inputUrl + " " + outputRemoteUrl ;
			
		
		}
		return cmd;
	}
	
	
	@Override
	public int result(BufferedReader in) throws IOException {
		
		int flag = 0;
		String line = "";
        while ((line = in.readLine()) != null) {   
        		System.out.println(line);
        			flag = 1;
        }
        
		return flag;
	}

}
