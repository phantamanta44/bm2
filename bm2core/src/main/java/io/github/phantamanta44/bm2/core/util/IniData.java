package io.github.phantamanta44.bm2.core.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.google.common.collect.Maps;

public class IniData implements PropertyMap {
	
	private final Map<String, String> properties = Maps.newHashMap();

	public IniData(InputStream streamIn) throws IOException {
		this(new BufferedReader(new InputStreamReader(streamIn)));
	}
	
	public IniData(BufferedReader streamIn) throws IOException {
		String line;
		while ((line = streamIn.readLine()) != null) {
			if (line.isEmpty() || line.startsWith(";") || line.startsWith("#") || !line.contains("="))
				continue;
			String[] keyValuePair = line.split("=", 2);
			this.properties.put(keyValuePair[0].toLowerCase(), keyValuePair[1]);
		}
		streamIn.close();
	}
	
	public String get(String key) {
		return this.properties.get(key);
	}
	
	public String getSafely(String key) {
		if (this.properties.containsKey(key))
			return get(key);
		return "";
	}

}
