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

    public static void main(String[] args) throws Exception {
        new CacheService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        
	/** Cache APIs */


	ChronicleMapBuilder<Long,Entry> builder;
	String tmp="";
	String pathname ="";
	File file;
	ChronicleMap<Long,Entry> map;
	try{
		 pathname = "/home/harsh/test/narnia.dat";
		 file = new File(pathname);
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
