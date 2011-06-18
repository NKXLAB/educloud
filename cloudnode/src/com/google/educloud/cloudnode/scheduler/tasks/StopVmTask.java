package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.virtualbox.CleanupMode;
import org.virtualbox.LockType;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.util.OsUtil;
import com.google.educloud.cloudnode.virtualbox.VirtualBoxConnector;
import com.google.educloud.internal.entities.VirtualMachine;

public class StopVmTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(StopVmTask.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {
		long start = System.nanoTime();

		LOG.debug("Running stop virtual machine task");

		// 1) Stop virtual machine process
		IVirtualBox vbox = VirtualBoxConnector.connect(NodeConfig.getVirtualBoxWebservicesUrl());

		IWebsessionManager manager = new IWebsessionManager(vbox.port);

		ISession session = manager.getSessionObject(vbox);

		IMachine findMachine = vbox.findMachine(vm.getUUID());
		findMachine.lockMachine(session, LockType.SHARED);

		IConsole console = session.getConsole();

		IProgress progress = console.powerDown();

		boolean completed = false;
		do {
			completed = progress.getCompleted();
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				LOG.error("Virtual machine process was interrupted", e);
			}
		} while (!completed);

		progress.release();
		console.release();

		// 2) Detach virtual machine medium
		IMachine machine = findMachine;
		List<IMedium> unregister = machine.unregister(CleanupMode.FULL);

		// remove medium from storage registry of vbox
		for (IMedium iMedium : unregister) {
			iMedium.close();
		}

		machine.delete(new ArrayList<IMedium>());
		machine.release();

		// 3) put the virtual machine back to machine storage dir
		copyMachineDiskToStorage();

		String property = System.getProperty("file.separator");
		try {
			new File(NodeConfig.getMachinesDir() + property + vm.getBootableMedium()).delete();
			LOG.debug("Local template '" + NodeConfig.getMachinesDir() + property + vm.getBootableMedium() + "' removed");
		} catch (SecurityException e) {
			LOG.warn("Impossible delete '" + NodeConfig.getMachinesDir() + property + vm.getBootableMedium() + "'", e);
		}

		LOG.debug("virtual machine was stopped");

		// 4) notify server that machine was dropped
		session.release();
		vbox.release();

		long end = System.nanoTime();
		double elapsedTime = ((end - start)) / 1000000000.0;
		LOG.debug("Elapsed time to stop a machine: '" + elapsedTime + "'");

	}

	private void copyMachineDiskToStorage() {
		String bootableMedium = vm.getBootableMedium();

		try {
			String command = NodeConfig.getMachineToStorageScript() + " " + bootableMedium;
			LOG.debug("Will run command: " + command);
			OsUtil.runScript(command);
		} catch (IOException e) {
			LOG.error("Error on copy template to local", e);
		} catch (InterruptedException e) {
			LOG.error("Error on copy template to local", e);
		}
	}

}
