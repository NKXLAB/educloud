package com.google.educloud.cloudnode.serverclient;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.gson.Gson;

public abstract class AbstractServerClient {

	protected static Gson gson = new Gson();

	protected URI getBaseURI() {
		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(NodeConfig.getServerHost());
		builder.append(':');
		builder.append(NodeConfig.getServerPort());
		builder.append("/internalrs");

		return UriBuilder.fromUri(builder.toString()).build();
	}

}
