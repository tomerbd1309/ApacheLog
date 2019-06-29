import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;

public class AnalysisResultWriter {

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
		System.out.println("Done writing the file of the analytics : Distribution");
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
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
				pw.close();
		}	
	}
}
