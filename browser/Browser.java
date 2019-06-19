
public enum Browser {
	FIREFOX("Fire Fox"),
	SEAMONKEY("SeaMonkey"),
	CHROME("Chrome"),
	CHROMIUM("Chromium"),
	SAFARI("Safari"),
	OPERA("Opera"),
	INTERNET_EXPLORER("Internet Explorer");
	
	private String browserIdentifier;
	
	Browser(String browserIdentifier){
		this.browserIdentifier = browserIdentifier;
	}
	
	@Override
	public String toString() {
		return this.browserIdentifier;
	}
	
	public static String getTitle(){
		return "Browser";
	}
}