package org.am.web.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class NFProperties {

	public static String PRIMARY_STORAGE_PATH;
	public static String SECONDARY_STORAGE_PATH;
	public static String SNMP_KEY;
	public static Integer UDP_LISTENER_PORT = 0;
	public static Integer WS_FORWARDER_PORT = 0;
	public static String UDP_LISTENER_IP;

	private static Properties prop;

	private static String propFilePath;
	
	static{
		propFilePath = "/etc/nf/conf/flowgen.properties";
		
		NFProperties.setProperties();
	}
	
	public static String getProperty(String key, String defaultValue) {
		if (prop == null) {
			prop = new Properties();
			try {
				File propFile = new File(propFilePath);
				prop.load(new FileInputStream(propFile));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return prop.getProperty(key, defaultValue).trim();
	}
	
	public static void setProperties(){
		PRIMARY_STORAGE_PATH = getProperty("primary.storage.path", PRIMARY_STORAGE_PATH);
		SECONDARY_STORAGE_PATH = getProperty("secondary.storage.path", SECONDARY_STORAGE_PATH);
		SNMP_KEY = getProperty("snmp.key", SNMP_KEY);
		
		UDP_LISTENER_IP = getProperty("udp.listener.ip", UDP_LISTENER_IP);
		UDP_LISTENER_PORT = Integer.parseInt(getProperty("udp.listener.port", Integer.toString(UDP_LISTENER_PORT)));
		WS_FORWARDER_PORT = Integer.parseInt(getProperty("ws.forwarder.port", Integer.toString(WS_FORWARDER_PORT)));
				
	}
}
