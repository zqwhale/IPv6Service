package mqeye.service.serial;


// Lock for TCPSocketTool 
public class TaskLock {
	 	public String id = null;
		int lock = 0;
		
		public TaskLock(String id)
		{	this.id = id; 	}
		
		public synchronized boolean equals(TaskLock lock)
		{	
			return this.id.equals(lock.id) ;
		}
		
		public synchronized void lock(){
			lock = 1;
		}
		public synchronized void unlock(){
			lock = 0;
		}
		public synchronized boolean islock(){
			return ( lock==1 );
		}
		

	
}
