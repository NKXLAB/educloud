package com.google.educloud.cloudserver.nodecllient;

import java.net.URI;
import java.util.HashMap;

import javax.ws.rs.core.UriBuilder;

import org.apache.log4j.Logger;

import com.google.educloud.internal.entities.Node;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class NodeResourcePool {

	private static Logger LOG = Logger.getLogger(NodeResourcePool.class);

	private static NodeResourcePool pool;

	private HashMap<Integer, WebResource> map;

	private NodeResourcePool() {
		map = new HashMap<Integer, WebResource>();
	}

	public static NodeResourcePool getInstance() {
		if (null == pool) {
			pool = new NodeResourcePool();
		}

		return pool;
	}

	public WebResource getResource(Node node) {
		if (!map.containsKey(node.getId())) {
			createNodeResource(node);
		}

		return map.get(node.getId());
	}

	private void createNodeResource(Node node) {

		LOG.debug("Create new resource for node #" + node.getId());

		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);

		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(node.getHostname());
		builder.append(':');
		builder.append(node.getPort());
		builder.append("/rs");

		URI build = UriBuilder.fromUri(builder.toString()).build();
		WebResource resource = client.resource(build);

		map.put(node.getId(), resource);
	}

}
