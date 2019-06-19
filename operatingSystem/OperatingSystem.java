
public enum OperatingSystem {
	ANDROID("Android"),
	WINDOWS("Windows"),
	MAC("Mac OS"),
	LINUX("Linux"),
	UBUNTU("Ubuntu");
	
	private String OSIdentifier;
	
	OperatingSystem(String OSIdentifier){
		this.OSIdentifier = OSIdentifier;
	}
	
	@Override
	public String toString() {
		return this.OSIdentifier;
	}
	
	public static String getTitle(){
		return "Operating System";
	}
}