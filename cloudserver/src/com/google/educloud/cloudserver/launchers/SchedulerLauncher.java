package com.google.educloud.cloudserver.launchers;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.scheduler.Scheduler;

public class SchedulerLauncher {

	private static Logger LOG = Logger.getLogger(SchedulerLauncher.class);

	public static void main(String[] args) {

		LOG.debug("Starting cloud server scheduler");

		Scheduler target = new Scheduler();
		Thread thread = new Thread(target);
		thread.setName("SCHEDULE-THREAD");
		thread.start();

		LOG.debug("scheduler cloud server was started");
	}

}
