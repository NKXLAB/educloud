package com.google.educloud.cloudserver.scheduler.tasks;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.database.dao.TaskDao;


public abstract class AbstractTask implements CloudTask {

	private static Logger LOG = Logger.getLogger(StartVmTask.class);

	private int id;

	private Status status;

	private HashMap<String, String> parameters = new HashMap<String, String>();

	private Date scheduleTime;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public HashMap<String, String> getParameters() {
		return parameters;
	}

	public void setParameter(String name, String value) {
		parameters.put(name, value);
	}

	public String getParameter(String name) {
		return parameters.get(name);
	}

	public void setScheduleTime(Date date) {
		this.scheduleTime = date;
	}

	public Date getScheduleTime() {
		return scheduleTime == null ? Calendar.getInstance().getTime() : scheduleTime;
	}

	protected void markAsCompleted() {
		this.setStatus(Status.COMPLETED);
		TaskDao.getInstance().updateStatus(this);
		LOG.debug("Task #" + this.id + " was completed");
	}

	protected void markAsRunning() {
		this.setStatus(Status.RUNNING);
		TaskDao.getInstance().updateStatus(this);
		LOG.debug("Task #" + this.id + " in progress");
	}

	protected void reschedule(Date time) {
		this.setStatus(Status.PENDING);
		TaskDao.getInstance().updateStatus(this, time);
		LOG.debug("Task #" + this.id + " was rescheduled");
	}
}
