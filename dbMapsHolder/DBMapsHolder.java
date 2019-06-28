import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

public class DBMapsHolder {
	private static DBMapsHolder dbMapsHolder = null;
	private RedissonClient client = null;
	private RMap<String, Integer> browserAccessToServerMap;
	private RMap<String, Integer> operatingSystemAccessToServerMap;
	private RMap<String, Integer> countryAccessToServerMap;
	private static Object lock = new Object();
	
	private DBMapsHolder(){
		this.client = this.createRedisClient();
	}
	
	public static DBMapsHolder getInstance(){
		if(dbMapsHolder == null){
			dbMapsHolder = new DBMapsHolder();
		}
		return dbMapsHolder;
	}

	public void insertIntoAllAccessToServerMap(LineInfo lineInfo){
		this.insertIntoAccessToServerMap(this.getBrowserAccessToServerMap(), lineInfo.getBrowser());
		this.insertIntoAccessToServerMap(this.getOperatingSystemAccessToServerMap(), lineInfo.getOperatingSystem());
		this.insertIntoAccessToServerMap(this.getCountryAccessToServerMap(), lineInfo.getCountry());
	}
	
	public void shutDownClient(){
		this.client.shutdown();
	}
	
	
	
//************************************************************************************************
	//helper methods:
	private RedissonClient createRedisClient(){
		Config config = new Config();
		config.useSingleServer().setAddress("127.0.0.1:6379");
		RedissonClient client = Redisson.create(config);		
		this.setBrowserAccessToServerMap(client.getMap("browserAccessToServerMap"));
		this.getBrowserAccessToServerMap().clear();
		this.setOperatingSystemAccessToServerMap(client.getMap("operatingSystemAccessToServerMap"));
		this.getOperatingSystemAccessToServerMap().clear();
		this.setCountryAccessToServerMap(client.getMap("countrySystemAccessToServerMap"));
		this.getCountryAccessToServerMap().clear();
		return client;
	}
	
	private void insertIntoAccessToServerMap(RMap<String, Integer> accessToServerMap, String LineInfoStr){
		if(LineInfoStr == null){
			return;
		}
		if(accessToServerMap.putIfAbsent(LineInfoStr, 1)!= null){
			synchronized (lock) {
				int countAccess = accessToServerMap.get(LineInfoStr);
				countAccess ++;
				accessToServerMap.put(LineInfoStr,countAccess);
			}
		}
	}
	
	//getters & setters
	public RMap<String, Integer> getBrowserAccessToServerMap() {
		return browserAccessToServerMap;
	}
	public void setBrowserAccessToServerMap(RMap<String, Integer> browserAccessToServerMap) {
		this.browserAccessToServerMap = browserAccessToServerMap;
	}
	public RMap<String, Integer> getOperatingSystemAccessToServerMap() {
		return operatingSystemAccessToServerMap;
	}
	public void setOperatingSystemAccessToServerMap(RMap<String, Integer> operatingSystemAccessToServerMap) {
		this.operatingSystemAccessToServerMap = operatingSystemAccessToServerMap;
	}
	public RMap<String, Integer> getCountryAccessToServerMap() {
		return countryAccessToServerMap;
	}
	public void setCountryAccessToServerMap(RMap<String, Integer> countryAccessToServerMap) {
		this.countryAccessToServerMap = countryAccessToServerMap;
	}

}
