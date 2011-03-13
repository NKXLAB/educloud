package com.google.educloud.cloudnode;

import java.net.InetAddress;
import java.net.UnknownHostException;

import com.google.educloud.cloudnode.configuration.InvalidConfigurationException;
import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.launchers.JettyLauncher;
import com.google.educloud.cloudnode.serverclient.RegistrationClient;
import com.google.educloud.internal.entities.Node;


public class App {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			NodeConfig.setup();
		} catch (InvalidConfigurationException e) {
			System.exit(-1);
		}

		/* try start jetty */
		JettyLauncher.main(args);

		/* register started node in the cloudserver */
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			System.err.println("Unable to register new node in the cloudserver");
			System.exit(-1);
		}

	    // Get IP Address
		Node node = new Node();
		node.setHostname(addr.getHostAddress());
		node.setPort(NodeConfig.getNodePort());

		new RegistrationClient().register(node);
	}

}
