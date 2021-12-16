package mqeye.sys.video;

import java.util.List;

import mqeye.sys.execute.CommandExecutor;
import mqeye.sys.execute.ICommand;

public class FfmpegCloseTest {
	public static void main(String[] args) {
		CameraBeanDAO dao1 = new CameraBeanDAO();
		List<CameraBean> list = dao1.getAllCameraBean();
		for(CameraBean camera:list)
		{
			CloudVideoDAO dao2 = new CloudVideoDAO();
			CloudVideo cvideo = dao2.getValidMasterCVideo();
			CloudCameraHolder holder = new CloudCameraHolder();
			camera = holder.getValidCameraBean(cvideo, camera);
			
			ICommand cmd2 = new FfmpegCloseStreamCmd(camera,cvideo);
			CommandExecutor.execute(cmd2);
			
		}
	}

}
