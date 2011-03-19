package com.google.educloud.cloudserver.nodecllient;

import com.google.educloud.internal.entities.Node;

public class ClientFactory {

	/**
	 * setup a new instance of VMNodeClient
	 *
	 * @param node
	 * @return
	 */
	public static VMNodeClient createVMNodeClient(Node node) {
		VMNodeClient vmNodeClient = new VMNodeClient();
		vmNodeClient.setNode(node);

		return vmNodeClient;
	}

	/**
	 * setup a new instance of NodeClient
	 *
	 * @param node
	 * @return
	 */
	public static NodeClient createNodeClient(Node node) {
		NodeClient nodeClient = new NodeClient();
		nodeClient.setNode(node);

		return nodeClient;
	}
}
