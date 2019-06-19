import java.io.BufferedReader;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;

public class LogFile {
	private String filePath = "";
	
	/*
	 *Each line is read, sent to a thread that parse and insert the parsed information to the suitable map that counts number of accesses to server.
	 */
	public void readAndProcessLogFile(String logLine, int numOfLinesToWait, BufferedReader br, GateWay gateWay, ExecutorService exec, CountDownLatch latch){
		
		try {
			while((logLine = br.readLine())!= null){

				//creates and activates threads for the thread fixed pool
				exec.submit(new ApacheLogThread(logLine, gateWay, new LineInfo(), new ApacheLogParser(), latch));
				
				numOfLinesToWait++;
				if(numOfLinesToWait == GateWay.NUM_OF_THREADS){
					numOfLinesToWait = 0;
					//wait until all threads are available to the next line process. can optimize by sending every available thread to proccess the next line and not wait until all the four are done
					latch.await();
					if(latch.getCount() == 0){
						latch = new CountDownLatch(GateWay.NUM_OF_THREADS);
					}
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (InterruptedException e){
			e.printStackTrace();
		}
	}
	public LogFile(String filePath){
		this.filePath = filePath;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
}
