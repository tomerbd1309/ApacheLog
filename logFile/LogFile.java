
public class LogFile {
	private String filePath = "";
	
	public LogFile(String filePath){
		this.filePath = filePath;
	}
	
	public String getFilePath(){
		return this.filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}
}
