package com.google.educloud.cloudserver;

import com.google.educloud.cloudserver.configuration.InvalidConfigurationException;
import com.google.educloud.cloudserver.configuration.ServerConfig;
import com.google.educloud.cloudserver.launchers.JettyLauncher;
import com.google.educloud.cloudserver.launchers.MonitorsLaunchers;
import com.google.educloud.cloudserver.launchers.SchedulerLauncher;

public class App {

	public static void main(String args[]) {

		/* try load configuration file */
		try {
			ServerConfig.setup();
		} catch (InvalidConfigurationException e) {
			System.exit(-1);
		}

		/* try start scheduler */
		SchedulerLauncher.main(args);

		/* try start jetty */
		JettyLauncher.main(args);
		
		/* try start monitors */
		MonitorsLaunchers.main(args);
	}
}
