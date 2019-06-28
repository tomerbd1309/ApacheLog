import java.util.concurrent.ConcurrentHashMap;

public class DistributionMapsHolder {
	
	private static DistributionMapsHolder distributionMapsHolder = null;
	private ConcurrentHashMap<String, Double> browserMapAfterAnalysis = null;
	private ConcurrentHashMap<String, Double> operatingSystemMapAfterAnalysis = null;
	private ConcurrentHashMap<String, Double> countryMapAfterAnalysis = null;
	
	private DistributionMapsHolder(){
		this.initiateDistributionMaps();
	}
	
	public static DistributionMapsHolder getInstance(){
		if(distributionMapsHolder == null){
			distributionMapsHolder = new DistributionMapsHolder();
		}
		return distributionMapsHolder;
	}
	
	
		
//************************************************************************************************
	//helpers methods:
	private void initiateDistributionMaps(){
		this.browserMapAfterAnalysis = new ConcurrentHashMap<>();
		this.operatingSystemMapAfterAnalysis = new ConcurrentHashMap<>();
		this.countryMapAfterAnalysis = new ConcurrentHashMap<>();
	}
	
	//getters & setters
	public ConcurrentHashMap<String, Double> getBrowserMapAfterAnalysis() {
		return browserMapAfterAnalysis;
	}
	public void setBrowserMapAfterAnalysis(ConcurrentHashMap<String, Double> browserMapAfterAnalysis) {
		this.browserMapAfterAnalysis = browserMapAfterAnalysis;
	}
	public ConcurrentHashMap<String, Double> getOperatingSystemMapAfterAnalysis() {
		return operatingSystemMapAfterAnalysis;
	}
	public void setOperatingSystemMapAfterAnalysis(ConcurrentHashMap<String, Double> operatingSystemMapAfterAnalysis) {
		this.operatingSystemMapAfterAnalysis = operatingSystemMapAfterAnalysis;
	}
	public ConcurrentHashMap<String, Double> getCountryMapAfterAnalysis() {
		return countryMapAfterAnalysis;
	}
	public void setCountryMapAfterAnalysis(ConcurrentHashMap<String, Double> countryMapAfterAnalysis) {
		this.countryMapAfterAnalysis = countryMapAfterAnalysis;
	} 
}
