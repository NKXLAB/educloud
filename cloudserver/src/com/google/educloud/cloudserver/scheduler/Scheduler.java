package com.google.educloud.cloudserver.scheduler;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.configuration.ServerConfig;
import com.google.educloud.cloudserver.database.dao.TaskDao;
import com.google.educloud.cloudserver.scheduler.tasks.CloudTask;

public class Scheduler implements Runnable {

	private static Logger LOG = Logger.getLogger(Scheduler.class);

	private static final long INTERNAL = ServerConfig.getScheduleInterval();

	private ExecutorService es;

	public Scheduler() {
		es = Executors.newCachedThreadPool();
	}

	@Override
	public void run() {
		while (true) {
			try {
				// will consume PENDING tasks
				List<CloudTask> searchForNewTasks = searchForNewTasks();

				for (CloudTask cloudTask : searchForNewTasks) {
					consume(cloudTask);
				}

				Thread.sleep(INTERNAL);
			} catch (InterruptedException e) {
				LOG.error("Scheduler was interrupted", e);
			}
		}
	}

	private List<CloudTask> searchForNewTasks() {
		return TaskDao.getInstance().findPendingTasks();
	}

	private void consume(CloudTask task) {
		LOG.debug("Scheduler will consume a new task");

		es.execute(task);
	}


}
