package com.google.educloud.cloudnode.launchers;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.scheduler.Scheduler;
import com.google.educloud.cloudnode.scheduler.tasks.RegisterNodeTask;

public class SchedulerLauncher {

	private static Logger LOG = Logger.getLogger(SchedulerLauncher.class);

	public static void main(String[] args) {

		LOG.debug("Starting cloud node scheduler");

		Scheduler scheduler = Scheduler.getInstance();

		new Thread(scheduler).start();

		// add register node task
		scheduler.addTask(new RegisterNodeTask());
	}

}
