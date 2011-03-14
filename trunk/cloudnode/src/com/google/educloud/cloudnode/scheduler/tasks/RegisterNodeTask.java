package com.google.educloud.cloudnode.scheduler.tasks;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.RegistrationClient;
import com.google.educloud.internal.entities.Node;

public class RegisterNodeTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(RegisterNodeTask.class);

	@Override
	public void run() {
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

		LOG.debug("scheduler cloud node was started");
	}

}
