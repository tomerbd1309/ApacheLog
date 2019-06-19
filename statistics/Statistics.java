
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ConcurrentHashMap;
import org.redisson.api.RMap;


public class Statistics {
	
	/*
	 * Navigates the map to the requested analysis 
	 */
	public ConcurrentHashMap<String, Double> analyzeMap(RMap<String, Integer> accessToServerRMap, ConcurrentHashMap<String, Double> percentageAccessToServerMap, String analayzeRequest){
		Statistics stat = new Statistics();
		
		switch(analayzeRequest){
			case("Distribution"):
				return stat.getDistribution(accessToServerRMap, percentageAccessToServerMap);
			default:
				return null;
		}
	}
	
	/*
	 * Calculates the percentage of accesses to server 
	 */
	public ConcurrentHashMap<String, Double> getDistribution(RMap<String, Integer> accessToServerRMap, ConcurrentHashMap<String, Double> percentageAccessToServerMap){
		try{
		final int totalAccess = accessToServerRMap.values().stream().mapToInt(i -> i).sum();
			accessToServerRMap.entrySet().stream().forEach(entry -> percentageAccessToServerMap.put(entry.getKey(),calcPercentage(entry.getValue(), totalAccess)));
		}
		catch(ArithmeticException e){
			e.printStackTrace();
		}
		catch(NullPointerException e){
			e.printStackTrace();
		}
		return percentageAccessToServerMap;
	}

	//helpers methods:
	
	private double calcPercentage(int countryAccessCount, int totalAccess){
		return(((double)(countryAccessCount)*100)/(double)(totalAccess));
	}
	
	public void printDistributionMap(String title, ConcurrentHashMap<String, Double> MapAfterStatistics){
		System.out.println(title);
		System.out.println("************************************");
		MapAfterStatistics.entrySet().stream().sorted((e1,e2) -> e2.getValue().compareTo(e1.getValue())).forEach(entry -> System.out.println(entry.getKey() + " : " + String.format("%.2f", entry.getValue()) + "%"));
		System.out.println("_____________________________________________________");
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
	
	private static String addSuffix(String fileName, String suf) {
		return fileName + "." + suf;
	}
	
	private static String concatPatFileName(String path, String fileName, String suf){
		StringBuffer sb = new StringBuffer();
		sb.append(path);
		sb.append("\\");//check if needed"\\\\"
		sb.append(fileName);
		sb.append(".");
		sb.append(suf);
		return sb.toString();
	}
}
