import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class Gateway {
	public final static String DISTRIBUTION = "Distribution";
	public static final int NUM_OF_THREADS = 4;
	
	//local maps for data after statistics analyzing 
	public static ConcurrentHashMap<String, Double> browserMapAfterStatistics = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Double> operatingSystemMapAfterStatistics = new ConcurrentHashMap<>();
	public static ConcurrentHashMap<String, Double> countryMapAfterStatistics = new ConcurrentHashMap<>();

	private RMap<String, Integer> browserAccessToServerMap;
	private RMap<String, Integer> operatingSystemAccessToServerMap;
	private RMap<String, Integer> countryAccessToServerMap;
	
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
		
		//Creating a Redis client
		Config config = new Config();
		config.useSingleServer().setAddress("127.0.0.1:6379");
		RedissonClient client = Redisson.create(config);
		
		Gateway gateway = new Gateway();
		//initializing data base maps
		gateway.setBrowserAccessToServerMap(client.getMap("browserAccessToServerMap"));
		gateway.getBrowserAccessToServerMap().clear();
		gateway.setOperatingSystemAccessToServerMap(client.getMap("operatingSystemAccessToServerMap"));
		gateway.getOperatingSystemAccessToServerMap().clear();
		gateway.setCountryAccessToServerMap(client.getMap("countrySystemAccessToServerMap"));
		gateway.getCountryAccessToServerMap().clear();
		
		LogFile logFile = new LogFile(logPath);
		int numOfLinesToWait = 0;
		Statistics statistics = new Statistics();	//operates different statistics calculation on the access to server data
		ExecutorService exec = Executors.newFixedThreadPool(NUM_OF_THREADS);
		CountDownLatch latch = new CountDownLatch(NUM_OF_THREADS);
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new FileReader(new File(logFile.getFilePath())));
			String logLine = "";
			logFile.readAndProcessLogFile(logLine, numOfLinesToWait, br, gateway, exec, latch);
			br.close();
			
			analyzeAllMaps(gateway,statistics);
			
			clearAndWriteResultsOfAnalytics(outPutAnalysisFilePath, statistics);
		} 
		catch (FileNotFoundException e){
			e.printStackTrace();
		} 
		catch (IOException e){
			e.printStackTrace();
		}
		finally{
			try{
				br.close();
			} 
			catch (IOException e){
				e.printStackTrace();
			}
			exec.shutdown();
			client.shutdown();	
		}
	}

	
	//helpers methods:
	
	//getters & setters
	public RMap<String, Integer> getBrowserAccessToServerMap() {
		return browserAccessToServerMap;
	}
	public void setBrowserAccessToServerMap(RMap<String, Integer> browserAccessToServerMap) {
		this.browserAccessToServerMap = browserAccessToServerMap;
	}
	public RMap<String, Integer> getOperatingSystemAccessToServerMap() {
		return operatingSystemAccessToServerMap;
	}
	public void setOperatingSystemAccessToServerMap(RMap<String, Integer> operatingSystemAccessToServerMap) {
		this.operatingSystemAccessToServerMap = operatingSystemAccessToServerMap;
	}
	public RMap<String, Integer> getCountryAccessToServerMap() {
		return countryAccessToServerMap;
	}
	public void setCountryAccessToServerMap(RMap<String, Integer> countryAccessToServerMap) {
		this.countryAccessToServerMap = countryAccessToServerMap;
	}
	
	//other helpers:
	
	private static void clearAndWriteResultsOfAnalytics(String outPutAnalysisFilePath, Statistics statistics){
		statistics.clearFile("Distribution", "txt", outPutAnalysisFilePath);
		statistics.writeToFile("Distribution", "txt", "Browsers Distribution", browserMapAfterStatistics, outPutAnalysisFilePath);
		statistics.writeToFile("Distribution", "txt", "Operating Systems Distribution", operatingSystemMapAfterStatistics, outPutAnalysisFilePath);
		statistics.writeToFile("Distribution", "txt", "Country Distribution", countryMapAfterStatistics, outPutAnalysisFilePath);	
	}

	private static void analyzeAllMaps(Gateway gateway, Statistics statistics){
		browserMapAfterStatistics = statistics.analyzeMap(gateway.getBrowserAccessToServerMap(), browserMapAfterStatistics, DISTRIBUTION);
		operatingSystemMapAfterStatistics = statistics.analyzeMap(gateway.getOperatingSystemAccessToServerMap(), operatingSystemMapAfterStatistics, DISTRIBUTION);
		countryMapAfterStatistics = statistics.analyzeMap(gateway.getCountryAccessToServerMap(), countryMapAfterStatistics, DISTRIBUTION);
		
	}

	
}
