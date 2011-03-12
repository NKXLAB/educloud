package com.google.educloud.cloudserver;

import com.google.educloud.cloudserver.launchers.JettyLauncher;
import com.google.educloud.cloudserver.launchers.SchedulerLauncher;

public class App {

	public static void main(String args[]) {
		/* try start scheduler */
		SchedulerLauncher.main(args);

		/* try start jetty */
		JettyLauncher.main(args);
	}
}
