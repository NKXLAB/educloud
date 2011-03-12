package com.google.educloud.cloudserver.scheduler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.scheduler.tasks.CloudTask;

public class Scheduler implements Runnable {

	private static Logger LOG = Logger.getLogger(Scheduler.class);

	private static final int CAPACITY = 20;

	public static ArrayBlockingQueue<CloudTask> cloudTasks = new ArrayBlockingQueue<CloudTask>(CAPACITY);

	private ExecutorService es;

	public Scheduler() {
		es = Executors.newCachedThreadPool();
	}

	@Override
	public void run() {
		while (true) {
			try {
				// will consume PENDING tasks
				consume(cloudTasks.take());
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void consume(CloudTask task) {
		LOG.debug("Scheduler will consume a new task");
		// change task status to RUNNING

		es.execute(task);

		// change task status to COMPLETED
		LOG.debug("Task executed");
	}


}
