package com.google.educloud.cloudserver.launchers;

import org.apache.log4j.Logger;

import com.google.educloud.cloudserver.monitor.NodeMonitor;
import com.google.educloud.cloudserver.monitor.VMMonitor;

public class MonitorsLaunchers {
	
	private static Logger LOG = Logger.getLogger(MonitorsLaunchers.class);

	public static void main(String[] args) {

		LOG.debug("Starting cloud server monitors");

		NodeMonitor target = new NodeMonitor();
		Thread thread = new Thread(target);
		thread.setName("NODEMONITOR-THREAD");
		thread.start();
		
		LOG.debug("node monitor cloud server was started");
		
		VMMonitor target2 = new VMMonitor();
		Thread thread2 = new Thread(target2);
		thread2.setName("MACHINEMONITOR-THREAD");
		thread2.start();

		LOG.debug("vm monitor cloud server was started");	
	}

}
