package com.google.educloud.cloudserver.scheduler.tasks;

public class StartVmTask implements CloudTask {

	@Override
	public void run() {
		// TODO implements start VM logic

		// 1) select a registered host
		// 2) create a new virtual machine from database
		// 3) send requisition for host
	}

}
