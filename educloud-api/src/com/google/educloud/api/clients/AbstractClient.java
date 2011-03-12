package com.google.educloud.api.clients;

import java.net.URI;

import javax.ws.rs.core.UriBuilder;

import com.google.educloud.api.EduCloudConfig;
import com.google.educloud.cloudserver.api.entities.EduCloudErrorMessage;
import com.google.educloud.cloudserver.api.entities.exceptions.EduCloudServerException;
import com.google.gson.Gson;

public abstract class AbstractClient {

	protected EduCloudConfig config;

	protected static Gson gson = new Gson();

	public void setConfig(EduCloudConfig config) {
		this.config = config;
	}

	protected URI getBaseURI() {
		StringBuilder builder = new StringBuilder();
		builder.append("http://");
		builder.append(config.getHost());
		builder.append(':');
		builder.append(config.getPort());
		builder.append("/rs");

		return UriBuilder.fromUri(builder.toString()).build();
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

}
