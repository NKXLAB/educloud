package com.google.educloud.api;

import java.net.URI;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import com.google.educloud.api.clients.EduCloudNodeClient;
import com.google.educloud.api.clients.EduCloudTemplateClient;
import com.google.educloud.api.clients.EduCloudUserClient;
import com.google.educloud.api.clients.EduCloudVMClient;
import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.User;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class EduCloudFactory {

	private static Gson gson = new Gson();

	public static EduCloudAuthorization createAuthorization(EduCloudConfig eduCloudConfig) throws EduCloudServerException {
		DefaultApacheHttpClientConfig apcc = new  DefaultApacheHttpClientConfig();
        apcc.getProperties().put(DefaultApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, true);

        ApacheHttpClient client = ApacheHttpClient.create(apcc);
        client.addFilter(new LoggingFilter());

		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(eduCloudConfig.getHost());
		builder.append(':');
		builder.append(eduCloudConfig.getPort());
		builder.append("/rs");

		URI uri = UriBuilder.fromUri(builder.toString()).build();

		WebResource service = client.resource(uri);

		ClientResponse response = service.path("user").path("login")
			.queryParam("login", eduCloudConfig.getLogin())
			.queryParam("pass", eduCloudConfig.getPass())
			.accept(MediaType.APPLICATION_JSON)
			.get(ClientResponse.class);

		String entity = response.getEntity(String.class);
		handleError(response.getStatus(), entity);

		User user = gson.fromJson(entity, User.class);

		EduCloudAuthorization eduCloudAuthorization = new EduCloudAuthorization();
		eduCloudAuthorization.config = eduCloudConfig;
		eduCloudAuthorization.client = client;
		eduCloudAuthorization.setUser(user);
		eduCloudAuthorization.uri = uri;

		return eduCloudAuthorization;
	}

	public static EduCloudVMClient createVMClient(EduCloudAuthorization auth) {

		EduCloudVMClient client = new EduCloudVMClient();
		client.setClient(auth.client);
		client.setBaseURI(auth.uri);

		return client;
	}

	public static EduCloudTemplateClient createTemplateClient(EduCloudAuthorization auth) {

		EduCloudTemplateClient client = new EduCloudTemplateClient();
		client.setClient(auth.client);
		client.setBaseURI(auth.uri);

		return client;
	}

	public static EduCloudNodeClient createNodeClient(EduCloudAuthorization auth) {

		EduCloudNodeClient client = new EduCloudNodeClient();
		client.setClient(auth.client);
		client.setBaseURI(auth.uri);

		return client;
	}

	protected static void handleError(int status, String entity) throws EduCloudServerException {
		if (status == 400) {
			EduCloudErrorMessage message = gson.fromJson(entity, EduCloudErrorMessage.class);
			throw new EduCloudServerException(message);
		}

		if (status != 200) {
			EduCloudErrorMessage message = gson.fromJson(entity, EduCloudErrorMessage.class);
			throw new EduCloudServerException(message);
		}
	}

	public static EduCloudUserClient createUserClient(EduCloudAuthorization auth) {
		EduCloudUserClient client = new EduCloudUserClient();
		client.setClient(auth.client);
		client.setBaseURI(auth.uri);

		return client;
	}
}
