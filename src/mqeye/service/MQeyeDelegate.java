package mqeye.service;


public class MQeyeDelegate {
	private static MQeyeExecutor exe = MQeyeExecutor.getIntance();
	
	/** 
	 * �жϼ�ط����Ƿ������̳߳���ֻҪ���̣߳���������״̬
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String isRunning(){
		String msg = "";
		if (exe.isRunning())  
				msg = "����";
		else
				msg = "ֹͣ";
		return msg;
	}
	
	/** 
	 * �жϼ�ط����Ƿ������̳߳���ֻҪ���̣߳���������״̬
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String isRunning(String dcode){
		String msg = "";
		if (exe.isRunning(dcode))  
				msg = "���:"+ dcode +" �豸�ļ�ط�����������";
		else
				msg = "���:"+ dcode +" �豸�ļ�ط��񲻴���";
		return msg;
	}
	
	
	/** 
	 * ��������Device����߳�
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String start(){
		return exe.start();				//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	/** 
	 * ����ĳDevice����߳�
	 * @param dcode ������Ҫ�����ļ�ض��� DCode
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String start(String dcode){
		return exe.start(dcode);				//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	/** 
	 * ֹͣ����Device����߳�
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String stop(){
		return exe.stop();			//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	
	/** 
	 * ֹͣĳDevice����߳�
	 * @param dcode ������Ҫֹͣ�ļ�ض��� DCode
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String stop(String dcode){
		return exe.stop(dcode);				//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	/** 
	 * ��������Device����߳�
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String restart(){
		return exe.restart();			//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	/** 
	 * ����ĳDevice����߳�
	 * @param dcode ������Ҫֹͣ�ļ�ض��� DCode
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String restart(String dcode){
		return exe.restart(dcode);				//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	/** 
	 * ˢ��ĳDevice�ķ����б�
	 * @param dcode ������Ҫˢ�µļ�ض��� DCode
	 * @return ���ز��������Ϣ ��������ɹ��򷵻�"" �������ʧ�ܾͷ���ʧ��ԭ��
	 */
	public static String refresh(String dcode){
		return exe.refresh(dcode);				//ֱ�ӷ����Ƿ����豸���߳���Ϊ���������ò���ȷ���޷�ִ��
	}
	
	
}
