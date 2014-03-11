package com.example.co_reading.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CacheStringKeyMap<String, V> extends HashMap<String, V> {
	private List<String>	m_keyCacheList = new ArrayList<String>();
	
	public List<String> getCachedKeyList() {
		return m_keyCacheList;
	}
	
	@Override
	public void clear() {
		super.clear();
		m_keyCacheList.clear();
	}
	
	@Override
	public V put(String key, V value) {
		m_keyCacheList.add(key);
		return super.put(key, value);
	}

	// TODO: putAll()
}
