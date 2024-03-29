package com.google.educloud.cloudserver.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ServerConfig {

	private static Logger LOG = Logger.getLogger(ServerConfig.class);

	private static int serverPort;

	private static long scheduleInterval;

	private static boolean useUIExpanded = false;

	private static String expandedUI;

	private static String police;

	private static String vrdePortRange;

	public static void setup() throws InvalidConfigurationException {

		LOG.debug("Server will load system properties...");

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("conf/cloudserver.properties"));
		} catch (IOException e) {
			LOG.error("Error on read file cloudserver.properties", e);
			throw new InvalidConfigurationException("Error on read file cloudserver.properties", e);
		}

		int port = 8000;
		long interval = 1000;

		useUIExpanded = Boolean.valueOf(props.getProperty("ui.useExpanded"));
		expandedUI = props.getProperty("ui.expandedDir");
		police = props.getProperty("server.police");
		vrdePortRange = props.getProperty("vbox.vrdePortRange");

		try {
			port = Integer.parseInt(props.getProperty("server.port"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration server.port, " + port + " will be assumed by default");
		}

		try {
			interval = Long.parseLong(props.getProperty("scheduler.interval"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration server.port, " + interval + " will be assumed by default");
		}

		serverPort = port;
		scheduleInterval = interval;

	}

	public static int getServerPort() {
		return serverPort;
	}

	public static long getScheduleInterval() {
		return scheduleInterval;
	}

	public static boolean useUIExpanded() {
		return useUIExpanded;
	}

	public static String getExpandedUI() {
		return expandedUI;
	}

	public static String getPolice(){
		return police;
	}

	public static int getMinVRDEPort() {
		String[] split = vrdePortRange.split("-");
		return Integer.valueOf(split[0]);
	}

	public static int getMaxVRDEPort() {
		String[] split = vrdePortRange.split("-");
		return Integer.valueOf(split[1]);
	}

}
