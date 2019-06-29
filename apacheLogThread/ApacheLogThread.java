
public class ApacheLogThread extends Thread{
	private LineInfo lineInfo;
	private ApacheLogParser logParser;
	private DBMapsHolder dbMapsHolder;
	private String logLine;
	
	public ApacheLogThread(String logLine, DBMapsHolder dbMapsHolder){
		this.lineInfo =  new LineInfo();
		this.logParser = new ApacheLogParser();
		this.dbMapsHolder = dbMapsHolder;
		this.logLine = logLine;
	}
	
	@Override
	public void run() {
		this.logParser.parse(this.logLine, this.lineInfo);
		this.dbMapsHolder.insertIntoAllAccessToServerMap(this.lineInfo);
	}

//************************************************************************************************
	//getters & setters
	public LineInfo getLineInfo() {
		return lineInfo;
	}
	public void setLineInfo(LineInfo lineInfo) {
		this.lineInfo = lineInfo;
	}
	public ApacheLogParser getLogParser() {
		return logParser;
	}
	public void setLogParser(ApacheLogParser logParser) {
		this.logParser = logParser;
	}
	public DBMapsHolder getDbMapsHolder() {
		return dbMapsHolder;
	}
	public void setDbMapsHolder(DBMapsHolder dbMapsHolder) {
		this.dbMapsHolder = dbMapsHolder;
	}
	public String getLogLine() {
		return logLine;
	}
	public void setLogLine(String logLine) {
		this.logLine = logLine;
	}


}
