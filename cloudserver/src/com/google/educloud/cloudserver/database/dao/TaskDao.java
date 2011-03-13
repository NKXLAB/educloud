package com.google.educloud.cloudserver.database.dao;

import java.util.ArrayList;
import java.util.List;

import com.google.educloud.cloudserver.scheduler.tasks.CloudTask;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask.Status;

public class TaskDao {

	private static int currentId = 0;

	private static List<CloudTask> cloudTasks = new ArrayList<CloudTask>();

	private static TaskDao dao;

	public static TaskDao getInstance() {
		if (null == dao) {
			dao = new TaskDao();
		}

		return dao;
	}

	public List<CloudTask> findPendingTasks() {
		List<CloudTask> list = new ArrayList<CloudTask>();

		for (CloudTask task : cloudTasks) {
			if (task.getStatus() == Status.PENDING) {
				list.add(task);
			}
		}

		return list;
	}

	public void insert(CloudTask task) {
		task.setId(++currentId);
		cloudTasks.add(task);
	}

	public void updateStatus(CloudTask task) {
		// TODO update status from database
	}

}
