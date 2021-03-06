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
		LogFileHandler logFileHandler = new LogFileHandler(logPath);
		ApacheLogAnalyzer apacheLogAnalyzer = new ApacheLogAnalyzer();
		ExecutorService exec = Executors.newFixedThreadPool(NUM_OF_THREADS);
		try{
			logFileHandler.readAndProcessLogFile(dbMapsHolder, exec);
			apacheLogAnalyzer.analyzeAllMaps(dbMapsHolder, distributionMapsHolder, outPutAnalysisFilePath);
		} 
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			exec.shutdown();
			dbMapsHolder.shutDownClient();
		}
	}
}
