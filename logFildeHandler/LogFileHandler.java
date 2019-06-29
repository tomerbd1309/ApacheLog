import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;


public class LogFileHandler {
	private String filePath = "";
	
	public LogFileHandler(String filePath){
		this.filePath = filePath;
	}
	
	/*
	 * Each line is read, sent to a thread that parse and insert the 
	 * parsed information to the suitable map that counts number of 
	 * accesses to server.
	 */
	public void readAndProcessLogFile(DBMapsHolder dbMapsHolder, ExecutorService exec){
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(this.getFilePath())));
			String logLine = "";
			while((logLine = br.readLine())!= null){
				exec.submit(new ApacheLogThread(logLine, dbMapsHolder));
			}	
			exec.shutdown();
			exec.awaitTermination(5, TimeUnit.MINUTES);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
//************************************************************************************************
	//helper methods:
	
	//getters & setters:
	public String getFilePath(){
		return this.filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	
	
	



	
	
}
