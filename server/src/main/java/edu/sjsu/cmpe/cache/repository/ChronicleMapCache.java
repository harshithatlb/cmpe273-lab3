package edu.sjsu.cmpe.cache.repository;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import net.openhft.chronicle.map.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.*;
import edu.sjsu.cmpe.cache.domain.Entry;

public class ChronicleMapCache implements CacheInterface {
    /** In-memory map cache. (Key, Value) -> (Key, Entry) */
    private final ChronicleMap<Long, Entry> chronicleMap;

    public ChronicleMapCache(ChronicleMap<Long, Entry> entries) {
       	chronicleMap = entries;
    }

    @Override
    public Entry save(Entry newEntry) {
        checkNotNull(newEntry, "newEntry instance must not be null");
        chronicleMap.putIfAbsent(newEntry.getKey(), newEntry);

        return newEntry;
    }

    @Override
    public Entry get(Long key) {
        checkArgument(key > 0,
                "Key was %s but expected greater than zero value", key);
	Entry entry = chronicleMap.get(key);
        return entry;
    }

    @Override
    public List<Entry> getAll() {
	ArrayList<Entry> entries = new ArrayList<Entry>();
	for( Long key: chronicleMap.keySet()){
		entries.add(chronicleMap.get(key));
	}
        return entries;
    }

}
