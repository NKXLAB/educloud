package com.google.educloud.cloudnode.scheduler.tasks;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.serverclient.RegistrationClient;
import com.google.educloud.internal.entities.Node;

public class RegisterNodeTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(RegisterNodeTask.class);

	@Override
	public void run() {
		/* register started node in the cloudserver */
		Node node = new Node();
		node.setHostname(NodeConfig.getNodeAddress());
		node.setPort(NodeConfig.getNodePort());

		new RegistrationClient().register(node);

		LOG.debug("scheduler cloud node was started");
	}

}
