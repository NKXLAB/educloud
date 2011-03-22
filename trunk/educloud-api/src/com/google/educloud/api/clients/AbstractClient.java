package com.google.educloud.api.clients;

import java.net.URI;

import com.google.educloud.api.entities.EduCloudErrorMessage;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.google.gson.Gson;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.client.apache.ApacheHttpClient;

public abstract class AbstractClient {

	private ApacheHttpClient client;

	private URI uri;

	protected Gson gson = new Gson();

	public void setClient(ApacheHttpClient client) {
		this.client = client;
	}

	public void setBaseURI(URI uri) {
		this.uri = uri;
	}

	protected void handleError(int status, String entity) throws EduCloudServerException {
		if (status == 400) {
			EduCloudErrorMessage message = gson.fromJson(entity, EduCloudErrorMessage.class);
			throw new EduCloudServerException(message);
		}

		if (status != 200) {
			EduCloudErrorMessage message = gson.fromJson(entity, EduCloudErrorMessage.class);
			throw new EduCloudServerException(message);
		}
	}

	protected WebResource getWebResouce() {
		return client.resource(uri);
	}

}
