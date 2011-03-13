package com.google.educloud.cloudnode.launchers;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.scheduler.Scheduler;

public class SchedulerLauncher {

	private static Logger LOG = Logger.getLogger(SchedulerLauncher.class);

	public static void main(String[] args) {

		LOG.debug("Starting cloud node scheduler");

		new Thread(Scheduler.getInstance()).start();

		LOG.debug("scheduler cloud node was started");
	}

}
