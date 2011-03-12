package com.google.educloud.cloudserver.rs;


import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class VMRestTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testStartVM() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		ClientResponse response = service.path("vm").path("start").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		System.out.println(response.getStatus());
		System.out.println(response.getEntity(String.class));
	}

	@Test
	public void testStopVM() {
		ClientConfig config = new DefaultClientConfig();
		Client client = Client.create(config);
		WebResource service = client.resource(getBaseURI());

		ClientResponse response = service.path("vm").path("stop").path("0").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class);

		System.out.println(response.getStatus());
		System.out.println(response.getEntity(String.class));
	}

	private static URI getBaseURI() {
		return UriBuilder.fromUri(
				"http://localhost:8000/rs").build();
	}

	@After
	public void tearDown() throws Exception {
	}

}
