package com.google.educloud.api;

import java.net.URI;

import com.google.educloud.api.entities.User;
import com.sun.jersey.client.apache.ApacheHttpClient;

public class EduCloudAuthorization {

	EduCloudConfig config;

	URI uri;

	public ApacheHttpClient client;

	public User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
