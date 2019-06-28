import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import org.redisson.api.RMap;	

public class ApacheLogAnalyzer {

		public ConcurrentHashMap<String, Double> getDistribution(RMap<String, Integer> accessToServerRMap, ConcurrentHashMap<String, Double> percentageAccessToServerMap){
			try{
				final int totalAccess = accessToServerRMap.values().stream().mapToInt(i -> i).sum();
				accessToServerRMap.entrySet().stream().forEach(entry -> percentageAccessToServerMap.put(entry.getKey(),calcPercentage(entry.getValue(), totalAccess)));
			}
			catch(Exception e){
				e.printStackTrace();
			}
			return percentageAccessToServerMap;
		}
		
		public void analyzeAllMaps(DBMapsHolder dbMapsHolder, DistributionMapsHolder distrinutionMapsHolder){
			distrinutionMapsHolder.setBrowserMapAfterAnalysis(this.getDistribution(dbMapsHolder.getBrowserAccessToServerMap(), distrinutionMapsHolder.getBrowserMapAfterAnalysis()));
			distrinutionMapsHolder.setOperatingSystemMapAfterAnalysis(this.getDistribution(dbMapsHolder.getOperatingSystemAccessToServerMap(), distrinutionMapsHolder.getOperatingSystemMapAfterAnalysis()));
			distrinutionMapsHolder.setCountryMapAfterAnalysis(this.getDistribution(dbMapsHolder.getCountryAccessToServerMap(), distrinutionMapsHolder.getCountryMapAfterAnalysis()));	
		}
	

//************************************************************************************************
	//helpers methods:
	private double calcPercentage(int AccessCount, int totalAccess){
		return(((double)(AccessCount)*100)/(double)(totalAccess));
	}



}
	
