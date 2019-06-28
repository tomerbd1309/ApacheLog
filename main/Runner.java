import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Runner {
	public static final int NUM_OF_THREADS = 4;

	public static void main(String[] args){
		//Initializing command line arguments
		String logPath = null;
		String outPutAnalysisFilePath = null;
		if(args.length > 0){
			logPath = args[0];
			if(args.length > 1){
				outPutAnalysisFilePath = args[1];
			}
		}	
		DBMapsHolder dbMapsHolder = DBMapsHolder.getInstance();
		DistributionMapsHolder distributionMapsHolder = DistributionMapsHolder.getInstance();
		ApacheLogAnalyzer apacheLogAnalyzer = new ApacheLogAnalyzer();
		LogFileHandler logFileHandler = new LogFileHandler(logPath);
		int numOfLinesToWait = 0;
		ExecutorService exec = Executors.newFixedThreadPool(NUM_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);
		try{
			logFileHandler.readAndProcessLogFile(numOfLinesToWait, NUM_OF_THREADS, dbMapsHolder, exec, latch);
			apacheLogAnalyzer.analyzeAllMaps(dbMapsHolder, distributionMapsHolder);
			logFileHandler.clearAndWriteResultsOfAnalytics(outPutAnalysisFilePath, distributionMapsHolder);
		} 
		catch (Exception e){
			e.printStackTrace();
		}
		finally{
			exec.shutdown();
			dbMapsHolder.shutDownClient();
		}
	}
}
