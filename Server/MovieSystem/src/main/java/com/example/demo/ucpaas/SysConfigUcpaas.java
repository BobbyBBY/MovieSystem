package com.example.demo.ucpaas;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

public class SysConfigUcpaas {
	private Properties props = null;// configUcpaas.properties
	private static volatile SysConfigUcpaas conf;

	private SysConfigUcpaas() {
		props = new Properties();
		loadConfigProps();
	}

	public static SysConfigUcpaas getInstance() {
		if (conf == null) {
			synchronized (SysConfigUcpaas.class) {
				if (conf == null) {
					conf = new SysConfigUcpaas();
				}
			}
		}
		return conf;
	}

	public void loadConfigProps() {
		InputStream is = null;
		try {
			is = getClass().getResourceAsStream(
					"/main/resources/configUcpaas.properties");
			//./src/main/resources/generatorConfig.xml
			if (is == null) {
				is = getClass().getResourceAsStream("/configUcpaas.properties");
			}
			InputStreamReader reader = new InputStreamReader(is, "UTF-8");
			props.load(reader);
			Iterator<String> iter = props.stringPropertyNames().iterator();
			while (iter.hasNext()) {
				String key = iter.next();
				props.setProperty(key, props.getProperty(key));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (is != null) {
				try {
					is.close();
					is = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public String getProperty(String key) {
		String tmp = props.getProperty(key);
		if (StringUtils.isNotEmpty(tmp)) {
			return tmp.trim();
		}
		return tmp;
	}

	public String getProperty(String key, String defaultValue) {
		String tmp = props.getProperty(key, defaultValue);
		if (StringUtils.isNotEmpty(tmp)) {
			return tmp.trim();
		}
		return tmp;
	}

	public int getPropertyInt(String key) {
		String tmp = props.getProperty(key);
		if (StringUtils.isNotEmpty(tmp)) {
			return Integer.parseInt(tmp.trim());
		}
		return 0;

	}

	public int getPropertyInt(String key, int defaultValue) {
		String tmp = props.getProperty(key);
		if (StringUtils.isNotEmpty(tmp)) {
			return Integer.parseInt(tmp.trim());
		}
		return defaultValue;
	}

	public long getPropertyLong(String key, long defaultValue) {
		String tmp = props.getProperty(key);
		if (StringUtils.isNotEmpty(tmp)) {
			return Integer.parseInt(tmp.trim());
		}
		return defaultValue;
	}
}
