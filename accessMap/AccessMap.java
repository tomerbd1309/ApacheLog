import org.redisson.api.RMap;

public class AccessMap {
	private static Object lock = new Object();
	
	public static void insertToAccessToServerMap(RMap<String, Integer> accessToServerMap, String LineInfoStr){
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
}
