package com.google.educloud.cloudserver.scheduler.tasks;

import org.apache.log4j.Logger;


public class StartVmTask extends AbstractTask {

	public static final String VM_ID = "VM_ID";

	private static Logger LOG = Logger.getLogger(StartVmTask.class);

	@Override
	public void run() {

		markAsRunning();

		// TODO implements start VM logic

		// 1) select a registered host
		// 2) create a new virtual machine from database
		// 3) send requisition for host

		markAsCompleted();
	}

}
