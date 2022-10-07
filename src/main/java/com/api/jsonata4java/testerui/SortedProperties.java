package com.api.jsonata4java.testerui;

import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

public class SortedProperties extends Properties {

	private static final long serialVersionUID = 1L;

	public SortedProperties(Properties unsortedProperties) {
        putAll(unsortedProperties);
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public synchronized Enumeration keys() {
        Enumeration<Object> keysEnum = super.keys();
        Vector<String> keyList = new Vector<String>();
        while (keysEnum.hasMoreElements()) {
            keyList.add((String) keysEnum.nextElement());
        }
        Collections.sort(keyList);
        return keyList.elements();
    }

    @Override
    public Set<java.util.Map.Entry<Object, Object>> entrySet() {
        // use a TreeMap since in java 9 entrySet() instead of keys() is used in store()
        TreeMap<Object, Object> treeMap = new TreeMap<>();
        Set<Map.Entry<Object, Object>> entrySet = super.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            treeMap.put(entry.getKey(), entry.getValue());
        }
        return Collections.synchronizedSet(treeMap.entrySet());
    }
}
