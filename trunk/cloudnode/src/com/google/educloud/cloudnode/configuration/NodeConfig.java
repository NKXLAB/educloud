package com.google.educloud.cloudnode.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class NodeConfig {

	private static Logger LOG = Logger.getLogger(NodeConfig.class);

	private static int nodePort;

	private static String nodeMachineDir;

	private static String nodeTemplateDir;

	private static String nodeStorageDir;

	private static String localStorageDir;

	private static String serverHost;

	private static int serverPort;

	private static String virtualBoxWebservicesUrl;

	private static String vboxFrontendType;

	private static String nodeAddress;

	private static String hostInterface;

	private static String templateToLocalScript;

	private static String localToStorageScript;

	private static String storageToMachineScript;

	private static String machineToStorageScript;

	private static String removeFromStorageScript;

	public static void setup() throws InvalidConfigurationException {

		LOG.debug("Node will load system properties...");

		Properties props = new Properties();
		try {
			props.load(new FileInputStream("conf/cloudnode.properties"));
		} catch (IOException e) {
			LOG.error("Error on read file cloudnode.properties", e);
			throw new InvalidConfigurationException("Error on read file cloudnode.properties", e);
		}

		nodePort = 8111;
		serverPort = 8000;
		serverHost = "localhost";
		virtualBoxWebservicesUrl = "http://localhost:18083/";
		nodeMachineDir = null;
		nodeTemplateDir = null;
		nodeStorageDir = null;
		localStorageDir = null;

		try {
			nodePort = Integer.parseInt(props.getProperty("node.port"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration server.port, " + nodePort + " will be assumed by default");
		}

		try {
			serverPort = Integer.parseInt(props.getProperty("cloudserver.port"));
		} catch (NumberFormatException e) {
			LOG.warn("Invalid configuration cloudserver.port, " + serverPort + " will be assumed by default");
		}

		serverHost = props.getProperty("cloudserver.host");
		virtualBoxWebservicesUrl = props.getProperty("vbox.websrv.url");
		nodeAddress = props.getProperty("node.host");
		nodeMachineDir = props.getProperty("node.machinedir");
		nodeStorageDir = props.getProperty("node.storagedir");
		localStorageDir = props.getProperty("node.localstoragedir");
		nodeTemplateDir = props.getProperty("vbox.templatedir");
		vboxFrontendType = props.getProperty("vbox.frontendType");
		hostInterface = props.getProperty("vbox.hostInterface");


		templateToLocalScript = props.getProperty("node.scripts.templateToLocal");
		localToStorageScript = props.getProperty("node.scripts.localToStorage");
		storageToMachineScript = props.getProperty("node.scripts.storageToMachine");
		machineToStorageScript = props.getProperty("node.scripts.machineToStorage");
		removeFromStorageScript = props.getProperty("node.scripts.removeFromStorage");
	}

	public static String getLocalStorageDir() {
		return localStorageDir;
	}

	public static int getNodePort() {
		return nodePort;
	}

	public static String getServerHost() {
		return serverHost;
	}

	public static int getServerPort() {
		return serverPort;
	}

	public static String getVirtualBoxWebservicesUrl() {
		return virtualBoxWebservicesUrl;
	}

	public static String getMachinesDir() {
		return nodeMachineDir;
	}

	public static String getStorageDir(){
		return nodeStorageDir;
	}

	public static String getTemplateDir() {
		return nodeTemplateDir;
	}

	public static String getVboxFrontendType() {
		return vboxFrontendType;
	}

	public static String getNodeAddress() {
		return nodeAddress;
	}

	public static String getHostInterface() {
		return hostInterface;
	}

	public static String getTemplateToLocalScript() {
		return templateToLocalScript;
	}

	public static void setTemplateToLocalScript(String templateToLocalScript) {
		NodeConfig.templateToLocalScript = templateToLocalScript;
	}

	public static String getLocalToStorageScript() {
		return localToStorageScript;
	}

	public static void setLocalToStorageScript(String localToStorageScript) {
		NodeConfig.localToStorageScript = localToStorageScript;
	}

	public static String getStorageToMachineScript() {
		return storageToMachineScript;
	}

	public static void setStorageToMachineScript(String storageToMachineScript) {
		NodeConfig.storageToMachineScript = storageToMachineScript;
	}

	public static String getMachineToStorageScript() {
		return machineToStorageScript;
	}

	public static void setMachineToStorageScript(String machineToStorageScript) {
		NodeConfig.machineToStorageScript = machineToStorageScript;
	}

	public static void getMachineToStorageScript(String removeFromStorageScript) {
		NodeConfig.removeFromStorageScript = removeFromStorageScript;
	}

	public static String getRemoveFromStorageScript() {
		return NodeConfig.removeFromStorageScript;
	}

}
