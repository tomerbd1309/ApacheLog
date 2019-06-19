import java.util.concurrent.CountDownLatch;

public class ApacheLogThread extends Thread{
	private LineInfo lineInfo;
	private ApacheLogParser logParser;
	private GateWay gateWay;
	private String logLine;
	private CountDownLatch latch;

	public ApacheLogThread(String logLine, GateWay gateWay, LineInfo lineInfo, ApacheLogParser apacheLogParser, CountDownLatch latch) {
		this.lineInfo = lineInfo;
		this.logParser = apacheLogParser;
		this.gateWay = gateWay;
		this.logLine = logLine;
		this.latch = latch;
	}
	/*
	 * parse A String line from the log file and insert to the suitable concurrent map
	 */
	@Override
	public void run() {
		this.logParser.parse(logLine, lineInfo);
		AccessMap.insertToAccessToServerMap(this.gateWay.getBrowserAccessToServerMap(), this.lineInfo.getBrowser());
		AccessMap.insertToAccessToServerMap(this.gateWay.getOperatingSystemAccessToServerMap(), this.lineInfo.getOperatingSystem());
		AccessMap.insertToAccessToServerMap(this.gateWay.getCountryAccessToServerMap(), this.lineInfo.getCountry());
		this.latch.countDown();
	}

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
	
	public GateWay getGetWay() {
		return gateWay;
	}

	public void setGetWay(GateWay getWay) {
		this.gateWay = getWay;
	}

	public String getLogLine() {
		return logLine;
	}

	public void setLogLine(String logLine) {
		this.logLine = logLine;
	}


}
