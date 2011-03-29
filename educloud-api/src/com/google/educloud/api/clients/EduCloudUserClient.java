package com.google.educloud.api.clients;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MediaType;

import com.google.educloud.api.entities.User;
import com.google.educloud.api.entities.exceptions.EduCloudServerException;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class EduCloudUserClient extends AbstractClient {

	public List<User> describeUsers() throws EduCloudServerException {
		WebResource service = getWebResouce();

		ClientResponse response = service.path("user").path("describe").accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		Type type = new TypeToken<ArrayList<User>>(){}.getType();
		List<User> users = gson.fromJson(entity, type);

		return users;
	}

	public User createUser(User user) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(user);

		ClientResponse response = service.path("user").path("create").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);

		response.close();

		user = gson.fromJson(entity, User.class);

		return user;
	}

	public void delete(ArrayList<Integer> arrayList) throws EduCloudServerException {
		WebResource service = getWebResouce();

		String json = gson.toJson(arrayList);

		ClientResponse response = service.path("user").path("delete").accept(MediaType.APPLICATION_JSON).put(ClientResponse.class, json);

		int status = response.getStatus();
		String entity = response.getEntity(String.class);

		handleError(status, entity);
	}
}
