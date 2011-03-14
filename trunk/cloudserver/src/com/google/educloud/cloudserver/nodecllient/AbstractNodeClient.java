package com.google.educloud.cloudserver.nodecllient;

import com.google.educloud.internal.entities.Node;
import com.google.gson.Gson;
import com.sun.jersey.api.client.WebResource;

public class AbstractNodeClient {

	protected static Gson gson = new Gson();

	private Node node;

	protected WebResource getResouce() {
		return NodeResourcePool.getInstance().getResource(node);
	}

	void setNode(Node node) {
		this.node = node;
	}
}
