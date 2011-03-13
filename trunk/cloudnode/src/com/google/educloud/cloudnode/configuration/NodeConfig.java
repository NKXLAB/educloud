package com.google.educloud.cloudnode.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class NodeConfig {

	private static Logger LOG = Logger.getLogger(NodeConfig.class);

	private static int nodePort;

	private static long scheduleInterval;

	public static void setup() throws InvalidConfigurationException {

		LOG.debug("Node will load system properties...");

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("conf/cloudnode.properties"));
		} catch (IOException e) {
			LOG.error("Error on read file cloudnode.properties", e);
			throw new InvalidConfigurationException("Error on read file cloudnode.properties", e);
		}

		int port = 8111;
		long interval = 1000;

		try {
			port = Integer.parseInt(props.getProperty("node.port"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration server.port, " + port + " will be assumed by default");
		}

		try {
			interval = Long.parseLong(props.getProperty("scheduler.interval"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration server.port, " + interval + " will be assumed by default");
		}

		nodePort = port;
		scheduleInterval = interval;

	}

	public static int getNodePort() {
		return nodePort;
	}

	public static long getScheduleInterval() {
		return scheduleInterval;
	}
}
