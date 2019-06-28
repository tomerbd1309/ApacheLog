import java.io.IOException;

public class ApacheLogParser {
	
	/*
	 * Gets a log line and sets the different fields inside the lineInfo object
	 */
	public void parse(String logLine, LineInfo lineInfo) {
		Browser browser = getBrowserFromLogLine(logLine);
		if(browser != null){
			lineInfo.setBrowser(browser.toString());
		}
		else{
			lineInfo.setBrowser(null);//sets info to null and won't be inserted to map
		}
		OperatingSystem operatingSystem = getOperatingSystemFromLogLine(logLine);
		if(operatingSystem != null){
			lineInfo.setOperatingSystem(operatingSystem.toString());
		}
		else{
			lineInfo.setOperatingSystem(null);
		}
		String countryName;
		try{
			GeoLocator geoLocator = new GeoLocator();
			countryName = geoLocator.getCountryByIp(getIpFromLogLine(logLine));
			if(countryName != null){
				lineInfo.setCountry(countryName);
			}
			else{
				lineInfo.setCountry(null);
			}
			geoLocator.close();
		}
		catch(TaboolaException | IOException e){
			e.printStackTrace();
		}	
	}
	
	
	
//************************************************************************************************
	//helper methods:
	
	/*
	 * the if statements have a certain flow. 
	 * e.g Defining that the browser used by the client is Chrome
	 * to conditions must exist: logLone contains Chrome && logLine doesn't contain Chromium 
	 * in addition, the order of browser check was decided according to 
	 * "http://gs.statcounter.com/browser-market-share"
	*/
	private Browser getBrowserFromLogLine(String logLine){
		if(containsBrowserIdentifier(logLine, "Chromium")){
			return Browser.CHROMIUM;
		}
		if(containsBrowserIdentifier(logLine, "Chrome")){
			return Browser.CHROME;//contains "Chrome" not contains "Chromium"
		}
		if(containsBrowserIdentifier(logLine, "Safari")){
			return Browser.SAFARI;
		}
		if(containsBrowserIdentifier(logLine, "Seamonkey")){
			return Browser.SEAMONKEY;
		}
		if(containsBrowserIdentifier(logLine, "Firefox")){
			return Browser.FIREFOX;
		}
		if(containsBrowserIdentifier(logLine, "OPR")){
			return Browser.OPERA;
		}
		if(containsBrowserIdentifier(logLine, "Opera")){
			return Browser.OPERA;
		}
		if(containsBrowserIdentifier(logLine, "MSIE")){
			return Browser.INTERNET_EXPLORER;
		}
		return null;
	}
	
	/*
	 * The order of browser check was decided according to 
	 * "http://gs.statcounter.com/os-market-share".
	*/
	private OperatingSystem getOperatingSystemFromLogLine(String logLine) {
		if(containsOSIdentifier(logLine, "Windows")){
			return OperatingSystem.WINDOWS;
		}
		if(containsOSIdentifier(logLine, "Android")){
			return OperatingSystem.ANDROID;
		}
		if(containsOSIdentifier(logLine, "Mac OS")){
			return OperatingSystem.MAC;
		}
		if(containsOSIdentifier(logLine, "Linux")){
			return OperatingSystem.LINUX;
		}
		if(containsOSIdentifier(logLine, "Ubuntu")){
			return OperatingSystem.UBUNTU;
		}
		return null;
	}
	
	/*
	 * According to the current ApacheLog file format the IP is the first word read in line
	 */
	private String getIpFromLogLine(String logLine) {
		return logLine.split(" ", 2)[0];	
	}

	private boolean containsOSIdentifier(String userAgent, String identifier){
		return userAgent.indexOf(identifier) == -1 ? false : true;
	}
	
	private boolean containsBrowserIdentifier(String userAgent, String identifier){
		return userAgent.indexOf(identifier) == -1 ? false : true;
	}


}
