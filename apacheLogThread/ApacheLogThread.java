import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class ApacheLogThread extends Thread{
	private LineInfo lineInfo;
	private ApacheLogParser logParser;
	private DBMapsHolder dbMapsHolder;
	private String logLine;
	private CountDownLatch latch;
	
	public ApacheLogThread(String logLine, DBMapsHolder dbMapsHolder, LineInfo lineInfo, ApacheLogParser apacheLogParser, CountDownLatch latch){
		this.lineInfo = lineInfo;
		this.logParser = apacheLogParser;
		this.dbMapsHolder = dbMapsHolder;
		this.logLine = logLine;
		this.latch = latch;
	}
	
	@Override
	public void run() {
		this.logParser.parse(this.logLine, this.lineInfo);
		this.dbMapsHolder.insertIntoAllAccessToServerMap(this.lineInfo);
		this.latch.countDown();
		System.out.println("I am " + this.getId() + " barrier count " + this.latch.getCount());
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
