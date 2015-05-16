package edu.sjsu.cmpe.cache.client;
import com.google.common.base.Charsets;
import com.google.common.hash.HashFunction;
import com.google.common.hash.*;
import com.google.common.hash.HashCode;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class Client {
	private static HashFunction hashFunction = Hashing.md5();
	private static SortedMap<Integer,String> circle = new TreeMap<Integer, String>();
	
	static char[] ch = {'x','a', 'b', 'c', 'd','e', 'f', 'g', 'h', 'i','j','g', 'h'};
	public static void add(String node, int i){
		circle.put(hashFunction.hashLong(i).asInt(), node);
	}
	public static String getObject(Object key){
	
		if(circle.isEmpty())
			return  null;
		int hash = hashFunction.hashLong((Integer)key).asInt();
		if(!circle.containsKey(hash))
		{
			SortedMap<Integer,String> tailMap = circle.tailMap(hash);
			hash = tailMap.isEmpty()? circle.firstKey(): tailMap.firstKey();
		}
		return circle.get(hash);
	}
	
    public static void main(String[] args) throws Exception {
        
	System.out.println("Starting Cache Client...");
	String cacheServer1 = "http://localhost:3000";
	String cacheServer2 = "http://localhost:3001";
	String cacheServer3 = "http://localhost:3002";
        
	List<String> server = new ArrayList<String>();
	server.add(cacheServer1);
	server.add(cacheServer2);
	server.add(cacheServer3);
	int i = 0;
	for ( String x: server)
	{
		add(x,i);
		i++;
	}	
	
	for(int j =1;j<=10;j++){
	    int bucket = Hashing.consistentHash(Hashing.md5().hashInt(j), circle.size());
	    System.out.println(bucket);
	    String servers = getObject(bucket);
	    System.out.println("routed to server "+ servers);
	    CacheServiceInterface cache = new DistributedCacheService(servers);
	    cache.put(j, String.valueOf(ch[j]));
	}
	for(int j =1; j<=10;j++)
	{    
	   	int bucket = Hashing.consistentHash(Hashing.md5().hashInt(j), circle.size());
		System.out.println("get starts"); 
		String servers = getObject(bucket);
		 
	    System.out.println(servers);
	    CacheServiceInterface cache = new DistributedCacheService(servers);
	    System.out.println("reading key: "+ j + "from server: " +servers);
	}
	}	
}
