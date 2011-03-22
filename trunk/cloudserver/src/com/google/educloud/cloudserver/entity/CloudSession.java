package com.google.educloud.cloudserver.entity;

import java.util.Date;

public class CloudSession {

	public static final String HTTP_ATTR_NAME = "cloudSession";

	private int id;

	private User user;

	private Date lastUpdate;

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

	public Date getCreationDate() {
		return lastUpdate;
	}

	public void setCreationDate(Date time) {
		this.lastUpdate = time;

	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date time) {
		this.lastUpdate = time;
	}
}
