package com.google.educloud.cloudserver.nodecllient;

import javax.ws.rs.core.MediaType;

import com.google.educloud.internal.entities.Node;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;

public class NodeClient extends AbstractNodeClient {

	public Node checkNodeStatus(Node node) throws NodeComunicationException {

		String jsonNode = gson.toJson(node);

		ClientResponse response;
		try {
			response = getResouce().path("application").path("check").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, jsonNode);
		} catch (ClientHandlerException e) {
			throw new NodeComunicationException("Error on check node", e);
		}


		if (response.getStatus() != 200) {
			throw new NodeComunicationException("Error on check node");
		}

		node = gson.fromJson(response.getEntity(String.class), Node.class);

		return node;
	}

}
