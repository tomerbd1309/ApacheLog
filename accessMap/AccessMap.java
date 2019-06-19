import org.redisson.api.RMap;

public class AccessMap {
	
	public static void insertToAccessToServerMap(RMap<String, Integer> accessToServerMap, String LineInfoStr, Object lock){
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
