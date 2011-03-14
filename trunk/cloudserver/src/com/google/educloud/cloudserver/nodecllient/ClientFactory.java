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
}
