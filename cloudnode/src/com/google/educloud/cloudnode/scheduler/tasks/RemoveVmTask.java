package com.google.educloud.cloudnode.scheduler.tasks;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.google.educloud.cloudnode.configuration.NodeConfig;
import com.google.educloud.cloudnode.util.OsUtil;
import com.google.educloud.internal.entities.VirtualMachine;

public class RemoveVmTask extends AbstractTask {

	private static Logger LOG = Logger.getLogger(RemoveVmTask.class);

	private VirtualMachine vm;

	public void setVirtualMachine(VirtualMachine vm) {
		this.vm = vm;
	}

	@Override
	public void run() {

		LOG.debug("Running remove virtual machine task");

		String medium = vm.getBootableMedium();

		// remove machine file
		try {
			String command = NodeConfig.getRemoveFromStorageScript() + " " + medium;
			LOG.debug("will run command: '" + command + "'");
			OsUtil.runScript(command);
		} catch (IOException e) {
			LOG.error("Error on remove file from storage", e);
		} catch (InterruptedException e) {
			LOG.error("Error on remove file from storage", e);
		}
	}
}
