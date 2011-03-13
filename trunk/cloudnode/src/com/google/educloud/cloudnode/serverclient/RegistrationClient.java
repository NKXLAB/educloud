package com.google.educloud.cloudnode.serverclient;


import javax.ws.rs.core.MediaType;

import com.google.educloud.internal.entities.Node;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class RegistrationClient extends AbstractServerClient {

	public Node register(Node node) {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		String json = gson.toJson(node);

		ClientResponse response = service.path("node").path("resgister").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		String entity = response.getEntity(String.class);
		node.setId(gson.fromJson(entity, Node.class).getId());

		response.close();

		return node;
	}

}
