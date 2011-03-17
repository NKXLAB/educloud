package com.google.educloud.cloudnode.scheduler.tasks;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.virtualbox.CleanupMode;
import org.virtualbox.service.IConsole;
import org.virtualbox.service.IMachine;
import org.virtualbox.service.IMedium;
import org.virtualbox.service.IProgress;
import org.virtualbox.service.ISession;
import org.virtualbox.service.IVirtualBox;
import org.virtualbox.service.IWebsessionManager;

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
		LOG.debug("Running stop virtual machine task");

		// 1) Stop virtual machine process
		IVirtualBox vbox = VirtualBoxConnector.restoreSession(vm.getVboxSession());
		IWebsessionManager manager = new IWebsessionManager(vbox.port);

		ISession session = manager.getSessionObject(vbox);
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
		IMachine machine = vbox.findMachine(vm.getUUID());
		machine.unregister(CleanupMode.FULL);
		machine.delete(new ArrayList<IMedium>());
		machine.release();

		// 3) Remove virtual machine
		// 4) notify server that machine was dropped
		session.release();
		vbox.release();
	}

}
