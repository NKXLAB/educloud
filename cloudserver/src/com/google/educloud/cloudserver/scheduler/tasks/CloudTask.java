package com.google.educloud.cloudserver.scheduler.tasks;

import java.util.Date;
import java.util.HashMap;

public interface CloudTask extends Runnable {

	enum Status{PENDING, RUNNING, COMPLETED};

	int getId();

	void setId(int id);

	Status getStatus();

	void setStatus(Status status);

	HashMap<String, String> getParameters();

	void setParameter(String name, String value);

	String getParameter(String name);

	String getType();

	void setScheduleTime(Date date);

	Date getScheduleTime();

}
