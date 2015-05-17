package edu.sjsu.cmpe.cache;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfiguration;
import edu.sjsu.cmpe.cache.domain.Entry;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;
import net.openhft.chronicle.map.*;
import java.io.*;
import java.io.IOException;
import java.util.*;
public class CacheService extends Service<CacheServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private  static String args_= null;;
    public static void main(String[] args) throws Exception {
    	CacheService.args_=args[1];
	CacheService cache = new CacheService();
	cache.run(args);
	
    }
    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        
	/** Cache APIs */
	//System.out.println(args[1]);

	ChronicleMapBuilder<Long,Entry> builder;
	String tmp="";
	File file;
	ChronicleMap<Long,Entry> map;
	try{
		tmp = args_.substring(7,15);
		System.out.println("arguments"+tmp);
		String dir = System.getProperty("java.io.tmpdir");
		String path = dir+"/"+args_.substring(7,15);
		System.out.println("path:"+path);
		file = new File(path);
		System.out.println("file created");
		 builder = ChronicleMapBuilder.of(Long.class, Entry.class);

 		map = builder.createPersistedTo(file);
	CacheInterface cache = new ChronicleMapCache(map);
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");
	}
	catch(IOException e){
		e.printStackTrace();
	}
        
    }
}
