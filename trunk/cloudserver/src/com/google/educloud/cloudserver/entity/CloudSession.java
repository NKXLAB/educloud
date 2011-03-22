package com.google.educloud.cloudserver.entity;

public class CloudSession {

	public static final String HTTP_ATTR_NAME = "cloudSession";

	private int id;

	private User user;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
