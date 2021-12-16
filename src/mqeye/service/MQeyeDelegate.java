package mqeye.service;


public class MQeyeDelegate {
	private static MQeyeExecutor exe = MQeyeExecutor.getIntance();
	
	/** 
	 * 判断监控服务是否开启，线程池中只要有线程，则是启动状态
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String isRunning(){
		String msg = "";
		if (exe.isRunning())  
				msg = "运行";
		else
				msg = "停止";
		return msg;
	}
	
	/** 
	 * 判断监控服务是否开启，线程池中只要有线程，则是启动状态
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String isRunning(String dcode){
		String msg = "";
		if (exe.isRunning(dcode))  
				msg = "编号:"+ dcode +" 设备的监控服务正在运行";
		else
				msg = "编号:"+ dcode +" 设备的监控服务不存在";
		return msg;
	}
	
	
	/** 
	 * 启动所有Device监控线程
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String start(){
		return exe.start();				//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	/** 
	 * 启动某Device监控线程
	 * @param dcode 传递需要启动的监控对象 DCode
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String start(String dcode){
		return exe.start(dcode);				//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	/** 
	 * 停止所有Device监控线程
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String stop(){
		return exe.stop();			//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	
	/** 
	 * 停止某Device监控线程
	 * @param dcode 传递需要停止的监控对象 DCode
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String stop(String dcode){
		return exe.stop(dcode);				//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	/** 
	 * 重启所有Device监控线程
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String restart(){
		return exe.restart();			//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	/** 
	 * 重启某Device监控线程
	 * @param dcode 传递需要停止的监控对象 DCode
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String restart(String dcode){
		return exe.restart(dcode);				//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	/** 
	 * 刷新某Device的服务列表
	 * @param dcode 传递需要刷新的监控对象 DCode
	 * @return 返回操作结果信息 如果操作成功则返回"" 如果操作失败就返回失败原因
	 */
	public static String refresh(String dcode){
		return exe.refresh(dcode);				//直接返回是否有设备的线程因为参数的配置不正确而无法执行
	}
	
	
}
