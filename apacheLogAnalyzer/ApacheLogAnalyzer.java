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
		
		public void analyzeAllMaps(DBMapsHolder dbMapsHolder, DistributionMapsHolder distributionMapsHolder, String outPutAnalysisFilePath){
			AnalysisResultWriter analysisResultWriter = new AnalysisResultWriter();
			distributionMapsHolder.setBrowserMapAfterAnalysis(this.getDistribution(dbMapsHolder.getBrowserAccessToServerMap(), distributionMapsHolder.getBrowserMapAfterAnalysis()));
			distributionMapsHolder.setOperatingSystemMapAfterAnalysis(this.getDistribution(dbMapsHolder.getOperatingSystemAccessToServerMap(), distributionMapsHolder.getOperatingSystemMapAfterAnalysis()));
			distributionMapsHolder.setCountryMapAfterAnalysis(this.getDistribution(dbMapsHolder.getCountryAccessToServerMap(), distributionMapsHolder.getCountryMapAfterAnalysis()));
			analysisResultWriter.clearAndWriteResultsOfAnalytics(outPutAnalysisFilePath, distributionMapsHolder);
			
		}
	

//************************************************************************************************
	//helpers methods:
	private double calcPercentage(int AccessCount, int totalAccess){
		return(((double)(AccessCount)*100)/(double)(totalAccess));
	}



}
	
