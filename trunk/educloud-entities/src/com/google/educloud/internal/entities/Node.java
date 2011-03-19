package com.google.educloud.internal.entities;

import java.util.Date;

public class Node {

	private int id;

	private String hostname;

	private int port;

	private Date lastPing;

	private Date startTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date date) {
		startTime = date;
	}

	public void setLastPing(Date time) {
		this.lastPing = time;
	}

	public Date getLastPing() {
		return this.lastPing;
	}

}
