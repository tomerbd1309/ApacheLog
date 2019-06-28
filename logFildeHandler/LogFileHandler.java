import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;


public class LogFileHandler {
	private String filePath = "";
	
	public LogFileHandler(String filePath){
		this.filePath = filePath;
	}
	
	/*
	 * Each line is read, sent to a thread that parse and insert the 
	 * parsed information to the suitable map that counts number of 
	 * accesses to server.
	 */
	public void readAndProcessLogFile(int numOfLinesToWait,int numOfThreads, DBMapsHolder dbMapsHolder, ExecutorService exec, CountDownLatch latch){
		int numsOfThreadExacuted = 0;
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(new File(this.getFilePath())));
			String logLine = "";
			while((logLine = br.readLine())!= null){
				exec.submit(new ApacheLogThread(logLine, dbMapsHolder, new LineInfo(), new ApacheLogParser(), latch));
				numsOfThreadExacuted++;
				if(numsOfThreadExacuted == numOfThreads){
					numsOfThreadExacuted =0;
					latch.await();
					latch = new CountDownLatch(numOfThreads);
				}	
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			try {
				br.close();
			}
			catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public void clearAndWriteResultsOfAnalytics(String outPutAnalysisFilePath, DistributionMapsHolder distributionMapsHolder){
		this.clearFile("Distribution", "txt", outPutAnalysisFilePath);
		this.writeAllDistributionReportsToFile(outPutAnalysisFilePath, distributionMapsHolder);
		
	}
	

//************************************************************************************************
	//helper methods:
	
	private void writeAllDistributionReportsToFile(String outPutAnalysisFilePath, DistributionMapsHolder distributionMapsHolder){
		this.writeToFile("Distribution", "txt", "Browsers Distribution", distributionMapsHolder.getBrowserMapAfterAnalysis(), outPutAnalysisFilePath);
		this.writeToFile("Distribution", "txt", "Operating Systems Distribution", distributionMapsHolder.getOperatingSystemMapAfterAnalysis(), outPutAnalysisFilePath);
		this.writeToFile("Distribution", "txt", "Country Distribution", distributionMapsHolder.getCountryMapAfterAnalysis(), outPutAnalysisFilePath);	
		
	}
	
	private static String addSuffix(String fileName, String suf) {
		return fileName + "." + suf;
	}
	
	private static String concatPatFileName(String path, String fileName, String suf){
		StringBuffer sb = new StringBuffer();
		sb.append(path);
		sb.append("\\");
		sb.append(fileName);
		sb.append(".");
		sb.append(suf);
		return sb.toString();
	}
	
	private static File createAfile(File file, String fileName, String suf, String filePath){
		try{
			String fileWithSuf = addSuffix(fileName, suf);
			if(filePath != null){
				file = new File(concatPatFileName(filePath, fileName, suf));
				if(!file.exists()){
					file.createNewFile();	
				}
			}
			else{
				file = new File(fileWithSuf);
				if(!file.exists()){
					file.createNewFile();
				} 	
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return file;
	}
	
	/*
	 * clear the file of the last analytics
	 */
	public void clearFile(String fileName, String suf,String filePath){
		File file = null;
		file = createAfile(file, fileName, suf, filePath);
		FileWriter fw = null;
		PrintWriter pw = null;
		try{
			//writing an empty String into the file
			fw = new FileWriter(file.getAbsoluteFile(), false);
			pw = new PrintWriter(fw); 
			pw.print("");
			pw.close();
			System.out.println("Done clearing file : " + fileName);
		}
		catch (IOException e){
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			pw.close();
		}
	}

	/*
	 * Writing the analytics into a file
	 */
	public void writeToFile(String fileName, String suf, String title, ConcurrentHashMap<String, Double> MapAfterStatistics, String filePath){
		File file = null;
		file = createAfile(file, fileName, suf, filePath);
		FileWriter fw = null;
		try {
			//true = append file
			fw = new FileWriter(file.getAbsoluteFile(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		final PrintWriter pw = new PrintWriter(fw); 
		try{
			pw.println(title);
			pw.println("************************************");
			MapAfterStatistics.entrySet().stream().sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue())).forEach(entry -> pw.println(entry.getKey() + " : " + String.format("%.2f", entry.getValue()) + "%"));
			pw.println("_____________________________________________________");
			pw.close();
			System.out.println("Done writing the file of the analytics : " + fileName);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				pw.close();
		}	
	}
	
	//getters & setters:
	public String getFilePath(){
		return this.filePath;
	}
	public void setFilePath(String filePath){
		this.filePath = filePath;
	}

	
	
	



	
	
}